package com.tetrapak.customerhub.core.models;

import static org.junit.Assert.assertEquals;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.MockUserPreferenceServiceImpl;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import io.wcm.testing.mock.aem.junit.AemContext;

public class FooterModelTest {

    private FooterModel footerModel = null;

    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/dashboard/jcr:content";
    private static final String RESOURCE_JSON = "footer/test-content.json";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON, "/content/tetrapak/customerhub");

    @Before
    public void setUp() throws Exception {

        aemContext.currentResource(CONTENT_ROOT);
        footerModel = aemContext.request().adaptTo(FooterModel.class);
    }

    /**
     * Test model, resource and all getters of the Footer model.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void simpleLoadAndGettersTest() throws Exception {
        assertEquals("Link text 2", footerModel.getFooterLinks().get(0).getLinkText());
        assertEquals("/content/tetrapak/publicweb.html", footerModel.getFooterLinks().get(0).getLinkPath());
        assertEquals("Change Language", footerModel.getLanguageSwitchLabel());
    }
}