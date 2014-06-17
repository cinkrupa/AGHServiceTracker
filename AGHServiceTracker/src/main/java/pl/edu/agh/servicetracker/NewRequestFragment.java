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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import pl.edu.agh.servicetracker.request.Item;
import pl.edu.agh.servicetracker.request.MockRequestService;
import pl.edu.agh.servicetracker.validation.Form;
import pl.edu.agh.servicetracker.validation.FormUtils;
import pl.edu.agh.servicetracker.validation.validator.MinimumLengthValidator;
import pl.edu.agh.servicetracker.validation.validator.NonEmptyValidator;

public class NewRequestFragment extends Fragment {

    private EditText name;

    private EditText category;

    private EditText location;

    private EditText description;

    private Button sendRequestButton;

    private Form form;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_request, container, false);
        initFields(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initValidationForm();

        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSubmit();
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
        description.requestFocus();
        description.selectAll();
        FormUtils.showKeyboard(getActivity(), description);
    }

    private void validateAndSubmit() {
        if (form.isValid()) {
            MockRequestService.sendRequest(buildItem(),
                    description.getText().toString()
            );
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
    }

    private void initFields(View rootView) {
        name = (EditText) rootView.findViewById(R.id.name);
        category = (EditText) rootView.findViewById(R.id.category);
        location = (EditText) rootView.findViewById(R.id.location);
        description = (EditText) rootView.findViewById(R.id.description);
        sendRequestButton = (Button) rootView.findViewById(R.id.sendRequestButton);
    }

    private void initValidationForm() {
        form = new Form(getActivity());
        form.field(name).validator(new NonEmptyValidator());
        form.field(category).validator(new NonEmptyValidator());
        form.field(location).validator(new NonEmptyValidator());
        form.field(description).validator(new NonEmptyValidator()).validator(new MinimumLengthValidator(20));
    }

    private Item buildItem() {
        return new Item(null, name.getText().toString(),
                category.getText().toString(), location.getText().toString());
    }
}
