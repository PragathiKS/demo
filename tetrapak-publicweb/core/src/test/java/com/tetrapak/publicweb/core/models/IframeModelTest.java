package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.publicweb.core.models.IframeModel;

import io.wcm.testing.mock.aem.junit.AemContext;

public class IframeModelTest {

    @Rule
    public AemContext context = new AemContext();

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/iframe/test-content.json";

    /** The Constant ANOTHER_TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/publicweb/en/home";

    /** The Constant TEXTVIDEO_RESOURCE. */
    private static final String RESOURCE_PATH = TEST_CONTENT_ROOT + "/jcr:content/iframe";

    /** The model. */
    private IframeModel model;

    /** The resource. */
    private Resource resource;

    /**
     * Sets the up.
     *
     * @param context the new up
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
	final Class<IframeModel> modelClass = IframeModel.class;
	context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
	context.addModelsForClasses(modelClass);
	resource = context.currentResource(RESOURCE_PATH);
	model = resource.adaptTo(modelClass);
    }

    /**
     * Test model, resource and all getters of the accordion model
     *
     */
    @Test
    public void tesGetMehtods() {
        Assert.assertEquals("https://tsoperationalbenchmark.tetrapak.com/Templates/Pages/Intro.aspx", model.getIframe());
        Assert.assertEquals("anchorId", model.getAnchorId());
        Assert.assertEquals("anchor title", model.getAnchorTitle());
    }

}
