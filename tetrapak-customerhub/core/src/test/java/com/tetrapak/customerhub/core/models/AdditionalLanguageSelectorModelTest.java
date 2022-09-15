package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.MockUserPreferenceServiceImpl;
import com.tetrapak.customerhub.core.services.PreferredLanguagesService;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.services.impl.AzureTableStorageServiceImpl;
import com.tetrapak.customerhub.core.services.impl.PreferredLanguagesServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.HashMap;
import java.util.Map;

public class AdditionalLanguageSelectorModelTest {

    private AdditionalLanguageSelectorModel additionalLanguageSelectorModel;

    private AzureTableStorageServiceImpl azureTableStorageService = new AzureTableStorageServiceImpl();
    
    private UserPreferenceService userPreferenceService;
    
    @Spy
    @InjectMocks
    private PreferredLanguagesService preferredLanguagesServiceImpl = new PreferredLanguagesServiceImpl();
    
    private static final String CONTENT_ROOT = "/content/dam/tetrapak/customerhub/contentfragment/preferred-languages";
    private static final String RESOURCE_JSON = "preferredLangContentFragment.json";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(RESOURCE_JSON, CONTENT_ROOT);

    @Before
    public void setup() {
    	MockitoAnnotations.initMocks(this);
        Map<String, Object> _config = new HashMap<>();
        _config.put("defaultEndpointsProtocol", "https");
        _config.put("accountKey", "Fa6WBGXsJZ+9Hyt5ggAKQD4WJQ4j77foq4a8S2S+wr663sVxPO5AFrhOPEgbxsPt+WBYDyfH654CIlfncy0klg==");
        _config.put("accountName", "ta01cfedsta01");
        _config.put("tableName", "preferences");
        aemContext.registerInjectActivateService(azureTableStorageService, _config);

        userPreferenceService = new MockUserPreferenceServiceImpl();
        aemContext.registerService(UserPreferenceService.class, userPreferenceService);
        
        Map<String, Object> preferredLanguagesConfig = new HashMap<>();
        preferredLanguagesConfig.put("path", CONTENT_ROOT);
        aemContext.registerInjectActivateService(preferredLanguagesServiceImpl, preferredLanguagesConfig);
    	aemContext.registerService(PreferredLanguagesService.class, preferredLanguagesServiceImpl);
        
        aemContext.currentResource(CONTENT_ROOT);
        additionalLanguageSelectorModel = aemContext.request().adaptTo(AdditionalLanguageSelectorModel.class);
    }

    @Test
    public void testLanguageSelectorModelData() {
        Assert.assertEquals("Heading", "fr", additionalLanguageSelectorModel.getSelectedLanguage());
        Assert.assertEquals("Heading", 2, additionalLanguageSelectorModel.getListOfLanguages().size());
    }
}
