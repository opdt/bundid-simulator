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
package de.ba.oiam.bundidsim.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthLevelToolsTest {

    @Test
    void shouldReturnCompleteIdListWhenMinLevelIsStork1() {
        String[][] result = AuthLevelTools.createIdentificationWithList(AuthLevelTools.STORK_1);

        assertEquals(6, result.length);
        assertArrayEquals(new String[]{AuthLevelTools.IDENTIFICATION_BENUTZERKENNUNG, "Benutzername"}, result[0]);
        assertArrayEquals(new String[]{AuthLevelTools.IDENTIFICATION_ELSTER, "Elster"}, result[1]);
        assertArrayEquals(new String[]{AuthLevelTools.IDENTIFICATION_SMARTEID, "Smart eID"}, result[2]);
        assertArrayEquals(new String[]{AuthLevelTools.IDENTIFICATION_EID, "EID (Ausweis)"}, result[3]);
        assertArrayEquals(new String[]{AuthLevelTools.IDENTIFICATION_EIDAS, "eIDAS"}, result[4]);
        assertArrayEquals(new String[]{AuthLevelTools.IDENTIFICATION_FINK, "FINK"}, result[5]);
    }

    @Test
    void shouldReturnReducedIdListWhenMinLevelIsStork3() {
        String[][] result = AuthLevelTools.createIdentificationWithList(AuthLevelTools.STORK_3);

        assertEquals(5, result.length);
        assertArrayEquals(new String[]{AuthLevelTools.IDENTIFICATION_ELSTER, "Elster"}, result[0]);
        assertArrayEquals(new String[]{AuthLevelTools.IDENTIFICATION_SMARTEID, "Smart eID"}, result[1]);
        assertArrayEquals(new String[]{AuthLevelTools.IDENTIFICATION_EID, "EID (Ausweis)"}, result[2]);
        assertArrayEquals(new String[]{AuthLevelTools.IDENTIFICATION_EIDAS, "eIDAS"}, result[3]);
        assertArrayEquals(new String[]{AuthLevelTools.IDENTIFICATION_FINK, "FINK"}, result[4]);
    }

    @Test
    void shouldReturnStork4ForHighLevelIdentificationMethods() {
        assertEquals(AuthLevelTools.STORK_4, AuthLevelTools.getAuthnLevelByIdentificationMethod(AuthLevelTools.IDENTIFICATION_EIDAS));
        assertEquals(AuthLevelTools.STORK_4, AuthLevelTools.getAuthnLevelByIdentificationMethod(AuthLevelTools.IDENTIFICATION_EID));
        assertEquals(AuthLevelTools.STORK_4, AuthLevelTools.getAuthnLevelByIdentificationMethod(AuthLevelTools.IDENTIFICATION_FINK));
    }

}
