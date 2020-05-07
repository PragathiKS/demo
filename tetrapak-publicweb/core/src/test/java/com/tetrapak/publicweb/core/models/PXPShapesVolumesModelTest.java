package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.*;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.models.multifield.ManualModel;

import io.wcm.testing.mock.aem.junit.AemContext;

public class PXPShapesVolumesModelTest {

    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /**
     * The Constant PRODUCTS_DATA.
     */
    private static final String PRODUCTS_DATA = "/product/test-Products.json";

    /** The Constant EN_LANGUAGE_RESOURCE_CONTENT. */
    private static final String EN_LANGUAGE_RESOURCE_CONTENT = "/product/en.json";

    /**
     * The Constant EN_LANGUAGE_CONTENT_ROOT.
     */
    private static final String EN_LANGUAGE_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-master/en";

    /** The Constant TEST_RESOURCE_CONTENT. */
    private static final String TEST_RESOURCE_CONTENT = "/pxpshapesandvolumes/test-content.json";

    /**
     * The Constant FILLING_MACHINE_CONTENT_ROOT.
     */
    private static final String PACKAGE_TYPE_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-master/en/package-type";

    /** The model class. */
    Class<PXPShapesVolumesModel> modelClass = PXPShapesVolumesModel.class;

    /** The model. */
    private PXPShapesVolumesModel model;

    /**
     * The Constant PXP_FEATURES.
     */
    private static final String RESOURCE = PACKAGE_TYPE_CONTENT_ROOT + "/jcr:content/pxpshapesandvolumes";

    /** The resource. */
    private Resource resource;

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {

        context.load().json(PRODUCTS_DATA, PWConstants.PXP_ROOT_PATH);
        context.load().json(EN_LANGUAGE_RESOURCE_CONTENT, EN_LANGUAGE_CONTENT_ROOT);
        context.load().json(TEST_RESOURCE_CONTENT, PACKAGE_TYPE_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);
        resource = context.currentResource(RESOURCE);
        model = resource.adaptTo(modelClass);
    }

    /**
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testGetShapeMethod() throws Exception {

        ManualModel testshape = new ManualModel();
        testshape.setTitle("Mini");
        testshape.setFileReference(
                "/content/dam/tetrapak/publicweb/pxp/packagetypes/packagetype1647/image/PXP_TT_200_Mini_TaiA38_HAAD_NP_thumbnail.png");
        testshape.setDescription("200 ml");
        testshape.setAlt("Mini");
        assertEquals("PXPShapesVolumes", testshape.getTitle(), model.getTeaserList().get(0).getTitle());
    }

    /**
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testDailogValues() throws Exception {

        assertEquals("PXPShapesVolumes", "Packages and Volumes", model.getHeading());
        assertEquals("PXPShapesVolumes", "Anchor title", model.getAnchorTitle());
        assertEquals("PXPShapesVolumes", "anchor123", model.getAnchorId());
        assertEquals("PXPShapesVolumes", "grayscale-white", model.getPwTheme());

    }

    /**
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testMultipleVolume() throws Exception {

        ManualModel testshape2 = new ManualModel();
        testshape2.setTitle("Midi");
        testshape2.setFileReference(
                "/content/dam/tetrapak/publicweb/pxp/packagetypes/packagetype1647/image/pxp_tt_300_midi_huron_haad_np-em_thumbnail.png");
        testshape2.setDescription("250 ml, 300 ml, 330 ml, 500 ml");
        testshape2.setAlt("Midi");
        assertEquals("PXPShapesVolumes", testshape2.getDescription(), model.getTeaserList().get(1).getDescription());

    }

}
