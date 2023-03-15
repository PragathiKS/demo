package com.tetrapak.supplierportal.core.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.http.HttpStatus;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.tetrapak.supplierportal.core.constants.FormConstants;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import com.tetrapak.supplierportal.core.models.SupportRequestFormBean;
import com.tetrapak.supplierportal.core.services.SupportRequestFormEmailService;

@Component(immediate = true, service = SupportRequestFormEmailService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class SupportRequestFormEmailServiceImpl implements SupportRequestFormEmailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SupportRequestFormEmailServiceImpl.class);

	/** The job mgr. */
	@Reference
	private JobManager jobMgr;

	@Override
	public JsonObject sendEmailForNotification(SupportRequestFormBean supportRequestFormBean, String[] mailAddresses,
			List<Map<String, String>> files) {
		LOGGER.debug("inside sendEmailForNotification");
		JsonObject emailResponse = new JsonObject();
		if (Objects.nonNull(mailAddresses)) {
			LOGGER.debug("email address is " + mailAddresses);
			// Set the dynamic variables of email template
			final Map<String, String> emailParams = new HashMap<>();

			// these parameters are used in email template
			String emailSubject = supportRequestFormBean.getPurposeOfContact() + " "
					+ supportRequestFormBean.getCompanyLegalName();

			emailParams.put(FormConstants.NAME, supportRequestFormBean.getName());
			emailParams.put(FormConstants.PURPOSE, supportRequestFormBean.getPurposeOfContact());
			emailParams.put(SupplierPortalConstants.EMAILSUBJECT, emailSubject);
			emailParams.put(FormConstants.COUNTRY, supportRequestFormBean.getCountry());
			emailParams.put(FormConstants.QUERYDESCRIPTION, supportRequestFormBean.getHowHelp());
			emailParams.put(FormConstants.EMAIL, supportRequestFormBean.getEmailAddress());
			emailParams.put(FormConstants.COMPANY_LEGALNAME, supportRequestFormBean.getCompanyLegalName());
			emailParams.put(FormConstants.CITY, supportRequestFormBean.getCity());
			emailParams.put(FormConstants.PHONE, supportRequestFormBean.getOwnPhoneNumber());
			emailParams.put(FormConstants.ARIBANETWORKID, supportRequestFormBean.getAribaNetworkId());
			emailParams.put(FormConstants.ARIBAEMAIL, supportRequestFormBean.getAribaAccountAdminEmail());
			emailParams.put(FormConstants.TPEMAIL, supportRequestFormBean.getTpContactEmail());

			final Map<String, Object> properties = new HashMap<>();
			properties.put("templatePath", SupplierPortalConstants.SUPPORT_REQUEST_FORM_MAIL_TEMPLATE_PATH);
			properties.put("emailParams", emailParams);
			properties.put("receipientsArray", mailAddresses);
			properties.put("attachments", files);
			if (jobMgr != null) {
				LOGGER.debug("inside job");
				jobMgr.addJob(SupplierPortalConstants.SEND_EMAIL_JOB_TOPIC, properties);
				emailResponse.addProperty(SupplierPortalConstants.RESULT, "success");
				emailResponse.addProperty(SupplierPortalConstants.STATUS, "200");
			} else {
				LOGGER.error("JobManager Reference null");
				emailResponse.addProperty(SupplierPortalConstants.RESULT, "email failed");
				emailResponse.addProperty(SupplierPortalConstants.STATUS, HttpStatus.SC_BAD_REQUEST);
			}
		}

		return emailResponse;
	}
}
