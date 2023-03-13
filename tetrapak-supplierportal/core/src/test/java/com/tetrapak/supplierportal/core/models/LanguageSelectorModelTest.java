package com.tetrapak.supplierportal.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.msm.api.Blueprint;
import com.day.cq.wcm.msm.api.BlueprintManager;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;
import com.tetrapak.supplierportal.core.mock.MockUserPreferenceServiceImpl;
import com.tetrapak.supplierportal.core.mock.SupplierPortalCoreAemContext;
import com.tetrapak.supplierportal.core.services.UserPreferenceService;
import com.tetrapak.supplierportal.core.services.impl.AzureTableStorageServiceImpl;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class) public class LanguageSelectorModelTest {

    private AzureTableStorageServiceImpl azureTableStorageService = new AzureTableStorageServiceImpl();
    private UserPreferenceService userPreferenceService;

    private static final String CONTENT_ROOT = "/content/tetrapak/supplierportal/global/en";
    private static final String RESOURCE_JSON = "allContent.json";

    @Rule public final AemContext aemContext = SupplierPortalCoreAemContext.getAemContextWithJcrMock(RESOURCE_JSON,
            CONTENT_ROOT);

    @Mock private BlueprintManager blueprintManager;

    @Mock private PageManager pageManager;

    @Mock private SlingHttpServletRequest request;

    private Collection<Blueprint> blueprints = new ArrayList<>();

    @Mock private Blueprint blueprint;

    @Mock private LiveRelationshipManager liveRelationshipManager;

    @InjectMocks private LanguageSelectorModel languageSelectorModel = new LanguageSelectorModel();

    @Mock private Page page;

    @Before public void setup() throws WCMException {
        blueprints.add(blueprint);

        Map<String, Object> _config = new HashMap<>();
        _config.put("defaultEndpointsProtocol", "https");
        _config.put("accountKey",
                "Fa6WBGXsJZ+9Hyt5ggAKQD4WJQ4j77foq4a8S2S+wr663sVxPO5AFrhOPEgbxsPt+WBYDyfH654CIlfncy0klg==");
        _config.put("accountName", "ta01cfedsta01");
        _config.put("tableName", "preferences");
        aemContext.registerInjectActivateService(azureTableStorageService, _config);

        userPreferenceService = new MockUserPreferenceServiceImpl();
        aemContext.registerService(UserPreferenceService.class, userPreferenceService);

        aemContext.currentResource(CONTENT_ROOT);
        ResourceResolver rr = aemContext.request().getResourceResolver();
        Mockito.when(blueprint.getSitePath()).thenReturn("/content/tetrapak/supplierportal/global/en");
        Mockito.when(blueprintManager.getBlueprints()).thenReturn(blueprints);
        Mockito.when(pageManager.getPage(Matchers.anyString())).thenReturn(page);
        Mockito.when(page.getName()).thenReturn("en");
        Mockito.when(page.getTitle()).thenReturn("English");
        Mockito.when(request.getResourceResolver()).thenReturn(rr);
        Mockito.when(request.getResource()).thenReturn(aemContext.currentResource());
        languageSelectorModel.init();
    }

    @Test public void testLanguageSelectorModelData() {
        Assert.assertEquals("Heading", "Choose your language", languageSelectorModel.getHeadingI18n());
        Assert.assertEquals("Heading", "Close", languageSelectorModel.getCloseBtnTextI18n());
        Assert.assertEquals("Heading", "en", languageSelectorModel.getSelectedLanguage());
        Assert.assertEquals("Heading", "en", languageSelectorModel.getLocale());
        Assert.assertEquals("Heading", 1, languageSelectorModel.getListOfLanguages().size());
    }
}
