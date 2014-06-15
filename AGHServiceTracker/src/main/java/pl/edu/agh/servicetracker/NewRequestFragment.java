package pl.edu.agh.servicetracker;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import pl.edu.agh.servicetracker.request.Item;
import pl.edu.agh.servicetracker.request.MockRequestService;
import pl.edu.agh.servicetracker.validation.Form;
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initFields();

        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (form.isValid()) {
                    MockRequestService.sendRequest(new Item(null, name.getText().toString(),
                                    category.getText().toString(), location.getText().toString()),
                            description.getText().toString()
                    );
                    Toast.makeText(getActivity(), R.string.request_sent, Toast.LENGTH_SHORT);
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        Crouton.clearCroutonsForActivity(getActivity());
    }

    private void initFields() {
        name = (EditText) getActivity().findViewById(R.id.name);
        category = (EditText) getActivity().findViewById(R.id.category);
        location = (EditText) getActivity().findViewById(R.id.location);
        description = (EditText) getActivity().findViewById(R.id.description);
        sendRequestButton = (Button) getActivity().findViewById(R.id.sendRequestButton);
        initValidationForm();

    }

    private void initValidationForm() {
        form = new Form(getActivity());
        form.field(name).validator(new NonEmptyValidator());
        form.field(category).validator(new NonEmptyValidator());
        form.field(location).validator(new NonEmptyValidator());
        form.field(description).validator(new NonEmptyValidator()).validator(new MinimumLengthValidator(20));
    }


}
