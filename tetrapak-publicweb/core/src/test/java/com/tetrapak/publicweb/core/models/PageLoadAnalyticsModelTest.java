package com.tetrapak.publicweb.core.models;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.day.cq.tagging.TagManager.FindResults;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

/**
 * The Class PageLoadAnalyticsModelTest.
 */
public class PageLoadAnalyticsModelTest {

    private static final int COUNTRY_LEVEL = 4;
    /**
     * The context.
     */
    @Rule
    public AemContext context = new AemContext();

    /**
     * The Constant RESOURCE_CONTENT.
     */
    private static final String RESOURCE_CONTENT = "/pageContent/test-content.json";

    private static final String TEST_J = "/pageContent/tag-content.json";

    private static final String TEST_J_Path = "/content/cq:tags/tetrapak/end-to-end-solutions";

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

    private ResourceResolver resourceResolver;

    private String templatePath;

    private String pageType;

    private String tags;

    private FindResults findResults;

    @Mock
    private Tag mockTag;

    @Mock
    private  TagManager tagManager;

    /** The page manager. */
    @Mock
    private PageManager pageManager;

    /** The resolver. */
    private ResourceResolver resolver;

    private Page countryPage;

    private static final String PAGE_NAME = "en:title";

    private static final String CHANNEL = "title";

    @Mock
    private Page currentpage;

    @Mock
    private ValueMap properties;

    @Mock
    private ProductModel product;

    private static final String TAG_NAME = "End-to-End-Solution";

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
        model = request.adaptTo(modelClass);
        resolver = resource.getResourceResolver();
        pageManager = resolver.adaptTo(PageManager.class);
        tagManager = resolver.adaptTo(TagManager.class);
        mockTag = resolver.adaptTo(Tag.class);
        mockTag = tagManager.resolve(tags);
        product = resource.adaptTo(ProductModel.class);
        MockitoAnnotations.initMocks(this);
        Mockito.when(pageManager.getContainingPage(resource)).thenReturn(currentpage);
        Mockito.when(tagManager.resolve(tags)).thenReturn(mockTag);
        Mockito.when(properties.get("cq:template", String.class)).thenReturn("/conf/publicweb/settings/wcm/templates/public-web-landing-page");
        Mockito.when(properties.get("cq:tags", String.class)).thenReturn("we-retail:activity");
        Mockito.when(properties.get("title")).thenReturn(PAGE_NAME);
        Mockito.when(properties.get("title")).thenReturn(CHANNEL);
    }


    /**
     * Test model, resource and all getters of the PageLoadAnalytics model.
     *
     * @throws Exception the exception
     */

    @Test
    public void simpleLoadAndGettersTest() throws Exception {
        String[] methods = new String[]{"isProduction", "isStaging", "isDevelopment", "getDigitalData","getCurrentPageURL" , "getHreflangValues", "getCanonicalURL"};
        Util.testLoadAndGetters(methods, model, resource);
    }
}
