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

public class IntroScreenModelTest {

    //@Inject
    private IntroScreenModel introScreenModel;

    private static final String GET_STARTED_CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/about-my-tetra-pak/jcr:content/par/getstarted";
    private static final String GET_STARTED_RESOURCE_JSON = "getstarted.json";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(GET_STARTED_RESOURCE_JSON, GET_STARTED_CONTENT_ROOT);

    @Before
    public void setup() {
        Resource currentResource = aemContext.currentResource(GET_STARTED_CONTENT_ROOT);
        introScreenModel = currentResource.adaptTo(IntroScreenModel.class);
    }

    @Test
    public void testGetMessage() {
        Assert.assertEquals("Heading", "getStarted", introScreenModel.getHeadingI18n());
        Assert.assertEquals("abc", "abc", introScreenModel.getClassName());
        Assert.assertEquals("close button", "close", introScreenModel.getCloseBtnI18n());
        Assert.assertEquals("get started button", "About_GS_Started", introScreenModel.getGetStartedBtnI18n());
        Assert.assertEquals("next button", "About_GS_Next", introScreenModel.getNextBtnI18n());
        Assert.assertEquals("list size", 2, introScreenModel.getGetStartedList().size());
        List<GetStartedBean> list = introScreenModel.getGetStartedList();
        GetStartedBean bean = list.get(0);
        Assert.assertEquals("title", "One place for your business", bean.getTitleI18n());
        Assert.assertEquals("description", "Processing solutions and equipment for dairy, cheese, ice cream, beverages and prepared food.", bean.getDescriptionI18n());
        ImageBean imageBean = bean.getImage();
        Assert.assertEquals("image alt text", "alt1", imageBean.getAltText());
        Assert.assertEquals("image path", "/content/dam/customerhub/p3.PNG", imageBean.getImagePath());
    }

}
