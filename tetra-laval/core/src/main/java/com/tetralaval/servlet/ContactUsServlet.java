package com.tetralaval.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.xss.XSSAPI;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.acs.commons.email.EmailService;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tetralaval.beans.ContactUsResponse;
import com.tetralaval.constants.TLConstants;
import com.tetralaval.exceptions.TetraLavalException;
import com.tetralaval.models.FormContainer;
import com.tetralaval.services.FormService;

/**
 * The Class ContactUsServlet.
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
    private static final String CONTACT_US_MAIL_TEMPLATE_PATH = "/etc/notification/email/tetralaval/contactus/email.html";

    /** The Constant Status Type Message. */
    private static final String STATUS_TYPE_MESSAGE = "message";

    /** The Constant Status Type Redirect. */
    private static final String STATUS_TYPE_REDIRECT = "redirect";

    /** The Constant Thank You Type Message. */
    private static final String THANKYOU_TYPE_REDIRECT = "showThankYouPage";

    @Reference
    private FormService formService;

    /** The xss API. */
    @Reference
    private XSSAPI xssAPI;

    /** The job mgr. */
    @Reference
    private JobManager jobMgr;

    /**
     * Do get.
     *
     * @param request the request
     * @param resp    the resp
     */
    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response) {
	LOGGER.debug("Insdie doPost");
	ContactUsResponse contactUsResponse = null;
	int statusCode = HttpServletResponse.SC_OK;
	String type = STATUS_TYPE_MESSAGE;
	String redirect = null;
	String emailTemplatePath = CONTACT_US_MAIL_TEMPLATE_PATH;
	String[] to = null;
	Map<String, String> emailParams = new HashMap<>();
	try {
	    ResourceResolver resourceResolver = request.getResourceResolver();
	    emailParams = processInputParameters(request);
	    // get details configured on Form Component.
	    Resource formResource = request.getResource();
	    LOGGER.debug("Form Resource: {}", formResource);

	    if (null != formResource) {

		FormContainer form = formResource.adaptTo(FormContainer.class);
		if (StringUtils.isNotBlank(form.getEmailTemplate())) {
		    emailTemplatePath = form.getEmailTemplate();
		    LOGGER.debug("Form Resource emailTemplatePath: {}", emailTemplatePath);
		}
		if (form.getThankYouType().equals(THANKYOU_TYPE_REDIRECT)) {
		    type = STATUS_TYPE_REDIRECT;
		    redirect = request.getParameter(":redirect");
		}

		/* get company details */
		String company = request.getParameter(":company");
		if (StringUtils.isBlank(company)) {
		    statusCode = HttpServletResponse.SC_BAD_REQUEST;
		    throw new TetraLavalException("Selected Company not provided");
		} else {
		    to = processContentFragment(to, emailParams, resourceResolver, company);
		}
	    }
	    contactUsResponse = new ContactUsResponse(statusCode, "OK", type, redirect);
	    sendEmail(emailParams, to, emailTemplatePath);
	} catch (TetraLavalException te) {
	    contactUsResponse = new ContactUsResponse(statusCode, te.getMessage());
	} catch (final Exception e) {
	    LOGGER.error("Exception :{}", e);
	    contactUsResponse = new ContactUsResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error");
	}

	try {
	    sendResponse(response, contactUsResponse);
	} catch (Exception e) {
	    LOGGER.error("Exception:", e);
	}

    }

    private String[] processContentFragment(String[] to, Map<String, String> emailParams,
	    ResourceResolver resourceResolver, String company) {
	String resourcePath = formService.getContactUsFragmentsPath() + "/" + company;
	Resource resource = resourceResolver.getResource(resourcePath);
	if (null != resource) {
	    ContentFragment contentFragment = resource.adaptTo(ContentFragment.class);
	    if (null != contentFragment) {
		emailParams.put("selectedCompany", contentFragment.getTitle());
		to = contentFragment.hasElement("email")
			? contentFragment.getElement("email").getValue().getValue(String[].class)
			: null;
		String title = contentFragment.hasElement("emailTitle")
			? contentFragment.getElement("emailTitle").getValue().getValue(String.class)
			: null;
		emailParams.put("title", title);
		String content = contentFragment.hasElement("emailContent")
			? contentFragment.getElement("emailContent").getValue().getValue(String.class)
			: null;
		emailParams.put("content", content);
		String logoPath = contentFragment.hasElement("logoPath")
			? contentFragment.getElement("logoPath").getValue().getValue(String.class)
			: null;
		emailParams.put("logo", logoPath);
	    }
	}
	return to;
    }

    private Map<String, String> processInputParameters(final SlingHttpServletRequest request) {
	Map<String, String> emailParams = new HashMap<>();
	Map<String, String[]> requestParams = request.getParameterMap();
	requestParams.forEach((key, value) -> {
	    LOGGER.debug("Key: {} ::: Value: {}", key, value);
	    if (!StringUtils.startsWithAny(key, formService.getIgnoreParameters())) {
		String newValue = "";
		if (value.length > 1) {
		    newValue = StringUtils.join(value, ",");
		} else {
		    newValue = value[0];
		}
		newValue = xssAPI.encodeForHTML(newValue);
		emailParams.put(key, newValue);
	    }
	});
	return emailParams;
    }

    private void sendEmail(Map<String, String> emailParams, String[] recipients, String templatePath) {
	final Map<String, Object> properties = new HashMap<>();
	properties.put("templatePath", templatePath);
	properties.put("emailParams", emailParams);
	properties.put("receipients", recipients);
	if (jobMgr != null) {
	    LOGGER.debug("Starting JOB: {}", TLConstants.CONTACTUS_EMAIL_JOB);
	    jobMgr.addJob(TLConstants.CONTACTUS_EMAIL_JOB, properties);
	} else {
	    LOGGER.error("JobManager Reference null");
	}

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
	resp.setStatus(contactUsResponse.getStatusCode());
	resp.setContentType("text/html; charset=UTF-8");
	resp.setCharacterEncoding("UTF-8");
	resp.getWriter().write(mapper.writeValueAsString(contactUsResponse));
    }
}
