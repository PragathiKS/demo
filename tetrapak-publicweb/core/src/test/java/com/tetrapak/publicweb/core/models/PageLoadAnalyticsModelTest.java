package com.tetrapak.publicweb.core.models;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.spy;

/**
 * The Class PageLoadAnalyticsModelTest.
 */
public class PageLoadAnalyticsModelTest {

    /**
     * The context.
     */
    @Rule
    public AemContext context = new AemContext();

    /**
     * The Constant RESOURCE_CONTENT.
     */
    private static final String RESOURCE_CONTENT = "/pageContent/test-content.json";

    /**
     * The Constant TEST_CONTENT_ROOT.
     */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-masters/en/solutions/turn-ingredients-into-products/product1";

    /**
     * The Constant RESOURCE.
     */
    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content";

    /**
     * The model.
     */
    private PageLoadAnalyticsModel model;

    /**
     * The resource.
     */
    private Resource resource;

    /** The tags. */
    private String tags;

    /** The Mock Tag. */
    @Mock
    private Tag mockTag;

    /** The Tag Manager. */
    @Mock
    private  TagManager tagManager;

    /** The page manager. */
    @Mock
    private PageManager pageManager;

    /** The resolver. */
    private ResourceResolver resolver;

    /** The Page Name. */
    private static final String PAGE_NAME = "en:title";

    /** The Channel. */
    private static final String CHANNEL = "title";

    /** The Current Page. */
    @Mock
    private Page currentPage;

    /** The Properties. */
    @Mock
    private ValueMap properties;

    /** The Product Model. */
    @Mock
    private ProductModel product;

    /** The TAG VALUES. */
    private static final String[] TAGS_VALUE = new String[] { "val1", "val2" };

    /** The Tag1. */
    @Mock
    private Tag tag1;

    /** The Tag2. */
    @Mock
    private Tag tag2;

    /** The Inject Model Class. */
    @InjectMocks
    private PageLoadAnalyticsModel objectUnderTest = spy(new PageLoadAnalyticsModel());

    /** The CURRENT PAGE URL. */
    private static final String CURRENT_PAGE_URL= "/content/tetrapak/publicweb/gb";

    /** The CANONICAL URL. */
    private static final String CANONICAL_URL = "https://www-qa.tetrapak.com";

    /** The IS PRODUCTION. */
    private static final boolean IS_PRODUCTION = true;

    /** The IS DEVELOPMENT. */
    private static final boolean IS_DEVELOPMENT = true;

    /** The IS STAGING. */
    private static final boolean IS_STAGING = true;

    /** The IS PUBLISHER. */
    private static final boolean IS_PUBLISHER = true;

    /** The DATA DOMAIN SCRIPT. */
    private static final String DATA_DOMAIN_SCRIPT = "268df474-520d-4ad7-8453-cd5ddcd602b9";

    /**
     * Sets the up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        Class<PageLoadAnalyticsModel> modelClass = PageLoadAnalyticsModel.class;
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.load().json("/pageContent/tag_content.json", "/content/cq:tags/tetrapak");
        context.addModelsForClasses(modelClass);
        MockSlingHttpServletRequest request = context.request();
        context.request().setPathInfo(RESOURCE);
        request.setResource(context.resourceResolver().getResource(RESOURCE));
        resource = context.currentResource(RESOURCE);
        resolver = resource.getResourceResolver();
        pageManager = resolver.adaptTo(PageManager.class);
        tagManager = resolver.adaptTo(TagManager.class);
        mockTag = resolver.adaptTo(Tag.class);
        mockTag = tagManager.resolve(tags);
        product = resource.adaptTo(ProductModel.class);
        MockitoAnnotations.initMocks(this);
        Mockito.when(pageManager.getContainingPage(resource)).thenReturn(currentPage);
        Mockito.when(tagManager.resolve(tags)).thenReturn(mockTag);
        Mockito.when(properties.get("cq:template", String.class)).thenReturn("/conf/publicweb/settings/wcm/templates/public-web-landing-page");
        Mockito.when(properties.get("cq:tags", String.class)).thenReturn("we-retail:activity");
        Mockito.when(properties.get("title")).thenReturn(PAGE_NAME);
        Mockito.when(properties.get("title")).thenReturn(CHANNEL);
        Mockito.when(tagManager.resolve(TAGS_VALUE[0])).thenReturn(tag1);
        Mockito.when(tagManager.resolve(TAGS_VALUE[1])).thenReturn(tag2);
        Mockito.when(tag1.getTitle()).thenReturn(TAGS_VALUE[0]);
        Mockito.when(tag2.getTitle()).thenReturn(TAGS_VALUE[1]);
        model = request.adaptTo(modelClass);
    }

    /**
     * Test model, resource and all getters of the PageLoadAnalytics model.
     *
     * @throws Exception the exception
     */
    @Test
   public void simpleLoadAndGettersTest() throws Exception {
        Mockito.when(objectUnderTest.getCurrentPageURL()).thenReturn(CURRENT_PAGE_URL);
        Mockito.when(objectUnderTest.isDevelopment()).thenReturn(IS_DEVELOPMENT);
        Mockito.when(objectUnderTest.isStaging()).thenReturn(IS_STAGING);
        Mockito.when(objectUnderTest.isProduction()).thenReturn(IS_PRODUCTION);
        Mockito.when(objectUnderTest.isPublisher()).thenReturn(IS_PUBLISHER);
        Mockito.when(objectUnderTest.getDataDomainScript()).thenReturn(DATA_DOMAIN_SCRIPT);
    }
}