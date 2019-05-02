package com.tetrapak.publicweb.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the servlet that is triggered to get the sub categories from a given
 * tag path.
 * 
 * @author abhbhatn
 *
 */
@Component(service = Servlet.class, property = {
		Constants.SERVICE_DESCRIPTION + "=Tetra Pak - Public Web Contact Footer Form service",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.paths=" + "/bin/tetrapak/pw-contactfooter" })
public class ContactFooterFormSevlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(ContactFooterFormSevlet.class);

	@Reference
	private ResourceResolverFactory resolverFactory;

	private ResourceResolver resourceResolver;

	private String UGC_CONTENT_PATH = "/content/usergenerated";

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		log.info("Inside doGet() method");
		try {
			// get resource resolver, session objects.
			resourceResolver = request.getResourceResolver();
			Session session = resourceResolver.adaptTo(Session.class);

			String firstName = request.getParameter("first-name");
			String lastName = request.getParameter("last-name");
			String phoneNumber = request.getParameter("phone-number");
			String emailAddress = request.getParameter("email-address");			
			String position = request.getParameter("position");
			String company = request.getParameter("company");
			String contactIn = request.getParameter("contact-in");

			if (session != null) {
				log.info("Session not null");
				Node rootNode = session.getNode(UGC_CONTENT_PATH);
				Node pwNode = JcrUtils.getOrAddNode(rootNode, "terapak-publicweb");
				Node contactDetailsNode = JcrUtils.getOrAddNode(pwNode, "contact-details");
				Node itemNode = JcrUtils.getOrAddNode(contactDetailsNode, emailAddress);
				itemNode.setProperty("firstName", firstName);
				itemNode.setProperty("lastName", lastName);
				itemNode.setProperty("phoneNumber", phoneNumber);
				itemNode.setProperty("emailAddress", emailAddress);
				itemNode.setProperty("position", position);
				itemNode.setProperty("company", company);
				itemNode.setProperty("contactIn", contactIn);

				session.save();
			}

			// set the response type
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			PrintWriter writer = response.getWriter();
			writer.flush();
			writer.close();

		} catch (IOException e) {
			log.error("Error occurred while writing the response object. {}", e);
		} catch (PathNotFoundException e1) {
			log.error("Error finding the path. {}", e1);
		} catch (RepositoryException e2) {
			log.error("Error in repository operation. {}", e2);
		}

	}
	
}