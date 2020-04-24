package com.tetrapak.publicweb.core.servlets;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.publicweb.core.mock.MockHelper;
import com.tetrapak.publicweb.core.services.FindMyOfficeService;
import com.tetrapak.publicweb.core.services.impl.FindMyOfficeServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

public class FindMyOfficeServletTest {
    /** The Constant TEST_RESOURCE_CONTENT. */
    private static final String TEST_COUNTRY_CONTENT = "/contentFragments/country.json";
    private static final String TEST_OFFICE_CONTENT = "/contentFragments/office.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_COUNTRY_ROOT = "/content/dam/tetrapak/findMyOffice/contentFragments/countries";

    private static final String TEST_OFFICE_ROOT = "/content/dam/tetrapak/findMyOffice/contentFragments/offices";

    /** The context. */
    @Rule
    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private FindMyOfficeServlet findMyOfficeServlet = new FindMyOfficeServlet();;

    private FindMyOfficeService findMyOfficeService;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        findMyOfficeService = new FindMyOfficeServiceImpl();
        Map<String, Object> config = new HashMap<>();
        config.put("getCountriesContentFragmentRootPath",
                "/content/dam/tetrapak/findMyOffice/contentFragments/countries");
        config.put("getOfficesContentFragmentRootPath", "/content/dam/tetrapak/findMyOffice/contentFragments/offices");
        config.put("getGoogleAPIKey", "AIzaSyC1w2gKCuwiRCsgqBR9RnSbWNuFvI5lryQ");
        context.registerService(FindMyOfficeService.class, findMyOfficeService);
        context.getService(FindMyOfficeService.class);
        MockOsgi.activate(context.getService(FindMyOfficeService.class), context.bundleContext(), config);
        findMyOfficeServlet = MockHelper.getServlet(context, FindMyOfficeServlet.class);
        context.load().json(TEST_COUNTRY_CONTENT, TEST_COUNTRY_ROOT);
        context.load().json(TEST_OFFICE_CONTENT, TEST_OFFICE_ROOT);
        context.request().setResource(context.currentResource());

    }

    /**
     * Do get
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void doGet() throws IOException {
        findMyOfficeServlet.doGet(context.request(), context.response());
        assertEquals(
                "{\"Cyprus\":{\"longitude\":33.429859,\"latitude\":35.126413,\"offices\":[{\"name\":\"Tetra Pak Hellas S.A.\",\"address\":\"Note: office located in Greece\\nSorou 10, Maroussi, Athens\",\"phoneNumber\":\"+30 210 616 7500\",\"fax\":\"+30 210 619 9690\",\"longitude\":23.805462915702677,\"latitude\":38.045454804312044,\"localSiteUrl\":\"\"}]}}",
                context.response().getOutputAsString());
    }
}
