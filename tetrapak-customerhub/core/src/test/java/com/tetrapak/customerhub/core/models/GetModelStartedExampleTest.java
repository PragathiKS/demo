package com.tetrapak.customerhub.core.models;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.tetrapak.customerhub.core.beans.GetStartedBean;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;

import io.wcm.testing.mock.aem.junit.AemContext;

public class GetModelStartedExampleTest {
	
	private GetStartedModel getStartedModel= null;
	
	 @Rule
	 public final AemContext aemContext = CuhuCoreAemContext.getAemContext();
	
	 @Before
	 public void setup() {
		Resource getStartedResource = aemContext.currentResource(CuhuCoreAemContext.GET_STARTED_CONTENT_ROOT);
	    getStartedModel = getStartedResource.adaptTo(GetStartedModel.class);
	 	}
	
	 @Test
	 public void testGetStartedMessage() {
        String heading = getStartedModel.getHeadingI18n();
        assertNotNull(heading);
        assertTrue(heading.length() > 0);
        Assert.assertEquals("getStarted",getStartedModel.getHeadingI18n());
        assertTrue(getStartedModel.getGetStartedList().size() > 0);
        List<GetStartedBean> list = getStartedModel.getGetStartedList();
        GetStartedBean bean = list.get(0);
        Assert.assertEquals("One place for your business",bean.getTitleI18n());
        Assert.assertEquals("Processing solutions and equipment for dairy, cheese, ice cream, beverages and prepared food.",bean.getDescriptionI18n());
        Assert.assertEquals("Image1 alt",bean.getImageAltI18n());
        Assert.assertEquals("/content/dam/customerhub/p2.PNG",bean.getImagePath());
	}
}
