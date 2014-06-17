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