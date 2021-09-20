package com.tetrapak.publicweb.core.servlets;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.xss.XSSAPI;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.base.Function;
import com.tetrapak.publicweb.core.mock.MockHelper;
import com.tetrapak.publicweb.core.mock.MockXSSAPI;
import com.tetrapak.publicweb.core.services.ContactUsMailService;
import com.tetrapak.publicweb.core.services.CountryDetailService;
import com.tetrapak.publicweb.core.services.impl.ContactUsMailServiceImpl;
import com.tetrapak.publicweb.core.services.impl.CountryDetailServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

public class ContactUsSendMailServletTest {
    /** The Constant TEST_RESOURCE_CONTENT. */
    private static final String TEST_RESOURCE_CFM = "/contactus/test-countries-content.json";

    /** The context. */
    @Rule
    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private ContactUsSendMailServlet contactUsSendMailServlet = new ContactUsSendMailServlet();

    private ContactUsMailService contactUsMailService;

    private CountryDetailService countryDetailService;
    
    /** The Constant TEST_CONTENT_ROOT. */
    private static final String EN_CONTENT_ROOT = "/content/tetrapak/public-web/lang-masters/en";

    private static final String COUNTRIES_ROOT = "/content/dam/tetrapak/publicweb/contentfragment/pardot-countries";

    String inputJson;

    /**
     * Setup.
     *
     * @throws IOException
     */
    @Before
    public void setup() throws IOException {

        countryDetailService = new CountryDetailServiceImpl();
        final Map<String, Object> countryConfig = new HashMap<>();
        countryConfig.put("getPardotCountriesCFRootPath", COUNTRIES_ROOT);
        context.registerService(CountryDetailService.class, countryDetailService);
        MockOsgi.activate(context.getService(CountryDetailService.class), context.bundleContext(), countryConfig);

        contactUsMailService = new ContactUsMailServiceImpl();
        context.registerService(ContactUsMailService.class, contactUsMailService);
        MockOsgi.activate(context.getService(ContactUsMailService.class), context.bundleContext());

        context.load().json("/searchresult/en.json", EN_CONTENT_ROOT);
        context.load().json(TEST_RESOURCE_CFM, COUNTRIES_ROOT);

        // set request Input Json
        inputJson = IOUtils
                .toString(this.getClass().getResourceAsStream("/contactus/test-sendmailrequest.json"),
                "UTF-8");

        XSSAPI xssAPI = new MockXSSAPI(inputJson);
        context.registerService(XSSAPI.class, xssAPI);

        contactUsSendMailServlet = MockHelper.getServlet(context, ContactUsSendMailServlet.class);

        final Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("inputJson", inputJson);
        context.request().setParameterMap(parameterMap);

        context.request().setResource(context.currentResource());
        
        context.request().setPathInfo("/content/tetrapak/public-web/lang-masters/en");

        context.registerAdapter(SlingHttpServletRequest.class, XSSAPI.class,
                new Function<SlingHttpServletRequest, XSSAPI>() {

                    @Override
                    public XSSAPI apply(final SlingHttpServletRequest arg0) {
                        return new MockXSSAPI(inputJson);
                    }
                });

    }

    /**
     * Do get
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void doPost() throws IOException {
        contactUsSendMailServlet.doPost(context.request(), context.response());
        assertEquals(
                "{\"statusMessage\":\"Success\",\"statusCode\":\"200\"}",
                context.response().getOutputAsString());
    }
}
