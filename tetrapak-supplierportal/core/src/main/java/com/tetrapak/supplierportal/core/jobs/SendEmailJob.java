
package com.tetrapak.supplierportal.core.jobs;

import java.util.Map;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import com.adobe.acs.commons.email.EmailService;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;

/**
 * Sling job to send email.
 */
@Component(immediate = true, service = JobConsumer.class, property = {
		JobConsumer.PROPERTY_TOPICS + "=" + SupplierPortalConstants.SEND_EMAIL_JOB_TOPIC })
public class SendEmailJob implements JobConsumer {

	/** The email service. */
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

		emailService.sendEmail(job.getProperty("templatePath").toString(),
				(Map<String, String>) job.getProperty("emailParams"), (String[]) job.getProperty("receipientsArray"));
		return JobResult.OK;
	}
}
