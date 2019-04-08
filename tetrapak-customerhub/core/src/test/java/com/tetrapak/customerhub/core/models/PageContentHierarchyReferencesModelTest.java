package com.tetrapak.customerhub.core.models;

import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
public class PageContentHierarchyReferencesModelTest {
	
	private PageContentHierarchyReferencesModel pageContentHierarchyReferencesModel= null;
	private static final String PAGE_CONTENT_HIERARCHY_REFERENCE = "/content/tetrapak/customerhub/en/dashboard/training/jcr:content/root/responsivegrid/pagecontenthierarchy"; 
	private static final String PAGE_CONTENT_HIERARCHY_REFERENCE_RESOURCE_JSON = "pagecontenthierachyreference.json"; 
	
	 @Rule
	 public final AemContext aemContext = CuhuCoreAemContext.getAemContext(PAGE_CONTENT_HIERARCHY_REFERENCE_RESOURCE_JSON, PAGE_CONTENT_HIERARCHY_REFERENCE);

	 @Before
	 public void setup() {
		Resource pageContentHierarchyReferenceResource = aemContext.currentResource(PAGE_CONTENT_HIERARCHY_REFERENCE);
		pageContentHierarchyReferencesModel = pageContentHierarchyReferenceResource.adaptTo(PageContentHierarchyReferencesModel.class);
		
	 	}
	
	 @Test
	 public void testPageContentPath() {
		Assert.assertEquals("/content/tetrapak/customerhub/content-components/en/dashboard", pageContentHierarchyReferencesModel.getPageContentPath());
		Assert.assertEquals("false", pageContentHierarchyReferencesModel.getIncludeSubPages());
	}
	

}
