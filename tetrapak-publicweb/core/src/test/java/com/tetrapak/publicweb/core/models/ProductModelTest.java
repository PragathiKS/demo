package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.publicweb.core.constants.PWConstants;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class ProductModelTest.
 */
public class ProductModelTest {

    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /**
     * The Constant PRODUCTS_DATA.
     */
    private static final String PRODUCTS_DATA = "/product/test-Products.json";

    /** The Constant EN_LANGUAGE_RESOURCE_CONTENT. */
    private static final String EN_LANGUAGE_RESOURCE_CONTENT = "/product/en.json";

    /** The Constant PT_LANGUAGE_RESOURCE_CONTENT. */
    private static final String PT_LANGUAGE_RESOURCE_CONTENT = "/product/pt.json";

    /** The Constant TEST_RESOURCE_CONTENT1. */
    private static final String TEST_RESOURCE_CONTENT1 = "/product/test-content1.json";

    /** The Constant TEST_RESOURCE_CONTENT2. */
    private static final String TEST_RESOURCE_CONTENT2 = "/product/test-content2.json";

    /** The Constant TEST_RESOURCE_CONTENT3. */
    private static final String TEST_RESOURCE_CONTENT3 = "/product/test-content3.json";

    /**
     * The Constant EN_LANGUAGE_CONTENT_ROOT.
     */
    private static final String EN_LANGUAGE_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-master/en";

    /**
     * The Constant PT_LANGUAGE_CONTENT_ROOT.
     */
    private static final String PT_LANGUAGE_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-master/pt";

    /**
     * The Constant FILLING_MACHINE_CONTENT_ROOT.
     */
    private static final String FILLING_MACHINE_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-master/pt/filling-machine";

    /**
     * The Constant EQUIPMENT_CONTENT_ROOT.
     */
    private static final String EQUIPMENT_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-master/en/equipment";

    /**
     * The Constant PACKAGE_TYPE_CONTENT_ROOT.
     */
    private static final String PACKAGE_TYPE_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-master/en/package-type";

    /**
     * The Constant PXP_FEATURES.
     */
    private static final String RESOURCE1 = FILLING_MACHINE_CONTENT_ROOT + "/jcr:content/pxpfeatures";

    /**
     * The Constant PXP_FEATURES.
     */
    private static final String RESOURCE2 = EQUIPMENT_CONTENT_ROOT + "/jcr:content/pxpfeatures";

    /**
     * The Constant PXP_FEATURES.
     */
    private static final String RESOURCE3 = PACKAGE_TYPE_CONTENT_ROOT + "/jcr:content/pxpfeatures";

    /** The model. */
    private ProductModel model;

    /**
     * The resource.
     */
    private Resource resource;

