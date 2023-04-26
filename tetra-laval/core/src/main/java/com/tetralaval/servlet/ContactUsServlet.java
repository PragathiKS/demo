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
import com.tetralaval.utils.ServletUtils;

/**
 * The Class ContactUsServlet.
 */
@Component(service = Servlet.class, property = {
	Constants.SERVICE_DESCRIPTION + "=Tetra Laval Contact Us form Submit Servlet",
	"sling.servlet.methods=" + HttpConstants.METHOD_POST, "sling.servlet.selectors=" + "mail",
	"sling.servlet.extensions=" + "html",
	"sling.servlet.resourceTypes=" + "tetra-laval/components/content/form/container" })
public class ContactUsServlet extends SlingAllMethodsServlet {

    /**
     * The Constant for Content in Email/Contact Us Content Fragment .
     */
    private static final String EMAIL_CONTENT = "emailContent";

    /**
     * The Constant for Logo Path in Email Parameters.
     */
    private static final String EMAIL_LOGO_PATH = "logoPath";

    /**
     * The Constant for Content in Email Parameters.
     */
    private static final String EMAIL_PARAM_CONTENT = "content";

    /**
     * The Constant for Logo in Email Parameters.
     */
    private static final String EMAIL_PARAM_LOGO = "logo";

    /**
     * The Constant for Title in Email Parameters.
     */
    private static final String EMAIL_PARAM_TITLE = "title";

    /**
     * The Constant for Company in Email/Contact Us Content Fragment .
     */
    private static final String EMAIL_SELECTED_COMPANY = "selectedCompany";

    /**
     * The Constant for Title in Email/Contact Us Content Fragment .
     */
    private static final String EMAIL_TITLE = "emailTitle";

    /**
     * The Constant for Email in Email/Contact Us Content Fragment .
     */
    private static final String EMAIL_TO = "email";

    /**
     * The Constant forward slash.
     */
    public static final String FORWARD_SLASH = "/";

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactUsServlet.class);

    /**
     * The Constant for Request Parameter Company
     * 
     */
    private static final String PARAM_COMPANY = ":company";

    /**
     * The Constant for Request Parameter Redirect
     */
    private static final String PARAM_REDIRECT = ":redirect";

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -87202123215146767L;

    /**
     * The Constant for Sending 200 OK Response
     * 
     */
    private static final String STATUS_OK = "OK";

    /**
     * The Constant Status Type Message.
     */
    private static final String STATUS_TYPE_MESSAGE = "message";

    /**
     * The Constant Status Type Redirect.
     */
    private static final String STATUS_TYPE_REDIRECT = "redirect";

    /**
     * The Constant Thank You Type Message.
     */
    private static final String THANKYOU_TYPE_REDIRECT = "showThankYouPage";

    /**
     * The EMail service.
     */
    @Reference
    private EmailService emailService;

    @Reference
    private FormService formService;

    /**
     * The job mgr.
     */
    @Reference
    private JobManager jobMgr;

    /**
     * The xss API.
     */
    @Reference
    private XSSAPI xssAPI;

    /**
     * Do Post.
     *
     * @param request the request
     * @param resp    the response
     */
    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response) {
	LOGGER.debug("Insdie doPost");
	ContactUsResponse contactUsResponse = null;
	int statusCode = HttpServletResponse.SC_OK;
	String type = STATUS_TYPE_MESSAGE;
	String redirect = null;
	String emailTemplatePath = TLConstants.CONTACT_US_MAIL_TEMPLATE_PATH;
	String[] to = null;
	Map<String, String> emailParams = new HashMap<>();
	try {
	    ResourceResolver resourceResolver = request.getResourceResolver();
	    emailParams = ServletUtils.processInputParameters(request, xssAPI, formService.getIgnoreParameters());
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
		    redirect = request.getParameter(PARAM_REDIRECT);
		    redirect = resourceResolver.map(redirect);
		}

		/* get company details */
		String company = request.getParameter(PARAM_COMPANY);
		if (StringUtils.isBlank(company)) {
		    statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		    throw new TetraLavalException("Selected Company not provided");
		} else {
		    to = processContentFragment(to, emailParams, resourceResolver, company);
		}
	    }
	    contactUsResponse = new ContactUsResponse(statusCode, STATUS_OK, type, redirect);
	    sendEmail(emailParams, to, emailTemplatePath);
	} catch (TetraLavalException te) {
	    LOGGER.error("Exception :{}", te);
	    contactUsResponse = new ContactUsResponse(statusCode, te.getMessage());
	} catch (final Exception e) {
	    LOGGER.error("Exception :{}", e);
	    contactUsResponse = new ContactUsResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error");
	}
	sendResponse(response, contactUsResponse);

    }

    /**
     * Reads the data from contact us content fragment
     * 
     * @param to
     * @param emailParams
     * @param resourceResolver
     * @param company
     * @return list of recipients
     */
    private String[] processContentFragment(String[] to, Map<String, String> emailParams,
	    ResourceResolver resourceResolver, String company) {
	String resourcePath = formService.getContactUsFragmentsPath() + FORWARD_SLASH + company;
	Resource resource = resourceResolver.getResource(resourcePath);
	String title = null;
	String content = null;
	String logoPath = null;
	if (null != resource) {
	    ContentFragment contentFragment = resource.adaptTo(ContentFragment.class);
	    if (null != contentFragment) {
		emailParams.put(EMAIL_SELECTED_COMPANY, contentFragment.getTitle());
		if (contentFragment.hasElement(EMAIL_TO)) {
		    to = contentFragment.getElement(EMAIL_TO).getValue().getValue(String[].class);
		}
		if (contentFragment.hasElement(EMAIL_TITLE)) {
		    title = contentFragment.getElement(EMAIL_TITLE).getValue().getValue(String.class);
		}
		emailParams.put(EMAIL_PARAM_TITLE, title);

		if (contentFragment.hasElement(EMAIL_CONTENT)) {
		    content = contentFragment.getElement(EMAIL_CONTENT).getValue().getValue(String.class);
		}
		emailParams.put(EMAIL_PARAM_CONTENT, content);

		if (contentFragment.hasElement(EMAIL_LOGO_PATH)) {
		    logoPath = contentFragment.getElement(EMAIL_LOGO_PATH).getValue().getValue(String.class);
		}
		emailParams.put(EMAIL_PARAM_LOGO, logoPath);
	    }
	}
	return to;
    }

    /**
     * Creates a Sling Job that sends email
     * 
     * @param emailParams
     * @param recipients
     * @param templatePath
     */
    private void sendEmail(Map<String, String> emailParams, String[] recipients, String templatePath) {
	final Map<String, Object> properties = new HashMap<>();
	properties.put(TLConstants.JOB_TEMPLATE_PATH, templatePath);
	properties.put(TLConstants.JOB_EMAIL_PARAMS, emailParams);
	properties.put(TLConstants.JOB_RECIPIENTS, recipients);
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
    private void sendResponse(final SlingHttpServletResponse resp, final ContactUsResponse contactUsResponse) {
	try {
	    final ObjectMapper mapper = new ObjectMapper();
	    resp.setStatus(contactUsResponse.getStatusCode());
	    resp.setContentType(TLConstants.HTML_CONTENT_TYPE);
	    resp.setCharacterEncoding(TLConstants.UTF_8);
	    resp.getWriter().write(mapper.writeValueAsString(contactUsResponse));
	} catch (IOException e) {
	    LOGGER.error("IOException:", e);
	}
    }
}
