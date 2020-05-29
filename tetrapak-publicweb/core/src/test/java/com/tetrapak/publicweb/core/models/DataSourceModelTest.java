package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import com.adobe.granite.ui.components.ds.DataSource;
import com.tetrapak.publicweb.core.services.PseudoCategoryService;
import com.tetrapak.publicweb.core.services.impl.PseudoCategoryServiceImpl;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class DataSourceModelTest.
 */
public class DataSourceModelTest {

    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /** The mock request. */
    @Mock
    private SlingHttpServletRequest mockRequest;

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/contentFragments/pseudo-categories.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/dam/tetrapak/publicweb/contentfragment/pseudo-categories";

    /** The model. */
    private DataSourceModel model;

    /** The pseudo category service. */
    private PseudoCategoryService pseudoCategoryService;

    /** The model class. */
    final Class<DataSourceModel> modelClass = DataSourceModel.class;

    /**
     * The setup method.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        pseudoCategoryService = new PseudoCategoryServiceImpl();
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);
        context.registerInjectActivateService(pseudoCategoryService);
        final MockSlingHttpServletRequest request = context.request();
        model = request.adaptTo(modelClass);
    }

    /**
     * Test data.
     */
    @Test
    public void testData() {
        final DataSource dataSource = (DataSource) context.request().getAttribute(DataSource.class.getName());
        final Iterator<Resource> iterator = dataSource.iterator();
        final List<ValueMap> valueMapList = new ArrayList<>();
        while (iterator.hasNext()) {
            final Resource resource = iterator.next();
            valueMapList.add(resource.getValueMap());
        }
        assertEquals("Select", valueMapList.get(0).get("text"));
        assertEquals("", valueMapList.get(0).get("value"));
        assertEquals("TECHNOLOGY AREAS", valueMapList.get(1).get("text"));
        assertEquals("TECHNOLOGY AREAS", valueMapList.get(1).get("value"));
        assertEquals("CHEESE EQUIPMENT", valueMapList.get(2).get("text"));
        assertEquals("CHEESE EQUIPMENT", valueMapList.get(2).get("value"));
        assertEquals("ICE CREAM EQUIPMENT", valueMapList.get(3).get("text"));
        assertEquals("ICE CREAM EQUIPMENT", valueMapList.get(3).get("value"));
    }
}
