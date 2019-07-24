package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.beans.GetStartedBean;
import com.tetrapak.customerhub.core.beans.ImageBean;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GetStartedModelTest {

    private GetStartedModel getStartedModel = null;
    private static final String GET_STARTED_CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/about-my-tetra-pak/jcr:content/par/getstarted";
    private static final String GET_STARTED_RESOURCE_JSON = "getstarted.json";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(GET_STARTED_RESOURCE_JSON, GET_STARTED_CONTENT_ROOT);

    @Before
    public void setup() {
        Resource getStartedResource = aemContext.currentResource(GET_STARTED_CONTENT_ROOT);
        getStartedModel = getStartedResource.adaptTo(GetStartedModel.class);
    }

    @Test
    public void testGetStartedMessage() {
        String heading = getStartedModel.getHeadingI18n();
        assertNotNull("Heading should not be null", heading);
        assertTrue("Heading is not empty", heading.length() > 0);
        Assert.assertEquals("Heading", "getStarted", getStartedModel.getHeadingI18n());
        Assert.assertEquals("class name", "abc", getStartedModel.getClassName());
        assertTrue("size should be greater than 0", getStartedModel.getGetStartedList().size() > 0);
        assertTrue("size should be greater than 0", getStartedModel.getImageList().size() > 0);
        List<GetStartedBean> list = getStartedModel.getGetStartedList();
        GetStartedBean bean = list.get(0);
        Assert.assertEquals("title", "One place for your business", bean.getTitleI18n());
        Assert.assertEquals("description", "Processing solutions and equipment for dairy, cheese, ice cream, beverages and prepared food.", bean.getDescriptionI18n());
        ImageBean image = bean.getImage();
        Assert.assertEquals("alt text", "alt1", image.getAltText());
        Assert.assertEquals("image path", "/content/dam/customerhub/p3.PNG", image.getImagePath());
        Assert.assertEquals("Desktop Height", "200", image.getDheight());
        Assert.assertEquals("Desktop Width", "100", image.getDwidth());
        Assert.assertEquals("Mobile Height Landscape", "290", image.getMheightl());
        Assert.assertEquals("Mobile Width Landscape", "100", image.getMwidthl());
        Assert.assertEquals("Mobile Height Portrait", "290", image.getMheightp());
        Assert.assertEquals("Mobile Width Portrait", "100", image.getMwidthp());
        Assert.assertEquals("image crop parameters", "390,57,947,473", image.getImageCrop());
    }

}
