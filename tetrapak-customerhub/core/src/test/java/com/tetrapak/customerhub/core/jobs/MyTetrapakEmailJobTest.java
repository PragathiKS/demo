package com.tetrapak.customerhub.core.jobs;

import com.adobe.acs.commons.email.EmailService;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class MyTetrapakEmailJobTest {

    @Mock
    private EmailService emailService;

    @Mock
    private Job job;

    @InjectMocks
    private final MyTetrapakEmailJob sendMailJob = new MyTetrapakEmailJob();

    @Rule
    public final AemContext aemContext = new AemContext();

    private final String templatePath = "/etc/notification/email/customerhub/cotssupport/cotssupportemail.html";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcess() {
        List<Map<String,String>> listOfAttachments = new ArrayList<>();
        final Map<String, Object> emailParams = new HashMap<>();
        final String[] receipientsArray = { "mikesmith@tetrapak.com" };
        when(job.getProperty("templatePath")).thenReturn(templatePath);
        when(job.getProperty("emailParams")).thenReturn(emailParams);
        when(job.getProperty("receipientsArray")).thenReturn(receipientsArray);
        when(job.getProperty("attachments")).thenReturn(listOfAttachments);

        assertEquals("ProductAssetImportJob", JobConsumer.JobResult.OK, sendMailJob.process(job));
    }
}