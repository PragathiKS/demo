package com.trs.core.servlets;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class AuthCheckerServletTest {

    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private AuthCheckerServlet authCheckerServlet;

    @BeforeEach
    void setUp() throws Exception {
        Map<String, Object> props = new HashMap<>();
        props.put("sling.servlet.paths", "/bin/trs/permissioncheck");
        authCheckerServlet = context.registerInjectActivateService(new AuthCheckerServlet(), props);
    }

    @Test
    final void testDoHeadSlingHttpServletRequestSlingHttpServletResponse() {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();
        authCheckerServlet.doHead(request, response);
        assertEquals(HttpStatus.SC_OK, response.getStatus(), "status should be ok ");
    }

}
