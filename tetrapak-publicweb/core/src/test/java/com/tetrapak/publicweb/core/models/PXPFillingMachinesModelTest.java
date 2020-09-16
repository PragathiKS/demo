package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.mock.MockHelper;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class PXPFillingMachinesModelTest.
 */
public class PXPFillingMachinesModelTest {

    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /** The Constant PRODUCTS_DATA. */
    private static final String PRODUCTS_DATA = "/product/test-Products.json";

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/pxpfillingmachines/test-content.json";

    /** The Constant RESOURCE_CONTENT_TWO. */
    private static final String RESOURCE_CONTENT_TWO = "/pxpfillingmachines/test-content-two.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-masters/en";

    /** The Constant TEST_CONTENT_ROOT_TWO. */
    private static final String TEST_CONTENT_ROOT_TWO = "/content/tetrapak/publicweb/lang-masters/en/test-equip-19647";

    /** The Constant RESOURCE. */
    private static final String RESOURCE_PATH = TEST_CONTENT_ROOT + "/jcr:content/pxpfillingmachines";

    /** The resolver. */
    @Mock
    private ResourceResolver resourceResolver;

    /** The resource. */
    @Mock
    private Resource resource;

    /** The language resource. */
    @Mock
    private Resource languageResource;

    /** The session. */
    @Mock
    private Session session;

    /** The query builder. */
    @Mock
    private QueryBuilder queryBuilder;

    /** The result. */
    @Mock
    private SearchResult searchResult;

    /** The query. */
    @Mock
    private Query query;

    /** The currentPage. */
    @Mock
    private Page currentPage;

    /** The hit. */
    @Mock
    private Hit hit;

    /** The pxp filling machines model. */
    @InjectMocks
    private PXPFillingMachinesModel pxpFillingMachinesModel;

    /**
     * The setup method.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        context.load().json(PRODUCTS_DATA, PWConstants.PXP_ROOT_PATH);
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.load().json(RESOURCE_CONTENT_TWO, TEST_CONTENT_ROOT_TWO);
        final Class<PXPFillingMachinesModel> modelClass = PXPFillingMachinesModel.class;
        context.addModelsForClasses(modelClass);

        final List<String> pathList = new ArrayList<>();
        pathList.add("/content/tetrapak/publicweb/lang-masters/en/test-equip-19647");
        MockHelper.loadQuery(context, pathList);
        
        MockSlingHttpServletRequest request = context.request();
        context.request().setPathInfo(RESOURCE_PATH);
        request.setResource(context.resourceResolver().getResource(RESOURCE_PATH));
        resource = context.currentResource(RESOURCE_PATH);
        pxpFillingMachinesModel = request.adaptTo(modelClass);
        
        initMocks(this);
    }

    /**
     * Test getters.
     */
    @Test
    public void testGetters() {
        assertEquals("Some Heading", pxpFillingMachinesModel.getHeading());
        assertEquals("grayscale-white", pxpFillingMachinesModel.getPwTheme());
        assertEquals("anchorId", pxpFillingMachinesModel.getAnchorId());
        assertEquals("anchor title", pxpFillingMachinesModel.getAnchorTitle());
    }

    /**
     * Test teaser list.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testTeaserList() throws Exception {
        assertEquals("/content/tetrapak/publicweb/lang-masters/en/test-equip-19647.html",
                pxpFillingMachinesModel.getTeaserList().get(0).getLinkPath());
        assertEquals("The filling machine for water", pxpFillingMachinesModel.getTeaserList().get(0).getDescription());
        assertEquals("/content/dam/tetrapak/publicweb/pxp/fillingmachines/equipment19647/image/tt3_xh_ic_benefits.png",
                pxpFillingMachinesModel.getTeaserList().get(0).getFileReference());
    }
}
