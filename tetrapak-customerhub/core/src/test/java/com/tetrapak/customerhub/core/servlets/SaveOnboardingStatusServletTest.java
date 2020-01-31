package com.tetrapak.customerhub.core.servlets;

import com.microsoft.azure.storage.table.TableOperation;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.GenericServiceType;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.services.impl.AzureTableStorageServiceImpl;
import com.tetrapak.customerhub.core.services.impl.UserPreferenceServiceImpl;
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

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Nitin Kumar
 */
public class SaveOnboardingStatusServletTest {

    private static final String SERVLET_RESOURCE_PATH = "/content/tetrapak/customerhub/"
            + "global/dashboard/jcr:content/root/responsivegrid/introscreen";
    private static final String SERVLET_RESOURCE_JSON = "allContent.json";

    AzureTableStorageServiceImpl azureTableStorageService = new AzureTableStorageServiceImpl();

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(SERVLET_RESOURCE_JSON,
            SERVLET_RESOURCE_PATH, getMultipleMockedService());

    @Before
    public void setup() {
        Map<String, Object> _config = new HashMap<>();
        _config.put("defaultEndpointsProtocol", "https");
        _config.put("accountKey", "Fa6WBGXsJZ+9Hyt5ggAKQD4WJQ4j77foq4a8S2S+wr663sVxPO5AFrhOPEgbxsPt+WBYDyfH654CIlfncy0klg==");
        _config.put("accountName", "ta01cfedsta01");
        _config.put("tableName", "preferences");
        azureTableStorageService = aemContext.registerInjectActivateService(azureTableStorageService, _config);

        aemContext.load().json("/" + "user.json", "/home");
        aemContext.currentResource(SERVLET_RESOURCE_PATH);
        aemContext.request().setServletPath(SERVLET_RESOURCE_PATH);
        aemContext.request().setMethod(HttpConstants.METHOD_GET);
    }

    @Test
    public void doGet() throws IOException, ServletException {
        MockSlingHttpServletRequest request = aemContext.request();
        MockSlingHttpServletResponse response = aemContext.response();
        SaveOnboardingStatusServlet saveOnboardingStatusServlet = aemContext
                .getService(SaveOnboardingStatusServlet.class);
        aemContext.registerInjectActivateService(saveOnboardingStatusServlet);
        saveOnboardingStatusServlet.doGet(request, response);
        assertEquals("status should be ok ", HttpStatus.SC_OK, response.getStatus());
    }

    private <T> List<GenericServiceType<T>> getMultipleMockedService() {
        GenericServiceType<UserPreferenceService> userPreferenceGenericServiceType = new GenericServiceType<>();
        userPreferenceGenericServiceType.setClazzType(UserPreferenceService.class);
        userPreferenceGenericServiceType.set(new UserPreferenceServiceImpl());

        GenericServiceType<SaveOnboardingStatusServlet> saveOnboardingStatusServletGenericServiceType = new GenericServiceType<>();
        saveOnboardingStatusServletGenericServiceType.setClazzType(SaveOnboardingStatusServlet.class);
        saveOnboardingStatusServletGenericServiceType.set(new SaveOnboardingStatusServlet());

        List<GenericServiceType<T>> serviceTypes = new ArrayList<>();
        serviceTypes.add((GenericServiceType<T>) userPreferenceGenericServiceType);
        serviceTypes.add((GenericServiceType<T>) saveOnboardingStatusServletGenericServiceType);
        return serviceTypes;
    }
}
