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
import com.tetrapak.publicweb.core.services.PadrotService;
import com.tetrapak.publicweb.core.services.impl.PadrotServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

public class BusinessEnquiryPadrotServletTest {

    /** The context. */
    @Rule
    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private BusinessEnquiryPadrotServlet businessEnquiryPadrotServlet = new BusinessEnquiryPadrotServlet();

    private PadrotService padrotService;


    /**
     * Setup.
     *
     * @throws IOException
     */
    @Before
    public void setup() throws IOException {

        padrotService = new PadrotServiceImpl();
        final Map<String, Object> padrotConfig = new HashMap<>();
        padrotConfig.put("padrotBusinessInquiryServiceUrl", "http://go.tetrapak.com/l/857883/2020-05-29/w6xt");
        context.registerService(PadrotService.class, padrotService);
        MockOsgi.activate(context.getService(PadrotService.class), context.bundleContext(), padrotConfig);

        businessEnquiryPadrotServlet = MockHelper.getServlet(context, BusinessEnquiryPadrotServlet.class);

        final Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("firstName", "firstName");
        parameterMap.put("lastName", "lastName");

        context.request().setParameterMap(parameterMap);

        context.request().setResource(context.currentResource());

    }

    /**
     * Do get
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void doPost() throws IOException {
        businessEnquiryPadrotServlet.doPost(context.request(), context.response());
        assertEquals("Success", context.response().getOutputAsString());
    }
}
