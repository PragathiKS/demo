package com.tetrapak.customerhub.core.models;

import com.adobe.acs.commons.email.EmailService;
import com.adobe.cq.gfx.Plan;
import com.day.cq.wcm.api.LanguageManager;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.services.AIPCategoryService;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.config.AIPEmailConfiguration;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.event.jobs.JobManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class EngineeringLicenseModelTest {

    private EngineeringLicenseModel engineeringLicenseModel = null;
    private PlantMasterLicensesModel plantMasterLicensesModel = null;
    private static final String RESOURCE_JSON = "plantMasterLicensesComponent.json";
    private static final String RESOURCE_PATH = "/content/tetrapak/customerhub/global/en/automation-digital/licenses/jcr:content/root/responsivegrid/plantmasterlicenses";

    @Mock
    private AIPEmailConfiguration AIPEmailConfiguration;

    @Mock
    private APIGEEService apigeeService;

    @Mock
    private AIPCategoryService aipCategoryService;

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON,RESOURCE_PATH);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Resource currentResource = aemContext.resourceResolver().getResource(RESOURCE_PATH);
        aemContext.request().setResource(currentResource);
        aemContext.registerService(AIPEmailConfiguration.class, AIPEmailConfiguration);
        aemContext.registerService(APIGEEService.class, apigeeService);
        when(apigeeService.getApiMappings()).thenReturn(new String[]{"aip-product-details:productinformation/categories/{id}/products"});
        aemContext.registerService(AIPCategoryService.class, aipCategoryService);
        plantMasterLicensesModel = aemContext.request().adaptTo(PlantMasterLicensesModel.class);
        engineeringLicenseModel = plantMasterLicensesModel.getEngineeringLicenseModel();
    }

    @Test
    public void testGetProperties(){
        assertEquals("Unexpected value","Title",engineeringLicenseModel.getUserDetailsSectionTitle());
        assertEquals("Unexpected value","Name",engineeringLicenseModel.getName());
        assertEquals("Unexpected value","Placeholder",engineeringLicenseModel.getNamePlaceholder());
        assertEquals("Unexpected value","Date",engineeringLicenseModel.getActivationDate());
        assertEquals("Unexpected value","Placeholder",engineeringLicenseModel.getActivationDatePlaceholder());
        assertEquals("Unexpected value","License selection title",engineeringLicenseModel.getLicenseSelectionSectionTitle());
        assertEquals("Unexpected value","Add user",engineeringLicenseModel.getAddUser());
        assertEquals("Unexpected value","Comments",engineeringLicenseModel.getComments());
        assertEquals("Unexpected value","Submit",engineeringLicenseModel.getSubmitButton());
        assertEquals("Unexpected value","License",engineeringLicenseModel.getLicenseDescriptions());
        assertEquals("Unexpected value","Subject",engineeringLicenseModel.getSubject());
        assertEquals("Unexpected value","Salutation",engineeringLicenseModel.getSalutation());
        assertEquals("Unexpected value","Body",engineeringLicenseModel.getBody());
        assertEquals("Unexpected value","Success Message description",engineeringLicenseModel.getSuccessMessageDescription());
        assertEquals("Unexpected value","Success Message Heading",engineeringLicenseModel.getSuccessMessageHeading());
        assertEquals("Unexpected value","Users",engineeringLicenseModel.getUsers());
        assertEquals("Unexpected value","Title",engineeringLicenseModel.getTitle());
    }

}