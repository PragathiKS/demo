/**
 * 
 */
package com.tetrapak.customerhub.core.transformer.impl;

import java.io.IOException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.rewriter.ProcessingComponentConfiguration;
import org.apache.sling.rewriter.ProcessingContext;
import org.apache.sling.rewriter.Transformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.tetrapak.customerhub.core.factory.transformer.LinkTransformerFactory;

/**
 * @author swalamba
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CustomLinkTransformerTest {

	private Transformer customLinkTransformer;

	@Mock
	private Attributes atts;

	@Mock
	private ContentHandler ch;

	@Mock
	private ProcessingContext context;

	@Mock
	private ProcessingComponentConfiguration config;

	@Mock
	private SlingHttpServletRequest request;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		LinkTransformerFactory factory = new LinkTransformerFactory();
		customLinkTransformer = factory.createTransformer();
		customLinkTransformer.setContentHandler(ch);
		customLinkTransformer.characters(null, 0, 0);
		customLinkTransformer.dispose();
		customLinkTransformer.endDocument();
		customLinkTransformer.endElement("", "", "");
		customLinkTransformer.endPrefixMapping("prefix");
		customLinkTransformer.ignorableWhitespace(null, 0, 0);
		customLinkTransformer.processingInstruction("target", "data");
		customLinkTransformer.setDocumentLocator(null);
		customLinkTransformer.skippedEntity(null);
		customLinkTransformer.startDocument();
		customLinkTransformer.startPrefixMapping("", "");
	}

	/**
	 * Test method for
	 * {@link com.tetrapak.customerhub.core.transformer.impl.CustomLinkTransformer#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)}.
	 * 
	 * @throws SAXException
	 * @throws IOException
	 */
	@Test
	public void testStartElement() throws SAXException, IOException {
		String uri = "";
		String localName = "a";
		String qName = "a";
		Mockito.when(atts.getValue("href")).thenReturn("/content/tetrapak/customerhub/en/contacts.html");
		Mockito.when(context.getRequest()).thenReturn(request);
		Mockito.when(request.getPathInfo()).thenReturn("/global/en/MyTetraPak/dashboard.html");
		customLinkTransformer.init(context, config);
		customLinkTransformer.startElement(uri, localName, qName, atts);
		Mockito.verify(atts).getValue("href");

	}

	/**
	 * Test method for
	 * {@link com.tetrapak.customerhub.core.transformer.impl.CustomLinkTransformer#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)}.
	 * 
	 * @throws SAXException
	 * @throws IOException
	 */
	@Test
	public void testStartElementURLWithContentPath() throws SAXException, IOException {
		String uri = "";
		String localName = "a";
		String qName = "a";
		Mockito.when(atts.getValue("href")).thenReturn("/content/tetrapak/customerhub/en/contacts.html");
		Mockito.when(context.getRequest()).thenReturn(request);
		Mockito.when(request.getPathInfo()).thenReturn("/content/tetrapak/customerhub/en/contacts.html");
		customLinkTransformer.init(context, config);
		customLinkTransformer.startElement(uri, localName, qName, atts);
		Mockito.verify(atts).getValue("href");
	}

	/**
	 * Test method for
	 * {@link com.tetrapak.customerhub.core.transformer.impl.CustomLinkTransformer#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)}.
	 * 
	 * @throws SAXException
	 * @throws IOException
	 */
	@Test
	public void testStartElementCountryAndLocaleEmpty() throws SAXException, IOException {
		String uri = "";
		String localName = "a";
		String qName = "a";
		Mockito.when(atts.getValue("href")).thenReturn("/content/tetrapak/customerhub/en/contacts.html");
		customLinkTransformer.startElement(uri, localName, qName, atts);
		Mockito.verify(atts).getValue("href");
	}

	/**
	 * Test method for
	 * {@link com.tetrapak.customerhub.core.transformer.impl.CustomLinkTransformer#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)}.
	 * 
	 * @throws SAXException
	 * @throws IOException
	 */
	@Test
	public void testStartElementLinkUrlBlank() throws SAXException, IOException {
		String uri = "";
		String localName = "a";
		String qName = "a";
		Mockito.when(atts.getValue("href")).thenReturn("");
		Mockito.when(context.getRequest()).thenReturn(request);
		Mockito.when(request.getPathInfo()).thenReturn("/global/en/MyTetraPak/dashboard.html");
		customLinkTransformer.init(context, config);
		customLinkTransformer.startElement(uri, localName, qName, atts);
		Mockito.verify(atts).getValue("href");
	}

}
