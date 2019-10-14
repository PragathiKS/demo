package com.tetrapak.customerhub.core.servlets;

import com.microsoft.azure.storage.table.TableOperation;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.GenericServiceType;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.http.HttpStatus;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Nitin Kumar
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TableOperation.class, AuthCheckerServlet.class})
public class AuthCheckerServletTest {

    private static final String SERVLET_RESOURCE_PATH = "/content/tetrapak/customerhub/"
            + "global/dashboard/jcr:content/root/responsivegrid/introscreen";
    private static final String SERVLET_RESOURCE_JSON = "allContent.json";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(SERVLET_RESOURCE_JSON,
            SERVLET_RESOURCE_PATH, getMultipleMockedService());

    @Before
    public void setup() {
        aemContext.load().json("/" + "user.json", "/home");

        aemContext.currentResource(SERVLET_RESOURCE_PATH);
        aemContext.request().setServletPath(SERVLET_RESOURCE_PATH);
        aemContext.request().setMethod(HttpConstants.METHOD_GET);
    }

    @Test
    public void doHead() {
        MockSlingHttpServletRequest request = aemContext.request();
        MockSlingHttpServletResponse response = aemContext.response();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("uri", "/dashboard.html");
        request.setParameterMap(parameters);

        AuthCheckerServlet authCheckerServlet = aemContext
                .getService(AuthCheckerServlet.class);
        aemContext.registerInjectActivateService(authCheckerServlet);
        authCheckerServlet.doHead(request, response);
        assertEquals("status should be ok ", HttpStatus.SC_OK, response.getStatus());
    }

    private <T> List<GenericServiceType<T>> getMultipleMockedService() {
        GenericServiceType<AuthCheckerServlet> authCheckerServletGenericServiceType = new GenericServiceType<>();
        authCheckerServletGenericServiceType.setClazzType(AuthCheckerServlet.class);
        authCheckerServletGenericServiceType.set(new AuthCheckerServlet());

        List<GenericServiceType<T>> serviceTypes = new ArrayList<>();
        serviceTypes.add((GenericServiceType<T>) authCheckerServletGenericServiceType);
        return serviceTypes;
    }
}
