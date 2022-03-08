package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CotsSupportModelTest {

    private CotsSupportModel cotsSupportModel = null;
    private static final String RESOURCE_JSON = "cotsSupportComponent.json";
    private static final String RESOURCE_PATH = "/content/tetrapak/customerhub/global/en/automation-digital/cots-support/jcr:content/root/responsivegrid/cotssupport";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON,RESOURCE_PATH);

    @Before
    public void setup() {
        Resource currentResource = aemContext.resourceResolver().getResource(RESOURCE_PATH);
        aemContext.request().setResource(currentResource);
        cotsSupportModel = aemContext.request().adaptTo(CotsSupportModel.class);
    }

    @Test
    public void testGetMessage() {
        assertEquals("Unexpected value","Title",cotsSupportModel.getTitle());
        assertEquals("Unexpected value!","subtitle",cotsSupportModel.getSubTitle());
        assertEquals("Unexpected value!","select request",cotsSupportModel.getSelectRequest());
        assertEquals("Unexpected value!","Technical Issues",cotsSupportModel.getTechnicalIssues());
        assertEquals("Unexpected value!","Company",cotsSupportModel.getCompany());
        assertEquals("Unexpected value!","Customer Site",cotsSupportModel.getCustomerSite());
        assertEquals("Unexpected value!","Affected Systems",cotsSupportModel.getAffectedSystemsLabel());
        assertEquals("Unexpected value!","Product Involved",cotsSupportModel.getProductInvolvedLabel());
        assertEquals("Unexpected value!","Software version",cotsSupportModel.getSoftwareVersion());
        assertEquals("Unexpected value!","Engineering License Serial Number",cotsSupportModel.getEngineeringLicenseSerialNumber());
        assertEquals("Unexpected value!","Short Description",cotsSupportModel.getShortDescription());
        assertEquals("Unexpected value!","Select FIle",cotsSupportModel.getSelectFile());
        assertEquals("Unexpected value!","Questions",cotsSupportModel.getQuestion());
        assertEquals("Unexpected value!","Name",cotsSupportModel.getName());
        assertEquals("Unexpected value!","Email Address",cotsSupportModel.getEmailAddress());
        assertEquals("Unexpected value!","Telephone",cotsSupportModel.getTelephone());
        assertEquals("Unexpected value!","Select value",cotsSupportModel.getDropdownPlaceholder());
        assertEquals("Unexpected value!","Required Field",cotsSupportModel.getInputErrorMsg());
        assertEquals("Unexpected value!","Success!",cotsSupportModel.getSuccessMessage());

    }

    @Test
    public void testAffectedSystems(){
        List<AffectedSystemsModel> affectedSystemsModels = new ArrayList<>();
        AffectedSystemsModel productionControl = new AffectedSystemsModel();
        List<ProductsInvolvedModel> productionControlList = new ArrayList<>();
        productionControl.setLabel("Production Control");
        ProductsInvolvedModel tia = new ProductsInvolvedModel();
        tia.setProduct("TIA Portal");
        productionControlList.add(tia);
        ProductsInvolvedModel logix = new ProductsInvolvedModel();
        logix.setProduct("Control Logix");
        productionControlList.add(logix);
        ProductsInvolvedModel orion = new ProductsInvolvedModel();
        orion.setProduct("Orion");
        productionControlList.add(orion);
        ProductsInvolvedModel archestra = new ProductsInvolvedModel();
        archestra.setProduct("Archestra System Platform");
        productionControlList.add(archestra);
        productionControl.setProductsInvolved(productionControlList);
        AffectedSystemsModel mes = new AffectedSystemsModel();
        mes.setLabel("MES");
        List<ProductsInvolvedModel> mesList = new ArrayList<>();
        ProductsInvolvedModel integrator = new ProductsInvolvedModel();
        integrator.setProduct("Production Integrator");
        mesList.add(integrator);
        ProductsInvolvedModel aveva = new ProductsInvolvedModel();
        aveva.setProduct("Aveva MES SI Kit");
        mesList.add(aveva);
        mes.setProductsInvolved(mesList);
        affectedSystemsModels.add(mes);
        affectedSystemsModels.add(productionControl);

        assertEquals(2, cotsSupportModel.getAffectedSystems().size());
        assertEquals("Production Control", cotsSupportModel.getAffectedSystems().get(0).getLabel());
        assertEquals("TIA Portal",
                cotsSupportModel.getAffectedSystems().get(0).getProductsInvolved().get(0).getProduct());

    }

}