package com.tetrapak.customerhub.core.transformer.impl;

import org.apache.cocoon.xml.sax.AttributesImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.rewriter.ProcessingComponentConfiguration;
import org.apache.sling.rewriter.ProcessingContext;
import org.apache.sling.rewriter.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This Class is responsible for transforming the internal links of the pages
 * under /content/tetrapak/customerhub/global/en node with the shortened url
 *
 * @author Swati Lamba
 */
public class CustomLinkTransformer implements Transformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomLinkTransformer.class);
    private ContentHandler contentHandler;

    /**
     * CustomLinkTransformer constructor
     */
    public CustomLinkTransformer() {
        LOGGER.debug("CustomLinkTransformer created");
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        contentHandler.setDocumentLocator(locator);
    }

    @Override
    public void startDocument() throws SAXException {
        contentHandler.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        contentHandler.endDocument();
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        contentHandler.startPrefixMapping(prefix, uri);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        contentHandler.endPrefixMapping(prefix);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        String linkUrl = atts.getValue("href");

        AttributesImpl attrNew = new AttributesImpl(atts);
        if (StringUtils.isNotBlank(linkUrl)) {
            Pattern pattern = Pattern.compile("/content/tetrapak/customerhub/global/en");
            Matcher matcher = pattern.matcher(linkUrl);
            if (matcher.find()) {
                LOGGER.info("Transformed url from: {} to: {}", linkUrl, linkUrl.substring(matcher.end()).trim());
                String changedLinkUrl = "/customerhub" + linkUrl.substring(matcher.end()).trim();
                attrNew.removeAttribute("href");
                attrNew.addAttribute(uri, "href", "href", "string", changedLinkUrl);
            }
        }
        contentHandler.startElement(uri, localName, qName, attrNew);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        contentHandler.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        contentHandler.characters(ch, start, length);
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        contentHandler.ignorableWhitespace(ch, start, length);
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        contentHandler.processingInstruction(target, data);
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        contentHandler.skippedEntity(name);
    }

    @Override
    public void dispose() {
        LOGGER.debug("CustomLinkTransformer disposed");

    }

    @Override
    public void init(ProcessingContext context, ProcessingComponentConfiguration config) throws IOException {
        LOGGER.trace("init");
        LOGGER.debug(context.getRequest().getPathInfo());
    }

    @Override
    public void setContentHandler(ContentHandler ch) {
        this.contentHandler = ch;
    }

}
