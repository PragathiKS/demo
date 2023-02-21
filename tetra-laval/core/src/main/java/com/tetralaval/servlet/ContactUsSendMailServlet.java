package com.tetralaval.servlet;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.xss.XSSAPI;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tetralaval.beans.ContactUsResponse;

/**
 * The Class ContactUsSubmitRequestServlet.
 */
@Component(service = Servlet.class, property = {
	Constants.SERVICE_DESCRIPTION + "=Tetra Laval Contact Us form Submit Servlet",
	"sling.servlet.methods=" + HttpConstants.METHOD_POST, "sling.servlet.selectors=" + "mail",
	"sling.servlet.extensions=" + "html",
	"sling.servlet.resourceTypes=" + "tetra-laval/components/content/form/container" })
public class ContactUsSendMailServlet extends SlingAllMethodsServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -87202123215146767L;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactUsSendMailServlet.class);

    /** The contact us mail service. */

    /** The xss API. */
    @Reference
    protected transient XSSAPI xssAPI;

    /**
     * Do get.
     *
     * @param request the request
     * @param resp    the resp
     */
    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response) {
	ContactUsResponse contactUsResponse = new ContactUsResponse("500", "Server Error");
	try {
	    final String mailAddresses = request.getParameter("email");
	    String redirect = request.getParameter(":redirect");
	    if(StringUtils.isBlank(redirect)) {
		redirect = request.getHeader("Referrer");
	    }
	    contactUsResponse = new ContactUsResponse("200", "OK");
	    //sendResponse(response, contactUsResponse);
	    
	    redirect = redirect+"?status=200";
	    response.sendRedirect(redirect);

	} catch (final Exception e) {
	    LOGGER.error("Exception :{}", e.getMessage(), e);
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
	resp.setContentType("text/html; charset=UTF-8");
	resp.setCharacterEncoding("UTF-8");
	resp.getWriter().write(mapper.writeValueAsString(contactUsResponse));
    }
}
