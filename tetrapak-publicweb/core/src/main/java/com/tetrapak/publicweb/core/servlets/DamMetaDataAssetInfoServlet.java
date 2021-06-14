package com.tetrapak.publicweb.core.servlets;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;

/**
 * @author ojaswarn 
 * The Class DamMetaDataAssetInfoServlet.
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Dam Asset Metadata information",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.paths=" + "/bin/publicweb/metadatainfo" })
public class DamMetaDataAssetInfoServlet extends SlingSafeMethodsServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1347039390899977388L;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(DamMetaDataAssetInfoServlet.class);

	/**
	 * Do get.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws IOException 
	 */
	@Override
	protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) {
		ResourceResolver resolver = request.getResourceResolver();
		Resource resource = resolver.getResource("/content/dam/tetrapak/publicweb");
		Iterator<Resource> myItr = resource.listChildren();
		try {
			evaluateDamData(response, myItr);
		} catch (IOException e) {
			LOGGER.error("Error in DamMetaDataAssetInfoServlet - {}", e.getMessage());
		} catch (RepositoryException e) {
			LOGGER.error("Repository Exception - {}", e.getMessage());
		}
	}
	
	private void evaluateDamData(SlingHttpServletResponse response, Iterator<Resource> myItr) throws IOException, RepositoryException {
		while (myItr.hasNext()) {
			Resource resource = myItr.next();
			Node node = resource.adaptTo(Node.class);
			String primaryType = node.getProperty("jcr:primaryType").getString();
			if (primaryType.equalsIgnoreCase("dam:Asset")) {
				Asset myAsset = DamUtil.getAssets(resource).next();
				Map<String, Object> metadata = myAsset.getMetadata();
				response.getWriter().println(resource.getParent().getName()+" "+resource.getName()+" == "+metadata);
			} else if (primaryType.toLowerCase().contains("folder")) {
				evaluateDamData(response, resource.listChildren());
			}
		}
	}
}
