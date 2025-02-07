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
package de.ba.oiam.bundidsim.controller;

import de.ba.oiam.bundidsim.model.BundIdUser;
import de.ba.oiam.bundidsim.model.SamlRequestValues;
import de.ba.oiam.bundidsim.model.view.SelectFormData;
import de.ba.oiam.bundidsim.services.UserDefinitionService;
import de.ba.oiam.bundidsim.utils.AuthLevelTools;
import de.ba.oiam.bundidsim.utils.ObjectStringConverter;
import de.ba.oiam.bundidsim.utils.XmlParserTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;

import java.util.Base64;
import java.util.List;

/**
 * Webcontroller für den Einstiegspunkt "/saml" via POST und GET (nur Test)
 */
@Controller
@Slf4j
public class SamlController {

    @Autowired
    private UserDefinitionService userService;

    /**
     * GET /saml Einstiegspunkt für den Test.
     * Die Parameter "SamlRequest" und "RelayState" sind ein Fake. Mit diesem Aufruf kann die Web-Ui im Browser
     * ohne Keycloak-Anbindung getestet werden. Die Aktion "Person übernehmen" funktioniert nicht korrekt, da die
     * Rücksprung-Url im hier definierten SAML-Request ungültig ist.
     *
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(path = "/saml")
    public String samlRequestReceiverGet(Model model) throws Exception {

        String paramSamlRequest =
                "PHNhbWxwOkF1dGhuUmVxdWVzdCB4bWxuczpzYW1scD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOnByb3RvY29sIiB4bWxucz0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOmFzc2VydGlvbiIgeG1sbnM6c2FtbD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOmFzc2VydGlvbiIgQXNzZXJ0aW9uQ29uc3VtZXJTZXJ2aWNlVVJMPSJodHRwczovL3NlY3VyZS5kZXYub2NwLndlYmFwcC5pZHN0LmliYWludGVybi5kZS9laWQtaW5mcmEvYXV0aC9yZWFsbXMvQnVuZElELUlkZW50L2Jyb2tlci9idW5kaWQvZW5kcG9pbnQiIEF0dHJpYnV0ZUNvbnN1bWluZ1NlcnZpY2VJbmRleD0iMCIgRGVzdGluYXRpb249Imh0dHBzOi8vc2VjdXJlLmRldi5vY3Aud2ViYXBwLmlkc3QuaWJhaW50ZXJuLmRlL2VpZC1pbmZyYS9pZHBtb2NrL3NhbWwiIEZvcmNlQXV0aG49ImZhbHNlIiBJRD0iSURfNTVhYTFkODYtYmIzZi00YmJkLTkyMDktNWNlZTVkMzMxN2E3IiBJc3N1ZUluc3RhbnQ9IjIwMjMtMTItMTFUMTM6MzM6MjQuODUyWiIgUHJvdG9jb2xCaW5kaW5nPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YmluZGluZ3M6SFRUUC1QT1NUIiBWZXJzaW9uPSIyLjAiPjxzYW1sOklzc3Vlcj5odHRwczovL3NlY3VyZS5kZXYub2NwLndlYmFwcC5pZHN0LmliYWludGVybi5kZS9laWQtaW5mcmEvYXV0aC9yZWFsbXMvQnVuZElELUlkZW50PC9zYW1sOklzc3Vlcj48c2FtbHA6RXh0ZW5zaW9ucz48YWtkYjpBdXRoZW50aWNhdGlvblJlcXVlc3QgeG1sbnM6YWtkYj0iaHR0cHM6Ly93d3cuYWtkYi5kZS9yZXF1ZXN0LzIwMTgvMDkiIFZlcnNpb249IjIiPjxha2RiOlJlcXVlc3RlZEF0dHJpYnV0ZXM+PGFrZGI6UmVxdWVzdGVkQXR0cmlidXRlIE5hbWU9InVybjpvaWQ6MS4yLjQwLjAuMTAuMi4xLjEuMjI1NTk5IiBSZXF1aXJlZEF0dHJpYnV0ZT0iZmFsc2UiLz48YWtkYjpSZXF1ZXN0ZWRBdHRyaWJ1dGUgTmFtZT0idXJuOm9pZDoxLjMuNi4xLjQuMS4yNTQ4NC40OTQ0NTAuMTAuMSIgUmVxdWlyZWRBdHRyaWJ1dGU9ImZhbHNlIi8+PGFrZGI6UmVxdWVzdGVkQXR0cmlidXRlIE5hbWU9InVybjpvaWQ6MS4yLjQwLjAuMTAuMi4xLjEuMjYxLjk0IiBSZXF1aXJlZEF0dHJpYnV0ZT0iZmFsc2UiLz48YWtkYjpSZXF1ZXN0ZWRBdHRyaWJ1dGUgTmFtZT0idXJuOm9pZDoyLjUuNC43IiBSZXF1aXJlZEF0dHJpYnV0ZT0iZmFsc2UiLz48YWtkYjpSZXF1ZXN0ZWRBdHRyaWJ1dGUgTmFtZT0idXJuOm9pZDoyLjUuNC4xNiIgUmVxdWlyZWRBdHRyaWJ1dGU9ImZhbHNlIi8+PGFrZGI6UmVxdWVzdGVkQXR0cmlidXRlIE5hbWU9InVybjpvaWQ6MS4zLjYuMS40LjEuMjU0ODQuNDk0NDUwLjIiIFJlcXVpcmVkQXR0cmlidXRlPSJmYWxzZSIvPjxha2RiOlJlcXVlc3RlZEF0dHJpYnV0ZSBOYW1lPSJ1cm46b2lkOjIuNS40LjQiIFJlcXVpcmVkQXR0cmlidXRlPSJmYWxzZSIvPjxha2RiOlJlcXVlc3RlZEF0dHJpYnV0ZSBOYW1lPSJ1cm46b2lkOjEuMy42LjEuNS41LjcuOS4yIiBSZXF1aXJlZEF0dHJpYnV0ZT0iZmFsc2UiLz48YWtkYjpSZXF1ZXN0ZWRBdHRyaWJ1dGUgTmFtZT0idXJuOm9pZDoyLjUuNC4xNyIgUmVxdWlyZWRBdHRyaWJ1dGU9ImZhbHNlIi8+PGFrZGI6UmVxdWVzdGVkQXR0cmlidXRlIE5hbWU9InVybjpvaWQ6MC45LjIzNDIuMTkyMDAzMDAuMTAwLjEuNDAiIFJlcXVpcmVkQXR0cmlidXRlPSJmYWxzZSIvPjxha2RiOlJlcXVlc3RlZEF0dHJpYnV0ZSBOYW1lPSJ1cm46b2lkOjEuMi40MC4wLjEwLjIuMS4xLjU1IiBSZXF1aXJlZEF0dHJpYnV0ZT0iZmFsc2UiLz48YWtkYjpSZXF1ZXN0ZWRBdHRyaWJ1dGUgTmFtZT0idXJuOm9pZDoyLjUuNC40MiIgUmVxdWlyZWRBdHRyaWJ1dGU9ImZhbHNlIi8+PGFrZGI6UmVxdWVzdGVkQXR0cmlidXRlIE5hbWU9InVybjpvaWQ6MS4yLjQwLjAuMTAuMi4xLjEuMjI1NTY2IiBSZXF1aXJlZEF0dHJpYnV0ZT0iZmFsc2UiLz48L2FrZGI6UmVxdWVzdGVkQXR0cmlidXRlcz48L2FrZGI6QXV0aGVudGljYXRpb25SZXF1ZXN0Pjwvc2FtbHA6RXh0ZW5zaW9ucz48c2FtbHA6TmFtZUlEUG9saWN5IEFsbG93Q3JlYXRlPSJmYWxzZSIgRm9ybWF0PSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoxLjE6bmFtZWlkLWZvcm1hdDp1bnNwZWNpZmllZCIvPjxzYW1scDpSZXF1ZXN0ZWRBdXRobkNvbnRleHQgQ29tcGFyaXNvbj0ibWluaW11bSI+PHNhbWw6QXV0aG5Db250ZXh0Q2xhc3NSZWY+U1RPUkstUUFBLUxldmVsLTM8L3NhbWw6QXV0aG5Db250ZXh0Q2xhc3NSZWY+PC9zYW1scDpSZXF1ZXN0ZWRBdXRobkNvbnRleHQ+PC9zYW1scDpBdXRoblJlcXVlc3Q+";
        String paramRelayState =
                "lwN0_rRBl5jHaEJt4a0V6IRFOkaR3Krw4K9HQ-73NSM.1gou_vqz1yw.ocp-api-client-public";
        return samlRequestReceiver(model, paramSamlRequest, paramRelayState);
    }

    /**
     * POST /saml Einstiegspunkt in den BundID-Simulator. Der Endpunkt wird mit dem Parametern "SAMLRequest"
     * (siehe SamlRequest in der BundID-Spec) und "RelayState" aufgerufen.
     *
     * @param samlRequest SAML-Request (Base64)
     * @param state       Relaystate
     */
    @PostMapping(path = "/saml")
    public String samlRequestReceiver(Model model,
                                      @RequestParam(value = "SAMLRequest", required = false) String samlRequest,
                                      @RequestParam(value = "RelayState", required = false) String state)
            throws Exception {

        log.debug("Start samlRequestReceiver...");
        log.debug("RelayState: [{}]", state);

        // SAML-Request analysieren und Werte extrahieren
        SamlRequestValues samlRequestModel = analyseSamlRequest(samlRequest, state);

        // Default-Wert für ReqAuthnLevel setzen
        if (!StringUtils.hasText(samlRequestModel.getReqAuthnLevel())) {
            samlRequestModel.setReqAuthnLevel(AuthLevelTools.STORK_1);
        }

        List<BundIdUser> userList = userService.getUserList();
        SelectFormData formData =
                SelectFormData.builder()
                        .status(SelectFormData.STATUS_OK)
                        .userId(userList.getFirst().getId()) // Erster User-Eintrag aktiv
                        .identifikationWith(AuthLevelTools.IDENTIFICATION_EID)
                        .samlRequest(ObjectStringConverter.serializeAndEncode(samlRequestModel))
                        .build();

        model.addAttribute("formdata", formData);
        model.addAttribute("userlist", userList);
        model.addAttribute(
                "identWithList",
                AuthLevelTools.createIdentificationWithList(samlRequestModel.getReqAuthnLevel()));
        log.debug("Model: [{}]", formData.toString());

        return "select_view";
    }

