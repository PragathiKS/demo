package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Assert;
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
import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.services.impl.AggregatorServiceImpl;
import com.tetrapak.publicweb.core.services.impl.DynamicMediaServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class StoriesModelTest.
 */
public class StoriesModelTest {

    @Rule
    public AemContext context = new AemContext();
  

    /** The Constant RESOURCE_CONTENT_MANUAL. */
    private static final String RESOURCE_CONTENT_MANUAL = "/stories/test-content-manual.json";

    /** The Constant RESOURCE_CONTENT_SEMI. */
    private static final String RESOURCE_CONTENT_SEMI = "/stories/test-content-semi.json";

    /** The Constant RESOURCE_HOME. */
    private static final String RESOURCE_HOME = "/stories/home.json";

    /** The Constant RESOURCE_LANG. */
    private static final String RESOURCE_LANG = "/stories/en.json";

    /** The Constant RESOURCE_SOLUTIONS. */
    private static final String RESOURCE_SOLUTIONS = "/stories/solutions.json";

    /** The Constant HOME_PAGE. */
    private static final String HOME_PAGE = "/content/tetrapak/publicweb/lang-masters/en/home";

    /** The Constant SOLUTIONS_PAGE. */
    private static final String SOLUTIONS_PAGE = "/content/tetrapak/publicweb/lang-masters/en/solutions";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-masters/en/test40";

    /** The Constant LANG_PAGE. */
    private static final String LANG_PAGE = "/content/tetrapak/publicweb/lang-masters/en";

    /** The Constant RESOURCE. */
    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/stories";

    /** The Constant RESOURCE_TWO. */
    private static final String RESOURCE_TWO = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/stories_1";
    
    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/carousel/test-content.json";
    
    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT_TWO = "/content/publicweb/en";
    
    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_IMAGE = "/assets/logo_tetra_pak_white.json";

    /** The Constant DAM_IMAGE. */
    private static final String DAM_IMAGE = "/content/dam/tetrapak/p2.png";

    /** The Constant RESOURCE. */
    private static final String RESOURCE_THREE = TEST_CONTENT_ROOT_TWO + "/jcr:content/carousel";
    
    /** THE DYNAMIC MEDIA CONF. */
    String[] dynamicMediaConfMap = {
            "getstarted-desktop=1440\\,300,getstarted-mobileL=414\\,259\\,0.333\\,0\\,0.333\\,1,getstarted-mobileP=414\\,259\\,0.333\\,0\\,0.333\\,1" };

    /** The model. */
    private StoriesModel model;

    private AggregatorService aggregatorService;

    Class<StoriesModel> modelClass = StoriesModel.class;

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
    
    /** The model. */
    private DynamicImageModel dynamicImageModel;

