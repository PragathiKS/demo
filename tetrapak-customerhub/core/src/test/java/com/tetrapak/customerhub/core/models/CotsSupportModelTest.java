package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class CotsSupportModelTest {

    private CotsSupportModel cotsSupportModel = null;
    private static final String RESOURCE_JSON = "cotsSupportComponent.json";
    private static final String RESOURCE_PATH = "/content/tetrapak/customerhub/global/en/automation-digital/cots-support/jcr:content/root/responsivegrid/cotssupport";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON,RESOURCE_PATH);

    @Before
    public void setup() {
        Resource currentResource = aemContext.currentResource(RESOURCE_PATH);
        cotsSupportModel = currentResource.adaptTo(CotsSupportModel.class);
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

}