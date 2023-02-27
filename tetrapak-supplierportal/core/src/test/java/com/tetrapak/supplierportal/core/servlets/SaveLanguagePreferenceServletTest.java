package com.tetrapak.supplierportal.core.servlets;

import com.tetrapak.supplierportal.core.mock.GenericServiceType;
import com.tetrapak.supplierportal.core.mock.SupplierPortalCoreAemContext;
import com.tetrapak.supplierportal.core.services.UserPreferenceService;
import com.tetrapak.supplierportal.core.services.impl.AzureTableStorageServiceImpl;
import com.tetrapak.supplierportal.core.services.impl.UserPreferenceServiceImpl;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.http.HttpStatus;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SaveLanguagePreferenceServletTest {
    private static final String SERVLET_RESOURCE_PATH = "/content/tetrapak/supplierportal/"
            + "global/en/dashboard/jcr:content/root/responsivegrid/introscreen";
    private static final String SERVLET_RESOURCE_JSON = "allContent.json";

    private AzureTableStorageServiceImpl azureTableStorageService = new AzureTableStorageServiceImpl();

    @Rule
    public final AemContext aemContext = SupplierPortalCoreAemContext.getAemContextWithJcrMock(SERVLET_RESOURCE_JSON,
            SERVLET_RESOURCE_PATH, getMultipleMockedService());

    @Before
    public void setup() {
        Map<String, Object> _config = new HashMap<>();
        _config.put("defaultEndpointsProtocol", "https");
        _config.put("accountKey", "Fa6WBGXsJZ+9Hyt5ggAKQD4WJQ4j77foq4a8S2S+wr663sVxPO5AFrhOPEgbxsPt+WBYDyfH654CIlfncy0klg==");
        _config.put("accountName", "ta01cfedsta01");
        _config.put("tableName", "preferences");
        aemContext.registerInjectActivateService(azureTableStorageService, _config);

        aemContext.load().json("/" + "user.json", "/home");
        aemContext.currentResource(SERVLET_RESOURCE_PATH);
        aemContext.request().setServletPath(SERVLET_RESOURCE_PATH);
        aemContext.request().setMethod(HttpConstants.METHOD_GET);
    }

    @Test
    public void doPost() throws IOException {
        MockSlingHttpServletRequest request = aemContext.request();
        MockSlingHttpServletResponse response = aemContext.response();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("lang-code", "en");
        request.setParameterMap(parameters);

        SaveLanguagePreferenceServlet saveLanguagePreferenceServlet = aemContext
                .getService(SaveLanguagePreferenceServlet.class);
        aemContext.registerInjectActivateService(saveLanguagePreferenceServlet);
        saveLanguagePreferenceServlet.doPost(request, response);
        assertEquals("status should be ok ", HttpStatus.SC_OK, response.getStatus());
    }

    private <T> List<GenericServiceType<T>> getMultipleMockedService() {
        GenericServiceType<UserPreferenceService> userPreferenceGenericServiceType = new GenericServiceType<>();
        userPreferenceGenericServiceType.setClazzType(UserPreferenceService.class);
        userPreferenceGenericServiceType.set(new UserPreferenceServiceImpl());

        GenericServiceType<SaveLanguagePreferenceServlet> saveLanguagePreferenceServletGenericServiceType = new GenericServiceType<>();
        saveLanguagePreferenceServletGenericServiceType.setClazzType(SaveLanguagePreferenceServlet.class);
        saveLanguagePreferenceServletGenericServiceType.set(new SaveLanguagePreferenceServlet());

        List<GenericServiceType<T>> serviceTypes = new ArrayList<>();
        serviceTypes.add((GenericServiceType<T>) userPreferenceGenericServiceType);
        serviceTypes.add((GenericServiceType<T>) saveLanguagePreferenceServletGenericServiceType);
        return serviceTypes;
    }
}
