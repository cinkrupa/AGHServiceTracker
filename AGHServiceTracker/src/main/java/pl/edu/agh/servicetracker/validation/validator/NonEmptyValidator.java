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