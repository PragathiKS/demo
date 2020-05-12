package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

import com.day.cq.i18n.I18n;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.mock.MockHelper;
import com.tetrapak.publicweb.core.models.multifield.ManualModel;

import org.apache.sling.i18n.ResourceBundleProvider;
import org.apache.sling.servlethelpers.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class PXPBannerModelTest.
 */
public class PXPOpeningsModelTest {

    /** The context. */
    @Rule
    public final AemContext context = new AemContext();

    /** The resource bundle. */
    @Mock
    private ResourceBundle resourceBundle;

    /** The i18n. */
    @Mock
    private I18n i18n;

    /** The request. */
    private final MockSlingHttpServletRequest request = context.request();

    /** The Constant PRODUCTS_DATA. */
    private static final String PRODUCTS_DATA = "/product/test-Products.json";

    /** The Constant TEST_RESOURCE_CONTENT. */
    private static final String TEST_RESOURCE_CONTENT = "/pxpopenings/test-content.json";

    /** The Constant PACKAGE_TYPE_CONTENT_ROOT. */
    private static final String PACKAGE_TYPE_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-master/en/package-type";

    /** The Constant RESOURCE_PATH. */
    private static final String RESOURCE_PATH = PACKAGE_TYPE_CONTENT_ROOT + "/jcr:content/pxpopenings";

    /** The model. */
    private PXPOpeningsModel model;

    /** The model class. */
    private final Class<PXPOpeningsModel> modelClass = PXPOpeningsModel.class;

    /**
     * The setup method.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        context.load().json(PRODUCTS_DATA, PWConstants.PXP_ROOT_PATH);
        context.load().json(TEST_RESOURCE_CONTENT, PACKAGE_TYPE_CONTENT_ROOT);

        // Constructing i18n key value map
        final Map<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("openingtype", "Type");
        keyValueMap.put("openingprinciple", "Principle");
        keyValueMap.put("openingbenefits", "Benefits");

        context.registerService(ResourceBundleProvider.class,
                MockHelper.mockResourceBundleProvider(request, keyValueMap));
        context.addModelsForClasses(modelClass);
        request.setPathInfo(PACKAGE_TYPE_CONTENT_ROOT);
        request.setResource(context.currentResource(RESOURCE_PATH));
        model = request.adaptTo(modelClass);
        initMocks(this);
    }

    /**
     * Simple load test.
     */
    @Test
    public void simpleLoadTest() {
        assertNotNull(model);
    }

    /**
     * Test getters.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetters() throws Exception {
        assertEquals("Openings", model.getHeading());
        assertEquals("Anchor title", model.getAnchorTitle());
        assertEquals("anchor123", model.getAnchorId());
        assertEquals("grayscale-white", model.getPwTheme());
    }

    /**
     * Test teaser list.
     *
     * @throws Exception the exception
     */
    @Test
    public void testTeaserList() throws Exception {
        final List<ManualModel> teaserList = model.getTeaserList();
        assertEquals("Katla™ H38", teaserList.get(0).getTitle());
        assertEquals("Katla™ H38", teaserList.get(0).getAlt());
        assertEquals(
                "/content/dam/tetrapak/publicweb/pxp/packagetypes/packagetype1647/image/katla_h38_base_tt_closed_1.png",
                teaserList.get(0).getFileReference());
    }
}
