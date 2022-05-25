package com.tetrapak.customerhub.core.models;

import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;

import io.wcm.testing.mock.aem.junit.AemContext;

public class MediaLinkModelTest {
    
    @Rule
    public AemContext context = new AemContext();

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "mediaLink.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/customerhub/global/en/dashboard";

    /** The Constant RESOURCE. */
    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/mediaLink";

    /** The model. */
    private MediaLinkModel mediaLinkModel;

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
    
    @Before
    public void setup() {
        Resource mediaLinkResource = aemContext.currentResource(RESOURCE);
        assert mediaLinkResource != null;
        mediaLinkModel = mediaLinkResource.adaptTo(MediaLinkModel.class);
    }

    @Test
    public void testGettersAndSetters() {
        Assert.assertEquals("Heading", mediaLinkModel.getHeading());
        Assert.assertEquals("title 1", mediaLinkModel.getColumnOneTitle());
        Assert.assertEquals("title 2", mediaLinkModel.getColumnTwoTitle());
        Assert.assertEquals("title 3", mediaLinkModel.getColumnThreeTitle());        
        Assert.assertEquals("/content/tetrapak/customerhub/global/en/about-us.html",
                mediaLinkModel.getColumnOneList().get(0).getLinkUrl());
        Assert.assertEquals("/content/tetrapak/customerhub/global/en/about-us.html",
                mediaLinkModel.getColumnTwoList().get(0).getLinkUrl());
        Assert.assertEquals("/content/tetrapak/customerhub/global/en/about-us.html",
                mediaLinkModel.getColumnThreeList().get(0).getLinkUrl());
        Assert.assertEquals("Link Label 3", mediaLinkModel.getColumnThreeList().get(0).getLinkText());
        Assert.assertEquals("Link Label 2", mediaLinkModel.getColumnTwoList().get(0).getLinkText());
        Assert.assertEquals("Link Label 1", mediaLinkModel.getColumnOneList().get(0).getLinkText());
    }
}
