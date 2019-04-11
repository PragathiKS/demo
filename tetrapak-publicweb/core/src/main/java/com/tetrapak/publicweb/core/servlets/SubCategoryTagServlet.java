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
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
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
@Designate(ocd = SubCategoryTagServlet.Config.class)
public class SubCategoryTagServlet extends SlingSafeMethodsServlet {
	
	@ObjectClassDefinition(name = "Tetra Pak - Public Web Sub Category Tag Servlet", description = "Tetra Pak - Public Web Sub Category Tag servlet")
	public static @interface Config {

		@AttributeDefinition(name = "Category Tag Variable Name", description = "Name of variable being sent by Front end to the servlet, that tells us about the category tag.")
		String category_tag() default "categoryTag";
		
		@AttributeDefinition(name = "System User Name", description = "The system user name from user mapping that is used to access Resource resolver object.")
		String system_user() default "writeService";
	}

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(SubCategoryTagServlet.class);
	
	@Reference
	private ResourceResolverFactory resolverFactory;

	private ResourceResolver resourceResolver;
	
	private String CATEGORY_TAG;
	private String SYSTEM_USER;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		try {
			// get resource resolver, tagManager objects.
			resourceResolver = getResourceResolver(request);
			TagManager tagManager = resourceResolver.adaptTo(TagManager.class);

			Map<String, String> subCategoryTagsMap = new HashMap<>();

			if (tagManager != null) {
				String categoryTagId = request.getParameter(CATEGORY_TAG);
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
		param.put(ResourceResolverFactory.SUBSERVICE, SYSTEM_USER);
		ResourceResolver resourceResolver = null;
		try {
			resourceResolver = resolverFactory.getServiceResourceResolver(param);
			if(resourceResolver == null) {
				log.error("[Resource resolver from system user is null. Getting it from request now.");
				resourceResolver = request.getResourceResolver();
			}
		} catch (LoginException e) {
			log.error("[Error getting the resource resolver. {}", e);
			resourceResolver = request.getResourceResolver();
		}
		return resourceResolver;
	}
	
	@Activate
	protected void activate(final Config config) {
		this.CATEGORY_TAG = (String.valueOf(config.category_tag()) != null) ? String.valueOf(config.category_tag())
				: null;
		log.info("configure: CATEGORY_TAG='{}'", this.CATEGORY_TAG);
		this.SYSTEM_USER = (String.valueOf(config.system_user()) != null) ? String.valueOf(config.system_user())
				: null;
		log.info("configure: SYSTEM_USER='{}'", this.SYSTEM_USER);
	}
}
