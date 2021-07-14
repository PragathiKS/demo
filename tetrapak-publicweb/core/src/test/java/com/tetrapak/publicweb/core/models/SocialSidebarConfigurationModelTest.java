package com.tetrapak.publicweb.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SocialSidebarConfigurationModelTest {
    @Rule
    public AemContext context = new AemContext();

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/socialsidebarconfig/test-content.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/publicweb/en";

    /** The Constant RESOURCE. */
    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/socialsidebarconfig";

    /** The model. */
    private SocialSideBarConfigurationModel model;

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

        Class<SocialSideBarConfigurationModel> modelClass = SocialSideBarConfigurationModel.class;
        // load the resources for each object
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);
        
        resource = context.currentResource(RESOURCE);
        model = resource.adaptTo(modelClass);
    }

    /**
     * Test model, resource and all getters of the Header Config model.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void simpleLoadAndGettersTest() throws Exception {
        assertEquals("Linkedin", model.getSocialLinks().get(0).getSocialMedia());
        assertEquals("http://www.google.com", model.getSocialLinks().get(0).getSocialMediaLink());
        assertEquals("/content/dam/tetrapak/publicweb/gb/TetraPak-Wechat-QR-code.png", model.getWechatQrCodeReference());
        assertEquals("Alt Text",model.getQrAltText());
    }
}
