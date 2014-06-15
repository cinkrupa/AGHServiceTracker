package pl.edu.agh.servicetracker.validation.validator;

import android.text.TextUtils;
import pl.edu.agh.servicetracker.R;
import pl.edu.agh.servicetracker.validation.Field;
import pl.edu.agh.servicetracker.validation.ValidationResult;

public class NonEmptyValidator implements Validator {

    @Override
    public ValidationResult validate(Field field) {
        boolean isValid = !TextUtils.isEmpty(field.getTextView().getText());
        return isValid ? ValidationResult.success(field.getTextView()) : ValidationResult.failure(field.getTextView()
                , field.getContext().getString(R.string.validator_empty));
    }
}