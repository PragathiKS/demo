package com.tetralaval.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.InternetAddress;
import javax.servlet.Servlet;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.acs.commons.email.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tetralaval.beans.ContactUsResponse;
import com.tetralaval.models.FormContainer;

/**
 * The Class ContactUsSubmitRequestServlet.
 */
@Component(service = Servlet.class, property = {
	Constants.SERVICE_DESCRIPTION + "=Tetra Laval Contact Us form Submit Servlet",
	"sling.servlet.methods=" + HttpConstants.METHOD_POST, "sling.servlet.selectors=" + "mail",
	"sling.servlet.extensions=" + "html",
	"sling.servlet.resourceTypes=" + "tetra-laval/components/content/form/container" })
public class ContactUsServlet extends SlingAllMethodsServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -87202123215146767L;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactUsServlet.class);

    /** The EMail service. */
    @Reference
    private EmailService emailService;

    /** The Constant CONTACT_US_MAIL_TEMPLATE_PATH. */
    private final String CONTACT_US_MAIL_TEMPLATE_PATH = "/etc/notification/email/tetralaval/contactus/email.html";

    /**
     * Do get.
     *
     * @param request the request
     * @param resp    the resp
     */
    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response) {
	LOGGER.debug("Insdie doPost");
	ContactUsResponse contactUsResponse = new ContactUsResponse("500", "Server Error");
	try {
	    Map<String, String[]> requestParams = request.getParameterMap();
	    Map<String, String> emailParams = new HashMap<>();
	    requestParams.forEach((key, value) -> {
		LOGGER.debug("Key: {} ::: Value: {}", key, value);
		String newValue = "";
		if (value.length > 1) {
		    newValue = StringUtils.join(value, ",");
		} else {
		    newValue = value[0];
		}
		emailParams.put(key, newValue);
	    });
	    // get details configured on Form Component.
	    Resource formResource = request.getResource();
	    LOGGER.debug("Form Resource: {}", formResource);
	    String emailTemplatePath = CONTACT_US_MAIL_TEMPLATE_PATH;
	    String to = "";
	    if (null != formResource) {

		FormContainer form = formResource.adaptTo(FormContainer.class);
		if (StringUtils.isNotBlank(form.getEmailTemplate())) {
		    emailTemplatePath = form.getEmailTemplate();
		    LOGGER.debug("Form Resource emailTemplatePath: {}", emailTemplatePath);
		}
		if (StringUtils.isNotBlank(form.getTo())) {
		    to = form.getTo();
		    LOGGER.debug("Form Resource To: {}", to);
		}
		String subject = form.getSubject();
		emailParams.put("Subject", subject);
		LOGGER.debug("Form Resource Subject: {}", subject);
		sendEmail(emailParams, new InternetAddress(to), emailTemplatePath);
	    }

	    String redirect = request.getParameter(":redirect");
	    LOGGER.debug("Redirect from Request: {}", redirect);
	    if (StringUtils.isBlank(redirect)) {
		redirect = request.getHeader("Referrer");
		LOGGER.debug("Redirect from Referrer Header as Redirect is not provided: {}", redirect);
	    }
	    contactUsResponse = new ContactUsResponse("200", "OK");
	    sendResponse(response, contactUsResponse);

	   /* redirect = redirect + "?status=200";
	    LOGGER.debug("Redirect....");
	    response.sendRedirect(redirect);*/

	} catch (final Exception e) {
	    LOGGER.error("Exception :{}", e.getMessage(), e);
	}

    }

    private void sendEmail(Map<String, String> emailParams, InternetAddress recipients, String templatePath) {
	emailService.sendEmail(templatePath, emailParams, recipients);

    }

    /**
     * Sends HTTPServlet response.
     *
     * @param resp              the resp
     * @param contactUsResponse the contact us response
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void sendResponse(final SlingHttpServletResponse resp, final ContactUsResponse contactUsResponse)
	    throws IOException {
	final ObjectMapper mapper = new ObjectMapper();
	resp.setContentType("text/html; charset=UTF-8");
	resp.setCharacterEncoding("UTF-8");
	resp.getWriter().write(mapper.writeValueAsString(contactUsResponse));
    }
}
