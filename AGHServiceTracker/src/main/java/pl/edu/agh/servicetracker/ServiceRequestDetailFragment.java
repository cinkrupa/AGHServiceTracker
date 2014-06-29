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

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.gson.Gson;
import pl.edu.agh.servicetracker.request.RequestStatus;
import pl.edu.agh.servicetracker.request.ServiceRequest;

import java.text.DateFormat;

/**
 * A fragment representing a single ServiceRequest detail screen.
 * This fragment is either contained in a {@link ServiceRequestListActivity}
 * in two-pane mode (on tablets) or a {@link ServiceRequestDetailActivity}
 * on handsets.
 */
public class ServiceRequestDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM = "item";

    private ServiceRequest serviceRequest;

    TextView requestName;

    TextView requestItem;

    TextView requestDescription;

    TextView requestDateCreated;

    TextView requestLastUpdated;

    TextView requestStatus;

    TextView requestResponseLabel;

    TextView requestResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM)) {
            String jsonString = getArguments().getString(ARG_ITEM);
            serviceRequest =  new Gson().fromJson(jsonString, ServiceRequest.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_servicerequest_detail, container, false);
        initFields(rootView);
        if (serviceRequest != null) {
            setFieldValues(serviceRequest);
        }
        return rootView;
    }


    private void initFields(View rootView) {
        requestName = (TextView) rootView.findViewById(R.id.servicerequest_name);
        requestItem = (TextView) rootView.findViewById(R.id.servicerequest_item);
        requestDescription = (TextView) rootView.findViewById(R.id.servicerequest_description);
        requestDateCreated = (TextView) rootView.findViewById(R.id.servicerequest_date_created);
        requestLastUpdated = (TextView) rootView.findViewById(R.id.servicerequest_last_modified);
        requestStatus = (TextView) rootView.findViewById(R.id.servicerequest_status);
        requestResponseLabel = (TextView) rootView.findViewById(R.id.servicerequest_response_label);
        requestResponse = (TextView) rootView.findViewById(R.id.servicerequest_response);
    }

    private void setFieldValues(ServiceRequest serviceRequest) {
        requestName.setText(serviceRequest.toString());
        requestItem.setText(serviceRequest.getItem().toString());
        requestDescription.setText(serviceRequest.getDescription());
        requestDateCreated.setText(DateFormat.getInstance().format(serviceRequest.getDateCreated()));
        requestLastUpdated.setText(DateFormat.getInstance().format(serviceRequest.getLastModified()));
        requestStatus.setText(serviceRequest.getStatus().name());
        if (serviceRequest.getStatus() == RequestStatus.RESOLVED) {
            requestResponse.setText(serviceRequest.getResponse());
        } else {
            requestResponse.setVisibility(View.GONE);
            requestResponseLabel.setVisibility(View.GONE);
        }
    }
}
