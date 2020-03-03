package com.tetrapak.publicweb.core.servlets;

import java.io.IOException;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import io.wcm.testing.mock.aem.junit.AemContext;

// TODO: Auto-generated Javadoc
/**
 * The Class ContactFooterFormSevletTest.
 */
public class ContactFooterFormSevletTest {

	/** The context. */
	@Rule
	public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

	/** The req. */
	@Mock
	private MockSlingHttpServletRequest req;

	/** The res. */
	@Mock
	private MockSlingHttpServletResponse res;

	/** The under test. */
	@InjectMocks
	private ContactFooterFormSevlet underTest;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		underTest = new ContactFooterFormSevlet();
		req = context.request();
		res = context.response();
	}

	/**
	 * Do get should return header as expected.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void doGet_shouldReturnHeaderAsExpected() throws IOException {
		underTest.doGet(req, res);
	}
}
