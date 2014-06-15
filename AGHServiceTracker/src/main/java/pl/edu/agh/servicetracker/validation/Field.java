package pl.edu.agh.servicetracker.validation;

import android.content.Context;
import android.widget.EditText;
import pl.edu.agh.servicetracker.validation.validator.Validator;

import java.util.ArrayList;
import java.util.List;

public class Field {

    private List<Validator> validators = new ArrayList<Validator>();

    private EditText textView;

    private Context context;

    public Field(EditText textView, Context context) {
        this.textView = textView;
        this.context = context;
    }

    public Field validator(Validator validator) {
        validators.add(validator);
        return this;
    }

    public EditText getTextView() {
        return textView;
    }

    public FieldValidationResult validate() {
        FieldValidationResult fieldValidationResult = new FieldValidationResult();
        for (Validator validator : validators) {
            fieldValidationResult.addValidationResult(validator.validate(this));
        }
        return fieldValidationResult;
    }

    public Context getContext() {
        return context;
    }
}