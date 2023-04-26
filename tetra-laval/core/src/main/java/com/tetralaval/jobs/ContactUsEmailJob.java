package com.tetralaval.jobs;

import java.util.Map;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.acs.commons.email.EmailService;
import com.tetralaval.constants.TLConstants;

/**
 * Sling job to send email.
 */
@Component(immediate = true, service = JobConsumer.class, property = {
	JobConsumer.PROPERTY_TOPICS + "=" + TLConstants.CONTACTUS_EMAIL_JOB })
public class ContactUsEmailJob implements JobConsumer {

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactUsEmailJob.class);

    /**
     * The email service.
     */
    @Reference
    private EmailService emailService;

    /**
     * Process.
     *
     * @param job the job
     * @return the job result
     */
    @Override
    public JobResult process(final Job job) {
	JobResult result = JobResult.FAILED;
	LOGGER.debug("Start - ContactUsEmailJob");
	try {
	    emailService.sendEmail(job.getProperty(TLConstants.JOB_TEMPLATE_PATH).toString(),
		    (Map<String, String>) job.getProperty(TLConstants.JOB_EMAIL_PARAMS),
		    (String[]) job.getProperty(TLConstants.JOB_RECIPIENTS));
	    result = JobResult.OK;
	} catch (Exception e) {
	    LOGGER.error("An error occured with sending email", e);
	}
	LOGGER.debug("End - ContactUsEmailJob with result {}", result);
	return result;

    }
}