    private DynamicMediaService dynamicMediaService;

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
        
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT_TWO);
        context.load().json(RESOURCE_IMAGE, DAM_IMAGE);
        dynamicMediaService = new DynamicMediaServiceImpl();
        final Map<String, Object> configuraionServiceConfig = new HashMap<String, Object>();
        configuraionServiceConfig.put("rootPath", "/tetrapak");
        configuraionServiceConfig.put("dynamicMediaConfMap", dynamicMediaConfMap);
        configuraionServiceConfig.put("imageServiceUrl", "https://s7g10.scene7.com/is/image");
        configuraionServiceConfig.put("videoServiceUrl", "https://s7g10.scene7.com/is/content");
        context.registerInjectActivateService(dynamicMediaService, configuraionServiceConfig);
        context.registerService(DynamicMediaService.class, dynamicMediaService);
        context.request().setPathInfo(RESOURCE_THREE);
        context.request().setAttribute("imagePath", "/content/dam/tetrapak/p2.png");
        context.request().setAttribute("altText", "Image1 alt");
        context.request().setAttribute("imageCrop", "390,57,947,473");
        context.request().setAttribute("dwidth", "500");
        context.request().setAttribute("dheight", "600");
        context.request().setAttribute("mwidthl", "100");
        context.request().setAttribute("mheightl", "150");
        context.request().setAttribute("mwidthp", "250");
        context.request().setAttribute("mheightp", "100");
        context.request().setResource(context.resourceResolver().getResource(RESOURCE_THREE));
        dynamicImageModel = context.request().adaptTo(DynamicImageModel.class);

    }

    @Test
    public void testMethodsManual() throws Exception {

        context.load().json(RESOURCE_CONTENT_MANUAL, TEST_CONTENT_ROOT);       
        MockSlingHttpServletRequest request = context.request();
        context.request().setPathInfo(RESOURCE);
        request.setResource(context.resourceResolver().getResource(RESOURCE));
        resource = context.currentResource(RESOURCE);
        model = request.adaptTo(modelClass);
        
        assertEquals("Load More", model.getLinkLabel());
        assertEquals(Boolean.TRUE, model.isEnableBlackGradient());
        assertEquals("grayscale-white", model.getPwTheme());
        assertEquals("anchorId", model.getAnchorId());
        assertEquals("Anchor title", model.getAnchorTitle());
        assertEquals("Title", model.getStoryList().get(1).getHeading());
        assertEquals("/content/dam/we-retail/en/experiences/arctic-surfing-in-lofoten/northern-lights.jpg",
                model.getStoryList().get(1).getFileReference());
        assertEquals("Alt", model.getStoryList().get(1).getAlt());
        assertEquals("/content/tetrapak/publicweb/lang-masters/en/solutions.html",
                model.getStoryList().get(1).getLinkPath());
        
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
        
        assertEquals("Solutions", model.getStoryList().get(0).getHeading());
        assertEquals("/content/dam/tetrapak/publicweb/logo_tetra_pak_white.svg",
                model.getStoryList().get(0).getFileReference());
        assertEquals("alt", model.getStoryList().get(0).getAlt());
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
        assertEquals("/content/dam/tetrapak/publicweb/logo_tetra_pak_white.svg", aggregatorList.get(0).getImagePath());
        assertEquals("alt", aggregatorList.get(0).getAltText());
        assertEquals("/content/tetrapak/publicweb/lang-masters/en/home", aggregatorList.get(0).getLinkPath());
    }
    
    @Test
    public void testGetStartedMessage() {
        
        String imageServiceUrl = dynamicImageModel.getImageServiceURL();
        String videoServiceUrl = dynamicImageModel.getVideoServiceUrl();
        String dynamicImage = dynamicImageModel.getAltText();
        String finalPath = dynamicImageModel.getFinalPath();
        Assert.assertEquals("alt text", "Image1 alt", dynamicImage);
        assertNotNull("image service URL is not null", imageServiceUrl);
        Assert.assertEquals("final path", "/tetrapak/p2", finalPath);
        Assert.assertEquals("image service URL", imageServiceUrl, "https://s7g10.scene7.com/is/image");
        Assert.assertEquals("image service URL", videoServiceUrl, "https://s7g10.scene7.com/is/content");
        String[] dynamicMediaConfMap = dynamicImageModel.getDynamicMediaConfiguration();
        assertTrue("configuration map should not be empty", dynamicMediaConfMap.toString().length() > 0);
        Assert.assertEquals("default image URL", "/content/dam/customerhub/cow-blue-background.png",
                dynamicImageModel.getDefaultImageUrl());
        Assert.assertEquals("default image URL", "https://s7g10.scene7.com/is/image/tetrapak/p2?scl=1",
                dynamicImageModel.getDesktopLargeUrl());
        Assert.assertEquals("default image URL",
                "https://s7g10.scene7.com/is/image/tetrapak/p2?wid=500&hei=600&fmt=jpg&resMode=sharp2&qlt=85,0&op_usm=1.75,0.3,2,0",
                dynamicImageModel.getDesktopUrl());
        Assert.assertEquals("default image URL",
                "https://s7g10.scene7.com/is/image/tetrapak/p2?wid=100&hei=150&cropn=2.4375,2.28,3.48125,16.64&fmt=jpg&resMode=sharp2&qlt=85,0&op_usm=1.75,0.3,2,0",
                dynamicImageModel.getMobileLandscapeUrl());
        Assert.assertEquals("default image URL",
                "https://s7g10.scene7.com/is/image/tetrapak/p2?wid=250&hei=100&cropn=2.4375,2.28,3.48125,16.64&fmt=jpg&resMode=sharp2&qlt=85,0&op_usm=1.75,0.3,2,0",
                dynamicImageModel.getMobilePortraitUrl());
        Assert.assertEquals("default image URL", "/content/dam/tetrapak/p2.png", dynamicImageModel.getImagePath());

    }

}
