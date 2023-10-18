package com.tetrapak.publicweb.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MegaMenuColumnModelTest {

    @Rule
    public AemContext context = new AemContext();

    @Mock
    private ModelFactory modelFactory;

    @Mock
    private Resource mockResource;

    private static final String RESOURCE_CONTENT = "/headerv2/megamenuconfigv2.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/experience-fragments/publicweb/en/solutions-mega-menu/master";

    /** The Constant RESOURCE. */
    private static final String RESOURCE_PATH = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/megamenuconfig/col1";

    private MegaMenuColumnModel model;

    /** The resource. */
    private Resource resource;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        final Class<com.tetrapak.publicweb.core.models.MegaMenuColumnModel> modelClass = com.tetrapak.publicweb.core.models.MegaMenuColumnModel.class;
        // load the resources for each object
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);
        resource = context.currentResource(RESOURCE_PATH);
        context.registerService(ModelFactory.class, modelFactory,
                org.osgi.framework.Constants.SERVICE_RANKING, Integer.MAX_VALUE);
        when(modelFactory.getModelFromResource(any())).thenReturn(mockResource);
        model = resource.adaptTo(modelClass);
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals("white", model.getBgColor());
        assertEquals(7, model.getItems().size());
    }

}
