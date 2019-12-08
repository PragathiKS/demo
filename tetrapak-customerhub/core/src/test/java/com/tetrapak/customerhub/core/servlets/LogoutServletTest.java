package com.tetrapak.customerhub.core.servlets;

import com.microsoft.azure.storage.table.TableOperation;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.GenericServiceType;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Nitin Kumar
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TableOperation.class, LogoutServlet.class})
public class LogoutServletTest {

    private static final String SERVLET_RESOURCE_PATH = "/content/tetrapak/customerhub/"
            + "global/dashboard/jcr:content/root/responsivegrid/introscreen";
    private static final String SERVLET_RESOURCE_JSON = "allContent.json";

    @Mock
    private SlingHttpServletRequest request;

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(SERVLET_RESOURCE_JSON,
            SERVLET_RESOURCE_PATH, getMultipleMockedService());

    @Before
    public void setup() {
        aemContext.load().json("/" + "user.json", "/home");
        aemContext.request().addCookie(new Cookie("authToken", "a"));
        aemContext.request().addCookie(new Cookie("acctoken", "b"));
        aemContext.request().addCookie(new Cookie("login-token", "c"));
        aemContext.request().addCookie(new Cookie("saml_request_path", "d"));
        aemContext.currentResource(SERVLET_RESOURCE_PATH);
        aemContext.request().setServletPath(SERVLET_RESOURCE_PATH);
        aemContext.request().setMethod(HttpConstants.METHOD_GET);
    }

    @Test
    public void doGet() {
        MockSlingHttpServletRequest request = aemContext.request();
        MockSlingHttpServletResponse response = aemContext.response();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("redirectURL", "/logout.html");
        request.setParameterMap(parameters);

        LogoutServlet logoutServlet = aemContext
                .getService(LogoutServlet.class);
        aemContext.registerInjectActivateService(logoutServlet);
        logoutServlet.doGet(request, response);
        assertEquals("status should be 302 ", HttpStatus.SC_MOVED_TEMPORARILY, response.getStatus());
    }

    private <T> List<GenericServiceType<T>> getMultipleMockedService() {
        GenericServiceType<LogoutServlet> logoutServletGenericServiceType = new GenericServiceType<>();
        logoutServletGenericServiceType.setClazzType(LogoutServlet.class);
        logoutServletGenericServiceType.set(new LogoutServlet());

        List<GenericServiceType<T>> serviceTypes = new ArrayList<>();
        serviceTypes.add((GenericServiceType<T>) logoutServletGenericServiceType);
        return serviceTypes;
    }
}
