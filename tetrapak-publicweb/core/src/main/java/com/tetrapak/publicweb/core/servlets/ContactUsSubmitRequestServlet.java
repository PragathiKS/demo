package com.tetrapak.publicweb.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
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
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.paths=" + "/bin/tetrapak/pw-contactus" })
public class ContactUsSubmitRequestServlet extends SlingAllMethodsServlet {

    /**
     *
     */
    private static final long serialVersionUID = -4582610735374949058L;

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
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse resp) {
        LOGGER.debug("calling servlet");
        ContactUsResponse contactusResp = new ContactUsResponse();
        try {
            final String inputJson = request.getParameter("inputJson");
            LOGGER.debug("inPutJson :: {}", inputJson);
            final ContactUs contactUs = new ObjectMapper().readValue(inputJson,
                    ContactUs.class);

            if (validateRequest(contactUs)) {
                // send email
                contactusResp = sendEmailForNotification(contactUs, request);
            } else {
                contactusResp.setStatusCode("500");
                contactusResp.setStatusMessage("Validation Error");
            }

            // send response
            sendResponse(resp, contactusResp);

        } catch (final IOException ioException) {
            LOGGER.error("ioException :{}", ioException.getMessage(), ioException);
        }
    }

    /**
     *
     * @param contactUs
     * @return
     */
    private boolean validateRequest(final ContactUs contactUs) {
        return !StringUtils.isEmpty(contactUs.getCountry());
    }

    /**
     * @param resp
     * @param contactUsResponse
     * @throws IOException
     * @throws JsonProcessingException
     */
    private void sendResponse(final SlingHttpServletResponse resp, final ContactUsResponse contactUsResponse)
            throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(mapper.writeValueAsString(contactUsResponse));
    }

    /**
     *
     * @param receipientsArray
     * @param contactUs
     * @param request
     * @return
     */
    private ContactUsResponse sendEmailForNotification(final ContactUs contactUs,
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
            jobMgr.addJob("com/tetrapak/publicweb/sendemail", properties);
        } else {
            LOGGER.error("JobManager Reference null");
        }

        final ContactUsResponse contactUsResponse = new ContactUsResponse();
        contactUsResponse.setStatusCode("200");
        contactUsResponse.setStatusMessage("Success");

        return contactUsResponse;
    }

}
