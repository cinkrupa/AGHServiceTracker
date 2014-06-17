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