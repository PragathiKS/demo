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
import java.util.Base64;
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
    private static final String OK_ERROR_MESSAGE = "Jobresult should be OK.";
    private static final String FAIL_ERROR_MESSAGE = "Jobresult should be CANCEL";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        List<Map<String,String>> listOfAttachments = new ArrayList<>();
        final Map<String, Object> emailParams = new HashMap<>();
        when(job.getProperty("templatePath")).thenReturn(templatePath);
        when(job.getProperty("emailParams")).thenReturn(emailParams);
        when(job.getProperty("attachments")).thenReturn(listOfAttachments);
    }

    @Test
    public void testProcessSuccess() {
        final String[] receipientsArray = { "mikesmith@tetrapak.com" };
        when(job.getProperty(MyTetrapakEmailJob.RECIPIENTS_ARRAY)).thenReturn(receipientsArray);
        List<Map<String, String>> listOfAttachments = new ArrayList<>();
        Map<String, String> attachment = new HashMap<>();
        attachment.put("stream", Base64.getEncoder().encodeToString("Test".getBytes()));
        listOfAttachments.add(attachment);
        when(job.getProperty(MyTetrapakEmailJob.ATTACHMENTS)).thenReturn(listOfAttachments);
        assertEquals(OK_ERROR_MESSAGE, JobConsumer.JobResult.OK, sendMailJob.process(job));
    }

    @Test
    public void testProcessFail(){
        assertEquals(FAIL_ERROR_MESSAGE, JobConsumer.JobResult.CANCEL, sendMailJob.process(job));
    }
}