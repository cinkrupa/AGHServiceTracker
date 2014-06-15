package pl.edu.agh.servicetracker.validation.validator;

import pl.edu.agh.servicetracker.validation.Field;
import pl.edu.agh.servicetracker.validation.ValidationResult;

public interface Validator {

    ValidationResult validate(Field field);

}