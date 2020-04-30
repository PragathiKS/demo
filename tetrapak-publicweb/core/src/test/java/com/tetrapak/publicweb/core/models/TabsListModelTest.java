package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jcr.Session;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
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

public class TabsListModelTest {

    @Rule
    public AemContext context = new AemContext();

    /** The Constant RESOURCE_CONTENT_MANUAL. */
    private static final String RESOURCE_CONTENT_MANUAL = "/tablist/test-content-manual.json";

    /** The Constant RESOURCE_CONTENT_SEMI. */
    private static final String RESOURCE_CONTENT_SEMI = "/tablist/test-content-semi.json";
    
    /** The Constant RESOURCE_HOME. */
    private static final String RESOURCE_HOME = "/tablist/home.json";
    
    /** The Constant RESOURCE_LANG. */
    private static final String RESOURCE_LANG = "/tablist/en.json";
    
    /** The Constant RESOURCE_SOLUTIONS. */
    private static final String RESOURCE_SOLUTIONS = "/tablist/solutions.json";

    /** The Constant HOME_PAGE. */
    private static final String HOME_PAGE = "/content/tetrapak/publicweb/lang-masters/en/home";
    
    /** The Constant SOLUTIONS_PAGE. */
    private static final String SOLUTIONS_PAGE = "/content/tetrapak/publicweb/lang-masters/en/solutions";
    
    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-masters/en/test40";
    
    /** The Constant LANG_PAGE. */
    private static final String LANG_PAGE = "/content/tetrapak/publicweb/lang-masters/en";

    /** The Constant RESOURCE. */
    private static final String RESOURCE = TEST_CONTENT_ROOT+ "/jcr:content/root/responsivegrid/tabslist";

    /** The model. */
    private TabsListModel model;

    private AggregatorService aggregatorService;

    private DynamicMediaService dynamicMediaService;
    
    Class<TabsListModel> modelClass = TabsListModel.class;
    
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
    

    /** THE DYNAMIC MEDIA CONF. */
    String[] dynamicMediaConfMap = {
	    "getstarted-desktop=1440\\,300,getstarted-mobileL=414\\,259\\,0.333\\,0\\,0.333\\,1,getstarted-mobileP=414\\,259\\,0.333\\,0\\,0.333\\,1" };

    /**
     * Sets the up.
     *
     * @param context the new up
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {	

	// Dynamic Media Serivce
	dynamicMediaService = new DynamicMediaServiceImpl();
	final Map<String, Object> configuraionServiceConfig = new HashMap<String, Object>();
	configuraionServiceConfig.put("rootPath", "/tetrapak");
	configuraionServiceConfig.put("dynamicMediaConfMap", dynamicMediaConfMap);
	configuraionServiceConfig.put("imageServiceUrl", "https://s7g10.scene7.com/is/image");
	configuraionServiceConfig.put("videoServiceUrl", "https://s7g10.scene7.com/is/content");
	context.registerInjectActivateService(dynamicMediaService, configuraionServiceConfig);

	// Set run modes
	context.runMode("publish");

	aggregatorService = new AggregatorServiceImpl();
	context.registerService(AggregatorService.class, aggregatorService);
	
	context.load().json(RESOURCE_LANG, LANG_PAGE);
	context.load().json(RESOURCE_HOME, HOME_PAGE);
	context.load().json(RESOURCE_SOLUTIONS, SOLUTIONS_PAGE);
	
	context.addModelsForClasses(modelClass);
	
	MockitoAnnotations.initMocks(this);
	
    }

    /**
     * Test model, resource and all getters of the TabsListModel model.
     *
     * @throws Exception the exception
     */
    @Test
    public void testMethodsManual() throws Exception {

	context.load().json(RESOURCE_CONTENT_MANUAL, TEST_CONTENT_ROOT);
	resource = context.currentResource(RESOURCE);
	model = resource.adaptTo(modelClass);
	
	assertEquals("Some Heading", model.getHeading());
	assertEquals("Read for here", model.getReadMoreText());
	assertEquals("/content/community/badging.html", model.getReadMorePath());
	assertEquals("_blank", model.getReadMoreTarget());
	assertEquals("grayscale-white", model.getPwTheme());
	assertEquals("display-row", model.getPwDisplay());
	assertEquals("anchorId", model.getAnchorId());
	assertEquals("anchor title", model.getAnchorTitle());
	assertEquals("hyperlink", model.getPwLinkTheme());
	assertEquals("badging", model.getAssetName());
	
	assertEquals("Title", model.getTabs().get(1).getTitle());
	assertEquals("Sub Title", model.getTabs().get(1).getSubTitle());
	assertEquals("Description Text", model.getTabs().get(1).getDescription());
	assertEquals("_blank", model.getTabs().get(1).getTargetBlank());
	assertEquals("/content/dam/we-retail/en/experiences/arctic-surfing-in-lofoten/northern-lights.jpg",
		model.getTabs().get(1).getFileReference());
	assertEquals("Alt", model.getTabs().get(1).getAlt());
	assertEquals("hyperlink", model.getTabs().get(1).getPwLinkTheme());
	assertEquals("Click Here", model.getTabs().get(1).getLinkText());
	assertEquals("/content/commons-ux/helloearth.html", model.getTabs().get(1).getLinkURL());
	assertEquals("secondary", model.getTabs().get(1).getPwButtonTheme());

	assertEquals("damVideo", model.getTabs().get(2).getVideoSource());
	assertEquals("https://s7g10.scene7.com/is/content/tetrapak/file_example_MOV_1280_1_4MB",
		model.getTabs().get(2).getDamVideoPath());
	assertEquals("/content/dam/tetrapak/publicweb/ContentImage6.png", model.getTabs().get(2).getThumbnailPath());
	assertEquals("https://www.youtube.com/embed/UEfCxCLtOwk", model.getTabs().get(3).getYoutubeEmbedURL());

	assertEquals("file_example_MOV_1280_1_4MB", model.getTabs().get(2).getVideoName());
	assertEquals("badging", model.getTabs().get(3).getAssetName());

    }
    
