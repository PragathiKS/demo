package com.tetrapak.customerhub.core.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import com.tetrapak.customerhub.core.beans.ImageBean;
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
		Assert.assertEquals("heading","Heading For the Tabs List Component", heading);
		
		assertNotNull("Component ID is not null", tabsListModel.getComponentId());
		assertTrue("Component ID is not empty", tabsListModel.getComponentId().length() > 0);
		
		assertFalse("tabs",tabsListModel.getTabs().isEmpty());
		assertFalse("image list",tabsListModel.getImageList().isEmpty());
		List<TabsListBean> tabs = tabsListModel.getTabs();
		
		TabsListBean imageTabBean = tabs.get(0);
		Assert.assertEquals("title","Image Tab", imageTabBean.getTabTitleI18n());
		Assert.assertEquals("media title","Image Content Title", imageTabBean.getMediaTitleI18n());
		Assert.assertEquals("media description","<p>Some description</p> ", imageTabBean.getMediaDescriptionI18n());

		Assert.assertEquals("is external",true, imageTabBean.getIsExternal());
		Assert.assertEquals("link text","link text", imageTabBean.getLinkTextI18n());
		Assert.assertEquals("link URL","http://www.tetrapak.com", imageTabBean.getLinkURL());
		Assert.assertEquals("link type","button", imageTabBean.getLinkType());
		
		TabsListBean videoTabBean = tabs.get(1);
		Assert.assertEquals("media type","video", videoTabBean.getType());
		Assert.assertEquals("video source","youtube", videoTabBean.getVideoSource());
		Assert.assertEquals("youtube embed URL","https://www.youtube.com/embed/ZofQqevIIZA", videoTabBean.getYoutubeEmbedURL());
		Assert.assertEquals("thumbnail path","/content/dam/customerhub/thumbnail.png", videoTabBean.getThumbnailPath());

		ImageBean image = imageTabBean.getImage();
		Assert.assertEquals("image path","/content/dam/customerhub/p3.PNG", image.getImagePath());
		Assert.assertEquals("alt1","alt1", image.getAltText());
		Assert.assertEquals("Desktop Height","200", image.getDheight());
		Assert.assertEquals("Desktop Height","100", image.getDwidth());
		Assert.assertEquals("Mobile Height Landscape","400", image.getMheightl());
		Assert.assertEquals("Mobile Width Landscape","200", image.getMwidthl());
		Assert.assertEquals("Mobile Height Portrait","500", image.getMheightp());
		Assert.assertEquals("Mobile Width Portrait","300", image.getMwidthp());
		Assert.assertEquals("Image crop","390,57,947,473", image.getImageCrop());
	}
}
