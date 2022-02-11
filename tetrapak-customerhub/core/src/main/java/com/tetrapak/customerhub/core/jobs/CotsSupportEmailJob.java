
package com.tetrapak.customerhub.core.jobs;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.acs.commons.email.EmailService;
import com.tetrapak.customerhub.core.servlets.CotsSupportEmailServlet;

/**
 * Sling job to send email.
 */
@Component(immediate = true, service = JobConsumer.class, property = {
        JobConsumer.PROPERTY_TOPICS + "=" + CotsSupportEmailJob.JOB_TOPIC_NAME
})
public class CotsSupportEmailJob implements JobConsumer {
    
    public static final String JOB_TOPIC_NAME = "customerhub/jobs/sendEmailJob";
    public static final String TEMPLATE_PATH = "templatePath";
    public static final String EMAIL_PARAMS = "emailParams";
    public static final String RECIPIENTS_ARRAY = "receipientsArray";
    public static final String ATTACHMENTS = "attachments";
    private static final Logger LOGGER = LoggerFactory.getLogger(CotsSupportEmailJob.class);
    
    /** The email service. */
    @Reference
    private EmailService emailService;
    
    /**
     * Process.
     * @param job the job
     * @return the job result
     */
    @Override
    public JobResult process(final Job job) {
        LOGGER.debug("Inside process method of CotsSupportEmailJob");
        List<Map<String, String>> listOfAttachments = (List<Map<String, String>>) job.getProperty(ATTACHMENTS);
        Map<String, DataSource> attachments = new HashMap<>();
        String[] recipients = (String[]) job.getProperty(RECIPIENTS_ARRAY);
        if (listOfAttachments != null) {
            for (Map<String, String> attachment : listOfAttachments) {
                attachments.put(attachment.get(CotsSupportEmailServlet.FILE_NAME),
                        new ByteArrayDataSource(
                                Base64.getDecoder().decode(attachment.get(CotsSupportEmailServlet.STREAM)),
                                attachment.get(CotsSupportEmailServlet.CONTENT_TYPE)));
            }
        }
        if (recipients != null) {
            emailService.sendEmail(job.getProperty(TEMPLATE_PATH).toString(),
                    (Map<String, String>) job.getProperty(EMAIL_PARAMS), attachments, recipients);
        } else {
            LOGGER.error("Recipients email address missing in COTS Support component dialog and OSGI configuration.");
            return JobResult.CANCEL;
        }
        return JobResult.OK;
    }
}
