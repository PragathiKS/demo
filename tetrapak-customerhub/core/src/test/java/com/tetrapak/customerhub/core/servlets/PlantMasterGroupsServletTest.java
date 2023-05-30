package com.tetrapak.customerhub.core.servlets;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.http.HttpStatus;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlantMasterGroupsServletTest {
    private static final String MOCK_USER_PATH = "/home/users/pingusers/mockUser";

    @Rule
    public final AemContext context = CuhuCoreAemContext.getAemContextWithJcrMock("mockUser.json", MOCK_USER_PATH);

    private PlantMasterGroupsServlet plantMasterGroupsServlet;

    @Spy
    private MockSlingHttpServletRequest request = context.request();

    private MockSlingHttpServletResponse response = context.response();

    @Mock
    private Authorizable mockUser;

    @Mock
    private Session session;

    @Mock
    private UserManager userManager;

    @Before
    public void setUp() throws RepositoryException {
        context.registerAdapter(ResourceResolver.class, UserManager.class, userManager);
        when(userManager.getAuthorizable(anyString())).thenReturn(mockUser);
        Map<String, Object> props = new HashMap<>();
        plantMasterGroupsServlet = context.registerInjectActivateService(new PlantMasterGroupsServlet(), props);

    }

    @Test
    public void testDoGet() throws RepositoryException, IOException {
        context.load().json("/" + "aipTrainingsAndLicenseGroupsStatic.json",
                "/content/dam/customerhub/aip/aipTrainingsAndLicenseGroupsStatic.json");
        request.setMethod("GET");
        when(mockUser.getPath()).thenReturn(MOCK_USER_PATH);
        plantMasterGroupsServlet.doGet(request, response);
        assertEquals("Response status", HttpStatus.SC_OK, context.response().getStatus());
        assertEquals("List of groups",
                "{\"groups\":[{\"groupName\":\"cuhu_tppm_product_common\",\"trainingId\":\"CT-29001\"},{\"groupName\":\"cuhu_tppm_product_siemenstiaplatform\",\"trainingId\":\"CT-29002\",\"engLicenseId\":\"3479535-0100TIA\",\"siteLicenseId\":\"3479532-0100TIA\"}]}",
                context.response().getOutputAsString());
    }

    @Test
    public void testDoGetNoUser() throws IOException, RepositoryException {
        context.load().json("/" + "aipTrainingsAndLicenseGroupsStatic.json",
                "/content/dam/customerhub/aip/aipTrainingsAndLicenseGroupsStatic.json");
        checkNoGroupsInResponse();
    }

    @Test
    public void testDoGetStaticGroupDefinition() throws IOException, RepositoryException {
        // aip static group json file not uvailable in crx
        checkNoGroupsInResponse();
    }

    private void checkNoGroupsInResponse() throws RepositoryException, IOException {
        request.setMethod("GET");
        when(mockUser.getPath()).thenReturn("/");
        plantMasterGroupsServlet.doGet(request, response);
        assertEquals("Response status", HttpStatus.SC_OK, context.response().getStatus());
        assertEquals("List of groups", "{\"groups\":[]}", context.response().getOutputAsString());
    }
}
