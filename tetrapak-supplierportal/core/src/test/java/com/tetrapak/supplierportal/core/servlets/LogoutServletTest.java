package com.tetrapak.supplierportal.core.servlets;

import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.settings.SlingSettingsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class) public class LogoutServletTest {

    @InjectMocks private LogoutServlet servlet = new LogoutServlet();

    @Mock private SlingHttpServletRequest request;

    @Mock private SlingHttpServletResponse response;

    @Mock private SlingSettingsService slingSettingsService;

    @Mock private Cookie loginCookie;

    @Mock private Cookie tokenCookie;

    @Mock private Cookie authCookie;

    @Mock private Cookie samlCookie;

    @Mock private Cookie cookie;

    @Mock private Cookie cookieEmail;

    @Before public void setUp() throws IOException {
        loginCookie.setMaxAge(1);
        tokenCookie.setMaxAge(1);
        authCookie.setMaxAge(1);
        samlCookie.setMaxAge(1);
        cookie.setMaxAge(1);


        Mockito.when(request.getCookie(LogoutServlet.LOGIN_TOKEN_KEY)).thenReturn(loginCookie);
        Mockito.when(request.getCookie(SupplierPortalConstants.TOKEN_NAME)).thenReturn(tokenCookie);
        Mockito.when(request.getCookie(LogoutServlet.AUTH_TOKEN_KEY)).thenReturn(authCookie);
        Mockito.when(request.getCookie(SupplierPortalConstants.SAML_REQUEST_PATH)).thenReturn(samlCookie);
        Mockito.when(request.getCookie(SupplierPortalConstants.COOKIE_NAME)).thenReturn(cookie);
        Mockito.when(request.getCookie(SupplierPortalConstants.COOKIE_EMAIL)).thenReturn(cookieEmail);

        Set<String> modes = new HashSet<>();
        modes.add("publish");
        Mockito.when(slingSettingsService.getRunModes()).thenReturn(modes);

    }

    @Test public void doGet() {
        servlet.doGet(request, response);
        assertEquals("Age should be 0", loginCookie.getMaxAge(), 0);
        assertEquals("Age should be 0", tokenCookie.getMaxAge(), 0);
        assertEquals("Age should be 0", authCookie.getMaxAge(), 0);
        assertEquals("Age should be 0", samlCookie.getMaxAge(), 0);
        assertEquals("Age should be 0", cookie.getMaxAge(), 0);
    }
}
