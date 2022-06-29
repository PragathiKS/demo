package com.tetrapak.customerhub.core.models;

import static org.junit.Assert.assertEquals;

import java.security.AccessControlException;
import java.util.Locale;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.day.cq.tagging.InvalidTagFormatException;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;

import io.wcm.testing.mock.aem.junit.AemContext;

public class KeylinesModelTest {
    /** The Constant TEST_CONTENT. */
    private static final String TEST_CONTENT = "keylines.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/customerhub/content-components/en/keyline";

    /** The Constant RESOURCE_PATH. */
    private static final String RESOURCE_PATH = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/keylines";

    private static final String TEST_API_URL = ".assets.json";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(TEST_CONTENT, TEST_CONTENT_ROOT);

    /** The model. */
    private KeylinesModel model;

    /** The Tag Manager. */
    @Mock
    private TagManager tagManager;

    @Mock
    private Tag tag1;

    @Mock
    private Tag tag2;

    @Mock
    private Tag tag3;

    @Mock
    private Tag parentTag;

    @Mock
    private ResourceResolver resourceResolver;

    @Mock
    private PageManager pageManager;

    @Mock
    private Page page;

    private static final String[] TAGS_VALUE = new String[] { "tetrapak:keylines/tetra-rex/mid",
	    "tetrapak:keylines/tetra-rex/base", "tetrapak:keylines/tetra-rex/high", "tetrapak:keylines/tetra-rex" };

    private static final String[] TAGS_VALUE_TITLES = new String[] { "mid", "base", "high", "tetra-rex" };

    /**
     * Sets the up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
	Resource resource = aemContext.currentResource(RESOURCE_PATH);
	aemContext.request().setResource(resource);
	model = aemContext.request().adaptTo(KeylinesModel.class);
	//Resource tagResource = aemContext.load().json("/keyline-tags.json", "/content/cq:tags/tetrapak");
	MockitoAnnotations.initMocks(this);
	
	Mockito.when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
	Mockito.when(resourceResolver.adaptTo(TagManager.class)).thenReturn(tagManager);
	Mockito.when(pageManager.getContainingPage(resource)).thenReturn(page);
	Mockito.when(page.getLanguage(true)).thenReturn(new Locale("en"));
//	tagManager = resourceResolver.adaptTo(TagManager.class);
//	tag1 = tagManager.createTag(TAGS_VALUE[0], TAGS_VALUE_TITLES[0], TAGS_VALUE_TITLES[0]);
//	tag2 = tagManager.createTag(TAGS_VALUE[1], TAGS_VALUE_TITLES[1], TAGS_VALUE_TITLES[1]);
//	tag3 = tagManager.createTag(TAGS_VALUE[2], TAGS_VALUE_TITLES[2], TAGS_VALUE_TITLES[2]);
//	parentTag = tagManager.createTag(TAGS_VALUE[3], TAGS_VALUE_TITLES[3], TAGS_VALUE_TITLES[3]);
	Mockito.when(tagManager.resolve(TAGS_VALUE[0])).thenReturn(tag1);
	Mockito.when(tagManager.resolve(TAGS_VALUE[1])).thenReturn(tag2);
	Mockito.when(tagManager.resolve(TAGS_VALUE[2])).thenReturn(tag3);
	Mockito.when(tag1.getParent()).thenReturn(parentTag);
	Mockito.when(tag1.getParent().getTagID()).thenReturn("tetrapak:keylines/tetra-rex");
	Mockito.when(tagManager.resolve(TAGS_VALUE[0]).getTitle()).thenReturn(TAGS_VALUE_TITLES[0]);
	Mockito.when(tag2.getTitle(new Locale("en"))).thenReturn(TAGS_VALUE_TITLES[1]);
	Mockito.when(tag3.getTitle(new Locale("en"))).thenReturn(TAGS_VALUE_TITLES[2]);
	Mockito.when(tag1.getName()).thenReturn(TAGS_VALUE_TITLES[0]);
	Mockito.when(tag2.getName()).thenReturn(TAGS_VALUE_TITLES[1]);
	Mockito.when(tag3.getName()).thenReturn(TAGS_VALUE_TITLES[2]);
    }

    /**
     * Test method for TextVideo model.
     * 
     * @throws InvalidTagFormatException
     * @throws AccessControlException
     */
    @Test
    public void testModel() throws AccessControlException, InvalidTagFormatException {
	assertEquals("Keylines", model.getTitle());
	assertEquals("keylines", model.getAnchorId());
	assertEquals("Keylines", model.getAnchorTitle());
	assertEquals("grayscale-white", model.getPwTheme());
	assertEquals("cuhu.packDesign.keylines.download", model.getDowloadText());
	assertEquals(RESOURCE_PATH + TEST_API_URL, model.getApiUrl());
	ShapeModel shape = model.getShapes().get(0);
	assertEquals("tetrapak:keylines/tetra-rex/mid", shape.getShape());
	assertEquals(
		"/content/dam/tetrapak/media-box/global/en/packaging/package-type/tetra-classic-aseptic/images/test-Adobe-Image.jpg",
		shape.getFileReference());
	assertEquals("Mid", shape.getAlt());
//	assertEquals("mid", shape.getName());
//	assertEquals("Mid", shape.getTitle());

    }
}
