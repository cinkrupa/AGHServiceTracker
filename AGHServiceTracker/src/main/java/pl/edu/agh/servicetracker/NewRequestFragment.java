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
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import pl.edu.agh.servicetracker.auth.AuthPreferencesUtil;
import pl.edu.agh.servicetracker.request.Item;
import pl.edu.agh.servicetracker.service.RequestService;
import pl.edu.agh.servicetracker.service.UiCallback;
import pl.edu.agh.servicetracker.validation.Form;
import pl.edu.agh.servicetracker.validation.FormUtils;
import pl.edu.agh.servicetracker.validation.validator.NonEmptyValidator;

import java.util.ArrayList;
import java.util.List;

public class NewRequestFragment extends Fragment {

    private EditText name;

    private EditText category;

    private EditText location;

    private Spinner typicalBreakdownsSpinner;

    private EditText description;

    private Button sendRequestButton;

    private Form form;

    private List<String> typicalBreakdowns = new ArrayList<>();

    ArrayAdapter<String> typicalBreakdownsArrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_request, container, false);
        initFields(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initValidationForm();

        String token = AuthPreferencesUtil.getToken(getActivity());

        if (token == null) {
            startActivity(new Intent(getActivity(), SendTokenActivity.class));
        }

        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSubmit();
            }
        });

        typicalBreakdownsArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, typicalBreakdowns);

        typicalBreakdownsSpinner.setAdapter(typicalBreakdownsArrayAdapter);

        typicalBreakdownsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (typicalBreakdowns.isEmpty() || position == typicalBreakdowns.size() - 1) {
                    description.setVisibility(View.VISIBLE);
                    description.setText("");
                    description.requestFocus();
                    FormUtils.showKeyboard(getActivity(), description);
                } else {
                    description.setVisibility(View.GONE);
                    description.setText(typicalBreakdowns.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                description.setVisibility(View.VISIBLE);
                description.setText("");
                description.requestFocus();
            }
        });

    }

    @Override
    public void onDestroy() {
        form.onDestroy();
        super.onDestroy();

    }

    public void setFieldValues(Item item) {
        name.setText(item.getName());
        category.setText(item.getCategory());
        location.setText(item.getLocation());
        name.setEnabled(false);
        category.setEnabled(false);
        location.setEnabled(false);
        typicalBreakdowns.clear();
        typicalBreakdownsSpinner.setVisibility(View.VISIBLE);
        typicalBreakdowns.addAll(item.getTypicalBreakdowns());
        typicalBreakdowns.add(getString(R.string.other));
        if(item.getTypicalBreakdowns().isEmpty()) {
            description.setVisibility(View.VISIBLE);
            description.setText("");
            description.requestFocus();
            FormUtils.showKeyboard(getActivity(), description);
        } else {
            description.setText(item.getTypicalBreakdowns().get(0));
        }
        typicalBreakdownsArrayAdapter.notifyDataSetChanged();
    }

    private void validateAndSubmit() {
        if (form.isValid()) {
            final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.sending));

            RequestService.sendRequest(getActivity(), buildItem(), description.getText().toString(),
                    new UiCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            progressDialog.dismiss();
                            startActivity(new Intent(getActivity(), MainActivity.class));
                        }

                        @Override
                        public void onError(String message) {
                            progressDialog.dismiss();
                            Crouton.makeText(getActivity(), String.format("%s: %s",
                                    getActivity().getString(R.string.connection_error), message), Style.ALERT).show();
                        }
                    });
        }
    }

    private void initFields(View rootView) {
        name = (EditText) rootView.findViewById(R.id.name);
        category = (EditText) rootView.findViewById(R.id.category);
        location = (EditText) rootView.findViewById(R.id.location);
        typicalBreakdownsSpinner = (Spinner) rootView.findViewById(R.id.typicalBreakdownsSpinner);
        description = (EditText) rootView.findViewById(R.id.description);
        sendRequestButton = (Button) rootView.findViewById(R.id.sendRequestButton);
    }

    private void initValidationForm() {
        form = new Form(getActivity());
        form.field(name).validator(new NonEmptyValidator());
        form.field(category).validator(new NonEmptyValidator());
        form.field(location).validator(new NonEmptyValidator());
        form.field(description).validator(new NonEmptyValidator());
    }

    private Item buildItem() {
        Item item = new Item();
        item.setName(name.getText().toString());
        item.setCategory(category.getText().toString());
        item.setLocation(location.getText().toString());
        return item;
    }
}
