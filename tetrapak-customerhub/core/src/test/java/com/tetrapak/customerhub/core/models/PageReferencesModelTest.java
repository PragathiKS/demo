package com.tetrapak.customerhub.core.models;

import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
public class PageReferencesModelTest {
	
	private PageReferencesModel pageReferencesModel= null;
	private static final String PAGE_REFERENCE = "/content/tetrapak/customerhub/global/en/dashboard/jcr:content/root/responsivegrid/pagereference";
	private static final String PAGE_REFERENCE_RESOURCE_JSON = "pagereference.json"; 
	
	 @Rule
	 public final AemContext aemContext = CuhuCoreAemContext.getAemContext(PAGE_REFERENCE_RESOURCE_JSON, PAGE_REFERENCE);

	 @Before
	 public void setup() {
		Resource pageReferenceResource = aemContext.currentResource(PAGE_REFERENCE);
		pageReferencesModel = pageReferenceResource.adaptTo(PageReferencesModel.class);
	 	}
	
	 @Test
	 public void testPageContentPath() {
		Assert.assertEquals("/content/tetrapak/customerhub/content-components/en/dashboard", pageReferencesModel.getPageContentPath());
	}
	

}
