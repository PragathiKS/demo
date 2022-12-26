package com.tetrapak.supplierportal.core.models;

import static org.junit.Assert.assertEquals;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;

public class FooterConfigurationModelTest {
    @Rule
    public AemContext context = new AemContext();

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/footerconfiguration/test-content.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/supplierportal/global";

    /** The Constant RESOURCE. */
    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/footerconfiguration";

    /** The model. */
    private FooterConfigurationModel model;

    /** The resource. */
    private Resource resource;

    /**
     * Sets the up.
     *
     * @param context
     *            the new up
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {

        Class<FooterConfigurationModel> modelClass = FooterConfigurationModel.class;
        // load the resources for each object
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);        
        resource = context.currentResource(RESOURCE);
        model = resource.adaptTo(modelClass);
    }

    /**
     * Test model, resource and all getters of the Footer Config model.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void simpleLoadAndGettersTest() throws Exception {
        assertEquals("Link text 2", model.getFooterLinks().get(0).getLinkLabel());
        assertEquals("/content/tetrapak/publicweb", model.getFooterLinks().get(0).getLinkPath());
        assertEquals("true", model.getLinkLangPopup());
        
    }
}