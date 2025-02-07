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

import lombok.*;

/**
 * Formulardaten für die Personen-Schnellauswahl (Context /saml).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SelectFormData {

  public static final String STATUS_OK = "OK";
  public static final String STATUS_CANCEL = "CANCEL";
  public static final String STATUS_ERROR = "ERROR";

  private String samlRequest;
  private String status;              // Form-Status
  private String userId;              // ausgewählter User
  private String identifikationWith;  // Identifzierungsmittel

  private String domainContext;       // fachlicher Kontext als Suffix an die bpk2 und email
  private String eidasCountry;
  private String eidasLoa;
}
