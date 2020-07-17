package com.tetrapak.publicweb.core.transformer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.sling.rewriter.ProcessingContext;
import org.apache.sling.rewriter.Transformer;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.tetrapak.publicweb.core.mock.MockAttributes;
import com.tetrapak.publicweb.core.mock.MockContentHandler;

import io.wcm.testing.mock.aem.junit.AemContext;

public class PWLinkTransformerFactoryTest {
    
    /** The context. */
    @Rule
    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);
    
    PWLinkTransformerFactory pwLinkTransformerFactory = new PWLinkTransformerFactory();
    
    @Mock
    private ProcessingContext processingContext;
    
    private static final String URI = "/content/tetrapak/publicweb/lang-masters/en/home";
    

    /**
     * Setup.
     *
     * @throws IOException
     */
    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        context.runMode("publish");
        context.requestPathInfo().setResourcePath(URI);
        Mockito.when(processingContext.getRequest()).thenReturn(context.request());
    }
    

    /**
     * Do get
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws SAXException 
     */
    @Test
    public void testPWTrasformer() throws IOException, SAXException {
        Transformer transformer = pwLinkTransformerFactory.createTransformer();
        transformer.init(processingContext, null);
        Attributes attributes = new MockAttributes(URI);
        ContentHandler contentHandler = new MockContentHandler();
        transformer.setContentHandler(contentHandler);
        transformer.startDocument();
        transformer.characters(new char[3], 0, 3);
        transformer.dispose();
        transformer.startElement(URI, "a", "href", attributes);
        transformer.endElement(URI, "a", "href");       
        transformer.endPrefixMapping("href");
        transformer.ignorableWhitespace(new char[3], 0, 3);
        transformer.processingInstruction("blank", "href");
        transformer.setDocumentLocator(null);
        transformer.skippedEntity("p");
        transformer.startPrefixMapping("href", URI);
        transformer.endDocument();
        assertEquals("PWLinkTransformerFactoryTest", "PWLinkTransformerFactoryTest",
                "PWLinkTransformerFactoryTest");
    }
}
