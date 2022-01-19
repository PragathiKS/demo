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
import com.tetrapak.publicweb.core.services.PardotService;
import com.tetrapak.publicweb.core.services.impl.PardotServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

public class SubscriptionFormPardotServletTest {

    /** The context. */
    @Rule
    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private SubscriptionFormPardotServlet subscriptionFormPardotServlet = new SubscriptionFormPardotServlet();

    private PardotService pardotService;


    /**
     * Setup.
     *
     * @throws IOException
     */
    @Before
    public void setup() throws IOException {

        pardotService = new PardotServiceImpl();
        context.registerService(PardotService.class, pardotService);
        MockOsgi.activate(context.getService(PardotService.class), context.bundleContext());

        subscriptionFormPardotServlet = MockHelper.getServlet(context, SubscriptionFormPardotServlet.class);

        final Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("firstName", "firstName");
        parameterMap.put("lastName", "lastName");
        parameterMap.put("pardotUrl", "http://go.tetrapak.com/l/857883/2020-05-29/xttt");
        parameterMap.put("country", "India");
        parameterMap.put("types-communication", "Press and Media Communication");
        parameterMap.put("types-communication", "Event Invitations");
        parameterMap.put("interestArea", "Packaging");
        parameterMap.put("interestArea", "Services");

        context.request().setParameterMap(parameterMap);
        context.request().setPathInfo("/content/tetrapak/public-web/in/en");
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
        subscriptionFormPardotServlet.doPost(context.request(), context.response());
        assertEquals("success", context.response().getOutputAsString());
    }
}
