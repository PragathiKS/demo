package com.tetrapak.publicweb.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
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

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.gson.Gson;
import com.tetrapak.publicweb.core.beans.ProductInfoBean;
import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * This is the servlet that is triggered when a search is performed on the page and return the results to the
 * front-end.
 * @author abhbhatn
 *
 */
@Component(service = Servlet.class,
property = {
        Constants.SERVICE_DESCRIPTION + "=Tetra Pak - Public Web Product Listing service",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths=" + "/bin/tetrapak/pw-productlisting"
})
@Designate(ocd = ProductListingServlet.Config.class)
public class ProductListingServlet extends SlingSafeMethodsServlet {
	
	@ObjectClassDefinition(name = "Tetra Pak - Public Web Product Listing Servlet", description = "Tetra Pak - Public Web Product Listing servlet")
	public static @interface Config {

		@AttributeDefinition(name = "Product Category Variable Name", description = "Name of variable being sent by Front end to the servlet, that tells about the product category.")
		String product_category() default "productCategory";
		
		@AttributeDefinition(name = "Product Root Path Variable Name", description = "Name of variable being sent by Front end to the servlet, that tells about the product root path.")
		String product_rootpath() default "productRootPath";
		
		@AttributeDefinition(name = "Product Page Template Path", description = "Path for the Product Page template.")
		String product_template() default "/apps/publicweb/templates/productpage";
		
		@AttributeDefinition(name = "System User Name", description = "The system user name from user mapping that is used to access Resource resolver object.")
		String system_user() default "writeService";
	}

	private static final long serialVersionUID = 1L;
	
    private static final Logger log = LoggerFactory.getLogger(ProductListingServlet.class);

	@Reference
	private ResourceResolverFactory resolverFactory;
	
    @Reference
    private QueryBuilder queryBuilder;
    
    private Session session; 
    private ResourceResolver resourceResolver;
    
    private String PRODUCT_CATEGORY;
    private String PRODUCT_ROOT_PATH;
    private String PRODUCT_TEMPLATE;
    private String SYSTEM_USER;
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
		
		// get resource resolver, session and queryBuilder objects.
		resourceResolver = getResourceResolver(request);
		session = resourceResolver.adaptTo(Session.class);
		queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
		
		// get search arguments	    
		String productCategory = request.getParameter(PRODUCT_CATEGORY);	
		String productRootPath = request.getParameter(PRODUCT_ROOT_PATH);	
		log.info("Product category : " + productCategory);
		
		
		Gson gson = new Gson();
		String responseJSON = "not-set";
		
		// search for resources
		List<ProductInfoBean> resources = getListOfProducts(productCategory, productRootPath);		
		if(resources != null) {
			responseJSON = gson.toJson(resources);
			log.info("Here is the JSON object : {}",  responseJSON);
		}
		
		// set the response type
		response.setContentType("application/json");		
		response.setStatus(HttpServletResponse.SC_OK);
		
		PrintWriter writer = response.getWriter();
		writer.println(responseJSON);
		writer.flush();
		writer.close();

	}
	
	/**
	 * Method to create a query and execute to get the results.
	 * @param productRootPath 
	 * @param fulltextSearchTerm
	 * @param searchRootPath
	 * @return List<ProductInfoBean>
	 */
	public List<ProductInfoBean> getListOfProducts(String productCategory, String productRootPath) {
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("path", productRootPath);
		map.put("type", "cq:Page");
        map.put("1_property", "jcr:content/cq:template");
        map.put("1_property.value", PRODUCT_TEMPLATE);
        
		if(!productCategory.equalsIgnoreCase("all")) {
			map.put("2_property", "jcr:content/cq:tags");
	        map.put("2_property.value", productCategory);
		}
		
        map.put("p.limit", "9");
		
		log.info("Here is the query PredicateGroup : {} ",  PredicateGroup.create(map));			
		
        Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);        
        SearchResult result = query.getResult();
                        
        // paging metadata
        log.info("Total number of results : {}", result.getTotalMatches());
        List<ProductInfoBean> resources = new LinkedList<ProductInfoBean>();
		if (result.getHits().size() == 0)
			return resources;

		// add all the items to the result list
		for (Hit hit : query.getResult().getHits()) {
			ProductInfoBean productItem = new ProductInfoBean();
			try {
				log.info("Hit : {}", hit.getPath());
				Resource res = resourceResolver.getResource(hit.getPath() + "/jcr:content");
				ValueMap properties = res.adaptTo(ValueMap.class);
				productItem.setTitle(properties.get("title", String.class)!=null ? properties.get("title", String.class) : properties.get("jcr:title", String.class));
				productItem.setDescription(properties.get("vanityDescription", String.class)!=null ? properties.get("vanityDescription", String.class) : "");
				productItem.setProductImage(properties.get("productImagePath", String.class)!=null ? properties.get("productImagePath", String.class) : "");
				productItem.setImageAltText(properties.get("productImageAltI18n", String.class)!=null ? properties.get("productImageAltI18n", String.class) : "");
				productItem.setLinkText(properties.get("ctaTexti18nKey", String.class)!=null ? properties.get("ctaTexti18nKey", String.class) : "");
				productItem.setLinkPath(LinkUtils.sanitizeLink(hit.getPath()));				
			} catch (RepositoryException e) {
				log.error("[performSearch] There was an issue getting the resource {}", hit.toString());
			}
			resources.add(productItem);
		}
		
		return resources;
	}
	
	private ResourceResolver getResourceResolver(SlingHttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(ResourceResolverFactory.SUBSERVICE, SYSTEM_USER);
		ResourceResolver resourceResolver = null;
		try {
			resourceResolver = resolverFactory
					.getServiceResourceResolver(param);
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
		this.PRODUCT_CATEGORY = (String.valueOf(config.product_category()) != null) ? String.valueOf(config.product_category())
				: null;
		log.info("configure: PRODUCT_CATEGORY='{}'", this.PRODUCT_CATEGORY);
		this.PRODUCT_ROOT_PATH = (String.valueOf(config.product_rootpath()) != null) ? String.valueOf(config.product_rootpath())
				: null;
		log.info("configure: PRODUCT_ROOT_PATH='{}'", this.PRODUCT_ROOT_PATH);
		this.PRODUCT_TEMPLATE = (String.valueOf(config.product_template()) != null) ? String.valueOf(config.product_template())
				: null;
		log.info("configure: PRODUCT_TEMPLATE='{}'", this.PRODUCT_TEMPLATE);
		this.SYSTEM_USER = (String.valueOf(config.system_user()) != null) ? String.valueOf(config.system_user())
				: null;
		log.info("configure: SYSTEM_USER='{}'", this.SYSTEM_USER);
	}
}
