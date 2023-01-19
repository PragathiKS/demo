package com.tetrapak.supplierportal.core.authentication;

import com.day.cq.commons.Externalizer;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import org.apache.commons.compress.utils.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.auth.core.spi.AuthenticationInfo;
import org.apache.sling.auth.core.spi.AuthenticationInfoPostProcessor;
import org.apache.sling.settings.SlingSettingsService;
import org.eclipse.jetty.util.StringUtil;
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

@Component(immediate = true, service = AuthenticationInfoPostProcessor.class)
public class SupplierPortalSAMLResponsePostProcessor implements AuthenticationInfoPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierPortalSAMLResponsePostProcessor.class);
    private static final int MAX_FIRSTLEVEL_CHILD_COUNT = 10;
    private static final String LOCATION_HEADER = "Location";
    private static final String SAML_LOGIN = "supplierportal/saml_login";
    private static final String TOKEN_VALUE = "accesstoken";
    private static final String SAML_RESPONSE = "SAMLResponse";
    private static final String EMPTY = "empty";
    private static final String FIRST_NAME = "firstname";
    private static final String LAST_NAME = "lastname";
    private static final String SAML_ATTRIBUTE_VALUE = "saml:AttributeValue";
    private static final String SAML_ATTRIBUTE = "saml:Attribute";
    private static final String SAML_ATTRIBUTE_STATEMENT = "saml:AttributeStatement";
    private static final String SAML_ASSERTION = "saml:Assertion";
    private static final String LOGOUT = "logout";
    private static final String TOKEN_NAME = "acctoken";

    @Reference private SlingSettingsService slingSettingsService;

    public void postProcess(AuthenticationInfo info, HttpServletRequest request, HttpServletResponse response) {
        HttpServletRequest httpRequest;
        Map<String, String> attrMap = new HashMap<>();
        try {
            LOGGER.debug("SAMLResponse Post Processor invoked");
            String url = request.getRequestURI();
            String processedURL = setSAMLRequestPathCookie(request, response, url);

            httpRequest = request;
            String pathInfo = httpRequest.getRequestURI();
            Set<String> runModes = slingSettingsService.getRunModes();
            String base64DecodedResponse;
            if (runModes.contains(Externalizer.PUBLISH) && StringUtils.isNotEmpty(pathInfo) && pathInfo.contains(
                    SAML_LOGIN)) {
                LOGGER.info("SAMLResponse Post Processor processing ...");
                String responseSAMLMessage = httpRequest.getParameter(SAML_RESPONSE);
                if (StringUtils.isNotEmpty(responseSAMLMessage)) {
                    LOGGER.debug("responseSAMLMessage: {}", responseSAMLMessage);
                    base64DecodedResponse = decodeStr(responseSAMLMessage);
                    LOGGER.debug("base64DecodedResponse: {}", base64DecodedResponse);
                    attrMap = parseSAMLResponse(base64DecodedResponse);
                } else {
                    LOGGER.debug("SAMLResponse parameter is empty of null!");
                }
                setCustomerNameCookie(response, attrMap);
                setAccesTokenCookie(response, attrMap);
                if (processedURL.contains(EMPTY)) {
                    response.setHeader(LOCATION_HEADER, SupplierPortalConstants.HTTPS + "://" + request.getServerName() + processedURL);
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

    private static void setAccesTokenCookie(HttpServletResponse response, Map<String, String> attrMap) {
        if (StringUtils.isNotBlank(attrMap.get(TOKEN_VALUE))) {
            Cookie accToken = new Cookie(TOKEN_NAME, attrMap.get(TOKEN_VALUE));
            accToken.setPath("/");
            final int SECONDS = 900;
            accToken.setMaxAge(SECONDS);
            response.addCookie(accToken);
        }
    }

    private static void setCustomerNameCookie(HttpServletResponse response, Map<String, String> attrMap)
            throws UnsupportedEncodingException {
        String firstName = StringUtils.EMPTY;
        if (StringUtils.isNoneBlank(attrMap.get(FIRST_NAME))) {
            firstName = attrMap.get(FIRST_NAME);
        }
        String lastName = StringUtils.EMPTY;
        if (StringUtils.isNoneBlank(attrMap.get(LAST_NAME))) {
            lastName = attrMap.get(LAST_NAME);
        }

        String customerName = URLEncoder.encode(firstName + " " + lastName, "UTF-8").replaceAll("\\+", "%20");

        if (StringUtils.isNotBlank(firstName) || StringUtils.isNotBlank(lastName)) {
            Cookie samlCookie = new Cookie(SupplierPortalConstants.COOKIE_NAME, customerName);
            samlCookie.setHttpOnly(true);
            samlCookie.setDomain(SupplierPortalConstants.DOMAIN_NAME);
            samlCookie.setPath("/");
            response.addCookie(samlCookie);
        }
    }

    private static String setSAMLRequestPathCookie(HttpServletRequest request, HttpServletResponse response,
            String url) {
        StringBuilder processedUrl = new StringBuilder();
        if (isValidURL(url)) {
            LOGGER.debug("request URI {}", url);
            processedUrl.append(StringUtils.substringBetween(url, "/en", StringUtils.substringAfter(url, ".")))
                    .append("html");
            String queryString = request.getQueryString();
            if (StringUtil.isNotBlank(queryString)) {
                processedUrl.append("?").append(queryString);
            }

            Cookie samlRequestPath = new Cookie(SupplierPortalConstants.SAML_REQUEST_PATH, processedUrl.toString());
            samlRequestPath.setHttpOnly(true);
            samlRequestPath.setPath("/");
            response.addCookie(samlRequestPath);
        }
        return processedUrl.toString();
    }

    private static boolean isValidURL(String url) {
        return url.contains(SupplierPortalConstants.CONTENT_ROOT_PATH) && !url.contains(LOGOUT) && !url.contains(EMPTY)
                && url.endsWith(SupplierPortalConstants.HTML_EXTENSION);
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
    private static Map<String, String> parseSAMLResponse(String base64DecodedResponse)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        } catch (ParserConfigurationException e) {
            LOGGER.error("ParserConfigurationException ", e);
        }
        documentBuilderFactory.setXIncludeAware(false);
        documentBuilderFactory.setExpandEntityReferences(false);
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
        Map<String, String> samlAttributeMap = new HashMap<>();
        StringReader strReader = new StringReader(base64DecodedResponse);
        InputSource inputSource = new InputSource(strReader);
        Document document = docBuilder.parse(inputSource);
        NodeList samlAssertion = document.getElementsByTagName(SAML_ASSERTION);
        return populateSAMLAttrMap(samlAttributeMap, samlAssertion);
    }

    /**
     * This method would populate the SAML attribute map object based on the
     * attributes present in the response.
     *
     * @param samlAttributeMap samlAttributeMap
     * @param samlAssertion    samlAssertion
     */
    private static Map<String, String> populateSAMLAttrMap(Map<String, String> samlAttributeMap,
            NodeList samlAssertion) {
        Node samlAssertionNode = samlAssertion.item(0);
        NodeList childNodes = samlAssertionNode.getChildNodes();

        int maxChildNodeCount = childNodes.getLength();
        if (maxChildNodeCount <= MAX_FIRSTLEVEL_CHILD_COUNT) {
            for (int childCount = 0; childCount < maxChildNodeCount; childCount++) {
                Node subChildNode = childNodes.item(childCount);
                if (SAML_ATTRIBUTE_STATEMENT.equalsIgnoreCase(subChildNode.getNodeName())) {
                    getNodes(samlAttributeMap, subChildNode);
                }
            }
        }
        return samlAttributeMap;
    }

    private static void getNodes(Map<String, String> samlAttributeMap, Node attributeStatementNode) {
        NodeList attributeStatementChildNodes = attributeStatementNode.getChildNodes();

        int maxChildNodeCount = attributeStatementChildNodes.getLength();
        if (maxChildNodeCount <= MAX_FIRSTLEVEL_CHILD_COUNT) {
            for (int childCount = 0; childCount < maxChildNodeCount; childCount++) {
                Node childNode = attributeStatementChildNodes.item(childCount);
                if (SAML_ATTRIBUTE.equalsIgnoreCase(childNode.getNodeName())) {
                    String attributeValue = childNode.getAttributes().item(0).getNodeValue();
                    NodeList attrValNodeList = childNode.getChildNodes();
                    int maxNodeCount = Math.min(attrValNodeList.getLength(), MAX_FIRSTLEVEL_CHILD_COUNT);
                    putSAMLAttributes(samlAttributeMap, attributeValue, attrValNodeList, maxNodeCount);
                }
            }
        }
    }

    private static void putSAMLAttributes(Map<String, String> samlAttributeMap, String attributeValue,
            NodeList attrValNodeList, int maxNodeCount) {
        for (int attrValNodeCount = 0; attrValNodeCount < maxNodeCount; attrValNodeCount++) {
            putAttributes(samlAttributeMap, attributeValue, attrValNodeList, attrValNodeCount);
        }
    }

    private static void putAttributes(Map<String, String> samlAttributeMap, String attributeValue,
            NodeList attributeValueNodeList, int attributeValueCount) {
        Node currentNode = attributeValueNodeList.item(attributeValueCount);
        if (SAML_ATTRIBUTE_VALUE.equalsIgnoreCase(currentNode.getNodeName())) {
            samlAttributeMap.put(attributeValue, currentNode.getTextContent());
            LOGGER.debug("SAML Assertions {} : {}", attributeValue, currentNode.getTextContent());
        }
    }

    private static String decodeStr(String encodedStr) {
        org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
        byte[] base64DecodedByteArray = base64.decode(encodedStr);
        return new String(base64DecodedByteArray, Charsets.UTF_8);
    }

}
