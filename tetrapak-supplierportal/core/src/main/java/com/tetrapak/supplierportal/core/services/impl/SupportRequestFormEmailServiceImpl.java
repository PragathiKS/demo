package com.tetrapak.supplierportal.core.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	public void sendEmailForNotification(SupportRequestFormBean SupportRequestFormBean, String mailAddresses) {
		LOGGER.debug("inside sendEmailForNotification");

		if (Objects.nonNull(mailAddresses)) {
			// Set the dynamic variables of email template
			final Map<String, String> emailParams = new HashMap<>();

			// these parameters are used in email template
			String emailSubject = SupportRequestFormBean.getPurposeOfContact() + " "
					+ SupportRequestFormBean.getCompanyLegalName();

			emailParams.put(FormConstants.NAME, SupportRequestFormBean.getName());
			emailParams.put(FormConstants.PURPOSE, SupportRequestFormBean.getPurposeOfContact());
			emailParams.put(SupplierPortalConstants.EMAILSUBJECT, emailSubject);
			emailParams.put(FormConstants.COUNTRY, SupportRequestFormBean.getCountry());
			emailParams.put(FormConstants.QUERYDESCRIPTION, SupportRequestFormBean.getHowHelp());
			emailParams.put(FormConstants.EMAIL, SupportRequestFormBean.getEmailAddress());
			emailParams.put(FormConstants.COMPANY_LEGALNAME, SupportRequestFormBean.getCompanyLegalName());
			emailParams.put(FormConstants.CITY, SupportRequestFormBean.getCity());
			emailParams.put(FormConstants.PHONE, SupportRequestFormBean.getOwnPhoneNumber());
			emailParams.put(FormConstants.ARIBANETWORKID, SupportRequestFormBean.getAribaNetworkId());
			emailParams.put(FormConstants.ARIBAEMAIL, SupportRequestFormBean.getAribaAccountAdminEmail());
			emailParams.put(FormConstants.TPEMAIL, SupportRequestFormBean.getTpContactEmail());
			final Map<String, Object> properties = new HashMap<>();
			properties.put("templatePath", SupplierPortalConstants.SUPPORT_REQUEST_FORM_MAIL_TEMPLATE_PATH);
			properties.put("emailParams", emailParams);
			properties.put("receipientsArray", mailAddresses);
			if (jobMgr != null) {
			//	jobMgr.addJob(PWConstants.SEND_EMAIL_JOB_TOPIC, properties);
			} else {
				LOGGER.error("JobManager Reference null");
			}
		}
	}
}
