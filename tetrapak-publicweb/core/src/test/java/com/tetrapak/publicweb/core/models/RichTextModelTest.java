package com.tetrapak.publicweb.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RichTextModelTest {

    @Rule
    public AemContext context = new AemContext();

    private static final String RESOURCE_CONTENT = "/richtext/test-content.json";

    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-masters/en/solutions";

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
        String[] methods = new String[]{"getAnchorId", "getAnchorTitle", "getText", "getPwTheme", "getPwPadding", "getTopSpacing", "getPwTextColor"};
        Util.testLoadAndGetters(methods, model, resource);
    }
}
