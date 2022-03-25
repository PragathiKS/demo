package com.tetrapak.customerhub.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.apache.sling.event.jobs.JobManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.adobe.acs.commons.email.EmailService;
import com.day.cq.wcm.api.LanguageManager;
import com.tetrapak.customerhub.core.beans.aip.PlantMasterTrainingsFormBean;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.MockAIPCategoryServiceImpl;
import com.tetrapak.customerhub.core.mock.MockAPIGEEServiceImpl;
import com.tetrapak.customerhub.core.services.AIPCategoryService;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.config.PlantMasterEmailConfiguration;
import com.tetrapak.customerhub.core.utils.GlobalUtil;

import io.wcm.testing.mock.aem.junit.AemContext;

public class PlantMasterTrainingsServiceImplTest {

    /** The job manager. */
    @Mock
    private JobManager jobManager;

    @Mock
    private EmailService emailService;

    @Mock
    private PlantMasterEmailConfiguration PlantMasterEmailConfiguration;

    @Mock
    private LanguageManager languageManager;

    /** The apigeeService. */
    private APIGEEService apigeeService;

    /** The aip category service. */
    private AIPCategoryService aipCategoryService;

    @Spy
    GlobalUtil globalUtil;

    private static final String RESOURCE_JSON = "plantmastertrainingscomponent.json";
    private static final String RESOURCE_PATH = "/content/tetrapak/customerhub/global/en/automation-digital/plantmaster-trainings/jcr:content/root/responsivegrid/plantmastertrainings";
    private final String templatePath = "/etc/notification/email/customerhub/plantmastertrainings/plantmastertrainingsemail.html";
    private final String recipientEmail = "testUser@company.com";
    private final String ERROR_MESSAGE = "Unexpected value. Please check.";

    @Spy
    @InjectMocks
    private PlantMasterTrainingsServiceImpl plantMasterTrainingsServiceImpl = new PlantMasterTrainingsServiceImpl();

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON, RESOURCE_PATH);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        apigeeService = new MockAPIGEEServiceImpl();
        aemContext.registerService(APIGEEService.class, apigeeService);
        aipCategoryService = new MockAIPCategoryServiceImpl();
        aemContext.registerService(AIPCategoryService.class, aipCategoryService);
        aemContext.registerService(JobManager.class, jobManager);
        aemContext.registerService(EmailService.class, emailService);
        aemContext.registerService(LanguageManager.class, languageManager);
        aemContext.registerService(PlantMasterEmailConfiguration.class, PlantMasterEmailConfiguration);
        when(PlantMasterEmailConfiguration.plantMasterEmailTemplatePath()).thenReturn(templatePath);
        when(PlantMasterEmailConfiguration.plantMasterRecipientAddresses()).thenReturn(new String[] { recipientEmail });
        when(PlantMasterEmailConfiguration.isPlantMasterEmailServiceEnabled()).thenReturn(true);
        aemContext.registerService(PlantMasterTrainingsServiceImpl.class, plantMasterTrainingsServiceImpl);
        when(plantMasterTrainingsServiceImpl.getI18nValue(any(), any(), any())).thenReturn("");
    }

    @Test
    public void testSendEmail() {
        PlantMasterTrainingsFormBean planMasterTrainingsFormBean = new PlantMasterTrainingsFormBean();
        aemContext.request().setResource(aemContext.resourceResolver().getResource(RESOURCE_PATH));
        assertEquals(ERROR_MESSAGE, true,
                plantMasterTrainingsServiceImpl.sendEmail(planMasterTrainingsFormBean, aemContext.request()));
    }
}
