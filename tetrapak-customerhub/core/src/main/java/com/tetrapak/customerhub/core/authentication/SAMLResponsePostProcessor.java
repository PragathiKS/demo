package com.tetrapak.customerhub.core.authentication;

import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.auth.core.spi.AuthenticationInfo;
import org.apache.sling.auth.core.spi.AuthenticationInfoPostProcessor;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * SAML Response post processor
 *
 * @author Tushar
 */
@Component(immediate = true, service = AuthenticationInfoPostProcessor.class)
public class SAMLResponsePostProcessor implements AuthenticationInfoPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SAMLResponsePostProcessor.class);

    @Reference
    private SlingSettingsService slingSettingsService;

    public void postProcess(AuthenticationInfo info, HttpServletRequest request, HttpServletResponse response) {
        HttpServletRequest httpRequest;
        Map<String, String> attrMap = new HashMap<>();
        try {
            LOGGER.info("SAMLResponse Post Processor invoked");
            httpRequest = request;
            String pathInfo = httpRequest.getRequestURI();
            Set<String> runModes = slingSettingsService.getRunModes();
            if (runModes.contains("publish") && StringUtils.isNotEmpty(pathInfo) && pathInfo.contains("saml_login")) {
                LOGGER.info("SAMLResponse Post Processor processing ...");
                String responseSAMLMessage = httpRequest.getParameter("SAMLResponse");
                if (StringUtils.isNotEmpty(responseSAMLMessage)) {
                    LOGGER.debug("responseSAMLMessage:" + responseSAMLMessage);
                    String base64DecodedResponse = decodeStr(responseSAMLMessage);
                    LOGGER.debug("base64DecodedResponse:" + base64DecodedResponse);
                    attrMap = parseSAMLResponse(base64DecodedResponse);
                } else {
                    LOGGER.info("responseSAMLMessage is empty or null");
                }
                String firstName = StringUtils.isNoneBlank(attrMap.get("firstname")) ? attrMap.get("firstname") : StringUtils.EMPTY;
                String lastName = StringUtils.isNoneBlank(attrMap.get("lastname")) ? attrMap.get("lastname") : StringUtils.EMPTY;

                if (StringUtils.isNotBlank(firstName) || StringUtils.isNotBlank(lastName)) {
                    Cookie samlCookie = new Cookie("CustomerName", firstName + "-" + lastName);
                    response.addCookie(samlCookie);
                }

                if (StringUtils.isNotBlank(attrMap.get("BusinessPartnerID"))) {
                    Cookie bPNumber = new Cookie("BPN", attrMap.get("BusinessPartnerID"));
                    response.addCookie(bPNumber);
                }

            }
        } catch (ParserConfigurationException e) {
            LOGGER.error("Unable to get Document Builder ", e);
        } catch (SAXException e) {
            LOGGER.error("Unable to parse the xml document ", e);
        } catch (IOException e) {
            LOGGER.error("IOException ", e);
        }

    }

    /**
     * This method will parse the SAML response to create the Cookie by reading the
     * attributes.
     *
     * @param base64DecodedResponse decoded response
     * @return parsed SAML Response
     * @throws ParserConfigurationException ParserConfigurationException
     * @throws SAXException                 SAXException
     * @throws IOException                  IOException
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    private Map<String, String> parseSAMLResponse(String base64DecodedResponse)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
        Map<String, String> samlAttributeMap = new HashMap<>();
        StringReader strReader = new StringReader(base64DecodedResponse);
        InputSource inputSource = new InputSource(strReader);
        Document document = docBuilder.parse(inputSource);
        NodeList samlAssertion = document.getElementsByTagName("saml:Assertion");
        return populateSAMLAttrMap(samlAttributeMap, samlAssertion);
    }

    /**
     * This method would populate the SAML attribute map object based on the
     * attributes present in the response.
     *
     * @param samlAttributeMap samlAttributeMap
     * @param samlAssertion    samlAssertion
     */
    private Map<String, String> populateSAMLAttrMap(Map<String, String> samlAttributeMap, NodeList samlAssertion) {
        for (int i = 0; i < samlAssertion.getLength(); i++) {
            Node item = samlAssertion.item(i);
            NodeList childNodes = item.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                Node subChildNode = childNodes.item(j);
                if ("saml:AttributeStatement".equalsIgnoreCase(subChildNode.getNodeName())) {
                    getNodes(samlAttributeMap, subChildNode);
                }
            }
        }
        return samlAttributeMap;
    }

    private void getNodes(Map<String, String> samlAttributeMap, Node subChildNode) {
        NodeList childNodes2 = subChildNode.getChildNodes();
        for (int k = 0; k < childNodes2.getLength(); k++) {
            Node item2 = childNodes2.item(k);
            if ("saml:Attribute".equalsIgnoreCase(item2.getNodeName())) {
                String attributeValue = item2.getAttributes().item(0).getNodeValue();
                NodeList attributeValueNodeList = item2.getChildNodes();
                for (int l = 0; l < attributeValueNodeList.getLength(); l++) {
                    putSamlAttributes(samlAttributeMap, attributeValue, attributeValueNodeList, l);
                }
            }
        }
    }

    private void putSamlAttributes(Map<String, String> samlAttributeMap, String attributeValue, NodeList attributeValueNodeList, int l) {
        if ("saml:AttributeValue".equalsIgnoreCase(attributeValueNodeList.item(l).getNodeName())) {
            samlAttributeMap.put(attributeValue,
                    attributeValueNodeList.item(l).getTextContent());
            LOGGER.info("SAML Assertions" + attributeValue + " : " +
                    attributeValueNodeList.item(l).getTextContent());
        }
    }

    /**
     * This method would decode the SAML response.
     *
     * @param encodedStr encoded SAML response
     * @return string decoded SAML response
     */
    public static String decodeStr(String encodedStr) {
        org.apache.commons.codec.binary.Base64 base64Decoder = new org.apache.commons.codec.binary.Base64();
        byte[] base64DecodedByteArray = base64Decoder.decode(encodedStr);
        return new String(base64DecodedByteArray, Charsets.UTF_8);
    }

}
