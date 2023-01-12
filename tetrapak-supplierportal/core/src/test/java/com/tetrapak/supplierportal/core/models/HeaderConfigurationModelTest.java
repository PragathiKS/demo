package com.tetrapak.supplierportal.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HeaderConfigurationModelTest {
    @Rule public AemContext context = new AemContext();

    private static final String RESOURCE_CONTENT = "/header/header.json";

    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/supplierportal/global/en";

    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/headerconfiguration";

    private HeaderConfigurationModel model;

    private Resource resource;

    @Before public void setUp() throws Exception {

        Class<HeaderConfigurationModel> modelClass = HeaderConfigurationModel.class;
        // load the resources for each object
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);

        resource = context.currentResource(RESOURCE);
        model = resource.adaptTo(modelClass);
    }

    @Test public void simpleLoadAndGettersTest() throws Exception {
        assertEquals("/content/tetrapak/supplierportal/ca/en", model.getLogoLink());
        assertEquals("/content/dam/tetrapak/supplierportal/Logo.png", model.getLogoUrl());
    }
}
