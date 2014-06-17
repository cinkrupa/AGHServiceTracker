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

import android.content.Context;
import android.widget.EditText;
import pl.edu.agh.servicetracker.validation.validator.Validator;

import java.util.ArrayList;
import java.util.List;

public class Field {

    private List<Validator> validators = new ArrayList<Validator>();

    private EditText textView;

    private Context context;

    public Field(EditText textView, Context context) {
        this.textView = textView;
        this.context = context;
    }

    public Field validator(Validator validator) {
        validators.add(validator);
        return this;
    }

    public EditText getTextView() {
        return textView;
    }

    public FieldValidationResult validate() {
        FieldValidationResult fieldValidationResult = new FieldValidationResult();
        for (Validator validator : validators) {
            fieldValidationResult.addValidationResult(validator.validate(this));
        }
        return fieldValidationResult;
    }

    public Context getContext() {
        return context;
    }
}