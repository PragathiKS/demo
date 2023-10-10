package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.AggregatorService;
import com.tetrapak.publicweb.core.services.impl.AggregatorServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class TeaserModelTest.
 */
public class TeaserModelTest {

    @Rule
    public AemContext context = new AemContext();
  

    /** The Constant RESOURCE_CONTENT_MANUAL. */
    private static final String RESOURCE_CONTENT_MANUAL = "/teaser/test-content-manual.json";

    /** The Constant RESOURCE_CONTENT_SEMI. */
    private static final String RESOURCE_CONTENT_SEMI = "/teaser/test-content-semi.json";

    /** The Constant RESOURCE_HOME. */
    private static final String RESOURCE_HOME = "/teaser/home.json";

    /** The Constant RESOURCE_LANG. */
    private static final String RESOURCE_LANG = "/teaser/en.json";

    /** The Constant RESOURCE_SOLUTIONS. */
    private static final String RESOURCE_SOLUTIONS = "/teaser/solutions.json";

    /** The Constant HOME_PAGE. */
    private static final String HOME_PAGE = "/content/tetrapak/publicweb/lang-masters/en/home";

    /** The Constant SOLUTIONS_PAGE. */
    private static final String SOLUTIONS_PAGE = "/content/tetrapak/publicweb/lang-masters/en/solutions";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-masters/en/test40";

    /** The Constant LANG_PAGE. */
    private static final String LANG_PAGE = "/content/tetrapak/publicweb/lang-masters/en";

    /** The Constant RESOURCE. */
    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/teaser";

    /** The Constant RESOURCE_TWO. */
    private static final String RESOURCE_TWO = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/teaser_1";

    /** The model. */
    private TeaserModel model;

    private AggregatorService aggregatorService;

    Class<TeaserModel> modelClass = TeaserModel.class;

    /** The resolver. */
    @Mock
    ResourceResolver resolver;

    /** The resource. */
    @Mock
    Resource resource;

    /** The session. */
    @Mock
    Session session;

    /** The query builder. */
    @Mock
    private QueryBuilder queryBuilder;

    /** The result. */
    @Mock
    SearchResult searchResult;

    /** The query. */
    @Mock
    private Query query;

    /** The pageManager. */
    @Mock
    private PageManager pageManager;

    /** The currentPage. */
    @Mock
    private Page currentPage;

    /** The hit. */
    @Mock
    private Hit hit;
    
    @Mock
    private ValueMap properties;

    /**
     * Sets the up.
     *
     * @param context
     *            the new up
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {

        aggregatorService = new AggregatorServiceImpl();
        context.registerService(AggregatorService.class, aggregatorService);

        context.load().json(RESOURCE_LANG, LANG_PAGE);
        context.load().json(RESOURCE_HOME, HOME_PAGE);
        context.load().json(RESOURCE_SOLUTIONS, SOLUTIONS_PAGE);

        context.addModelsForClasses(modelClass);

        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testMethodsManual() throws Exception {

        context.load().json(RESOURCE_CONTENT_MANUAL, TEST_CONTENT_ROOT);
        
        MockSlingHttpServletRequest request = context.request();
        context.request().setPathInfo(RESOURCE);
        request.setResource(context.resourceResolver().getResource(RESOURCE));
        resource = context.currentResource(RESOURCE);
        model = request.adaptTo(modelClass);

        assertEquals("This is heading of teaser", model.getHeading());
        assertEquals("This is description of Teaser", model.getDescription());
        assertEquals("4", model.getNumberOfColumns());        
        assertEquals("View All", model.getLinkLabel());
        assertEquals("/content/tetrapak/publicweb/lang-masters/en/search.html", model.getLinkPath());
        assertEquals("grayscale-white", model.getPwTheme());
        assertEquals("true", model.getEnableCarousel());
        assertEquals("anchorId", model.getAnchorId());
        assertEquals("Anchor title", model.getAnchorTitle());
        assertEquals(true, model.getDisplayDate());

        assertEquals("Title", model.getTeaserList().get(1).getTitle());
        assertEquals("Description", model.getTeaserList().get(1).getDescription());
        assertEquals("/content/dam/we-retail/en/experiences/arctic-surfing-in-lofoten/northern-lights.jpg",
                model.getTeaserList().get(1).getFileReference());
        assertEquals("Alt", model.getTeaserList().get(1).getAlt());
        assertEquals("Link Text", model.getTeaserList().get(1).getLinkText());
        assertEquals("/content/tetrapak/publicweb/lang-masters/en/solutions.html",
                model.getTeaserList().get(1).getLinkPath());
        assertEquals("link", model.getTeaserList().get(1).getPwButtonTheme());
        assertEquals("2023-03-31", model.getTeaserList().get(1).getArticleDate());
    }

    /**
     * Test asset name.
     *
     * @throws Exception the exception
     */
    @Test
    public void testAssetName() throws Exception {
        context.load().json(RESOURCE_CONTENT_MANUAL, TEST_CONTENT_ROOT);
        MockSlingHttpServletRequest request = context.request();
        context.request().setPathInfo(RESOURCE_TWO);
        request.setResource(context.resourceResolver().getResource(RESOURCE_TWO));
        resource = context.currentResource(RESOURCE_TWO);
        model = request.adaptTo(modelClass);
        assertEquals("abc.pdf", model.getTeaserList().get(0).getAssetName());
    }

