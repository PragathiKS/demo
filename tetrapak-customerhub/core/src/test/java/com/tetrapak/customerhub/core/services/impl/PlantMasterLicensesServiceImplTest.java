package com.tetrapak.customerhub.core.services.impl;

import com.adobe.acs.commons.email.EmailService;
import com.day.cq.wcm.api.LanguageManager;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.models.PlantMasterLicensesModel;
import com.tetrapak.customerhub.core.services.AIPCategoryService;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.config.PlantMasterLicensesEmailConfiguration;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.event.jobs.JobManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class PlantMasterLicensesServiceImplTest {

    /** The job manager. */
    @Mock
    private JobManager jobManager;

    @Mock
    private EmailService emailService;

    @Mock
    private PlantMasterLicensesEmailConfiguration configuration;

    @Mock
    private LanguageManager languageManager;

    @Mock
    private APIGEEService apigeeService;

    @Mock
    private AIPCategoryService aipCategoryService;


    private static final String RESOURCE_JSON = "plantMasterLicensesComponent.json";
    private static final String RESOURCE_PATH = "/content/tetrapak/customerhub/global/en/automation-digital/licenses/jcr:content/root/responsivegrid/plantmasterlicenses";
    private final String ERROR_MESSAGE = "Unexpected value. Please check.";
    private static final String LICENSE_TYPE_REQUEST_PARAMETER = "licenseType";
    private static final String recipientEmail = "testUser@company.com";

    @Spy
    @InjectMocks
    private PlantMasterLicensesServiceImpl plantMasterLicensesServiceImpl = new PlantMasterLicensesServiceImpl() ;

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON,RESOURCE_PATH);

    @Before public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        aemContext.registerService(JobManager.class,jobManager);
        aemContext.registerService(EmailService.class,emailService);
        aemContext.registerService(LanguageManager.class,languageManager);
        aemContext.registerService(APIGEEService.class, apigeeService);
        when(configuration.isLicensesEmailEnabled()).thenReturn(true);
        aemContext.registerService(AIPCategoryService.class, aipCategoryService);
        aemContext.registerService( PlantMasterLicensesServiceImpl.class,plantMasterLicensesServiceImpl);
        when(plantMasterLicensesServiceImpl.getI18nValue(any(),any(),any())).thenReturn("");

    }

    @Test
    public void testActivateConfiguration() throws Exception {
        Map<String, Object> _config = new HashMap<>();
        _config.put("recipientAddresses", "testing@test.com");
        _config.put("isLicensesEmailEnabled", "true");
        aemContext.registerService(PlantMasterLicensesEmailConfiguration.class, configuration);
        aemContext.registerInjectActivateService(plantMasterLicensesServiceImpl, _config);
        aemContext.request().setResource(aemContext.resourceResolver().getResource(RESOURCE_PATH));
        aemContext.request().setHeader("licenseType","activeWithdrawal");
        String requestBody = "{}";
        when(apigeeService.getApiMappings()).thenReturn(new String[]{"aip-active-licenses:installedbase/softwarelicenses"});
        aemContext.request().setContent(requestBody.getBytes(StandardCharsets.UTF_8));
        ResourceBundle resourceBundle = aemContext.request().getResourceBundle(aemContext.request().getLocale());
        String requestData = aemContext.request().getReader().lines().collect(Collectors.joining());
        PlantMasterLicensesModel masterLicensesModel = aemContext.request().adaptTo(PlantMasterLicensesModel.class);
        assertEquals(ERROR_MESSAGE,true,plantMasterLicensesServiceImpl.sendEmail(resourceBundle,
                aemContext.request().getHeader(LICENSE_TYPE_REQUEST_PARAMETER), requestData, masterLicensesModel));
    }

    @Test
    public void testSendEngineeringLicenseEmail() throws IOException {
        when(apigeeService.getApiMappings()).thenReturn(new String[]{"aip-product-details:productinformation/categories/{id}/products"});
        when(configuration.recipientAddresses()).thenReturn(new String[]{recipientEmail});
        aemContext.request().setResource(aemContext.resourceResolver().getResource(RESOURCE_PATH));
        aemContext.request().setHeader("licenseType", "engineering");
        String requestBody = "{\n" + "\t\"users\": [{\n" + "\t\t\t\"licenseHolderName\": \"name1\",\n"
                + "\t\t\t\"activationDate\": \"date1\",\n" + "\t\t\t\"licenses\": [\n" + "\t\t\t\t\"licensename1\",\n"
                + "\t\t\t\t\"licensename2\"\n" + "\t\t\t]\n" + "\t\t},\n" + "\t\t{\n"
                + "\t\t\t\"licenseHolderName\": \"name2\",\n" + "\t\t\t\"activationDate\": \"date2\",\n"
                + "\t\t\t\"licenses\": [\n" + "\t\t\t\t\"licensename3\",\n" + "\t\t\t\t\"licensename4\"\n" + "\t\t\t]\n"
                + "\t\t}\n" + "\t],\n" + "\t\"comments\": \"text\"\n" + "}";
        aemContext.request().setContent(requestBody.getBytes(StandardCharsets.UTF_8));
        ResourceBundle resourceBundle = aemContext.request().getResourceBundle(aemContext.request().getLocale());
        String requestData = aemContext.request().getReader().lines().collect(Collectors.joining());
        PlantMasterLicensesModel masterLicensesModel = aemContext.request().adaptTo(PlantMasterLicensesModel.class);
        assertEquals(ERROR_MESSAGE, true, plantMasterLicensesServiceImpl.sendEmail(resourceBundle,
                aemContext.request().getHeader(LICENSE_TYPE_REQUEST_PARAMETER), requestData, masterLicensesModel));
        when(configuration.recipientAddresses()).thenReturn(null);
        assertEquals(ERROR_MESSAGE, false, plantMasterLicensesServiceImpl.sendEmail(resourceBundle,
                aemContext.request().getHeader(LICENSE_TYPE_REQUEST_PARAMETER), requestData, masterLicensesModel));

    }

    @Test
    public void testSendSiteLicenseEmail() throws IOException {
        when(apigeeService.getApiMappings()).thenReturn(new String[]{"aip-product-details:productinformation/categories/{id}/products"});
        when(configuration.recipientAddresses()).thenReturn(new String[]{recipientEmail});
        aemContext.request().setResource(aemContext.resourceResolver().getResource(RESOURCE_PATH));
        aemContext.request().setHeader("licenseType","site");
        String requestBody = "{\n" + "\t\"nameOfSite\": \"nameOfSite\",\n"
                + "\t\"locationOfSite\": \"locationOfSite\",\n" + "\t\"application\": \"application\",\n"
                + "\t\"plc-type\": \"plc\",\n" + "\t\"hmi-type\": \"hmi\",\n" + "\t\"mes-type\": \"mes\",\n"
                + "\t\"basic-unit\": \"basic\",\n" + "\t\"advanced-units\": \"advanced\"\n" + "}";
        aemContext.request().setContent(requestBody.getBytes(StandardCharsets.UTF_8));
        ResourceBundle resourceBundle = aemContext.request().getResourceBundle(aemContext.request().getLocale());
        String requestData = aemContext.request().getReader().lines().collect(Collectors.joining());
        PlantMasterLicensesModel masterLicensesModel = aemContext.request().adaptTo(PlantMasterLicensesModel.class);
        assertEquals(ERROR_MESSAGE,true,plantMasterLicensesServiceImpl.sendEmail(resourceBundle,
                aemContext.request().getHeader(LICENSE_TYPE_REQUEST_PARAMETER), requestData, masterLicensesModel));
        when(configuration.recipientAddresses()).thenReturn(null);
        assertEquals(ERROR_MESSAGE, false, plantMasterLicensesServiceImpl.sendEmail(resourceBundle,
                aemContext.request().getHeader(LICENSE_TYPE_REQUEST_PARAMETER), requestData, masterLicensesModel));
    }

    @Test
    public void testSendEmailWithdrawlLicense() throws IOException {
        when(apigeeService.getApiMappings()).thenReturn(new String[]{"aip-active-licenses:installedbase/softwarelicenses"});
        when(configuration.recipientAddresses()).thenReturn(new String[]{recipientEmail});
        aemContext.request().setResource(aemContext.resourceResolver().getResource(RESOURCE_PATH));
        aemContext.request().setHeader("licenseType","activeWithdrawal");
        String requestBody = "{\n" + "\t\"name\": \"name\",\n"
                + "\t\"licenseKey\": \"T51010200099\",\n" + "\t\"platform\": \"xyz platform\",\n"
                + "\t\"startDate\": \"2023-04-04\",\n" + "\t\"endDate\": \"2024-04-04\",\n" + "\t\"site\": \"ARLAFOODS_SET\",\n"
                + "\t\"comments\": \"Testing comments\"\n" + "}";
        aemContext.request().setContent(requestBody.getBytes(StandardCharsets.UTF_8));
        ResourceBundle resourceBundle = aemContext.request().getResourceBundle(aemContext.request().getLocale());
        String requestData = aemContext.request().getReader().lines().collect(Collectors.joining());
        PlantMasterLicensesModel masterLicensesModel = aemContext.request().adaptTo(PlantMasterLicensesModel.class);
        assertEquals(ERROR_MESSAGE,true,plantMasterLicensesServiceImpl.sendEmail(resourceBundle,
                aemContext.request().getHeader(LICENSE_TYPE_REQUEST_PARAMETER), requestData, masterLicensesModel));
        when(configuration.recipientAddresses()).thenReturn(null);
        assertEquals(ERROR_MESSAGE, false, plantMasterLicensesServiceImpl.sendEmail(resourceBundle,
                aemContext.request().getHeader(LICENSE_TYPE_REQUEST_PARAMETER), requestData, masterLicensesModel));
    }
}