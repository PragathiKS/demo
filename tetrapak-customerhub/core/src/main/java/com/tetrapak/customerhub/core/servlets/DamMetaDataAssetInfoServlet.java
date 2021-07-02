package com.tetrapak.customerhub.core.servlets;

import javax.servlet.Servlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tetrapak.customerhub.core.services.DamUtilityService;

/**
 * @author Aalekh Mathur 
 * The Class DamMetaDataAssetInfoServlet.
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Dam Asset Metadata information",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.paths=" + "/bin/customerhub/fetchDamAssets" })
public class DamMetaDataAssetInfoServlet extends SlingSafeMethodsServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1347039390899977388L;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(DamMetaDataAssetInfoServlet.class);
	
	@Reference
	DamUtilityService damUtilityService;


	/**
	 * Do get.
	 *
	 * @param request  the request
	 * @param response the response
	 * 
	 */
	@Override
	protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) {
		ResourceResolver resolver = request.getResourceResolver();
		response.setContentType("application/json");
		try {
			//returns all the pdf assets from DAM in json format
			StringBuffer assets = new StringBuffer();
			assets.append(damUtilityService.getAssetsFromDam(resolver));
			LOGGER.info("In DamMetaDataAssetInfoServlet, response is "+assets);
            response.getWriter().write(assets.toString());        
		} catch (Exception e) {
			LOGGER.error("Error in DamMetaDataAssetInfoServlet - {}", e.getMessage());
		}
	}       
}
