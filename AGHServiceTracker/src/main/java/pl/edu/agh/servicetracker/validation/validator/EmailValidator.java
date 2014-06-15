package pl.edu.agh.servicetracker.validation.validator;

import android.widget.EditText;
import pl.edu.agh.servicetracker.R;
import pl.edu.agh.servicetracker.validation.Field;
import pl.edu.agh.servicetracker.validation.ValidationResult;

public class EmailValidator implements Validator {

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public ValidationResult validate(Field field) {
        EditText textView = field.getTextView();
        boolean isValid = textView.getText().toString().matches(EMAIL_PATTERN);
        return isValid ?
                ValidationResult.success(textView)
                : ValidationResult.failure(textView, field.getContext().getString(R.string.validator_not_email));
    }
}