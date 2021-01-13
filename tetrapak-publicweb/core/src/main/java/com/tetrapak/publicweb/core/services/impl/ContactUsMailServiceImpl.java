package com.tetrapak.publicweb.core.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.acs.commons.email.EmailServiceConstants;
import com.tetrapak.publicweb.core.beans.ContactUs;
import com.tetrapak.publicweb.core.beans.ContactUsResponse;
import com.tetrapak.publicweb.core.constants.FormConstants;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.ContactUsMailService;

/**
 * The Class ContactUsMailServiceImpl.
 */
@Component(immediate = true, service = ContactUsMailService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class ContactUsMailServiceImpl implements ContactUsMailService {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactUsMailServiceImpl.class);

    /** The job mgr. */
    @Reference
    private JobManager jobMgr;

    /**
     * Send email for notification.
     *
     * @param contactUs
     *            the contact us
     * @param logo
     *            the logo
     * @param mailAddresses
     *            the mail addresses
     * @return the contact us response
     */
    @Override
    public ContactUsResponse sendEmailForNotification(final ContactUs contactUs, String logo,
            final String[] mailAddresses) {
        LOGGER.debug("inside sendEmailForNotification");

        if (Objects.nonNull(mailAddresses)) {
            // Set the dynamic variables of email template
            final Map<String, String> emailParams = new HashMap<>();

            // these parameters are used in email template
            String emailSubject = "Contact request from "+contactUs.getDomainURL()+" for ";
            emailSubject = emailSubject+contactUs.getCountryTitle()+", "+contactUs.getPurposeOfContactTitle()+".";
            
            emailParams.put(FormConstants.FIRST_NAME, contactUs.getFirstName());
            emailParams.put(FormConstants.LAST_NAME, contactUs.getLastName());
            emailParams.put(FormConstants.PURPOSE, contactUs.getPurposeOfContactTitle());
            emailParams.put(EmailServiceConstants.SUBJECT, emailSubject);
            emailParams.put(FormConstants.COUNTRY, contactUs.getCountryTitle());
            emailParams.put(FormConstants.MESSAGE, contactUs.getMessage());
            emailParams.put(FormConstants.EMAIL, contactUs.getEmail());
            emailParams.put(FormConstants.LOGO, logo);
            emailParams.put("domainURL", contactUs.getDomainURL());
            final Map<String, Object> properties = new HashMap<>();
            properties.put("templatePath", PWConstants.CONTACT_US_MAIL_TEMPLATE_PATH);
            properties.put("emailParams", emailParams);
            properties.put("receipientsArray", mailAddresses);
            if (jobMgr != null) {
                jobMgr.addJob(PWConstants.SEND_EMAIL_JOB_TOPIC, properties);
            } else {
                LOGGER.error("JobManager Reference null");
            }
        }
        return new ContactUsResponse("200", "Success");
    }
}