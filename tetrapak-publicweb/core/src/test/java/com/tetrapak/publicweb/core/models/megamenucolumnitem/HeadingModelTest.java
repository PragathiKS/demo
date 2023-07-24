package com.tetrapak.publicweb.core.models.megamenucolumnitem;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HeadingModelTest {

    @Rule
    public AemContext context = new AemContext();

    private static final String RESOURCE_CONTENT = "/headerv2/megamenuconfigv2.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/experience-fragments/publicweb/en/solutions-mega-menu/master";

    /** The Constant RESOURCE. */
    private static final String RESOURCE_PATH = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/megamenuconfig/col1/heading";

    private HeadingModel model;

    /** The resource. */
    private Resource resource;

    @Before
    public void setUp() throws Exception {

        final Class<com.tetrapak.publicweb.core.models.megamenucolumnitem.HeadingModel> modelClass = com.tetrapak.publicweb.core.models.megamenucolumnitem.HeadingModel.class;
        // load the resources for each object
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);
        resource = context.currentResource(RESOURCE_PATH);
        model = resource.adaptTo(modelClass);
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals("Solutions", model.getHeading());
        assertEquals("/content/tetrapak/publicweb/lang-masters/en/solutions", model.getHeadingURL());
    }
}