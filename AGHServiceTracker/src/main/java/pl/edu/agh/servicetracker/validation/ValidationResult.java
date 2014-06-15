package pl.edu.agh.servicetracker.validation;

import android.widget.EditText;

public class ValidationResult {

    private final boolean valid;

    private final String message;

    private final EditText textView;

    public static ValidationResult success(EditText textView) {
        return new ValidationResult(true, "", textView);
    }

    public static ValidationResult failure(EditText textView, String message) {
        return new ValidationResult(false, message, textView);
    }

    private ValidationResult(boolean valid, String message, EditText textView) {
        this.valid = valid;
        this.message = message;
        this.textView = textView;
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }

    public EditText getTextView() {
        return textView;
    }
}
