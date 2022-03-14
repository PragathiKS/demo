package com.tetrapak.customerhub.core.services.impl;

import com.adobe.acs.commons.email.EmailService;
import com.day.cq.wcm.api.LanguageManager;
import com.tetrapak.customerhub.core.beans.aip.CotsSupportFormBean;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.services.config.AIPEmailConfiguration;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class PlantMasterLicensesServiceImplTest {

    /** The job manager. */
    @Mock
    private JobManager jobManager;

    @Mock
    private EmailService emailService;

    @Mock
    private AIPEmailConfiguration AIPEmailConfiguration;

    @Mock
    private LanguageManager languageManager;

    @Spy GlobalUtil globalUtil;

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
        aemContext.registerService(com.tetrapak.customerhub.core.services.config.AIPEmailConfiguration.class, AIPEmailConfiguration);
        when(AIPEmailConfiguration.engineeringLicenseEmailTemplatePath()).thenReturn(templatePath);
        when(AIPEmailConfiguration.siteLicenseEmailTemplatePath()).thenReturn(templatePath);
        when(AIPEmailConfiguration.recipientAddresses()).thenReturn(new String[]{recipientEmail});
        when(AIPEmailConfiguration.isLicensesEmailEnabled()).thenReturn(true);
        aemContext.registerService( PlantMasterLicensesServiceImpl.class,plantMasterLicensesServiceImpl);
        when(plantMasterLicensesServiceImpl.getI18nValue(any(),any(),any())).thenReturn("");

    }

    @Test
    public void testSendEngineeringLicenseEmail() throws IOException {
        List<Map<String,String>> attachments = new ArrayList<>();
        aemContext.request().setResource(aemContext.resourceResolver().getResource(RESOURCE_PATH));
        aemContext.request().setHeader("licenseType","engineering");
        String requestBody = "{\n" + "\t\"users\": [{\n" + "\t\t\t\"name\": \"name1\",\n"
                + "\t\t\t\"date\": \"date1\",\n" + "\t\t\t\"licenses\": [\n" + "\t\t\t\t\"licensename1\",\n"
                + "\t\t\t\t\"licensename2\"\n" + "\t\t\t]\n" + "\t\t},\n" + "\t\t{\n" + "\t\t\t\"name\": \"name2\",\n"
                + "\t\t\t\"date\": \"date2\",\n" + "\t\t\t\"licenses\": [\n" + "\t\t\t\t\"licensename3\",\n"
                + "\t\t\t\t\"licensename4\"\n" + "\t\t\t]\n" + "\t\t}\n" + "\t],\n" + "\t\"comments\": \"text\"\n" + "}";
        aemContext.request().setContent(requestBody.getBytes(StandardCharsets.UTF_8));
        assertEquals(ERROR_MESSAGE,true,plantMasterLicensesServiceImpl.sendEmail(aemContext.request()));
    }

    @Test
    public void testSendSiteLicenseEmail() throws IOException {
        List<Map<String,String>> attachments = new ArrayList<>();
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