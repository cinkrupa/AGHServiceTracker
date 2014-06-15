package pl.edu.agh.servicetracker.validation.validator;

import pl.edu.agh.servicetracker.R;
import pl.edu.agh.servicetracker.validation.Field;
import pl.edu.agh.servicetracker.validation.ValidationResult;

public class MinimumLengthValidator implements Validator {

    private int minimumLength;

    public MinimumLengthValidator(int minimumLength) {
        this.minimumLength = minimumLength;
    }

    @Override
    public ValidationResult validate(Field field) {
        boolean isValid = field.getTextView().toString().length() >= minimumLength;
        return isValid ? ValidationResult.success(field.getTextView()) : ValidationResult.failure(field.getTextView()
                , field.getContext().getString(R.string.validator_too_short));
    }

}
