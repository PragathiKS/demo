package com.tetrapak.commons.core.servlets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tetrapak.commons.core.mock.MockHelper;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * The Class OneTrustCookieJwtTokenServletTest.
 */
public class OneTrustCookieJwtTokenServletTest {
    @Rule
    public AemContext context = new AemContext();

    /**
     * The Constant TEST_CONTENT.
     */
    private static final String TEST_CONTENT = "/content/tetrapak/public-web/global/en/home";

    /**
     * The Constant CURRENT_RESOURCE.
     */
    private static final String CURRENT_RESOURCE = TEST_CONTENT + "/jcr:content";

    OneTrustCookieJwtTokenServlet oneTrustCookieJwtTokenServlet = new OneTrustCookieJwtTokenServlet();

    @Before
    public void setUp() throws Exception {
        context.load().json("/onetrustcookie/test-Content.json", TEST_CONTENT);
        Map<String, Object> config = new HashMap<>();
        config.put("oneTrustPrivatekeyPath", "src/test/resources/onetrustcookie/TestPrivateKey.pem");
        context.registerService(OneTrustCookieJwtTokenServlet.class, oneTrustCookieJwtTokenServlet);
        context.getService(OneTrustCookieJwtTokenServlet.class);
        MockOsgi.activate(context.getService(OneTrustCookieJwtTokenServlet.class), context.bundleContext(), config);
        MockitoAnnotations.initMocks(this);
        context.currentResource(CURRENT_RESOURCE);
        context.request().setPathInfo(CURRENT_RESOURCE);
        oneTrustCookieJwtTokenServlet = MockHelper.getServlet(context, OneTrustCookieJwtTokenServlet.class);
    }

    @Test
    public void testDoGet() throws IOException {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("UserID", StringUtils.EMPTY);
        context.request().setParameterMap(parameterMap);
        oneTrustCookieJwtTokenServlet.doGet(context.request(), context.response());
        JsonParser parser = new JsonParser();
        JsonObject oneTrustCookieServletResponse =  (JsonObject) parser.parse(context.response().getOutputAsString());
        assertEquals(200, context.response().getStatus());
        assertNotNull("Response is not null ", context.response().getOutputAsString());
        assertTrue("Valid GUID 4 version uid ", oneTrustCookieServletResponse.get("uid").toString().replace("\"","").length() >= 36);
    }

    @Test
    public void testExistingUID() throws IOException {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("UserID", "f39ceb7d-a6b3-474e-ae5d-70641ff22312");
        context.request().setParameterMap(parameterMap);
        oneTrustCookieJwtTokenServlet.doGet(context.request(), context.response());
        JsonParser parser = new JsonParser();
        JsonObject oneTrustCookieServletResponse =  (JsonObject) parser.parse(context.response().getOutputAsString());
        assertEquals(200, context.response().getStatus());
        assertNotNull("Response is not null ", context.response().getOutputAsString());
        assertEquals("f39ceb7d-a6b3-474e-ae5d-70641ff22312", oneTrustCookieServletResponse.get("uid").toString().replace("\"",""));
        assertTrue("Valid GUID 4 version uid ", oneTrustCookieServletResponse.get("uid").toString().replace("\"","").length() >= 36);
    }
    @Test
    public void testNonExistingPrivateKeyFile() throws IOException {
        Map<String, Object> config = new HashMap<>();
        config.put("oneTrustPrivatekeyPath", "src/test/resources/onetrustcookie/TestPrivateKey1.pem");
        context.registerService(OneTrustCookieJwtTokenServlet.class, oneTrustCookieJwtTokenServlet);
        context.getService(OneTrustCookieJwtTokenServlet.class);
        MockOsgi.activate(context.getService(OneTrustCookieJwtTokenServlet.class), context.bundleContext(), config);
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("UserID", StringUtils.EMPTY);
        context.request().setParameterMap(parameterMap);
        oneTrustCookieJwtTokenServlet.doGet(context.request(), context.response());
        JsonParser parser = new JsonParser();
        JsonObject oneTrustCookieServletResponse =  (JsonObject) parser.parse(context.response().getOutputAsString());
        assertEquals(200, context.response().getStatus());
        assertNotNull("Response is not null ", context.response().getOutputAsString());
        assertTrue("Empty uid token ", StringUtils.isEmpty(oneTrustCookieServletResponse.get("uid").toString().replace("\"","")));
    }

}
