package com.tetrapak.publicweb.core.servlets;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class ContactFooterFormSevletTest.
 */
public class ContactFooterFormServletTest {

	/** The Constant TEST_RESOURCE_CONTENT. */
	private static final String TEST_RESOURCE_CONTENT = "/usergenerated/test-content.json";

	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/usergenerated";

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
	private ContactFooterFormSevlet contactFooterFormSevlet;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {

		context.load().json(TEST_RESOURCE_CONTENT, TEST_CONTENT_ROOT);
		contactFooterFormSevlet = new ContactFooterFormSevlet();
		req = context.request();
		res = context.response();
	}

	/**
	 * Do get
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void doGet() throws IOException {
		contactFooterFormSevlet.doGet(req, res);
		assertEquals("status should be 302 ", HttpServletResponse.SC_NO_CONTENT, res.getStatus());
	}
}
