package com.tetrapak.publicweb.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;

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

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.google.gson.Gson;

/**
 * This is the servlet that is triggered to get the sub categories from a given
 * tag path.
 * 
 * @author abhbhatn
 *
 */
@Component(service = Servlet.class, property = {
		Constants.SERVICE_DESCRIPTION + "=Tetra Pak - Public Web Sub Category Tag service",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.paths=" + "/bin/tetrapak/pw-subcategorytag" })
public class SubCategoryTagServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(SubCategoryTagServlet.class);

	@Reference
	private ResourceResolverFactory resolverFactory;

	private ResourceResolver resourceResolver;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		try {
			// get resource resolver, tagManager objects.
			resourceResolver = getResourceResolver(request);
			TagManager tagManager = resourceResolver.adaptTo(TagManager.class);

			Map<String, String> subCategoryTagsMap = new HashMap<>();

			if (tagManager != null) {
				String categoryTagId = request.getParameter("categoryTag");
				Tag categoryTag = tagManager.resolve(categoryTagId);
				log.info("Category Tag path : {}", categoryTag.getPath());

				Iterator<Tag> subCategoryTags = categoryTag.listChildren();
				if (subCategoryTags != null) {
					while (subCategoryTags.hasNext()) {
						Tag subCategTag = subCategoryTags.next();
						log.info("Sub Category tag : {}", subCategTag);
						String tagTitle = subCategTag.getTitle();
						subCategoryTagsMap.put(tagTitle, subCategTag.getTagID());
					}
				}
			}
			
			Gson gson = new Gson(); 
			String json = gson.toJson(subCategoryTagsMap); 

			// set the response type
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_OK);
			PrintWriter writer = response.getWriter();
			writer.println(json);
			writer.flush();
			writer.close();

		} catch (IOException e) {
			log.error("Error occurred while writing the response object. {}", e);
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
