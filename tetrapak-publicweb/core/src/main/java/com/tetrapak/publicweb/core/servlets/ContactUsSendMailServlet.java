package com.tetrapak.publicweb.core.servlets;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.Servlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.xss.XSSAPI;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tetrapak.publicweb.core.beans.ContactUs;
import com.tetrapak.publicweb.core.beans.ContactUsResponse;
import com.tetrapak.publicweb.core.models.HeaderConfigurationModel;
import com.tetrapak.publicweb.core.services.ContactUsMailService;
import com.tetrapak.publicweb.core.services.CountryDetailService;
import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * The Class ContactUsSubmitRequestServlet.
 */
@Component(
        service = Servlet.class,
        property = { Constants.SERVICE_DESCRIPTION + "=Contact Us form Submit Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_POST, "sling.servlet.selectors=" + "sendmail",
                "sling.servlet.extensions=" + "json",
                "sling.servlet.resourceTypes=" + "publicweb/components/content/contactus" })
public class ContactUsSendMailServlet extends SlingAllMethodsServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4582610735374949058L;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactUsSendMailServlet.class);

    /** The contact us mail service. */
    @Reference
    private ContactUsMailService contactUsMailService;

    /** The country detail service. */
    @Reference
    private CountryDetailService countryDetailService;

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
            String inputJson = request.getParameter("inputJson");
            final XSSAPI xssapi = request.adaptTo(XSSAPI.class);
            inputJson = xssapi.getValidJSON(inputJson, "");
            LOGGER.debug("Contact us servlet InputJson :: {}", inputJson);
            if (!StringUtils.isEmpty(inputJson)) {
                final ContactUs contactUs = new ObjectMapper().readValue(inputJson, ContactUs.class);
                if (validateRequest(contactUs)) {
                    final String[] mailAddresses = countryDetailService.fetchContactEmailAddresses(contactUs,
                            request.getResourceResolver());
                    // send email
                    contactusResp = contactUsMailService.sendEmailForNotification(contactUs, getLogo(request),
                            mailAddresses);
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
     * Validate if all mandatory fields are present in request.
     *
     * @param contactUs
     *            the contact us
     * @return true, if successful
     */
    private boolean validateRequest(final ContactUs contactUs) {
        return !(StringUtils.isEmpty(contactUs.getCountry()) || StringUtils.isEmpty(contactUs.getEmail())
                || StringUtils.isEmpty(contactUs.getFirstName()) || StringUtils.isEmpty(contactUs.getLastName())
                || StringUtils.isEmpty(contactUs.getPurposeOfContact()) || StringUtils.isEmpty(contactUs.getMessage()));
    }

    /**
     * Sends HTTPServlet response.
     *
     * @param resp
     *            the resp
     * @param contactUsResponse
     *            the contact us response
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void sendResponse(final SlingHttpServletResponse resp, final ContactUsResponse contactUsResponse)
            throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(mapper.writeValueAsString(contactUsResponse));
    }

    /**
     * Gets the logo.
     *
     * @param request
     *            the request
     * @return the logo
     */
    private String getLogo(final SlingHttpServletRequest request) {
        String rootPath = LinkUtils.getRootPath(request.getPathInfo());
        final String path = rootPath + "/jcr:content/root/responsivegrid/headerconfiguration";
        final Resource headerConfigurationResource = request.getResourceResolver().getResource(path);
        if (Objects.nonNull(headerConfigurationResource)) {
            final HeaderConfigurationModel configurationModel = headerConfigurationResource
                    .adaptTo(HeaderConfigurationModel.class);
            if (Objects.nonNull(configurationModel)) {
                return configurationModel.getLogoImagePath();
            }
        }
        return StringUtils.EMPTY;
    }

}
