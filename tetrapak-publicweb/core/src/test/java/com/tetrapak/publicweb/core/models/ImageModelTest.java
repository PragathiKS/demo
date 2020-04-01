package com.tetrapak.publicweb.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ImageModelTest {

    @Rule
    public AemContext context = new AemContext();

    /**
     * The Constant RESOURCE_CONTENT.
     */
    private static final String RESOURCE_CONTENT = "/image/test-content.json";

    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/public-web/lang-masters/en/sustainability";

    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/image";


    private ImageModel model;

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

        Class<ImageModel> modelClass = ImageModel.class;
        // load the resources for each object
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
        String[] methods = new String[]{"getFileReference", "getAlt", "getLinkURL",
                "getPwPadding", "getAnchorId", "getAnchorTitle"};
        Util.testLoadAndGetters(methods, model, resource);
    }
}
