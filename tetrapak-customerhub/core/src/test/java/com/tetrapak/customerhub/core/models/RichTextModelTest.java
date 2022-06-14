package com.tetrapak.customerhub.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RichTextModelTest {

    @Rule
    public AemContext context = new AemContext();

    private static final String RESOURCE_CONTENT = "/rich-text-content.json";

    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/dashboard";

    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/richtext_985436779";

    private RichTextModel model;

    /**
     * The resource.
     */
    private Resource resource;

    /**
     * Sets the up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {

        Class<RichTextModel> modelClass = RichTextModel.class;

        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);

        resource = context.currentResource(RESOURCE);
        assert resource != null;
        model = resource.adaptTo(modelClass);
    }

    /**
     * Test model, resource and all getters of the accordion model
     *
     * @throws Exception exception
     */
    @Test
    public void testLoadAndGetters() throws Exception {
        Assert.assertNotNull(model.getText());
        Assert.assertNotNull(model.getPwPadding());
        Assert.assertNotNull(model.getPwTheme());
        Assert.assertNotNull(model.getTopSpacing());
        Assert.assertNotNull(model.getAnchorTitle());
        Assert.assertNotNull(model.getAnchorId());
    }
}
