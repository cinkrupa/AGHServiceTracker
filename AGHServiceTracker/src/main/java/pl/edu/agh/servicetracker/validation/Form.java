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

    public static Crouton showErrorMessage(Activity activity, EditText textView, String message) {
        textView.requestFocus();
        textView.selectAll();
        FormUtils.showKeyboard(activity, textView);
        Crouton crouton = Crouton.makeText(activity, message, Style.ALERT);
        crouton.show();
        return crouton;
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
        croutons.add(showErrorMessage(activity, result.getTextView(), result.getMessage()));
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
