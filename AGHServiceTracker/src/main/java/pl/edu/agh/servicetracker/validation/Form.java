package pl.edu.agh.servicetracker.validation;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import java.util.ArrayList;
import java.util.List;

public class Form {

    private List<Field> fields = new ArrayList<Field>();

    private List<Crouton> croutons = new ArrayList<Crouton>();

    private Activity activity;

    public Form(Activity activity) {
        this.activity = activity;
    }

    public Field field(EditText textView) {
        Field field = new Field(textView, activity);
        fields.add(field);
        return field;
    }

    public boolean isValid() {
        boolean isValid = true;
        clearErrorMessages();
        for (Field field : fields) {
            FieldValidationResult result = field.validate();
            ValidationResult firstFailedValidation = result.getFirstFailedValidationResult();
            if (firstFailedValidation != null) {
                showErrorMessage(firstFailedValidation);
                isValid = false;
                break;
            }
        }

        return isValid;
    }

    public void showErrorMessage(ValidationResult result) {
        EditText textView = result.getTextView();
        textView.requestFocus();
        textView.selectAll();
        FormUtils.showKeyboard(activity, textView);
        Crouton crouton = Crouton.makeText(activity, result.getMessage(), Style.ALERT);
        croutons.add(crouton);
        crouton.show();
    }

    public void clearErrorMessages() {
        for (Crouton crouton : croutons) {
            crouton.cancel();
        }
        croutons.clear();
    }

    public void onDestroy() {
        Crouton.clearCroutonsForActivity(activity);
    }
}
