package com.tetrapak.customerhub.core.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.tetrapak.customerhub.core.beans.TabsListBean;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * Test class for TabsListModel class.
 * 
 * @author tustusha
 */
public class TabsListModelTest {

	private TabsListModel tabsListModel;
	private static final String TABSLIST_CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/about-my-tetra-pak/jcr:content/par/tabslist";
	private static final String TABSLIST_RESOURCE_JSON = "tabslist.json";

	/**
	 * Setting the context for the class.
	 */
	@Rule
	public final AemContext aemContext = CuhuCoreAemContext.getAemContext(TABSLIST_RESOURCE_JSON,
			TABSLIST_CONTENT_ROOT);

	/**
	 * Setup method for the class.
	 */
	@Before
	public void setup() {
		Resource tabsListResource = aemContext.currentResource(TABSLIST_CONTENT_ROOT);
		tabsListModel = tabsListResource.adaptTo(TabsListModel.class);
	}

	/**
	 * Test method to validate the methods of TabsList model class.
	 */
	@Test
	public void testTabsListContent() {
		String heading = tabsListModel.getHeading();
		assertNotNull("Heading should not be null", heading);
		assertTrue("Heading is not empty", heading.length() > 0);
		Assert.assertEquals("Heading For the Tabs List Component", heading);
		
		assertFalse(tabsListModel.getTabs().isEmpty());
		List<TabsListBean> tabs = tabsListModel.getTabs();
		
		TabsListBean imageTabBean = tabs.get(0);
		Assert.assertEquals("Image Tab", imageTabBean.getTabTitleI18n());
		Assert.assertEquals("Image Content Title", imageTabBean.getMediaTitleI18n());
		Assert.assertEquals("<p>Some description</p> ", imageTabBean.getMediaDescriptionI18n());
		Assert.assertEquals("/content/dam/customerhub/tetrapak.png", imageTabBean.getImagePath());
		Assert.assertEquals("Image alt text", imageTabBean.getImageAltI18n());
		Assert.assertEquals(true, imageTabBean.getIsExternal());
		Assert.assertEquals("link text", imageTabBean.getLinkTextI18n());
		Assert.assertEquals("http://www.tetrapak.com", imageTabBean.getLinkURL());
		Assert.assertEquals("button", imageTabBean.getLinkType());
		
		TabsListBean videoTabBean = tabs.get(1);
		Assert.assertEquals("video", videoTabBean.getType());
		Assert.assertEquals("youtube", videoTabBean.getVideoSource());
		Assert.assertEquals("https://www.youtube.com/embed/ZofQqevIIZA", videoTabBean.getYoutubeEmbedURL());
		Assert.assertEquals("/content/dam/customerhub/thumbnail.png", videoTabBean.getThumbnailPath());
	}

}