    /**
     * Test model, resource and all getters of the TabsListModel model.
     *
     * @throws Exception the exception
     */
    @Test
    public void testMethodsAuto() throws Exception {
	String[] tags = {"properties:orientation/portrait","properties:orientation/landscape"};
	Mockito.when(resource.getResourceResolver()).thenReturn(resolver);
	Mockito.when(resolver.adaptTo(Session.class)).thenReturn(session);
	Mockito.when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);
	Mockito.when(resolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
	Mockito.when(pageManager.getContainingPage(resource)).thenReturn(currentPage);
	Mockito.when(currentPage.getAbsoluteParent(PWConstants.LANGUAGE_PAGE_LEVEL)).thenReturn(currentPage);
	Mockito.when(currentPage.getPath()).thenReturn(LANG_PAGE);
	Mockito.when(queryBuilder.createQuery(Mockito.any(PredicateGroup.class), Mockito.any(Session.class))).thenReturn(query);
	Mockito.when(query.getResult()).thenReturn(searchResult);
	List<Hit> hits = new ArrayList<Hit>();
	hits.add(hit);
	Mockito.when(searchResult.getHits()).thenReturn(hits);
	Mockito.when(hit.getPath()).thenReturn(SOLUTIONS_PAGE);
	Mockito.when(pageManager.getPage(SOLUTIONS_PAGE)).thenReturn(context.pageManager().getPage(SOLUTIONS_PAGE));
	List<AggregatorModel> aggregatorList = aggregatorService.getAggregatorList(resource, tags, 4,"and");
	assertEquals("Solutions", aggregatorList.get(0).getTitle());
	assertEquals("Solution Desc", aggregatorList.get(0).getDescription());
	assertEquals("_blank", aggregatorList.get(0).getLinkTarget());
	assertEquals("/content/dam/tetrapak/publicweb/logo_tetra_pak_white.svg",
		aggregatorList.get(0).getImagePath());
	assertEquals("alt", aggregatorList.get(0).getAltText());
	assertEquals("download", aggregatorList.get(0).getPwLinkTheme());
	assertEquals("link text1", aggregatorList.get(0).getLinkText());
	assertEquals("/content/tetrapak/public-web/lang-masters/en/home", aggregatorList.get(0).getLinkPath());
	assertEquals("link", aggregatorList.get(0).getPwButtonTheme());
    }
    
    /**
     * Test model, resource and all getters of the TabsListModel model.
     *
     * @throws Exception the exception
     */
    @Test
    public void testMethodsSemiAuto() throws Exception {
	context.load().json(RESOURCE_CONTENT_SEMI, TEST_CONTENT_ROOT);
	resource = context.currentResource(RESOURCE);
	model = resource.adaptTo(modelClass);
	assertEquals("Solutions", model.getTabs().get(0).getTitle());
	assertEquals("Solution Desc", model.getTabs().get(0).getDescription());
	assertEquals("_blank", model.getTabs().get(0).getTargetBlank());
	assertEquals("/content/dam/tetrapak/publicweb/logo_tetra_pak_white.svg",
		model.getTabs().get(0).getFileReference());
	assertEquals("alt", model.getTabs().get(0).getAlt());
	assertEquals("download", model.getTabs().get(0).getPwLinkTheme());
	assertEquals("link text1", model.getTabs().get(0).getLinkText());
	assertEquals("/content/tetrapak/public-web/lang-masters/en/home.html", model.getTabs().get(0).getLinkURL());
	assertEquals("link", model.getTabs().get(1).getPwButtonTheme());
    }
}
