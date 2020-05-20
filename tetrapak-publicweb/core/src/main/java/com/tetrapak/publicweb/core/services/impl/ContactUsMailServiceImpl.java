package com.tetrapak.publicweb.core.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.publicweb.core.beans.ContactUs;
import com.tetrapak.publicweb.core.beans.ContactUsResponse;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.ContactUsMailService;
import com.tetrapak.publicweb.core.services.FindMyOfficeService;

@Component(
        immediate = true,
        service = ContactUsMailService.class,
        configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = ContactUsMailServiceImpl.ContactUsMailServiceConfig.class)
public class ContactUsMailServiceImpl implements ContactUsMailService {

    /**
     * The Interface ContactUsMailServiceConfig.
     */
    @ObjectClassDefinition(
            name = "Contact us service configuration",
            description = "Contact us service configuration")
    @interface ContactUsMailServiceConfig {

        @AttributeDefinition(
                name = "Server URL",
                description = "server URL")
        String[] getServerURL() default "www.tetrapak.com";

    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactUsMailServiceImpl.class);

    @Reference
    private JobManager jobMgr;

    @Reference
    private FindMyOfficeService findMyOfficeService;

    @Override
    public ContactUsResponse sendEmailForNotification(final ContactUs contactUs,
            final SlingHttpServletRequest request) {
        LOGGER.debug("inside sendEmailForNotification");

        final String[] mailAddresses = findMyOfficeService.fetchContactEmailAddresses(contactUs,
                request.getResourceResolver());

        // Set the dynamic variables of email template
        final Map<String, String> emailParams = new HashMap<>();

        // these parameters are used in email template
        emailParams.put("firstName", contactUs.getFirstName());
        emailParams.put("lastName", contactUs.getLastName());
        emailParams.put("purpose", contactUs.getPurposeOfContactTitle());
        emailParams.put("country", contactUs.getCountryTitle());
        emailParams.put("message", contactUs.getMessage());
        emailParams.put("email", contactUs.getEmail());

        if (jobMgr != null) {
            final Map<String, Object> properties = new HashMap<>();
            properties.put("templatePath", PWConstants.CONTACT_US_MAIL_TEMPLATE_PATH);
            properties.put("emailParams", emailParams);
            properties.put("receipientsArray", mailAddresses);
            jobMgr.addJob(PWConstants.SEND_EMAIL_JOB_TOPIC, properties);
        } else {
            LOGGER.error("JobManager Reference null");
        }

        return new ContactUsResponse("200", "Success");

    }


}
