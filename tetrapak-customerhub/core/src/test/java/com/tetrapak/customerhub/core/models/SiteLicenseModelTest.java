package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.services.AIPCategoryService;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.config.CotsSupportEmailConfiguration;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class SiteLicenseModelTest {

    private SiteLicenseModel siteLicenseModel = null;
    private PlantMasterLicensesModel plantMasterLicensesModel = null;
    private static final String RESOURCE_JSON = "plantMasterLicensesComponent.json";
    private static final String RESOURCE_PATH = "/content/tetrapak/customerhub/global/en/automation-digital/licenses/jcr:content/root/responsivegrid/plantmasterlicenses";

    @Mock
    private CotsSupportEmailConfiguration AIPEmailConfiguration;

    @Mock
    private APIGEEService apigeeService;

    @Mock
    private AIPCategoryService aipCategoryService;

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON,RESOURCE_PATH);

    @Before
    public void setup() throws UnsupportedEncodingException {
        MockitoAnnotations.initMocks(this);
        Resource currentResource = aemContext.resourceResolver().getResource(RESOURCE_PATH);
        aemContext.request().setResource(currentResource);
        aemContext.registerService(CotsSupportEmailConfiguration.class, AIPEmailConfiguration);
        aemContext.registerService(APIGEEService.class, apigeeService);
        when(apigeeService.getApiMappings()).thenReturn(new String[]{"aip-product-details:productinformation/categories/{id}/products"});
        aemContext.registerService(AIPCategoryService.class, aipCategoryService);
        plantMasterLicensesModel = aemContext.request().adaptTo(PlantMasterLicensesModel.class);
        siteLicenseModel = plantMasterLicensesModel.getSiteLicenseModel();
    }

    @Test
    public void testGetProperties(){
        assertNotNull("Site license cannot be null", siteLicenseModel);
        assertEquals("Unexpected value","Title",siteLicenseModel.getTitle());
        assertEquals("Unexpected value","Desc",siteLicenseModel.getDescription());
        assertEquals("Unexpected value","Name of site",siteLicenseModel.getNameOfSite());
        assertEquals("Unexpected value","Location of site",siteLicenseModel.getLocationOfSite());
        assertEquals("Unexpected value","Application dropdown placeholder",siteLicenseModel.getApplicationDropdownPlaceholder());
        assertEquals("Unexpected value","PLC Type",siteLicenseModel.getPlcType());
        assertEquals("Unexpected value","HMI Type",siteLicenseModel.getHmiType());
        assertEquals("Unexpected value","MES TYpe",siteLicenseModel.getMesType());
        assertEquals("Unexpected value","Basic unit number",siteLicenseModel.getNumberOfBasicUnit());
        assertEquals("Unexpected value","basic unit tooltip heading",siteLicenseModel.getBasicUnitToolTipHeading());
        assertEquals("Unexpected value","basic unit tooltip description",siteLicenseModel.getBasicUnitToolTipDescription());
        assertEquals("Unexpected value","number of Advanced unit",siteLicenseModel.getNumberOfAdvancedUnit());
        assertEquals("Unexpected value","Advanced unit tooltip heading",siteLicenseModel.getAdvancedUnitToolTipHeading());
        assertEquals("Unexpected value","Advanced unit tooltip description",siteLicenseModel.getAdvancedUnitToolTipDescription());
        assertEquals("Unexpected value","submit",siteLicenseModel.getSubmitButtonLabel());
        assertEquals("Unexpected value","Success heading",siteLicenseModel.getSuccessMessageHeading());
        assertEquals("Unexpected value","Success description",siteLicenseModel.getSuccessMessageDescription());
        assertEquals("Unexpected value","Important information",siteLicenseModel.getImpInformationTitle());
        assertEquals("Unexpected value","important information description",siteLicenseModel.getImpInformationDescription());
        assertEquals("Unexpected value","Subject",siteLicenseModel.getSubject());
        assertEquals("Unexpected value","saluation",siteLicenseModel.getSalutation());
        assertEquals("Unexpected value","body",siteLicenseModel.getBody());
        assertEquals("Unexpected value","Application",siteLicenseModel.getApplication());
        assertEquals("Unexpected value","Input field error",siteLicenseModel.getInputFieldError());
		assertEquals("Unexpected value","Number field error",siteLicenseModel.getNumberFieldError());
        assertEquals("Unexpected value","select field error",siteLicenseModel.getSelectFieldError());

    }

    @Test
    public void testPlantMasterLicenseModelProps(){
        assertEquals("Unexpected value","",plantMasterLicensesModel.getEmailAddressValue());
        assertEquals("Unexpected value","",plantMasterLicensesModel.getEmailAddressValue());
        assertEquals("Unexpected value","/content/tetrapak/customerhub/global/en/automation-digital/licenses/_jcr_content/root/responsivegrid/plantmasterlicenses.email.html",plantMasterLicensesModel.getEmailApiUrl());
        assertEquals("Unexpected value","",plantMasterLicensesModel.getSiteLicenseApiUrl());
        assertEquals("Unexpected value","",plantMasterLicensesModel.getEngineeringLicenseApiUrl());
        assertEquals("Unexpected value",true,plantMasterLicensesModel.isPublishEnvironment());
        assertNotNull("i18 map cannot be null", plantMasterLicensesModel.getI18nKeysMap());
        assertEquals("Unexpected value","heading",plantMasterLicensesModel.getHeading());
    }

}