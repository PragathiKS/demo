package com.tetrapak.publicweb.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
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
		Constants.SERVICE_DESCRIPTION + "=Tetra Pak - Public Web Soft Conversion Form service",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.paths=" + "/bin/tetrapak/pw-softconversion" })
public class SoftConversionFormServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(SoftConversionFormServlet.class);

	@Reference
	private ResourceResolverFactory resolverFactory;

	private ResourceResolver resourceResolver;
	
	private String UGC_CONTENT_PATH = "/content/usergenerated";

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		try {
			// get resource resolver, tagManager objects.
			resourceResolver = getResourceResolver(request);
			Session session = resourceResolver.adaptTo(Session.class);
			Node node = resourceResolver.adaptTo(Node.class);
			Map<String, String> subCategoryTagsMap = new HashMap<>();

			String group = request.getParameter("group");
			String firstName = request.getParameter("first-name");
			log.info("** Here is the first name : " + firstName);
			String lastName = request.getParameter("last-name");
			String emailAddress = request.getParameter("email-address");
			String company = request.getParameter("company");
			String position = request.getParameter("position");
			
			Node rootNode = session.getNode(UGC_CONTENT_PATH);
			Node pwNode = JcrUtils.getOrAddNode(rootNode, "terapak-publicweb");
			Node itemNode = JcrUtils.getOrAddNode(pwNode, emailAddress);
			itemNode.setProperty("group", group);
			itemNode.setProperty("firstName", firstName);
			itemNode.setProperty("lastName", lastName);
			itemNode.setProperty("emailAddress", emailAddress);
			itemNode.setProperty("company", company);
			itemNode.setProperty("position", position);
			
			session.save();
			
			Cookie cookie = new Cookie("softConvUserExists","true");
			cookie.setPath("/");
			cookie.setMaxAge(60 * 60 * 24);
			response.addCookie(cookie);
			
			// set the response type
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			PrintWriter writer = response.getWriter();
			writer.println("Thanks for submitting the form.");
			writer.flush();
			writer.close();

		} catch (IOException e) {
			log.error("Error occurred while writing the response object. {}", e);
		} catch (PathNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (RepositoryException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private ResourceResolver getResourceResolver(SlingHttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(ResourceResolverFactory.SUBSERVICE, "writeService");
		ResourceResolver resourceResolver = null;
		try {
			resourceResolver = resolverFactory.getServiceResourceResolver(param);
		} catch (LoginException e1) {
			log.error("[Error getting the resource resolver");
			resourceResolver = request.getResourceResolver();
		}
		return resourceResolver;
	}
}