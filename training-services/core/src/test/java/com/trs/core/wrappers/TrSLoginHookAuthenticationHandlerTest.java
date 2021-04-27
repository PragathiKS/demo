package com.trs.core.wrappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.auth.core.spi.AuthenticationHandler;
import org.apache.sling.auth.core.spi.AuthenticationInfo;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class TrSLoginHookAuthenticationHandlerTest {

    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private TrSLoginHookAuthenticationHandler trSLoginHookAuthenticationHandler;
    @Mock
    private AuthenticationHandler wrappedAuthHandler;

    private MockSlingHttpServletRequest request;
    private MockSlingHttpServletResponse response;

    @BeforeEach
    void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        Map<String, Object> props = new HashMap<>();
        props.put("service.pid", "com.day.crx.security.token.impl.impl.TokenAuthenticationHandler");
        wrappedAuthHandler = context.registerService(AuthenticationHandler.class, wrappedAuthHandler, props);
        Mockito.when(wrappedAuthHandler.extractCredentials(Mockito.any(), Mockito.any())).thenReturn(new AuthenticationInfo(null));
        props.put("path", "/content/trs");
        trSLoginHookAuthenticationHandler = context
                .registerInjectActivateService(new TrSLoginHookAuthenticationHandler(), props);
        request = context.request();
        response = context.response();
        
    }

    @Test
    final void testExtractCredentials() {
        assertNotNull(trSLoginHookAuthenticationHandler.extractCredentials(request, response),"Unexpected value");
    }

    @Test
    final void testRequestCredentials() throws IOException {
        assertEquals(false, trSLoginHookAuthenticationHandler.requestCredentials(request, response),"Unexpected value");
    }

    @Test
    final void testAuthenticationSucceeded() {
       assertEquals(false, trSLoginHookAuthenticationHandler.authenticationSucceeded(request, response, new AuthenticationInfo(null)),"Unexpected value");
    }

}
