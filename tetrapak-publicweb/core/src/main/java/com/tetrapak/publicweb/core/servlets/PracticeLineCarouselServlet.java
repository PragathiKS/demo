package com.tetrapak.publicweb.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
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

import com.google.gson.Gson;
import com.tetrapak.publicweb.core.beans.BestPracticeLineBean;
import com.tetrapak.publicweb.core.services.BestPracticeLineService;

/**
 * This is the servlet that is triggered when a search is performed on the page and return the results to the
 * front-end.
 * @author abhbhatn
 *
 */
@Component(service = Servlet.class,
property = {
        Constants.SERVICE_DESCRIPTION + "=Tetra Pak - Public Web Carousel Listing service",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths=" + "/bin/tetrapak/pw-carousellisting"
})
@Designate(ocd = PracticeLineCarouselServlet.Config.class)
public class PracticeLineCarouselServlet extends SlingSafeMethodsServlet {
	
	@ObjectClassDefinition(name = "Tetra Pak - Public Web Carousel Listing Servlet", description = "Tetra Pak - Public Web Carousel Listing servlet")
	public static @interface Config {

		@AttributeDefinition(name = "Product Type Variable Name", description = "Name of variable being sent by Front end to the servlet, that tells us about the product type.")
		String productType() default "productType";
		
		@AttributeDefinition(name = "Sub Category Value Variable Name", description = "Name of variable being sent by Front end to the servlet, that tells us about the sub-category value.")
		String subcategoryValue() default "subCategoryVal";
		
		@AttributeDefinition(name = "Root Path Variable Name", description = "Name of variable being sent by Front end to the servlet, that tells us about the root path.")
		String rootPath() default "rootPath";
		
		@AttributeDefinition(name = "Best Practice Line Page Template Path", description = "Path for the Best Practice Line Page template.")
		String bestpracticeTemplate() default "/apps/publicweb/templates/bestpracticelinepage";
	}

	private static final long serialVersionUID = 1L;
	
    private static final Logger log = LoggerFactory.getLogger(PracticeLineCarouselServlet.class);

	@Reference
	private ResourceResolverFactory resolverFactory;
	
	@Reference
	BestPracticeLineService bestPracticeLineService;
    
    private ResourceResolver resourceResolver;
    
	private String PRODUCT_TYPE;
	private String SUBCATEGORY_VALUE;
	private String ROOT_PATH;
	private String BESTPRACTICE_TEMPLATE;
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
		log.info("Executing doGet method.");

		// get resource resolver, session and queryBuilder objects.
		resourceResolver = request.getResourceResolver();
		
		// get search arguments	    
		String productType = request.getParameter(PRODUCT_TYPE);	
		log.info("Product Type : {}", productType);
		String subCategoryVal = request.getParameter(SUBCATEGORY_VALUE);
		log.info("Sub Category Value : {}", subCategoryVal);
		String rootPath = request.getParameter(ROOT_PATH);	
		log.info("Root Path : {}", rootPath);
		
		Gson gson = new Gson();
		String responseJSON = "";
		
		// search for resources
		if(productType != null && subCategoryVal != null) { 
			List<BestPracticeLineBean> resources = bestPracticeLineService.getListOfPracticeLines(resourceResolver, productType, subCategoryVal, rootPath);		
			if(resources != null) {
				responseJSON = gson.toJson(resources);
				log.info("Here is the JSON object : {}",  responseJSON);
			}
		}		
		
		// set the response type
		response.setContentType("application/json");		
		response.setStatus(HttpServletResponse.SC_OK);
		
		PrintWriter writer = response.getWriter();
		writer.println(responseJSON);
		writer.flush();
		writer.close();

	}
	
	@Activate
	protected void activate(final Config config) {
		this.PRODUCT_TYPE = (String.valueOf(config.productType()) != null) ? String.valueOf(config.productType())
				: null;
		log.info("configure: PRODUCT_TYPE='{}'", this.PRODUCT_TYPE);
		
		this.SUBCATEGORY_VALUE = (String.valueOf(config.subcategoryValue()) != null) ? String.valueOf(config.subcategoryValue())
				: null;
		log.info("configure: SUBCATEGORY_VALUE='{}'", this.SUBCATEGORY_VALUE);
		
		this.ROOT_PATH = (String.valueOf(config.rootPath()) != null) ? String.valueOf(config.rootPath())
				: null;
		log.info("configure: ROOT_PATH='{}'", this.ROOT_PATH);
		
		this.BESTPRACTICE_TEMPLATE = (String.valueOf(config.bestpracticeTemplate()) != null) ? String.valueOf(config.bestpracticeTemplate())
				: null;
		log.info("configure: BESTPRACTICE_TEMPLATE='{}'", this.BESTPRACTICE_TEMPLATE);
	}
}
