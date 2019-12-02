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
	private static final String PAGE_CONTENT_HIERARCHY_REFERENCE = "/content/tetrapak/customerhub/global/en/dashboard/jcr:content/root/responsivegrid/pagecontenthierarchy";
	private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global/en";
	private static final String PAGE_CONTENT_HIERARCHY_REFERENCE_RESOURCE_JSON = "allContent.json";
	private static final String CONTENT_COMPONENT_RESOURCE_JSON = "/content-components.json";

	 @Rule
	 public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(PAGE_CONTENT_HIERARCHY_REFERENCE_RESOURCE_JSON, CONTENT_ROOT);

	 @Before
	 public void setup() {
	 	aemContext.load().json(CONTENT_COMPONENT_RESOURCE_JSON,"/content/tetrapak/customerhub/content-components");
		Resource pageContentHierarchyReferenceResource = aemContext.currentResource(PAGE_CONTENT_HIERARCHY_REFERENCE);
		pageContentHierarchyReferencesModel = pageContentHierarchyReferenceResource.adaptTo(PageContentHierarchyReferencesModel.class);
	 	}
	
	 @Test
	 public void testPageContentPath() {
		Assert.assertEquals("/content/tetrapak/customerhub/content-components/en/dashboard", pageContentHierarchyReferencesModel.getPageContentPath());
		Assert.assertEquals("true", pageContentHierarchyReferencesModel.getIncludeSubPages());
	}

}
