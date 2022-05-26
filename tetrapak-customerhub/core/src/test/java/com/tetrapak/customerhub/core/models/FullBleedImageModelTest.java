package com.tetrapak.customerhub.core.models;

import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;

import io.wcm.testing.mock.aem.junit.AemContext;

public class FullBleedImageModelTest {

    @Rule
    public AemContext context = new AemContext();

    /**
     * The Constant RESOURCE_CONTENT.
     */
    private static final String RESOURCE_CONTENT = "fullbleedimage.json";

    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/about-my-tetra-pak";

    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/fullbleedimage";


    private FullBleedImageModel model;
    
    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_CONTENT, TEST_CONTENT_ROOT);

    @Before
    public void setup() {
        Resource resource = aemContext.currentResource(RESOURCE);
        assert resource != null;
        model = resource.adaptTo(FullBleedImageModel.class);
    }

    @Test
    public void testGetStartedMessage() {
        Assert.assertEquals("/content/dam/tetrapak/publicweb/TetraPak.png", model.getFileReference());
        Assert.assertEquals("alt", model.getAlt());
        Assert.assertEquals("/content/tetrapak/customerhub/global/en/dashboard.html", model.getLinkURL());
    }
}
