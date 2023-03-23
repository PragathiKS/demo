package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.MockUserPreferenceServiceImpl;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.services.impl.AzureTableStorageServiceImpl;
import com.tetrapak.customerhub.core.services.impl.PreferredLanguagesServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class AdditionalLanguageSelectorModelTest {

    private AdditionalLanguageSelectorModel additionalLanguageSelectorModel;

    private AzureTableStorageServiceImpl azureTableStorageService = new AzureTableStorageServiceImpl();
    
    private UserPreferenceService userPreferenceService;
    
    private PreferredLanguagesServiceImpl preferredLanguagesService = new PreferredLanguagesServiceImpl();
    
    private static final String CONTENT_FRAGMENT_PATH = "/content/dam/tetrapak/customerhub/contentfragment/preferred-languages";
    private static final String CONTENT_FRAGMENT_RESOURCE_JSON = "preferredLangContentFragment.json";
    
    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global/en";
    private static final String RESOURCE_JSON = "allContent.json";
    private static final String PATH = "path";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(RESOURCE_JSON, CONTENT_ROOT);

    @Before
    public void setup() {
    	aemContext.load().json(CustomerHubConstants.PATH_SEPARATOR + CONTENT_FRAGMENT_RESOURCE_JSON, CONTENT_FRAGMENT_PATH);
        Map<String, Object> _config = new HashMap<>();
        _config.put(CustomerHubConstants.PARAMETERS.get(0), CustomerHubConstants.PARAMETER_VALUES.get(0));
        _config.put(CustomerHubConstants.PARAMETERS.get(1), CustomerHubConstants.PARAMETER_VALUES.get(1));
        _config.put(CustomerHubConstants.PARAMETERS.get(2), CustomerHubConstants.PARAMETER_VALUES.get(2));
        _config.put(CustomerHubConstants.PARAMETERS.get(3), CustomerHubConstants.PARAMETER_VALUES.get(3));
        aemContext.registerInjectActivateService(azureTableStorageService, _config);

        userPreferenceService = new MockUserPreferenceServiceImpl();
        aemContext.registerService(UserPreferenceService.class, userPreferenceService);
        
        Map<String, Object> preferredLanguagesConfig = new HashMap<>();
        preferredLanguagesConfig.put(PATH, CONTENT_FRAGMENT_PATH);
        aemContext.registerInjectActivateService(preferredLanguagesService, preferredLanguagesConfig);
        
        Resource resource = aemContext.currentResource(CONTENT_ROOT);
        aemContext.request().setResource(resource);
        additionalLanguageSelectorModel = aemContext.request().adaptTo(AdditionalLanguageSelectorModel.class);
    }

    @Test
    public void testLanguageSelectorModelData() {
        Assert.assertEquals("Selected language", "fr", additionalLanguageSelectorModel.getSelectedLanguage());
        Assert.assertEquals("List of languages", 2, additionalLanguageSelectorModel.getListOfLanguages().size());
    }
}
