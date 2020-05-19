
package com.tetrapak.publicweb.core.jobs;

import java.util.Map;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.acs.commons.email.EmailService;

/**
 * Sling job to add assets to AEM DAM
 *
 * @author Akash Bansal
 *
 */
@Component(
        immediate = true,
        service = JobConsumer.class,
        property = { JobConsumer.PROPERTY_TOPICS + "=com/tetrapak/publicweb/sendemail" })
public class SendEmailJob implements JobConsumer {

    @Reference
    private EmailService emailService;

    @Override
    public JobResult process(final Job job) {

        emailService.sendEmail(job.getProperty("templatePath").toString(),
                (Map<String, String>) job.getProperty("emailParams"),
                (String[]) job.getProperty("receipientsArray"));
                    return JobResult.OK;
    }

}
