package com.tetrapak.customerhub.core.services.impl;

import com.adobe.acs.commons.email.EmailService;
import com.day.cq.wcm.api.LanguageManager;
import com.tetrapak.customerhub.core.beans.aip.CotsSupportFormBean;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.services.config.CotsSupportEmailConfiguration;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class CotsSupportServiceImplTest {

    /** The job manager. */
    @Mock
    private JobManager jobManager;

    @Mock
    private EmailService emailService;

    @Mock
    private CotsSupportEmailConfiguration AIPEmailConfiguration;

    @Mock
    private LanguageManager languageManager;

    @Spy
    GlobalUtil globalUtil;

    private static final String RESOURCE_JSON = "cotsSupportComponent.json";
    private static final String RESOURCE_PATH = "/content/tetrapak/customerhub/global/en/automation-digital/cots-support/jcr:content/root/responsivegrid/cotssupport";
    private final String templatePath = "/etc/notification/email/customerhub/cotssupport/cotssupportemail.html";
    private static final String I18N_PATH = "/apps/customerhub/i18n/en";
    private final String recipientEmail = "testUser@company.com";
    private final String ERROR_MESSAGE = "Unexpected value. Please check.";

    @Spy
    @InjectMocks
    private CotsSupportServiceImpl cotsSupportServiceImpl = new CotsSupportServiceImpl() ;

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON,RESOURCE_PATH);

    @Before public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        aemContext.registerService(JobManager.class,jobManager);
        aemContext.registerService(EmailService.class,emailService);
        aemContext.registerService(LanguageManager.class,languageManager);
        aemContext.registerService(CotsSupportEmailConfiguration.class, AIPEmailConfiguration);
        when(AIPEmailConfiguration.emailTemplatePath()).thenReturn(templatePath);
        when(AIPEmailConfiguration.recipientAddresses()).thenReturn(new String[]{recipientEmail});
        when(AIPEmailConfiguration.isCotsSupportEmailEnabled()).thenReturn(true);
        aemContext.registerService( CotsSupportServiceImpl.class,cotsSupportServiceImpl);
        when(cotsSupportServiceImpl.getI18nValue(any(),any(),any())).thenReturn("");

    }

    @Test public void testSendEmail() {
        List<Map<String,String>> attachments = new ArrayList<>();
        CotsSupportFormBean cotsSupportFormBean = new CotsSupportFormBean();
        aemContext.request().setResource(aemContext.resourceResolver().getResource(RESOURCE_PATH));
        assertEquals(ERROR_MESSAGE,true,cotsSupportServiceImpl.sendEmail(attachments,cotsSupportFormBean,aemContext.request()));
    }
}