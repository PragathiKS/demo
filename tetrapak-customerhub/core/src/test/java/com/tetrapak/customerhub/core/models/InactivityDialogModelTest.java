package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.MockUserPreferenceServiceImpl;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.services.impl.AzureTableStorageServiceImpl;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class InactivityDialogModelTest {

    private InactivityDialogModel inactivityDialogModel;

    private AzureTableStorageServiceImpl azureTableStorageService = new AzureTableStorageServiceImpl();

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

        UserPreferenceService userPreferenceService = new MockUserPreferenceServiceImpl();
        aemContext.registerService(UserPreferenceService.class, userPreferenceService);

        aemContext.currentResource(CONTENT_ROOT);
        inactivityDialogModel = aemContext.request().adaptTo(InactivityDialogModel.class);
    }

    @Test
    public void testModelData() {
        Assert.assertEquals("Continue button", "Continue", inactivityDialogModel.getContinueBtnI18n());
        Assert.assertEquals("message", "You have been inactive for a while. Would you like to continue or Logout?", inactivityDialogModel.getMessageI18n());
        Assert.assertEquals("logout button", "Logout", inactivityDialogModel.getLogoutBtnI18n());
        Assert.assertEquals("close button", "Close", inactivityDialogModel.getCloseBtnI18n());
        Assert.assertEquals("logout URL", "https://ptwjveuug6a2mtsbyyycta-on.drv.tw/SSO/ssodev.tetrapak.com/idp/stopSSOdev_new.html", inactivityDialogModel.getLogoutURL());
        Assert.assertEquals("Locale", "fr", inactivityDialogModel.getLocale());
        Assert.assertEquals("Idle Timeout", "15", inactivityDialogModel.getIdleTimeoutMinutes());
    }
}
