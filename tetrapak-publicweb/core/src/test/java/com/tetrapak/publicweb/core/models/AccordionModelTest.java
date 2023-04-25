package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;

public class AccordionModelTest {
	@Rule
	public AemContext context = new AemContext();

	/** The Constant RESOURCE_CONTENT. */
	private static final String RESOURCE_CONTENT = "/accordion/test-content.json";

	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/en/dashboard";

	/** The Constant RESOURCE. */
	private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/accordion";

	/** The model. */
	private AccordionModel model;

	/** The resource. */
	private Resource resource;

	 /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {

        Class<AccordionModel> modelClass = AccordionModel.class;
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
    public void testSimpleLoadAndGetters() throws Exception {
    	assertEquals("Heading of the accordion component", model.getHeading());
        assertEquals("Accordion list - Heading 2", model.getAccordionList().get(1).getAccordionHeading());
     
    }
}
