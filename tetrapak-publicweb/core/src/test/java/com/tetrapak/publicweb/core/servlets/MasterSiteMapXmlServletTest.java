package com.tetrapak.publicweb.core.servlets;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.publicweb.core.mock.MockHelper;

import io.wcm.testing.mock.aem.junit.AemContext;

public class MasterSiteMapXmlServletTest {
    
    /** The Constant TEST_RESOURCE_CONTENT. */
    private static final String TEST_CONTENT = "/sitemap/test-content.json";

    /** The Constant MARKETS_CONTENT_ROOT. */
    private static final String MARKETS_CONTENT_ROOT = "/content/tetrapak/publicweb";
    
    /** The Constant RESOURCE. */
    private static final String RESOURCE = MARKETS_CONTENT_ROOT + "/publicweb";
    
    /** The context. */
    @Rule
    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private MasterSiteMapXmlServlet masterSiteMapXmlServlet = new MasterSiteMapXmlServlet();;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        
        masterSiteMapXmlServlet = MockHelper.getServlet(context, MasterSiteMapXmlServlet.class);
        context.load().json(TEST_CONTENT, MARKETS_CONTENT_ROOT);
        context.request().setResource(context.resourceResolver().getResource(RESOURCE));
        
    }

    /**
     * Do get
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void doGet() throws IOException, ServletException {

        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        masterSiteMapXmlServlet.doGet(request, response);
        assertEquals(
                "<?xml version=\"1.0\"?><sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\"><sitemap><loc>/content/tetrapak/publicweb/publicweb/gb/en.sitemap.xml</loc></sitemap><sitemap><loc>/content/tetrapak/publicweb/publicweb/ca/fr.sitemap.xml</loc></sitemap><sitemap><loc>/content/tetrapak/publicweb/publicweb/ca/en.sitemap.xml</loc></sitemap></sitemapindex>",
                context.response().getOutputAsString());
    }
}
