package com.tetrapak.customerhub.core.authentication;

import org.apache.commons.compress.utils.Charsets;
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
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * SAML Response post processor
 *
 * @author tustusha
 */
@Component(immediate = true, service = AuthenticationInfoPostProcessor.class)
public class SAMLResponsePostProcessor implements AuthenticationInfoPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SAMLResponsePostProcessor.class);
    private static final int MAX_FIRSTLEVEL_CHILD_COUNT = 10;

    @Reference
    private SlingSettingsService slingSettingsService;

    public void postProcess(AuthenticationInfo info, HttpServletRequest request, HttpServletResponse response) {
        HttpServletRequest httpRequest;
        Map<String, String> attrMap = new HashMap<>();
        try {
            LOGGER.debug("SAMLResponse Post Processor invoked");
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
                    LOGGER.debug("SAMLResponse parameter is empty of null!");
                }
                String firstName = StringUtils.isNoneBlank(attrMap.get("firstname")) ? attrMap.get("firstname")
                        : StringUtils.EMPTY;
                String lastName = StringUtils.isNoneBlank(attrMap.get("lastname")) ? attrMap.get("lastname")
                        : StringUtils.EMPTY;
                String customerName = URLEncoder.encode(firstName + " " + lastName, "UTF-8").replaceAll("\\+", "%20");

                if (StringUtils.isNotBlank(firstName) || StringUtils.isNotBlank(lastName)) {
                    Cookie samlCookie = new Cookie("CustomerName", customerName);
                    samlCookie.setHttpOnly(true);
                    samlCookie.setPath("/");
                    response.addCookie(samlCookie);
                }
                if (StringUtils.isNotBlank(attrMap.get("accesstoken"))) {
                    Cookie acctoken = new Cookie("acctoken", attrMap.get("accesstoken"));
                    acctoken.setHttpOnly(true);
                    acctoken.setPath("/");
                    response.addCookie(acctoken);
                }
            }
        } catch (ParserConfigurationException parserConfiExep) {
            LOGGER.error("Unable to get Document Builder ", parserConfiExep);
        } catch (SAXException saxExcep) {
            LOGGER.error("Unable to parse the xml document ", saxExcep);
        } catch (IOException iOExcep) {
            LOGGER.error("IOException ", iOExcep);
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
        try {
            documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        } catch (ParserConfigurationException ignore) {
            LOGGER.error("ParserConfigurationException ", ignore);
        }
        documentBuilderFactory.setXIncludeAware(false);
        documentBuilderFactory.setExpandEntityReferences(false);
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
        Node samlAssertionNode = samlAssertion.item(0);
        NodeList childNodes = samlAssertionNode.getChildNodes();

        int maxChildNodeCount = childNodes.getLength();
        if (maxChildNodeCount <= MAX_FIRSTLEVEL_CHILD_COUNT) {
            for (int childCount = 0; childCount < maxChildNodeCount; childCount++) {
                Node subChildNode = childNodes.item(childCount);
                if ("saml:AttributeStatement".equalsIgnoreCase(subChildNode.getNodeName())) {
                    getNodes(samlAttributeMap, subChildNode);
                }
            }
        }
        // [#3954] FIX END: Unchecked Input for Loop Condition
        return samlAttributeMap;
    }

    private void getNodes(Map<String, String> samlAttributeMap, Node attributeStatementNode) {
        NodeList attributeStatementChildNodes = attributeStatementNode.getChildNodes();

        int maxChildNodeCount = attributeStatementChildNodes.getLength();
        if (maxChildNodeCount <= MAX_FIRSTLEVEL_CHILD_COUNT) {
            for (int childCount = 0; childCount < maxChildNodeCount; childCount++) {
                Node childNode = attributeStatementChildNodes.item(childCount);
                if ("saml:Attribute".equalsIgnoreCase(childNode.getNodeName())) {
                    String attributeValue = childNode.getAttributes().item(0).getNodeValue();
                    NodeList attrValNodeList = childNode.getChildNodes();
                    int maxNodeCount = attrValNodeList.getLength() > MAX_FIRSTLEVEL_CHILD_COUNT ? MAX_FIRSTLEVEL_CHILD_COUNT
                            : attrValNodeList.getLength();
                    putSAMLAttributes(samlAttributeMap, attributeValue, attrValNodeList, maxNodeCount);
                }
            }
        }
    }

    private void putSAMLAttributes(Map<String, String> samlAttributeMap, String attributeValue,
                                   NodeList attrValNodeList, int maxNodeCount) {
        for (int attrValNodeCount = 0; attrValNodeCount < maxNodeCount; attrValNodeCount++) {
            putAttributes(samlAttributeMap, attributeValue, attrValNodeList, attrValNodeCount);
        }
    }

    private void putAttributes(Map<String, String> samlAttributeMap, String attributeValue,
                               NodeList attributeValueNodeList, int attributeValueCount) {
        Node currentNode = attributeValueNodeList.item(attributeValueCount);
        if ("saml:AttributeValue".equalsIgnoreCase(currentNode.getNodeName())) {
            samlAttributeMap.put(attributeValue, currentNode.getTextContent());
            LOGGER.debug("SAML Assertions" + attributeValue + " : " + currentNode.getTextContent());
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
