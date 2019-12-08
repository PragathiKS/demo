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
		
	}

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(SubCategoryTagServlet.class);
	
	@Reference
	private ResourceResolverFactory resolverFactory;

	private ResourceResolver resourceResolver;
	
	private String CATEGORY_TAG;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		log.info("Executing doGet method.");
		try {
			// get resource resolver, tagManager objects.
			resourceResolver = request.getResourceResolver();
			TagManager tagManager = resourceResolver.adaptTo(TagManager.class);

			Map<String, String> subCategoryTagsMap = new HashMap<>();

			if (tagManager != null) {
				String categoryTagId = request.getParameter(CATEGORY_TAG);
				log.info("** Category Tag : {}", categoryTagId);				
				Tag categoryTag = tagManager.resolve(categoryTagId);				

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

	@Activate
	protected void activate(final Config config) {
		this.CATEGORY_TAG = (String.valueOf(config.category_tag()) != null) ? String.valueOf(config.category_tag())
				: null;
		log.info("configure: CATEGORY_TAG='{}'", this.CATEGORY_TAG);
	}
}
