package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.beans.GetStartedBean;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class IntroScreenModelTest {

    //@Inject
    private IntroScreenModel introScreenModel;

    @Rule
    public final SlingContext context = new SlingContext();

    @Before
    public void setup() {
        context.addModelsForPackage("com.tetrapak.customerhub.core.models");
        context.build().resource("/content/tetrapak/customerhub/global/about-us/jcr:content/par/getstarted",
                "sling:resourceType", "customerhub/components/content/getstarted",
                "headingI18n", "About_GS_Heading",
                "className","abc",
                "closeBtnI18n", "About_GS_Close",
                "getStartedBtnI18n", "About_GS_Started",
                "nextBtnI18n", "About_GS_Next"
        ).commit();
        context.build().resource("/content/tetrapak/customerhub/global/about-us/jcr:content/par/getstarted/list",
                "jcr:primaryType", "nt:unstructured"
        ).commit();
        context.build().resource("/content/tetrapak/customerhub/global/about-us/jcr:content/par/getstarted/list/item0",
                "jcr:primaryType", "nt:unstructured",
                "titleI18n", "About_GS_title1",
                "descriptionI18n", "About_GS_Desc1",
                "imageAltI18n", "About_GS_Image1_alt",
                "imagePath", "/content/dam/customerhub/p2.PNG"
        ).commit();
        Resource currentResource = context.currentResource("/content/tetrapak/customerhub/global/about-us/jcr:content/par/getstarted");
        introScreenModel = currentResource.adaptTo(IntroScreenModel.class);
    }

    @Test
    public void testGetMessage() {
        Assert.assertEquals("About_GS_Heading", introScreenModel.getHeadingI18n());
        Assert.assertEquals("abc", introScreenModel.getClassName());
        Assert.assertEquals("About_GS_Close", introScreenModel.getCloseBtnI18n());
        Assert.assertEquals("About_GS_Started", introScreenModel.getGetStartedBtnI18n());
        Assert.assertEquals("About_GS_Next", introScreenModel.getNextBtnI18n());
        assertTrue(introScreenModel.getGetStartedList().size() > 0);
        List<GetStartedBean> list = introScreenModel.getGetStartedList();
        GetStartedBean bean = list.get(0);
        Assert.assertEquals("About_GS_title1",bean.getTitleI18n());
        Assert.assertEquals("About_GS_Desc1",bean.getDescriptionI18n());
        Assert.assertEquals("About_GS_Image1_alt",bean.getImageAltI18n());
        Assert.assertEquals("/content/dam/customerhub/p2.PNG",bean.getImagePath());
    }

}
