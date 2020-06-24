package com.tetrapak.publicweb.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.gson.Gson;
import com.tetrapak.publicweb.core.beans.ProductInfoBean;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
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

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This is the servlet that is triggered when a search is performed on the page and return the results to the
 * front-end.
 *
 * @author abhbhatn
 */
@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Tetra Pak - Public Web Product Listing service",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/tetrapak/pw-productlisting"
        })
@Designate(ocd = ProductListingServlet.Config.class)
public class ProductListingServlet extends SlingSafeMethodsServlet {

    @ObjectClassDefinition(name = "Tetra Pak - Public Web Product Listing Servlet",
            description = "Tetra Pak - Public Web Product Listing servlet")
    public static @interface Config {

        @AttributeDefinition(name = "Total number of results",
                description = "Total number of products that need to be shown on the listing.")
        String total_results() default "9";

        @AttributeDefinition(name = "Product Category Variable Name",
                description = "Name of variable being sent by Front end to the servlet, that tells about the product category.")
        String product_category() default "productCategory";

        @AttributeDefinition(name = "Product Root Path Variable Name",
                description = "Name of variable being sent by Front end to the servlet, that tells about the product root path.")
        String product_rootpath() default "productRootPath";

        @AttributeDefinition(name = "Product Page Template Path",
                description = "Path for the Product Page template.")
        String product_template() default "/conf/publicweb/settings/wcm/templates/public-web-product-page";

    }

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductListingServlet.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private QueryBuilder queryBuilder;

    private Session session;
    private ResourceResolver resourceResolver;

    private String totalResults;
    private String productCategory;
    private String productRootPath;
    private String productTemplate;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Executing doGet method.");
        // get resource resolver, session and queryBuilder objects.
        resourceResolver = request.getResourceResolver();
        session = resourceResolver.adaptTo(Session.class);
        queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);

        // get search arguments
        String productCategoryTeamp = request.getParameter(this.productCategory);
        String productRootPathTemp = request.getParameter(this.productRootPath);
        LOGGER.info("Product category : {}", productCategoryTeamp);


        Gson gson = new Gson();
        String responseJSON = "not-set";

        // search for resources
        List<ProductInfoBean> resources = getListOfProducts(productCategoryTeamp, productRootPathTemp);
        if (resources != null) {
            responseJSON = gson.toJson(resources);
            LOGGER.info("Here is the JSON object : {}", responseJSON);
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
     *
     * @param productCategory product category
     * @param productRootPath product root path
     * @return List<ProductInfoBean>
     */
    public List<ProductInfoBean> getListOfProducts(String productCategory, String productRootPath) {
        LOGGER.info("Executing getListOfProducts method.");
        Map<String, String> map = new HashMap<>();

        map.put("path", productRootPath);
        map.put("type", "cq:Page");
        map.put("1_property", "jcr:content/cq:template");
        map.put("1_property.value", productTemplate);

        if (!"all".equalsIgnoreCase(productCategory)) {
            map.put("2_property", "jcr:content/cq:tags");
            map.put("2_property.value", productCategory);
        }

        map.put("p.limit", totalResults);

        LOGGER.info("Here is the query PredicateGroup : {} ", PredicateGroup.create(map));

        Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
        SearchResult result = query.getResult();

        // paging metadata
        LOGGER.info("Total number of results : {}", result.getTotalMatches());
        List<ProductInfoBean> resources = new LinkedList<>();
        if (result.getHits().isEmpty()) {
            return resources;
        }
        // add all the items to the result list
        for (Hit hit : query.getResult().getHits()) {
            ProductInfoBean productItem = getProductInfoBean(hit);
            resources.add(productItem);
        }
        return resources;
    }

    private ProductInfoBean getProductInfoBean(Hit hit) {
        ProductInfoBean productItem = new ProductInfoBean();
        try {
            LOGGER.info("Hit : {}", hit.getPath());
            Resource res = resourceResolver.getResource(hit.getPath() + "/jcr:content");
            if (res != null) {
                ValueMap properties = res.adaptTo(ValueMap.class);
                productItem.setTitle(properties.get("jcr:title", String.class) != null ? properties.get("jcr:title", String.class) : "");
                productItem.setDescription(properties.get("jcr:description", String.class) != null ?
                        properties.get("jcr:description", String.class) : "");
                productItem.setProductImage(properties.get("productImagePath", String.class) != null ?
                        properties.get("productImagePath", String.class) : "");
                productItem.setImageAltText(properties.get("productImageAltI18n", String.class) != null ?
                        properties.get("productImageAltI18n", String.class) : "");
                productItem.setLinkText(properties.get("ctaTexti18nKey", String.class) != null ?
                        properties.get("ctaTexti18nKey", String.class) : "");
                productItem.setLinkPath(LinkUtils.sanitizeLink(hit.getPath(), resourceResolver));
            }

        } catch (RepositoryException e) {
            LOGGER.error("[performSearch] There was an issue getting the resource", e);
        }
        return productItem;
    }

    @Activate
    protected void activate(final Config config) {
        if (String.valueOf(config.total_results()) != null) {
            this.totalResults = String.valueOf(config.total_results());
        } else {
            this.totalResults = null;
        }
        LOGGER.info("configure: TOTAL_RESULTS='{}'", this.totalResults);
        if (String.valueOf(config.product_category()) != null) {
            this.productCategory = String.valueOf(config.product_category());
        } else {
            this.productCategory = null;
        }
        LOGGER.info("configure: PRODUCT_CATEGORY='{}'", this.productCategory);
        if (String.valueOf(config.product_rootpath()) != null) {
            this.productRootPath = String.valueOf(config.product_rootpath());
        } else {
            this.productRootPath = null;
        }
        LOGGER.info("configure: PRODUCT_ROOT_PATH='{}'", this.productRootPath);
        if (String.valueOf(config.product_template()) != null) {
            this.productTemplate = String.valueOf(config.product_template());
        } else {
            this.productTemplate = null;
        }
        LOGGER.info("configure: PRODUCT_TEMPLATE='{}'", this.productTemplate);
    }
}
