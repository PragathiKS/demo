
package com.tetrapak.customerhub.core.jobs;

import com.adobe.acs.commons.email.EmailService;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sling job to send email.
 */
@Component(immediate = true, service = JobConsumer.class, property = {
        JobConsumer.PROPERTY_TOPICS + "=" + CotsSupportEmailJob.JOB_TOPIC_NAME
})
public class CotsSupportEmailJob implements JobConsumer {
    
    public static final String JOB_TOPIC_NAME = "customerhub/jobs/sendEmailJob";
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
        LOGGER.debug("Inside process method of SendEmailJob");
        LOGGER.debug("job.getProperty(\"templatePath\").toString()" + job.getProperty("templatePath").toString());
        List<Map<String, String>> listOfAttachments = (List<Map<String, String>>) job.getProperty("attachments");
        Map<String, DataSource> attachments = new HashMap<>();
        for (Map<String, String> attachment : listOfAttachments) {
            attachments.put(attachment.get("fileName"), new ByteArrayDataSource(
                    Base64.getDecoder().decode(attachment.get("stream")), attachment.get("contentType")));
        }
        emailService.sendEmail(job.getProperty("templatePath").toString(),
                (Map<String, String>) job.getProperty("emailParams"), attachments,
                (String[]) job.getProperty("receipientsArray"));
        return JobResult.OK;
    }
}
