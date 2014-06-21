/*
 * Copyright (C) 2014  Marcin Krupa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.agh.servicetracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import pl.edu.agh.servicetracker.auth.AuthPreferencesUtil;
import pl.edu.agh.servicetracker.auth.UserCredentials;
import pl.edu.agh.servicetracker.auth.UserNotInitializedException;
import pl.edu.agh.servicetracker.request.InvalidTokenException;
import pl.edu.agh.servicetracker.request.MockRequestService;
import pl.edu.agh.servicetracker.request.ServiceRequest;

import java.util.ArrayList;

/**
 * A list fragment representing a list of ServiceRequests. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link ServiceRequestDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ServiceRequestListFragment extends SwipeRefreshListFragment {

    private ArrayAdapter<ServiceRequest> listAdapter;

    private ArrayList serviceRequests = new ArrayList<ServiceRequest>();

    private UserCredentials userCredentials;

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(long id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(long id) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ServiceRequestListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            userCredentials = AuthPreferencesUtil.getUserCredentials(getActivity());
        } catch (UserNotInitializedException e) {
            startActivity(new Intent(getActivity(), SendTokenActivity.class));
        }
        listAdapter = new ArrayAdapter<ServiceRequest>(getActivity(), android.R.layout.simple_list_item_activated_1,
                android.R.id.text1, serviceRequests);
        setListAdapter(listAdapter);
    }

    private void reloadData() {
        listAdapter.clear();
        try {
            listAdapter.addAll(MockRequestService.getRequestsByUser(userCredentials));
        } catch (InvalidTokenException e) {
            startActivity(new Intent(getActivity(), SendTokenActivity.class));
        }
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
        setColorScheme(R.color.agh_green, R.color.agh_red, R.color.blue, R.color.orange);
        setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTask<Void, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(Void... params) {
                        serviceRequests.clear();
                        try {
                            serviceRequests.addAll(MockRequestService.getRequestsByUser(userCredentials));
                        } catch (InvalidTokenException e) {
                            return false;
                        }
                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean success) {
                        if(success) {
                            listAdapter.notifyDataSetChanged();
                            setRefreshing(false);
                        } else {
                            startActivity(new Intent(getActivity(), SendTokenActivity.class));
                        }
                    }
                }.execute();
            }
        });
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", getString(R.string
                .loading));
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                serviceRequests.clear();
                try {
                    serviceRequests.addAll(MockRequestService.getRequestsByUser(userCredentials));
                } catch (InvalidTokenException e) {
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                progressDialog.dismiss();
                if(success) {
                    listAdapter.notifyDataSetChanged();
                    setRefreshing(false);
                } else {
                    startActivity(new Intent(getActivity(), SendTokenActivity.class));
                }
            }
        }.execute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(listAdapter.getItem(position).getId());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
}
