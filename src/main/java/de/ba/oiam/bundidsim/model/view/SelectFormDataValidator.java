/*
 * Copyright 2025. IT-Systemhaus der Bundesagentur fuer Arbeit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.ba.oiam.bundidsim.model.view;

import de.ba.oiam.bundidsim.utils.AuthLevelTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
@Slf4j
public class SelectFormDataValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        // return clazz == FormData.class;
        return SelectFormData.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.debug("start validate FormData...");
        SelectFormData formData = (SelectFormData)target;

        // Validierungen
        // - fachlicher Context (Pflichtfeld)
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "domainContext", "");

        // Spezielle Validierung, wenn als Identifizierungsmittel eIDAS ausgewählt wurde
        if (AuthLevelTools.IDENTIFICATION_EIDAS.equalsIgnoreCase(formData.getIdentifikationWith())) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eidasLoa", "");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eidasCountry", "");
        }
    }
}
