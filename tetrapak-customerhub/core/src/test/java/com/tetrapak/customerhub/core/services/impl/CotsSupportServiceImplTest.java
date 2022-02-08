package com.tetrapak.customerhub.core.services.impl;

import com.adobe.acs.commons.email.EmailService;
import com.tetrapak.customerhub.core.beans.aip.CotsSupportFormBean;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.models.CotsSupportModel;
import com.tetrapak.customerhub.core.services.config.CotsSupportServiceConfig;
import io.wcm.testing.mock.aem.junit.AemContext;
import junitx.util.PrivateAccessor;
import org.apache.sling.event.jobs.JobManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.osgi.service.component.annotations.Reference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class CotsSupportServiceImplTest {

    /** The job manager. */
    @Mock
    private JobManager jobManager;

    @Mock
    private EmailService emailService;

    @Mock
    private CotsSupportServiceConfig cotsSupportServiceConfig;


    private static final String RESOURCE_JSON = "cotsSupportComponent.json";
    private static final String RESOURCE_PATH = "/content/tetrapak/customerhub/global/en/automation-digital/cots-support/jcr:content/root/responsivegrid/cotssupport";
    private final String templatePath = "/etc/notification/email/customerhub/cotssupport/cotssupportemail.html";
    private final String recipientEmail = "testUser@company.com";

    @InjectMocks
    private CotsSupportServiceImpl cotsSupportServiceImpl = new CotsSupportServiceImpl();

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON,RESOURCE_PATH);

    @Before public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        aemContext.registerService(JobManager.class,jobManager);
        aemContext.registerService(EmailService.class,emailService);
        aemContext.registerService(CotsSupportServiceConfig.class,cotsSupportServiceConfig);
        when(cotsSupportServiceConfig.emailTemplatePath()).thenReturn(templatePath);
        when(cotsSupportServiceConfig.recipientAddresses()).thenReturn(new String[]{recipientEmail});
        aemContext.registerService( CotsSupportServiceImpl.class,cotsSupportServiceImpl);
    }

    @Test public void sendEmail() {
        List<Map<String,String>> attachments = new ArrayList<>();
        CotsSupportModel model = new CotsSupportModel();
        CotsSupportFormBean cotsSupportFormBean = new CotsSupportFormBean();
        assertEquals(true,cotsSupportServiceImpl.sendEmail(attachments,model,cotsSupportFormBean));
    }
}