package com.tetrapak.supplierportal.core.transformer;

import com.day.cq.rewriter.pipeline.AttributesImpl;
import com.day.cq.wcm.api.WCMMode;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import com.tetrapak.supplierportal.core.utils.LinkUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.rewriter.ProcessingComponentConfiguration;
import org.apache.sling.rewriter.ProcessingContext;
import org.apache.sling.rewriter.Transformer;
import org.apache.sling.rewriter.TransformerFactory;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * A factory for creating SupplierPortalLinkTransformer objects.
 */
@Component(immediate = true, service = TransformerFactory.class, property = { "pipeline.type=mtplinkrewriter" })
public class SupplierPortalLinkTransformerFactory implements TransformerFactory {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierPortalLinkTransformerFactory.class);

    /**
     * Creates a new MTPLinkTransformer object.
     *
     * @return the transformer
     */
    @Override
    public Transformer createTransformer() {
        LOGGER.debug("SupplierPortalLink Transformer called");
        return new SupplierPortalLinkRewriterTransformer();
    }

    /**
     * The Class SupplierPortalLinkRewriterTransformer.
     */
    private class SupplierPortalLinkRewriterTransformer implements Transformer {

        /** The content handler. */
        private ContentHandler contentHandler;

        /** The request. */
        private SlingHttpServletRequest request;

        protected boolean isSkip;

        /**
         * Characters.
         *
         * @param ch
         *               the ch
         * @param start
         *               the start
         * @param length
         *               the length
         * @throws SAXException
         *                      the SAX exception
         */
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            contentHandler.characters(ch, start, length);
        }

        /**
         * Dispose.
         */
        @Override
        public void dispose() {
            // Dispose call.
        }

        /**
         * End document.
         *
         * @throws SAXException
         *                      the SAX exception
         */
        @Override
        public void endDocument() throws SAXException {
            contentHandler.endDocument();
        }

        /**
         * End element.
         *
         * @param uri
         *                  the uri
         * @param localName
         *                  the local name
         * @param qName
         *                  the q name
         * @throws SAXException
         *                      the SAX exception
         */
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            contentHandler.endElement(uri, localName, qName);
        }

        /**
         * End prefix mapping.
         *
         * @param prefix
         *               the prefix
         * @throws SAXException
         *                      the SAX exception
         */
        @Override
        public void endPrefixMapping(String prefix) throws SAXException {
            contentHandler.endPrefixMapping(prefix);
        }

        /**
         * Ignorable whitespace.
         *
         * @param ch
         *               the ch
         * @param start
         *               the start
         * @param length
         *               the length
         * @throws SAXException
         *                      the SAX exception
         */
        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
            contentHandler.ignorableWhitespace(ch, start, length);
        }

        /**
         * Inits the.
         *
         * @param context
         *                the context
         * @param config
         *                the config
         * @throws IOException
         *                     Signals that an I/O exception has occurred.
         */
        @Override
        public void init(ProcessingContext context, ProcessingComponentConfiguration config) throws IOException {
            request = context.getRequest();
            final WCMMode wcmMode = WCMMode.fromRequest(context.getRequest());
            LOGGER.debug("SupplierPortalLink Transformer WCMMode: {}", wcmMode);
            isSkip = wcmMode != WCMMode.DISABLED;
        }

        /**
         * Processing instruction.
         *
         * @param target
         *               the target
         * @param data
         *               the data
         * @throws SAXException
         *                      the SAX exception
         */
        @Override
        public void processingInstruction(String target, String data) throws SAXException {
            contentHandler.processingInstruction(target, data);
        }

        /**
         * Sets the content handler.
         *
         * @param handler
         *                the new content handler
         */
        @Override
        public void setContentHandler(ContentHandler handler) {
            this.contentHandler = handler;
        }

        /**
         * Sets the document locator.
         *
         * @param locator
         *                the new document locator
         */
        @Override
        public void setDocumentLocator(Locator locator) {
            contentHandler.setDocumentLocator(locator);
        }

        /**
         * Skipped entity.
         *
         * @param name
         *             the name
         * @throws SAXException
         *                      the SAX exception
         */
        @Override
        public void skippedEntity(String name) throws SAXException {
            contentHandler.skippedEntity(name);
        }

        /**
         * Start document.
         *
         * @throws SAXException
         *                      the SAX exception
         */
        @Override
        public void startDocument() throws SAXException {
            contentHandler.startDocument();
        }

        /**
         * Start element.
         *
         * @param uri
         *                  the uri
         * @param localName
         *                  the local name
         * @param qName
         *                  the q name
         * @param atts
         *                  the atts
         * @throws SAXException
         *                      the SAX exception
         */
        @Override
        @SuppressWarnings("deprecation")
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            final AttributesImpl attributes = new AttributesImpl(atts);
            final String href = attributes.getValue("href");
            LOGGER.debug("SupplierPortalLink Transformer href: {}", href);
            if (!isSkip && StringUtils.isNotBlank(href) && Boolean.TRUE.equals(isValidURL(href))
                    && "a".equals(localName)) {
                LOGGER.info("SupplierPortalLink Transformer processing valid : {}", href);
                for (int i = 0; i < attributes.getLength(); i++) {
                    if ("href".equalsIgnoreCase(attributes.getQName(i))) {
                        attributes.setValue(i,
                                LinkUtil.sanitizeLink(attributes.getValue(i).replaceAll(".html", ""), request));
                        break;
                    }
                }
            }

            contentHandler.startElement(uri, localName, qName, attributes);
        }

        /**
         * Checks if is valid URL.
         *
         * @param url
         *            the url
         * @return the boolean
         */
        private Boolean isValidURL(String url) {
            Boolean isValidURL = false;
            if (url.startsWith(SupplierPortalConstants.CONTENT_ROOT_PATH)) {
                isValidURL = true;
            }
            return isValidURL;
        }

        /**
         * Start prefix mapping.
         *
         * @param prefix
         *               the prefix
         * @param uri
         *               the uri
         * @throws SAXException
         *                      the SAX exception
         */
        @Override
        public void startPrefixMapping(String prefix, String uri) throws SAXException {
            contentHandler.startPrefixMapping(prefix, uri);
        }

    }

}
