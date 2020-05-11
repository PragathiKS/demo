package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
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
public class PXPPackageTypesModelTest {

    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /** The Constant PRODUCTS_DATA. */
    private static final String PRODUCTS_DATA = "/product/test-Products.json";

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/pxppackagetypes/test-content.json";

    /** The Constant RESOURCE_CONTENT_TWO. */
    private static final String RESOURCE_CONTENT_PACKAGE_TYPES = "/pxppackagetypes/test-content-two.json";

    private static final String TEST_CONTENT_FILLING_MACHINE = "/content/tetrapak/public-web/lang-masters/en";

    private static final String TEST_CONTENT_PACKAGE_TYPE_ROOT = "/content/tetrapak/public-web/lang-masters/en/packagetypes";

    /** The Constant RESOURCE. */
    private static final String RESOURCE_PATH = TEST_CONTENT_FILLING_MACHINE + "/jcr:content/pxppackagetypes";

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
    private PXPPackageTypesModel pxpPackageTypesModel;

    /**
     * The setup method.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        context.load().json(PRODUCTS_DATA, PWConstants.PXP_ROOT_PATH);
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_FILLING_MACHINE);
        context.load().json(RESOURCE_CONTENT_PACKAGE_TYPES, TEST_CONTENT_PACKAGE_TYPE_ROOT);
        final Class<PXPPackageTypesModel> modelClass = PXPPackageTypesModel.class;
        context.addModelsForClasses(modelClass);

        final List<String> idList = new ArrayList<>();
        idList.add("/content/tetrapak/public-web/lang-masters/en/packagetypes/tetra-brik");
        idList.add("/content/tetrapak/public-web/lang-masters/en/packagetypes/tetra-fino");
        idList.add("/content/tetrapak/public-web/lang-masters/en/packagetypes/tetra-brik-aseptic");
        idList.add("/content/tetrapak/public-web/lang-masters/en/packagetypes/tetra-classic");
        MockHelper.loadQuery(context, idList);

        resource = context.currentResource(RESOURCE_PATH);
        pxpPackageTypesModel = resource.adaptTo(modelClass);
        initMocks(this);
    }

    /**
     * Test getters.
     */
    @Test
    public void testGetters() {
        assertEquals("Package Types", pxpPackageTypesModel.getHeading());
        assertEquals("grayscale-white", pxpPackageTypesModel.getPwTheme());
        assertEquals("anchor123", pxpPackageTypesModel.getAnchorId());
        assertEquals("Anchor title", pxpPackageTypesModel.getAnchorTitle());
    }

    /**
     * Test teaser list.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testTeaserList() throws Exception {
        assertEquals("/content/tetrapak/public-web/lang-masters/en/packagetypes/tetra-brik.html",
                pxpPackageTypesModel.getTeaserList().get(0).getLinkPath());
        assertEquals("500 ml, 1000 ml", pxpPackageTypesModel.getTeaserList().get(0).getDescription());
        assertEquals(
                "/content/dam/tetrapak/publicweb/pxp/fillingmachines/equipment1272/image/A3-Flex-no-DIMC-Benefits.png",
                pxpPackageTypesModel.getTeaserList().get(0).getFileReference());
    }
}
