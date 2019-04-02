package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.beans.GetStartedBean;
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

public class RecommendedForYouCardModelTest {

	private RecommendedForYouCardModel recommendedForYouCardModel = null;
	private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global/about-us/jcr:content/par/getstarted";
	private static final String RESOURCE_JSON = "getstarted.json";

	 @Rule
	 public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON, CONTENT_ROOT);

	 @Before
	 public void setup() {
		Resource getStartedResource = aemContext.currentResource(CONTENT_ROOT);
	    recommendedForYouCardModel = getStartedResource.adaptTo(RecommendedForYouCardModel.class);
	 	}

	 @Test
	 public void testGetStartedMessage() {
        String heading = recommendedForYouCardModel.getHeadingI18n();
        assertNotNull(heading);
        assertTrue(heading.length() > 0);
        Assert.assertEquals("getStarted", recommendedForYouCardModel.getHeadingI18n());
        Assert.assertEquals("abc", recommendedForYouCardModel.getClassName());
        assertTrue(recommendedForYouCardModel.getGetStartedList().size() > 0);
        List<GetStartedBean> list = recommendedForYouCardModel.getGetStartedList();
         GetStartedBean recommendedForYouCardBean = list.get(0);
        Assert.assertEquals("One place for your business",recommendedForYouCardBean.getTitleI18n());
        Assert.assertEquals("Processing solutions and equipment for dairy, cheese, ice cream, beverages and prepared food.",recommendedForYouCardBean.getDescriptionI18n());
        Assert.assertEquals("Image1 alt",recommendedForYouCardBean.getImageAltI18n());
        Assert.assertEquals("/content/dam/customerhub/p2.PNG",recommendedForYouCardBean.getImagePath());
        Assert.assertEquals("www.tetrapak.com", recommendedForYouCardBean.getLinkUrl());
        Assert.assertEquals("tetrapak", recommendedForYouCardBean.getLinkTextI18n());
        Assert.assertEquals(3,recommendedForYouCardModel.getCols());
	}

}
