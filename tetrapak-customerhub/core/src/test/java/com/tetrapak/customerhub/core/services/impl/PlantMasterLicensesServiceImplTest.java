package com.tetrapak.customerhub.core.services.impl;

import com.adobe.acs.commons.email.EmailService;
import com.day.cq.wcm.api.LanguageManager;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
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
    private final String templatePath = "/etc/notification/email/customerhub/cotssupport/cotssupportemail.html";
    private static final String I18N_PATH = "/apps/customerhub/i18n/en";
    private final String recipientEmail = "testUser@company.com";
    private final String ERROR_MESSAGE = "Unexpected value. Please check.";

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
        aemContext.registerService(PlantMasterLicensesEmailConfiguration.class, configuration);
        when(configuration.engineeringLicenseEmailTemplatePath()).thenReturn(templatePath);
        when(configuration.siteLicenseEmailTemplatePath()).thenReturn(templatePath);
        when(configuration.recipientAddresses()).thenReturn(new String[]{recipientEmail});
        when(configuration.isLicensesEmailEnabled()).thenReturn(true);
        aemContext.registerService(APIGEEService.class, apigeeService);
        when(apigeeService.getApiMappings()).thenReturn(new String[]{"aip-product-details:productinformation/categories/{id}/products"});
        aemContext.registerService(AIPCategoryService.class, aipCategoryService);
        aemContext.registerService( PlantMasterLicensesServiceImpl.class,plantMasterLicensesServiceImpl);
        when(plantMasterLicensesServiceImpl.getI18nValue(any(),any(),any())).thenReturn("");

    }

    @Test
    public void testSendEngineeringLicenseEmail() throws IOException {
        aemContext.request().setResource(aemContext.resourceResolver().getResource(RESOURCE_PATH));
        aemContext.request().setHeader("licenseType","engineering");
        String requestBody = "{\n" + "\t\"users\": [{\n" + "\t\t\t\"licenseHolderName\": \"name1\",\n"
                + "\t\t\t\"activationDate\": \"date1\",\n" + "\t\t\t\"licenses\": [\n" + "\t\t\t\t\"licensename1\",\n"
                + "\t\t\t\t\"licensename2\"\n" + "\t\t\t]\n" + "\t\t},\n" + "\t\t{\n" + "\t\t\t\"licenseHolderName\": \"name2\",\n"
                + "\t\t\t\"activationDate\": \"date2\",\n" + "\t\t\t\"licenses\": [\n" + "\t\t\t\t\"licensename3\",\n"
                + "\t\t\t\t\"licensename4\"\n" + "\t\t\t]\n" + "\t\t}\n" + "\t],\n" + "\t\"comments\": \"text\"\n" + "}";
        aemContext.request().setContent(requestBody.getBytes(StandardCharsets.UTF_8));
        assertEquals(ERROR_MESSAGE,true, plantMasterLicensesServiceImpl.sendEmail(aemContext.request()));
    }

    @Test
    public void testSendSiteLicenseEmail() throws IOException {
        aemContext.request().setResource(aemContext.resourceResolver().getResource(RESOURCE_PATH));
        aemContext.request().setHeader("licenseType","site");
        String requestBody = "{\n" + "\t\"nameOfSite\": \"nameOfSite\",\n"
                + "\t\"locationOfSite\": \"locationOfSite\",\n" + "\t\"application\": \"application\",\n"
                + "\t\"plc-type\": \"plc\",\n" + "\t\"hmi-type\": \"hmi\",\n" + "\t\"mes-type\": \"mes\",\n"
                + "\t\"basic-unit\": \"basic\",\n" + "\t\"advanced-units\": \"advanced\"\n" + "}";
        aemContext.request().setContent(requestBody.getBytes(StandardCharsets.UTF_8));
        assertEquals(ERROR_MESSAGE,true,plantMasterLicensesServiceImpl.sendEmail(aemContext.request()));
    }
}