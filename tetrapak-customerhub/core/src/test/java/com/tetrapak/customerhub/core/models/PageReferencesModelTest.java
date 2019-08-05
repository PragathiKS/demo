package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.MockUserPreferenceServiceImpl;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;

public class PageReferencesModelTest {

    private PageReferencesModel pageReferencesModel = null;
    private UserPreferenceService userPreferenceService;
    private static final String PAGE_REFERENCE = "/content/tetrapak/customerhub/global/en/dashboard/jcr:content/root/responsivegrid/pagereference";
    private static final String PAGE_REFERENCE_RESOURCE_JSON = "pagereference.json";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(PAGE_REFERENCE_RESOURCE_JSON, PAGE_REFERENCE);

    @Before
    public void setup() {
        userPreferenceService = new MockUserPreferenceServiceImpl();
        aemContext.registerService(UserPreferenceService.class, userPreferenceService);
        aemContext.currentResource(PAGE_REFERENCE);
        aemContext.request().setAttribute("pageContentPath", "/content/tetrapak/customerhub/content-components/en/dashboard");
        pageReferencesModel = aemContext.request().adaptTo(PageReferencesModel.class);
    }

    @Test
    public void testPageContentPath() {
        Assert.assertEquals("page reference", "/content/tetrapak/customerhub/content-components/en/dashboard", pageReferencesModel.getPageContentPath());
    }


}
