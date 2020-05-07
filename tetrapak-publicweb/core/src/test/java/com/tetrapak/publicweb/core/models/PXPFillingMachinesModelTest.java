package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.constants.PWConstants;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Session;

import io.wcm.testing.mock.aem.junit.AemContext;

public class PXPFillingMachinesModelTest {

    @Rule
    public AemContext context = new AemContext();

    /** The Constant PRODUCTS_DATA. */
    private static final String PRODUCTS_DATA = "/product/test-Products.json";

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/pxpfillingmachines/test-content.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-masters/en";

    /** The Constant RESOURCE. */
    private static final String RESOURCE_PATH = TEST_CONTENT_ROOT + "/jcr:content/pxpfillingmachines";

    /** The Constant SOLUTIONS_PAGE. */
    private static final String SOLUTIONS_PAGE = "/content/tetrapak/publicweb/lang-masters/en/solutions";

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

    @InjectMocks
    private PXPFillingMachinesModel pxpFillingMachinesModel;

    @Before
    public void setUp() throws Exception {
        context.load().json(PRODUCTS_DATA, PWConstants.PXP_ROOT_PATH);
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        initMocks(this);
    }

    @Test
    public void testGetters() {
        final Class<PXPFillingMachinesModel> modelClass = PXPFillingMachinesModel.class;
        context.addModelsForClasses(modelClass);
        resource = context.currentResource(RESOURCE_PATH);
        pxpFillingMachinesModel = resource.adaptTo(modelClass);

        assertEquals("Some Heading", pxpFillingMachinesModel.getHeading());
        assertEquals("grayscale-white", pxpFillingMachinesModel.getPwTheme());
        assertEquals("anchorId", pxpFillingMachinesModel.getAnchorId());
        assertEquals("anchor title", pxpFillingMachinesModel.getAnchorTitle());
    }

    @Test
    public void testMethodsAuto() throws Exception {

        when(resource.getResourceResolver()).thenReturn(resourceResolver);
        when(resourceResolver.getResource("/var/commerce/products/pxp/packagetypes/packagetype1647/en"))
        .thenReturn(languageResource);
        when(resourceResolver.adaptTo(Session.class)).thenReturn(session);
        when(resourceResolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
        when(queryBuilder.createQuery(any(PredicateGroup.class), any(Session.class))).thenReturn(query);
        when(query.getResult()).thenReturn(searchResult);
        final List<Hit> hits = new ArrayList<>();
        hits.add(hit);
        when(searchResult.getHits()).thenReturn(hits);
        when(hit.getPath()).thenReturn(SOLUTIONS_PAGE);
        pxpFillingMachinesModel.getProductPageMap();
    }
}
