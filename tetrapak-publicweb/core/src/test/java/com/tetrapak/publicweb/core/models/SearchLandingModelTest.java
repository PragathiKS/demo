package com.tetrapak.publicweb.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;

import static org.junit.Assert.assertEquals;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SearchLandingModelTest {

    @Rule
    public AemContext context = new AemContext();

    private static final String RESOURCE_CONTENT = "/searchlanding/test-content.json";

    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-masters/en/about-tetra-pak/news-and-events/events-landing-page";

    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/searchlanding";

    private SearchLandingModel model;

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

        Class<SearchLandingModel> modelClass = SearchLandingModel.class;

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
    	assertEquals("Tetra Pak event archive", model.getHeading());
    	assertEquals("Looking for a previous event or information for a specific Tetra Pak event? Use the search field or filtering options below to find events on topics related to our business and products.", model.getDescription());
    	assertEquals("events", model.getType());
    	assertEquals(resource.getPath(), model.getServletPath());
    }
}
