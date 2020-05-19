package com.tetrapak.publicweb.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tetrapak.publicweb.core.beans.ContactUs;
import com.tetrapak.publicweb.core.beans.ContactUsResponse;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.FindMyOfficeService;

/**
 * The Class ContactUsSubmitRequestServlet.
 */
@Component(
        service = Servlet.class,
        property = { Constants.SERVICE_DESCRIPTION + "=Contact Us form Submit Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "publicweb/components/content/contactus" })
public class ContactUsSubmitRequestServlet extends SlingSafeMethodsServlet {
    /**
     *
     */
    private static final long serialVersionUID = 1114119284497481645L;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactUsSubmitRequestServlet.class);

    @Reference
    private FindMyOfficeService findMyOfficeService;

    @Reference
    private JobManager jobMgr;

    /**
     * Do get.
     *
     * @param request
     *            the request
     * @param resp
     *            the resp
     */
    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse resp) {
        try {

            final ContactUs contactUs = new ObjectMapper().readValue(request.getAttribute("inputJson").toString(),
                    ContactUs.class);
            final String[] mailAddresses = findMyOfficeService.fetchEmailAddresses(contactUs);

            // send email
            sendEmailForNotification(mailAddresses, contactUs);

            // form response
            final ContactUsResponse contactUsResponse = new ContactUsResponse();
            contactUsResponse.setStatusCode("200");
            contactUsResponse.setStatusMessage("Success");
            final ObjectMapper mapper = new ObjectMapper();
            resp.setContentType("text/html; charset=UTF-8");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(mapper.writeValueAsString(contactUsResponse));

        } catch (final IOException ioException) {
            LOGGER.error("ioException :{}", ioException.getMessage(), ioException);
        }
    }

    /**
     *
     * @param receipientsArray
     * @param contactUs
     */
    public void sendEmailForNotification(final String[] receipientsArray, final ContactUs contactUs) {
        LOGGER.debug("inside sendEmailForNotification");
        // Set the dynamic variables of email template
        final Map<String, String> emailParams = new HashMap<>();

        // these parameters are used in email template
        emailParams.put("firstName", contactUs.getFirstName());
        emailParams.put("lastName", contactUs.getLastName());
        emailParams.put("purposeOfContact", contactUs.getPurposeOfContact());
        emailParams.put("country", contactUs.getCountry());
        emailParams.put("message", contactUs.getMessage());
        emailParams.put("userEmail", contactUs.getEmail());

        if (jobMgr != null) {
            final Map<String, Object> properties = new HashMap<>();
            properties.put("templatePath", PWConstants.CONTACT_US_MAIL_TEMPLATE_PATH);
            properties.put("emailParams", emailParams);
            properties.put("receipientsArray", receipientsArray);
            jobMgr.addJob("com/tetrapak/publicweb/sendemail", properties);
        } else {
            LOGGER.error("JobManager Reference null");
        }
    }

}
