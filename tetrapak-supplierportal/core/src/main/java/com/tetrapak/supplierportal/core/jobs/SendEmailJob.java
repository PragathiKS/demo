
package com.tetrapak.supplierportal.core.jobs;

import java.io.IOException;
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
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import com.tetrapak.supplierportal.core.servlets.SupportRequestFormServlet;

/**
 * Sling job to send email.
 */
@Component(immediate = true, service = JobConsumer.class, property = {
		JobConsumer.PROPERTY_TOPICS + "=" + SupplierPortalConstants.SEND_EMAIL_JOB_TOPIC })
public class SendEmailJob implements JobConsumer {

	/** The email service. */
	@Reference
	private EmailService emailService;
	public static final String TEMPLATE_PATH = "templatePath";
	public static final String EMAIL_PARAMS = "emailParams";
	public static final String RECIPIENTS_ARRAY = "receipientsArray";
	public static final String ATTACHMENTS = "attachments";

	private static final Logger LOGGER = LoggerFactory.getLogger(SendEmailJob.class);

	/**
	 * Process.
	 *
	 * @param job the job
	 * @return the job result
	 */
	@Override
	public JobResult process(final Job job) {
		LOGGER.debug("Inside process method of Support Request Form Email Job");
		List<Map<String, String>> listOfAttachments = (List<Map<String, String>>) job.getProperty(ATTACHMENTS);
		Map<String, DataSource> attachments = new HashMap<>();
		String[] recipients = (String[]) job.getProperty(RECIPIENTS_ARRAY);
		if (listOfAttachments != null) {
			for (Map<String, String> attachment : listOfAttachments) {
				try {
					DataSource fileDS = new ByteArrayDataSource(attachment.get(SupportRequestFormServlet.STREAM),
							attachment.get(SupportRequestFormServlet.CONTENT_TYPE));
					attachments.put(attachment.get(SupportRequestFormServlet.FILE_NAME), fileDS);
				} catch (IOException e) {
					LOGGER.error("error in attachment -- " + e);
				}
			}
		}
		List<String> emailResult = emailService.sendEmail(job.getProperty(TEMPLATE_PATH).toString(),
				(Map<String, String>) job.getProperty(EMAIL_PARAMS), attachments, recipients);

		if (emailResult.isEmpty()) {
			return JobResult.OK;
		}
		return JobResult.FAILED;

	}
}
