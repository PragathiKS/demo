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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TableOperation.class, SaveOrderPreferencesServlet.class})
public class SaveOrderPreferencesServletTest {

	private static final String SERVLET_RESOURCE_PATH = "/content/tetrapak/customerhub/global/en/ordering/jcr:content/root/responsivegrid/orderingcard";
	private static final String SERVLET_RESOURCE_JSON = "allContent.json";

    AzureTableStorageServiceImpl azureTableStorageService = new AzureTableStorageServiceImpl();

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(SERVLET_RESOURCE_JSON, SERVLET_RESOURCE_PATH, getMultipleMockedService());

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
        aemContext.request().setMethod(HttpConstants.METHOD_POST);
        Map<String, Object> params = new HashMap<>();
        params.put("fields", "orderNumber.poNumber");
        aemContext.request().setParameterMap(params);
    }

    @Test
    public void doPost() throws IOException {
        MockSlingHttpServletRequest request = aemContext.request();
        MockSlingHttpServletResponse response = aemContext.response();
        SaveOrderPreferencesServlet saveOrderPreferencesServlet = aemContext.getService(SaveOrderPreferencesServlet.class);
        aemContext.registerInjectActivateService(saveOrderPreferencesServlet);
        saveOrderPreferencesServlet.doPost(request, response);
        assertEquals("status from response", HttpStatus.SC_OK, response.getStatus());
    }

	public <T> List<GenericServiceType<T>> getMultipleMockedService() {
        GenericServiceType<UserPreferenceService> userPreferenceGenericServiceType = new GenericServiceType<>();
        userPreferenceGenericServiceType.setClazzType(UserPreferenceService.class);
        userPreferenceGenericServiceType.set(new UserPreferenceServiceImpl());

		GenericServiceType<SaveOrderPreferencesServlet> saveOrderPreferencesServletGenericServiceType = new GenericServiceType<>();
		saveOrderPreferencesServletGenericServiceType.setClazzType(SaveOrderPreferencesServlet.class);
		saveOrderPreferencesServletGenericServiceType.set(new SaveOrderPreferencesServlet());

        List<GenericServiceType<T>> serviceTypes = new ArrayList<>();
        serviceTypes.add((GenericServiceType<T>) userPreferenceGenericServiceType);
        serviceTypes.add((GenericServiceType<T>) saveOrderPreferencesServletGenericServiceType);
        return serviceTypes;
    }
}
