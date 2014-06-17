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
