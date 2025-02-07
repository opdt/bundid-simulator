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
package de.ba.oiam.bundidsim.model;

import lombok.*;

/**
 * Repräsentation eines BundID-Users. Besteht aus Personen-, Status- und Identifizierungsdaten. Objekt wird durch
 * Form bearbeitet.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BundIdUser {

    String id;  // Datensatz-ID

    // personenbezogene Daten (auch aus Repo)
    private String surname;         // Nachname
    private String givenname;       // Vornamen
    private String mail;            // Email-Adresse (optionale)
    private String postalAddress;   // Strasse+Hnr (optional)
    private String postalCode;      // Plz
    private String localityName;    // Wohnort
    private String country;         // Land (Adresse)
    private String personalTitle;   // Akad. Titel (optional)
    private String gender;          // Anrede
    private String birthdate;       // Geburtsdatum
    private String placeOfBirth;    // Geburtsort (optional)
    private String birthName;       // Geburtsname
    private String bpk2;                // Personenschlüssel

    // technische Nutzdaten BundID-Response
    private String eidasIssuingCountry; // Eidas-Land
    private String eidCitizenQaaLevel;  // Vetrauensniveau
    private String assertionProvedBy;   // Identifizierungsart
    private String version;             // Datenmodell-Version (fix)

}
