package com.tetrapak.customerhub.core.models;

import static org.junit.Assert.assertEquals;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;

public class FooterConfigurationModelTest {

    private FooterConfigurationModel model = null;

    private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/home";

    private static final String FOOTER_CONFIG_CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/home/jcr:content/root/responsivegrid/footerconfiguration";

    private static final String FOOTER_CONFIG_JSON = "/footerconfiguration/test-content.json";

    @Rule
    public AemContext context = new AemContext();

    private Resource resource;

    @Before
    public void setUp() throws Exception {

        context.load().json(FOOTER_CONFIG_JSON, CONTENT_ROOT);
        context.addModelsForClasses(FooterConfigurationModel.class);

        resource = context.currentResource(FOOTER_CONFIG_CONTENT_ROOT);
        model = resource.adaptTo(FooterConfigurationModel.class);

    }

    /**
     * Test model, resource and all getters of the Footer Config model.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void simpleLoadAndGettersTest() throws Exception {
        assertEquals("Link text 2", model.getFooterLinks().get(0).getLinkText());
        assertEquals("/content/tetrapak/publicweb", model.getFooterLinks().get(0).getLinkPath());

    }
}
