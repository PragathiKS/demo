package com.tetrapak.publicweb.core.servlets;

import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * The Class MasterSiteMapXmlServlet.
 */
@Component(
        service = Servlet.class,
        property = { Constants.SERVICE_DESCRIPTION + "=Master Site Map XML Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.selectors=" + "sitemap",
                "sling.servlet.extensions=" + "xml",
                "sling.servlet.resourceTypes=" + "publicweb/components/structure/pages/publicwebpage" })
public class MasterSiteMapXmlServlet extends SlingSafeMethodsServlet {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MasterSiteMapXmlServlet.class);

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5454240563218059527L;

    /** The Constant SITEMAP_NAMESPACE. */
    private static final String SITEMAP_NAMESPACE = "http://www.sitemaps.org/schemas/sitemap/0.9";

    /** The Constant SITEMAP_XML. */
    public static final String SITEMAP_XML = "/sitemap.xml";

    /**
     * Do get.
     *
     * @param slingRequest
     *            the sling request
     * @param slingResponse
     *            the sling response
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Override
    protected void doGet(SlingHttpServletRequest slingRequest, SlingHttpServletResponse slingResponse)
            throws ServletException, IOException {

        LOGGER.debug("MasterSiteMapXmlServlet :: Inside doGet method");

        slingResponse.setContentType(slingRequest.getResponseContentType());
        final ResourceResolver resourceResolver = slingRequest.getResourceResolver();
        final PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        final Page publicWebRootPage = pageManager.getContainingPage(slingRequest.getResource());

        try {
            final XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
            final XMLStreamWriter xmlStreamWriter = outputFactory.createXMLStreamWriter(slingResponse.getWriter());
            try {
                xmlStreamWriter.writeStartDocument("1.0");
                xmlStreamWriter.writeStartElement(StringUtils.EMPTY, "sitemapindex", SITEMAP_NAMESPACE);
                xmlStreamWriter.writeNamespace(StringUtils.EMPTY, SITEMAP_NAMESPACE);
                if (Objects.nonNull(publicWebRootPage)) {
                    getMarketPages(publicWebRootPage.listChildren(), xmlStreamWriter, slingRequest);
                }
                xmlStreamWriter.writeEndElement();
                xmlStreamWriter.writeEndDocument();
            } finally {
                if(Objects.nonNull(xmlStreamWriter)) {
                    xmlStreamWriter.flush();
                    xmlStreamWriter.close();
                }               
            }
        } catch (XMLStreamException xmlStreamException) {
            LOGGER.error("MasterSiteMapXmlServlet :: Error in doGet method {}", xmlStreamException.getMessage());
        }
    }

    /**
     * Gets the market pages.
     *
     * @param marketPages
     *            the market pages
     * @param xmlStreamWriter
     *            the xml stream writer
     * @param slingRequest
     *            the sling request
     * @return the market pages
     */
    private void getMarketPages(Iterator<Page> marketPages, XMLStreamWriter xmlStreamWriter,
            SlingHttpServletRequest slingRequest) {
        while (marketPages.hasNext()) {
            Page marketPage = marketPages.next();
            if (!marketPage.getName().equalsIgnoreCase(PWConstants.LANG_MASTERS)) {
                Iterator<Page> languagePages = marketPage.listChildren();
                while (languagePages.hasNext()) {
                    Page languagePage = languagePages.next();
                    createSiteMapXml(xmlStreamWriter, slingRequest, languagePage);
                }
            }
        }
    }

    /**
     * Creates the site map xml.
     *
     * @param xmlStreamWriter
     *            the xml stream writer
     * @param slingRequest
     *            the sling request
     * @param languagePage
     *            the language Page
     */
    private void createSiteMapXml(XMLStreamWriter xmlStreamWriter, SlingHttpServletRequest slingRequest,
            Page languagePage) {
        final String siteMapPath = languagePage.getPath();
        try {
            if(!languagePage.getProperties().get(PWConstants.NOINDEX_PROPERTY, "").equals(PWConstants.NOINDEX_VALUE)) {
                writeXML(siteMapPath, xmlStreamWriter, slingRequest);
            }
        } catch (XMLStreamException e) {
            LOGGER.error("MasterSiteMapXmlServlet :: Error while writing XML {}", e.getMessage());
        }
    }

    /**
     * Write XML.
     *
     * @param siteMapPath
     *            the site map path
     * @param xmlStreamWriter
     *            the xml stream writer
     * @param resolver
     *            the resolver
     * @throws XMLStreamException
     *             the XML stream exception
     */
    private void writeXML(String siteMapPath, XMLStreamWriter xmlStreamWriter, SlingHttpServletRequest slingRequest)
            throws XMLStreamException {

        String siteMapURL;
        xmlStreamWriter.writeStartElement(SITEMAP_NAMESPACE, "sitemap");
        siteMapURL = LinkUtils.sanitizeLink(siteMapPath, slingRequest);
        if(siteMapPath.contains("/global/en")) {
            siteMapURL = siteMapURL.concat("/en-global");
        }      
        writeXMLElement(xmlStreamWriter, "loc", siteMapURL.concat(SITEMAP_XML));
        xmlStreamWriter.writeEndElement();
    }

    /**
     * Write XML element.
     *
     * @param xmlStreamWriter
     *            the xml stream writer
     * @param elementName
     *            the element name
     * @param xmlText
     *            the xml text
     * @throws XMLStreamException
     *             the XML stream exception
     */
    private void writeXMLElement(final XMLStreamWriter xmlStreamWriter, final String elementName, final String xmlText)
            throws XMLStreamException {
        xmlStreamWriter.writeStartElement(SITEMAP_NAMESPACE, elementName);
        xmlStreamWriter.writeCharacters(xmlText);
        xmlStreamWriter.writeEndElement();
    }
}