    // Helper **********************************************************************************************************

    /**
     * Der encodierte SAML-Request wird dekodiert und XML-geparst. Die wichtigsten Daten werden in SamlRequestValues
     * gespeichert.
     *
     * @param samlRequest
     * @param state
     * @return
     * @throws Exception
     */
    private SamlRequestValues analyseSamlRequest(String samlRequest, String state) throws Exception {
        String decodedSamlRequest = new String(Base64.getDecoder().decode(samlRequest));
        Document doc = XmlParserTools.parseXML(decodedSamlRequest);

        String serviceUrl = doc.getDocumentElement().getAttribute("AssertionConsumerServiceURL");
        String id = doc.getDocumentElement().getAttribute("ID");
        String valueIssuer = XmlParserTools.findValueByTagname(doc, "saml:Issuer");
        String valueReqAutnLevel =
                XmlParserTools.findValueByTagname(doc, "samlp:RequestedAuthnContext");

        SamlRequestValues samlRequestModel =
                SamlRequestValues.builder()
                        .id(doc.getDocumentElement().getAttribute("ID"))
                        .issuer(XmlParserTools.findValueByTagname(doc, "saml:Issuer"))
                        .reqAuthnLevel(XmlParserTools.findValueByTagname(doc, "samlp:RequestedAuthnContext"))
                        .ascUrl(doc.getDocumentElement().getAttribute("AssertionConsumerServiceURL"))
                        .relayState(state)
                        .build();
        log.debug("model samrequest: [{}]", samlRequestModel);
        return samlRequestModel;
    }
}