    Class<ProductModel> modelClass = ProductModel.class;

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        // load the resources for each object
        context.load().json(PRODUCTS_DATA, PWConstants.PXP_ROOT_PATH);
        context.load().json(EN_LANGUAGE_RESOURCE_CONTENT, EN_LANGUAGE_CONTENT_ROOT);
        context.load().json(PT_LANGUAGE_RESOURCE_CONTENT, PT_LANGUAGE_CONTENT_ROOT);
        context.load().json(TEST_RESOURCE_CONTENT1, FILLING_MACHINE_CONTENT_ROOT);
        context.load().json(TEST_RESOURCE_CONTENT2, EQUIPMENT_CONTENT_ROOT);
        context.load().json(TEST_RESOURCE_CONTENT3, PACKAGE_TYPE_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);
    }

    /**
     * Test all methods.
     */
    @Test
    public void testFillingMachineMethods() {
        resource = context.currentResource(RESOURCE1);
        model = resource.adaptTo(modelClass);
        assertEquals("FillingMachine", "Tetra Pak® A3/Flex", model.getName());
        assertEquals("FillingMachine", "Versatilidade de formas, tamanhos e fechamentos", model.getHeader());
        assertEquals("FillingMachine",
                "/content/dam/tetrapak/publicweb/pxp/fillingmachines/equipment1272/image/A3-Flex-no-DIMC-Benefits.png",
                model.getBenefitsimage());
        assertEquals("FillingMachine",
                "<ul><li>Fácil conversão entre diversas formas e tamanhos de embalagem família</li><li>Fácil adaptação a vários fechamentos diferentes</li><li>Alta segurança dos alimentos (processo asséptico exclusivo registrado na FDA)</li></ul>",
                model.getBenifits());
        assertEquals("FillingMachine",
                "Conceito de moldagem por injeção direta para uma abertura altamente funcional e de baixo custo",
                model.getOptions().get(1).getHeader());
        assertEquals("FillingMachine", "DIMC no orifício pré-laminado (PLH, Pre-Laminate Hole)",
                model.getOptions().get(1).getName());
        assertEquals("FillingMachine",
                "/content/dam/tetrapak/publicweb/pxp/fillingmachines/equipment1272/image/thumbnail-3953_0002.jpg",
                model.getOptions().get(1).getVideo().getPoster());
        assertEquals("FillingMachine",
                "/content/dam/tetrapak/publicweb/pxp/fillingmachines/equipment1272/video/dimc_animation_external_mp4_1350220660.mp4",
                model.getOptions().get(1).getVideo().getSrc());
        assertEquals("FillingMachine",
                "<p>O material de embalagem plano com um PLH é alimentado na unidade DIMC e o gargalo da abertura da tampa de rosca é injetado no PLH usando a tecnologia de moldagem por injeção direta.<br />A tampa real é rosqueada no gargalo pelo aplicador de tampas.<br />A unidade DIMC é colocada no nível do chão para acesso fácil do operador. </p>",
                model.getOptions().get(1).getBody());
        assertEquals("FillingMachine", "Tetra Gemina® Aseptic", model.getPackageTypeReferences().get(0).getName());
        assertEquals("FillingMachine", "packagetype1652", model.getPackageTypeReferences().get(0).getId());
        assertEquals("FillingMachine", "Square", model.getPackageTypeReferences().get(0).getShapes().get(2).getName());
        assertEquals("FillingMachine",
                "/content/dam/tetrapak/publicweb/pxp/packagetypes/packagetype1652/image/tga_1500_sq_helicap27_np3_relative.png",
                model.getPackageTypeReferences().get(0).getShapes().get(2).getThumbnail());
        assertEquals("FillingMachine", "1000 ml",
                model.getPackageTypeReferences().get(0).getShapes().get(2).getVolumes().get(2));

    }

    /**
     * Test all methods.
     */
    @Test
    public void testEquipementMethods() {
        resource = context.currentResource(RESOURCE2);
        model = resource.adaptTo(modelClass);
        assertEquals("ProcessingEquipement", "Tetra Pak® Aseptic Dosing unit E", model.getName());
        assertEquals("ProcessingEquipement",
                "/content/dam/tetrapak/publicweb/pxp/processingequipments/processing_equipment13225/image/tetra_aldose_benefits_1.png",
                model.getBenefitsimage());
        assertEquals("ProcessingEquipement",
                "<ul><li>One process step is removed when going from batch to inline dosing, thereby reducing equipment and running costs.</li><li>Ingredients savings - since dosing takes place after heating it eliminates the need for overdosing</li><li>Excellent quality end product thanks to low heat load, high dosing precision and repeatability</li></ul>",
                model.getBenifits());
        assertEquals("ProcessingEquipement", "Cost efficient aseptic dosing", model.getHeader());
        assertEquals("ProcessingEquipement", "Twice the shelf life", model.getFeatures().get(1).getHeader());
        assertEquals("ProcessingEquipement", "Product quality control", model.getFeatures().get(1).getName());
        assertEquals("ProcessingEquipement",
                "/content/dam/tetrapak/publicweb/pxp/processingequipments/processing_equipment13225/image/tetra_aldose_expert_dosing_service.jpg",
                model.getFeatures().get(2).getImage());
        assertEquals("ProcessingEquipement",
                "/content/dam/tetrapak/publicweb/pxp/processingequipments/processing_equipment13225/image/thumbnail-65579_0002.jpg",
                model.getFeatures().get(1).getVideo().getPoster());
        assertEquals("ProcessingEquipement",
                "/content/dam/tetrapak/publicweb/pxp/processingequipments/processing_equipment13225/video/6tetra_flexdos-product_quality_control_2_mp4_1436277603.mp4",
                model.getFeatures().get(1).getVideo().getSrc());
        assertEquals("ProcessingEquipement",
                "<p>In the production of UHT lactose reduced milk, aseptic dosing means less lactase is added to the product, and this in turn means less sugar content in the milk. Less sugar content reduces the browning of the milk(caused by the Maillard reaction) and it typically doubles product shelf life from 3 to 6 months.</p>",
                model.getFeatures().get(1).getBody());
        assertEquals("ProcessingEquipement", "processing_equipment_tech13124", model.getTechnology().getId());
        assertEquals("ProcessingEquipement", "Dosing", model.getTechnology().getName());
        assertEquals("ProcessingEquipement", "processing_equipment_techtype13124", model.getTechnologyType().getId());
        assertEquals("ProcessingEquipement", "Dosing PT", model.getTechnologyType().getName());
        assertEquals("ProcessingEquipement", "cat156", model.getCategories().get(1).getId());
        assertEquals("ProcessingEquipement", "Ice Cream", model.getCategories().get(1).getName());
    }

    /**
     * Test all methods.
     */
    @Test
    public void testPackageTypeMethods() {
        resource = context.currentResource(RESOURCE3);
        model = resource.adaptTo(modelClass);
        assertEquals("PackageType", "Tetra Top® AD", model.getName());
        assertEquals("PackageType", "Midi", model.getShapes().get(1).getName());
        assertEquals("PackageType",
                "/content/dam/tetrapak/publicweb/pxp/packagetypes/packagetype1647/image/pxp_tt_300_midi_huron_haad_np-em_thumbnail.png",
                model.getShapes().get(1).getThumbnail());
        assertEquals("PackageType", "330 ml", model.getShapes().get(1).getVolumes().get(2));
        assertEquals("PackageType", "openingclosure1456", model.getOpeningClousers().get(1).getId());
        assertEquals("PackageType", "Huron™", model.getOpeningClousers().get(1).getName());
        assertEquals("PackageType", "Lid covers whole package top",
                model.getOpeningClousers().get(1).getBenefits().get(1));
        assertEquals("PackageType", "Single-use", model.getOpeningClousers().get(1).getPrinciple());
        assertEquals("PackageType",
                "/content/dam/tetrapak/publicweb/pxp/packagetypes/packagetype1647/image/pxp_huron_tt_openingsthumb.png",
                model.getOpeningClousers().get(1).getThumbnail());
        assertEquals("PackageType", "Peel back lid", model.getOpeningClousers().get(1).getType());
        assertEquals("PackageType", "Flexible two-line filling machine goes ambient",
                model.getFillingMachineReferences().get(1).getHeader());
        assertEquals("PackageType", "equipment1260", model.getFillingMachineReferences().get(1).getId());
        assertEquals("PackageType",
                "/content/dam/tetrapak/publicweb/pxp/fillingmachines/equipment1260/image/tt3_haad_hd1_copy.png",
                model.getFillingMachineReferences().get(1).getThumbnail());
        assertEquals("PackageType", "Tetra Pak® TT/3 AD", model.getFillingMachineReferences().get(1).getName());

    }
}
