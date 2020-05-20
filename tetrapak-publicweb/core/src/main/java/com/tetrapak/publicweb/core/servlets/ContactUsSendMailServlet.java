package com.tetrapak.publicweb.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tetrapak.publicweb.core.beans.ContactUs;
import com.tetrapak.publicweb.core.beans.ContactUsResponse;
import com.tetrapak.publicweb.core.services.ContactUsMailService;

/**
 * The Class ContactUsSubmitRequestServlet.
 */
@Component(
        service = Servlet.class,
        property = { Constants.SERVICE_DESCRIPTION + "=Contact Us form Submit Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.paths=" + "/bin/tetrapak/pw-contactus" })
public class ContactUsSendMailServlet extends SlingAllMethodsServlet {

    /**
     *
     */
    private static final long serialVersionUID = -4582610735374949058L;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactUsSendMailServlet.class);

    @Reference
    private ContactUsMailService contactUsMailService;

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
        ContactUsResponse contactusResp = new ContactUsResponse("500", "Server Error");
        try {
            final String inputJson = request.getParameter("inputJson");
            LOGGER.debug("Contact us servlet InputJson :: {}", inputJson);
            if (!StringUtils.isEmpty(inputJson)) {
                final ContactUs contactUs = new ObjectMapper().readValue(inputJson, ContactUs.class);
                if (validateRequest(contactUs)) {
                    // send email
                    contactusResp = contactUsMailService.sendEmailForNotification(contactUs, request);
                } else {
                    contactusResp.setStatusMessage("Mandatory fields Validation Error");
                }
            } else {
                contactusResp.setStatusMessage("Invalid Input Json");
            }

            // send response
            sendResponse(resp, contactusResp);

        } catch (final IOException ioException) {
            LOGGER.error("ioException :{}", ioException.getMessage(), ioException);
        }
    }

    /**
     * Validate if all mandatory fields are present in request
     *
     * @param contactUs
     * @return
     */
    private boolean validateRequest(final ContactUs contactUs) {
        return !(StringUtils.isEmpty(contactUs.getCountry()) || StringUtils.isEmpty(contactUs.getEmail())
                || StringUtils.isEmpty(contactUs.getFirstName()) || StringUtils.isEmpty(contactUs.getLastName())
                || StringUtils.isEmpty(contactUs.getPurposeOfContact()) || StringUtils.isEmpty(contactUs.getMessage()));
    }

    /**
     * Sends HTTPServlet response
     *
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


}
