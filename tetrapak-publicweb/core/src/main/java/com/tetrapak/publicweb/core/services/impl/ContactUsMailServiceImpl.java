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

import com.tetrapak.publicweb.core.beans.ContactUs;
import com.tetrapak.publicweb.core.beans.ContactUsResponse;
import com.tetrapak.publicweb.core.constants.FormConstants;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.ContactUsMailService;

@Component(immediate = true, service = ContactUsMailService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class ContactUsMailServiceImpl implements ContactUsMailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactUsMailServiceImpl.class);

    @Reference
    private JobManager jobMgr;

    @Override
    public ContactUsResponse sendEmailForNotification(final ContactUs contactUs, final String[] mailAddresses) {
        LOGGER.debug("inside sendEmailForNotification");

        if (Objects.nonNull(mailAddresses)) {
            // Set the dynamic variables of email template
            final Map<String, String> emailParams = new HashMap<>();

            // these parameters are used in email template

            emailParams.put(FormConstants.FIRST_NAME, contactUs.getFirstName());
            emailParams.put(FormConstants.LAST_NAME, contactUs.getLastName());
            emailParams.put(FormConstants.PURPOSE, contactUs.getPurposeOfContactTitle());
            emailParams.put(FormConstants.COUNTRY, contactUs.getCountryTitle());
            emailParams.put(FormConstants.MESSAGE, contactUs.getMessage());
            emailParams.put(FormConstants.EMAIL, contactUs.getEmail());
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