    /**
     * Test model, resource and all getters of the TabsListModel model.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testMethodsSemiAuto() throws Exception {
        context.load().json(RESOURCE_CONTENT_SEMI, TEST_CONTENT_ROOT);
        MockSlingHttpServletRequest request = context.request();
        context.request().setPathInfo(RESOURCE);
        request.setResource(context.resourceResolver().getResource(RESOURCE));
        resource = context.currentResource(RESOURCE);
        model = request.adaptTo(modelClass);

        assertEquals("This is heading of teaser", model.getHeading());
        assertEquals("This is description of Teaser", model.getDescription());
        assertEquals(true, model.getDisplayDate());
        assertEquals("4", model.getNumberOfColumns());   

        assertEquals("Solutions", model.getTeaserList().get(0).getTitle());
        assertEquals("Solution Desc", model.getTeaserList().get(0).getDescription());
        assertEquals("/content/dam/tetrapak/publicweb/logo_tetra_pak_white.svg",
                model.getTeaserList().get(0).getFileReference());
        assertEquals("alt", model.getTeaserList().get(0).getAlt());
        assertEquals("link text1", model.getTeaserList().get(0).getLinkText());
        assertEquals("/content/tetrapak/publicweb/lang-masters/en/home.html",
                model.getTeaserList().get(0).getLinkPath());
        assertEquals("link", model.getTeaserList().get(1).getPwButtonTheme());
        assertEquals("2023-03-30", model.getTeaserList().get(0).getArticleDate());
    }

    /**
     * Test model, resource and all getters of the TabsListModel model.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testMethodsAuto() throws Exception {
        String[] tags = { "properties:orientation/portrait", "properties:orientation/landscape" };
        Mockito.when(resource.getResourceResolver()).thenReturn(resolver);
        Mockito.when(resolver.adaptTo(Session.class)).thenReturn(session);
        Mockito.when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);
        Mockito.when(resolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
        Mockito.when(pageManager.getContainingPage(resource)).thenReturn(currentPage);
        Mockito.when(currentPage.getAbsoluteParent(PWConstants.LANGUAGE_PAGE_LEVEL)).thenReturn(currentPage);
        Mockito.when(currentPage.getPath()).thenReturn(LANG_PAGE);
        Mockito.when(queryBuilder.createQuery(Mockito.any(PredicateGroup.class), Mockito.any(Session.class)))
                .thenReturn(query);
        Mockito.when(query.getResult()).thenReturn(searchResult);
        List<Hit> hits = new ArrayList<Hit>();
        hits.add(hit);
        Mockito.when(searchResult.getHits()).thenReturn(hits);
        Mockito.when(hit.getPath()).thenReturn(SOLUTIONS_PAGE);
        Mockito.when(properties.get("articleDate",String.class)).thenReturn("2020-12-12");
        Mockito.when(hit.getProperties()).thenReturn(properties);
        Mockito.when(pageManager.getPage(SOLUTIONS_PAGE)).thenReturn(context.pageManager().getPage(SOLUTIONS_PAGE));
        List<AggregatorModel> aggregatorList = aggregatorService.getAggregatorList(resource, tags, 4, "and");
        assertEquals("Solutions", aggregatorList.get(0).getTitle());
        assertEquals("Solution Desc", aggregatorList.get(0).getDescription());
        assertEquals("/content/dam/tetrapak/publicweb/logo_tetra_pak_white.svg", aggregatorList.get(0).getImagePath());
        assertEquals("alt", aggregatorList.get(0).getAltText());
        assertEquals("link text1", aggregatorList.get(0).getLinkText());
        assertEquals("/content/tetrapak/publicweb/lang-masters/en/home", aggregatorList.get(0).getLinkPath());
        assertEquals("link", aggregatorList.get(0).getPwButtonTheme());
        assertEquals("2023-03-30", aggregatorList.get(0).getArticleDate());
    }

}
