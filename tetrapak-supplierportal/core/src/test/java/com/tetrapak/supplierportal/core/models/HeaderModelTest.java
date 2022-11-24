package com.tetrapak.supplierportal.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HeaderModelTest {

    private static final String RESOURCE_CONTENT = "/header/header.json";

    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/supplierportal";

    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content";

    @Rule public AemContext context = new AemContext();

    /**
     * The resource.
     */
    private Resource resource;

    /**
     * The model.
     */
    private HeaderModel model;

    Class<HeaderModel> modelClass = HeaderModel.class;

	@Before
	public void setUp() throws Exception {
		//TODO
		
		/*
		 * 
		 * MockSlingHttpServletRequest request = context.request();
		 * context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
		 * context.addModelsForPackage("com.tetrapak.supplierportal.core.models");
		 * 
		 * context.request().setPathInfo(TEST_CONTENT_ROOT);
		 * request.setResource(context.resourceResolver().getResource(RESOURCE));
		 * resource = context.currentResource(RESOURCE); model =
		 * request.adaptTo(modelClass);
		 * 
		 */}

    @Test public void simpleLoadAndGettersTest() throws Exception {
    	//TODO
		/*
		 * assertEquals("Header", "/content/dam/tetrapak/supplierporta/Logo.png",
		 * model.getLogoUrl()); assertEquals("Header",
		 * "/content/tetrapak/supplierporta/ca/de.html", model.getMLogoLink());
		 * assertEquals("Header", "/content/tetrapak/supplierporta/ca/en.html",
		 * model.getDLogoLink());
		 */
    }
}