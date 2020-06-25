package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.beans.SectionMenuBean;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class DataSourceModelTest.
 */
public class SectionMenuModelTest {

    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /** The context 2. */
    @Rule
    public AemContext context2 = new AemContext();

    /** The current page. */
    @Mock
    private Page currentPage;

    /** The page. */
    @Mock
    private Page page;

    /** The mock request. */
    @Mock
    private SlingHttpServletRequest mockRequest;

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/sectionmenu/test-content.json";

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT_TWO = "/sectionmenu/test-content2.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/public-web/lang-masters/en";

    /** The Constant RESOURCE. */
    private static final String RESOURCE_PATH = TEST_CONTENT_ROOT + "/solutions/processing/jcr:content";

    /** The Constant RESOURCE. */
    private static final String RESOURCE_PATH_TWO = TEST_CONTENT_ROOT + "/insight/case-article/jcr:content";

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT1 = "/contentFragments/pseudo-categories.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT1 = "/content/dam/tetrapak/publicweb/contentfragment/pseudo-categories";

    /** The model. */
    private SectionMenuModel model;

    /** The model class. */
    final Class<SectionMenuModel> modelClass = SectionMenuModel.class;

    /** The resource. */
    private Resource resource;

    /**
     * The setup method.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.load().json(RESOURCE_CONTENT1, TEST_CONTENT_ROOT1);
        context2.load().json(RESOURCE_CONTENT_TWO, TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);
        context2.addModelsForClasses(modelClass);
        final MockSlingHttpServletRequest request = context.request();
        resource = context.currentResource(RESOURCE_PATH);
        request.setPathInfo("/content/tetrapak/public-web/lang-masters/en/solutons/processing");
        model = request.adaptTo(modelClass);
    }

    /**
     * Test section home page title.
     */
    @Test
    public void testSectionHomePageTitle() {
        assertEquals("Processing overview", model.getSectionHomePageTitle());
    }

    /**
     * Test section home page path.
     */
    @Test
    public void testSectionHomePagePath() {
        assertEquals("/content/tetrapak/public-web/lang-masters/en/solutions/processing.html",
                model.getSectionHomePagePath());
    }

    /**
     * Test page hierarchy.
     */
    @Test
    public void testPageHierarchy() {
        final Map<String, String> hierarchyMap = model.getPageHierarchy();
        assertEquals("Solutions", hierarchyMap.get("l1"));
        assertEquals("Processing", hierarchyMap.get("l2"));
    }

    /**
     * Test section menu.
     */
    @Test
    public void testSectionMenu() {
        final List<SectionMenuBean> sectionMenu = model.getSectionMenu();
        assertEquals("Applications", sectionMenu.get(0).getLinkText());
        assertEquals("/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications.html",
                sectionMenu.get(0).getLinkPath());
        assertEquals("Main technology areas", sectionMenu.get(1).getLinkText());
        assertEquals("/content/tetrapak/public-web/lang-masters/en/solutions/processing/main-technology-areas.html",
                sectionMenu.get(1).getLinkPath());
        assertEquals("External page", sectionMenu.get(2).getLinkText());
        assertEquals("https://www.google.com", sectionMenu.get(2).getLinkPath());
    }

    /**
     * Test insight.
     */
    @Test
    public void testInsight() {
        final MockSlingHttpServletRequest request = context2.request();
        resource = context2.currentResource(RESOURCE_PATH_TWO);
        request.setPathInfo("/content/tetrapak/public-web/lang-masters/en/insight/case-article");
        model = request.adaptTo(modelClass);
        final List<SectionMenuBean> sectionMenu = model.getSectionMenu();
        assertEquals("Case Article", sectionMenu.get(0).getLinkText());
    }
}
