package pl.edu.agh.servicetracker.validation;

import java.util.ArrayList;
import java.util.List;

public class FieldValidationResult {

    private List<ValidationResult> validationResults = new ArrayList<ValidationResult>();

    public void addValidationResult(ValidationResult validationResult) {
        validationResults.add(validationResult);
    }

    public boolean isValid() {
        return getFirstFailedValidationResult() != null;
    }

    public ValidationResult getFirstFailedValidationResult() {
        for (ValidationResult validationResult : validationResults) {
            if(!validationResult.isValid()) {
                return validationResult;
            }
        }
        return null;
    }
}