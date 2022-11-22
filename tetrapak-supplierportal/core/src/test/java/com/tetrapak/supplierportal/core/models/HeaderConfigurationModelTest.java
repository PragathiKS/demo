package com.tetrapak.supplierportal.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Ignore
public class HeaderConfigurationModelTest {
    @Rule
    public AemContext context = new AemContext();

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/headerconfiguration/test-content.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/supplierportal/en";

    /** The Constant RESOURCE. */
    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/headerconfiguration";

    /** The model. */
    private HeaderConfigurationModel model;

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

        Class<HeaderConfigurationModel> modelClass = HeaderConfigurationModel.class;
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
        assertEquals("/content/tetrapak/supplierportal/global/en", model.getLogoLink());
        assertEquals("Logo ", model.getLogoAlt());
        assertEquals("http://www.google.com", model.getLoginLink());
        assertEquals("Login Label", model.getLoginLabel());
        assertEquals("/content/tetrapak/supplierportal/global/en", model.getContactLink());
        assertEquals("Contact Us Label", model.getContactText());
        assertEquals("/content/tetrapak/supplierportal/global/en/solutions", model.getSolutionPage());
        assertEquals("/content/dam/tetrapak/supplierportal/global/header/header.png", model.getLogoImagePath());
        assertEquals("/content/tetrapak/supplierportal/lang-masters/en/search", model.getSearchPage());
        assertEquals(false, model.getMarketSelectorDisabled());
    }
}
