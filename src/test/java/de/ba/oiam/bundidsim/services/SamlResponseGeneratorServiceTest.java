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
package de.ba.oiam.bundidsim.services;

import de.ba.oiam.bundidsim.model.BundIdUser;
import de.ba.oiam.bundidsim.model.SamlResponseValues;
import de.ba.oiam.bundidsim.model.Status;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest(classes={SamlResponseGeneratorService.class,
        UserFieldDefinitionService.class, UserAttributesService.class})
public class SamlResponseGeneratorServiceTest {

    @Autowired
    private SamlResponseGeneratorService service;

    @Test
    void buildSamlResponseTest() throws Exception {

        SamlResponseValues params = SamlResponseValues.builder()
                .id(UUID.randomUUID().toString())
                .requestId(UUID.randomUUID().toString())
                .ascUrl("https://samltool-ewg.pre.buergerserviceportal.de/saml/SSO")
                .userAuthnLevel("STORK-QAA-Level-4")
                .created(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .idpId("https://samltool-ewg.pre.buergerserviceportal.de")
                .spEntityId("https://samltool-ewg.pre.buergerserviceportal.de")
                .user(UUID.randomUUID().toString())     // hier die BPK2 des users
                .build();

        BundIdUser user = BundIdUser.builder()
                .surname("Müller")
                .givenname("Hans")
                .birthdate("1973-11-15")
                .postalAddress("Berlin Stra0e 12")
                .postalCode("90781")
                .localityName("Nürnberg")
                .country("DE")
                .eidCitizenQaaLevel("STORK-QAA-Level-4")
                .assertionProvedBy("EID")
                .build();

        String samlResponse = service.generateSamlResponse(Status.buildOkStatus(), user, params);

        assertNotNull(samlResponse);
        assertThat(samlResponse).isNotBlank();

        log.debug("encoded SamlResponse [{}]", samlResponse);
    }

}
