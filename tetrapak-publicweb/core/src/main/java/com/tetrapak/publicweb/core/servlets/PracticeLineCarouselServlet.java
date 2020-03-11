package com.tetrapak.publicweb.core.servlets;

import com.google.gson.Gson;
import com.tetrapak.publicweb.core.beans.BestPracticeLineBean;
import com.tetrapak.publicweb.core.services.BestPracticeLineService;
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

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * This is the servlet that is triggered when a search is performed on the page and return the results to the
 * front-end.
 *
 * @author abhbhatn
 */
@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Tetra Pak - Public Web Carousel Listing service",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/tetrapak/pw-carousellisting"
        })
@Designate(ocd = PracticeLineCarouselServlet.Config.class)
public class PracticeLineCarouselServlet extends SlingSafeMethodsServlet {

    @ObjectClassDefinition(name = "Tetra Pak - Public Web Carousel Listing Servlet",
            description = "Tetra Pak - Public Web Carousel Listing servlet")
    public static @interface Config {

        @AttributeDefinition(name = "Product Type Variable Name",
                description = "Name of variable being sent by Front end to the servlet, that tells us about the product type.")
        String productType() default "productType";

        @AttributeDefinition(name = "Sub Category Value Variable Name",
                description = "Name of variable being sent by Front end to the servlet, that tells us about the sub-category value.")
        String subcategoryValue() default "subCategoryVal";

        @AttributeDefinition(name = "Root Path Variable Name",
                description = "Name of variable being sent by Front end to the servlet, that tells us about the root path.")
        String rootPath() default "rootPath";

        @AttributeDefinition(name = "Best Practice Line Page Template Path",
                description = "Path for the Best Practice Line Page template.")
        String bestpracticeTemplate() default "/conf/publicweb/settings/wcm/templates/public-web-best-practice-line-page";
    }

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PracticeLineCarouselServlet.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private BestPracticeLineService bestPracticeLineService;

    private String productType;
    private String subcategoryValue;
    private String rootPath;
    private String bestpracticeTemplate;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Executing doGet method.");

        // get resource resolver, session and queryBuilder objects.
        ResourceResolver resourceResolver = request.getResourceResolver();

        // get search arguments
        String productTypeTemp = request.getParameter(this.productType);
        LOGGER.info("Product Type : {}", productTypeTemp);
        String subCategoryVal = request.getParameter(subcategoryValue);
        LOGGER.info("Sub Category Value : {}", subCategoryVal);
        String rootPathTemp = request.getParameter(this.rootPath);
        LOGGER.info("Root Path : {}", rootPathTemp);

        Gson gson = new Gson();
        String responseJSON = "";

        // search for resources
        if (productTypeTemp != null && subCategoryVal != null) {
            List<BestPracticeLineBean> resources = bestPracticeLineService.getListOfPracticeLines(
                    resourceResolver, productTypeTemp, subCategoryVal, rootPathTemp);
            if (resources != null) {
                responseJSON = gson.toJson(resources);
                LOGGER.info("Here is the JSON object : {}", responseJSON);
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
        if (String.valueOf(config.productType()) != null) {
            this.productType = String.valueOf(config.productType());
        } else {
            this.productType = null;
        }
        LOGGER.info("configure: PRODUCT_TYPE='{}'", this.productType);

        if (String.valueOf(config.subcategoryValue()) != null) {
            this.subcategoryValue = String.valueOf(config.subcategoryValue());
        } else {
            this.subcategoryValue = null;
        }
        LOGGER.info("configure: SUBCATEGORY_VALUE='{}'", this.subcategoryValue);

        if (String.valueOf(config.rootPath()) != null) {
            this.rootPath = String.valueOf(config.rootPath());
        } else {
            this.rootPath = null;
        }
        LOGGER.info("configure: ROOT_PATH='{}'", this.rootPath);

        if (String.valueOf(config.bestpracticeTemplate()) != null) {
            this.bestpracticeTemplate = String.valueOf(config.bestpracticeTemplate());
        } else {
            this.bestpracticeTemplate = null;
        }
        LOGGER.info("configure: BESTPRACTICE_TEMPLATE='{}'", this.bestpracticeTemplate);
    }
}
