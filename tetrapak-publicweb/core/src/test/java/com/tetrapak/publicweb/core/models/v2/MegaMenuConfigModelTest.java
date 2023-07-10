package com.tetrapak.publicweb.core.models.v2;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MegaMenuConfigModelTest {

    @Rule
    public AemContext context = new AemContext();

    private static final String RESOURCE_CONTENT = "/headerv2/megamenuconfigv2.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/experience-fragments/publicweb/en/solutions-mega-menu/master";

    /** The Constant RESOURCE. */
    private static final String RESOURCE_PATH = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/megamenuconfig";

    private MegaMenuConfigModel model;

    /** The resource. */
    private Resource resource;

    @Before
    public void setUp() throws Exception {

        final Class<MegaMenuConfigModel> modelClass = MegaMenuConfigModel.class;
        // load the resources for each object
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);
        resource = context.currentResource(RESOURCE_PATH);
        model = resource.adaptTo(modelClass);
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(3, model.getNumberOfColumns());
    }

}
