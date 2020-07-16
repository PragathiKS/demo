package com.tetrapak.publicweb.core.transformer;

import java.io.IOException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.rewriter.ProcessingComponentConfiguration;
import org.apache.sling.rewriter.ProcessingContext;
import org.apache.sling.rewriter.Transformer;
import org.apache.sling.rewriter.TransformerFactory;
import org.osgi.service.component.annotations.Component;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import com.day.cq.rewriter.pipeline.AttributesImpl;
import com.tetrapak.publicweb.core.utils.LinkUtils;

@Component(
        immediate = true,
        service = TransformerFactory.class,
        property = {
                "pipeline.type=pwlinkrewriter"  
        })
public class PWLinkTransformerFactory implements TransformerFactory{

    @Override
    public Transformer createTransformer() {
        return new PWLinkRewriterTransformer();
    }
    
    private class PWLinkRewriterTransformer implements Transformer {
        
        private ContentHandler contentHandler;
        
        private SlingHttpServletRequest request;

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            contentHandler.characters(ch, start, length);
        }

        @Override
        public void dispose() {
            // Dispose call.
        }

        @Override
        public void endDocument() throws SAXException {
            contentHandler.endDocument();
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            contentHandler.endElement(uri, localName, qName);
        }

        @Override
        public void endPrefixMapping(String prefix) throws SAXException {
            contentHandler.endPrefixMapping(prefix);
        }

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
            contentHandler.ignorableWhitespace(ch, start, length);
        }

        @Override
        public void init(ProcessingContext context, ProcessingComponentConfiguration config) throws IOException {
            request = context.getRequest();
        }

        @Override
        public void processingInstruction(String target, String data) throws SAXException {
            contentHandler.processingInstruction(target, data);
        }

        @Override
        public void setContentHandler(ContentHandler handler) {
            this.contentHandler = handler;
        }

        @Override
        public void setDocumentLocator(Locator locator) {
            contentHandler.setDocumentLocator(locator);
        }

        @Override
        public void skippedEntity(String name) throws SAXException {
            contentHandler.skippedEntity(name);
        }

        @Override
        public void startDocument() throws SAXException {
            contentHandler.startDocument();
        }

        @Override
        @SuppressWarnings("deprecation")
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {           
            final AttributesImpl attributes = new AttributesImpl(atts);
            final String href = attributes.getValue("href");

            if (href != null) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    if ("href".equalsIgnoreCase(attributes.getQName(i))) {
                        attributes.setValue(i, LinkUtils.sanitizeLink(attributes.getValue(i), request.getResourceResolver()));
                        break;
                    }
                }
            }

            contentHandler.startElement(uri, localName, qName, attributes);
        }

        @Override
        public void startPrefixMapping(String prefix, String uri) throws SAXException {
            contentHandler.startPrefixMapping(prefix, uri);
        }
        
    }

}
