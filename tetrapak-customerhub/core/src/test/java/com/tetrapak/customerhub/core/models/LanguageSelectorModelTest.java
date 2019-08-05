package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.MockUserPreferenceServiceImpl;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.services.impl.AzureTableStorageServiceImpl;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class LanguageSelectorModelTest {

    private LanguageSelectorModel languageSelectorModel;

    private AzureTableStorageServiceImpl azureTableStorageService = new AzureTableStorageServiceImpl();
    private UserPreferenceService userPreferenceService;

    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global/en";
    private static final String RESOURCE_JSON = "allContent.json";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(RESOURCE_JSON, CONTENT_ROOT);

    @Before
    public void setup() {
        Map<String, Object> _config = new HashMap<>();
        _config.put("defaultEndpointsProtocol", "https");
        _config.put("accountKey", "Fa6WBGXsJZ+9Hyt5ggAKQD4WJQ4j77foq4a8S2S+wr663sVxPO5AFrhOPEgbxsPt+WBYDyfH654CIlfncy0klg==");
        _config.put("accountName", "ta01cfedsta01");
        _config.put("tableName", "preferences");
        aemContext.registerInjectActivateService(azureTableStorageService, _config);

        userPreferenceService = new MockUserPreferenceServiceImpl();
        aemContext.registerService(UserPreferenceService.class, userPreferenceService);

        aemContext.currentResource(CONTENT_ROOT);
        languageSelectorModel = aemContext.request().adaptTo(LanguageSelectorModel.class);
    }

    @Test
    public void testLanguageSelectorModelData() {
        Assert.assertEquals("Heading", "Choose your language", languageSelectorModel.getHeadingI18n());
        Assert.assertEquals("Heading", "Close", languageSelectorModel.getCloseBtnTextI18n());
        Assert.assertEquals("Heading", "fr", languageSelectorModel.getSelectedLanguage());
        Assert.assertEquals("Heading", "fr", languageSelectorModel.getPageLanguage());
        Assert.assertEquals("Heading", 5, languageSelectorModel.getListOfLanguages().size());
    }
}
