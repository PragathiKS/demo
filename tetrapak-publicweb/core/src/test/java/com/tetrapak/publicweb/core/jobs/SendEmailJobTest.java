package com.tetrapak.publicweb.core.jobs;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer.JobResult;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.adobe.acs.commons.email.EmailService;
import com.tetrapak.publicweb.core.constants.PWConstants;

import io.wcm.testing.mock.aem.junit.AemContext;

public class SendEmailJobTest {

    @Mock
    private EmailService emailService;

    @Mock
    private Job job;

    /** The site search servlet. */
    @InjectMocks
    private final SendEmailJob sendMailJob = new SendEmailJob();

    @Rule
    public final AemContext aemContext = new AemContext();

    private final String templatePath = PWConstants.CONTACT_US_MAIL_TEMPLATE_PATH;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcess() {
        final Map<String, Object> emailParams = new HashMap<>();
        emailParams.put("firstName", "Mike");
        emailParams.put("lastName", "Smith");
        final String[] receipientsArray = { "mikesmith@tetrapak.com" };
            when(job.getProperty("templatePath")).thenReturn(templatePath);
        when(job.getProperty("emailParams")).thenReturn(emailParams);
        when(job.getProperty("receipientsArray")).thenReturn(receipientsArray);

        assertEquals("ProductAssetImportJob", JobResult.OK, sendMailJob.process(job));
    }

}
