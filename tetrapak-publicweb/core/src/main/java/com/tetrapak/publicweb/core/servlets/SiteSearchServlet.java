package com.tetrapak.publicweb.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.Gson;
import com.tetrapak.publicweb.core.beans.SearchResultBean;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This is the servlet that is triggered when a search is performed on the page
 * and return the results to the front-end.
 *
 * @author abhbhatn
 */
@Component(service = Servlet.class, property = {
        Constants.SERVICE_DESCRIPTION + "=Tetra Pak - Public Web Search service",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/tetrapak/pw-search"})
@Designate(ocd = SiteSearchServlet.Config.class)
public class SiteSearchServlet extends SlingSafeMethodsServlet {

    @ObjectClassDefinition(name = "Tetra Pak - Public Web Search Servlet", description = "Tetra Pak - Public Web Search servlet")
    public static @interface Config {

        @AttributeDefinition(name = "Search Root Path Variable Name", description = "Name of variable being sent by Front end to the servlet, that tells about the search root path.")
        String search_rootpath() default "searchRootPath";

        @AttributeDefinition(name = "Full Text Search Term Variable Name", description = "Name of variable being sent by Front end to the servlet, that tells about the full text search term.")
        String fulltext_searchterm() default "fulltextSearchTerm";

    }

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(SiteSearchServlet.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private QueryBuilder queryBuilder;

    private Session session;
    private ResourceResolver resourceResolver;

    private String SEARCH_ROOT_PATH;
    private String FULLTEXT_SEARCH_TERM;

    private static final String TEMPLATE_BASE_PATH = "/conf/publicweb/settings/wcm/templates/";

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        log.info("Executing doGet method.");
        try {

            // get resource resolver, session and queryBuilder objects.
            resourceResolver = request.getResourceResolver();
            session = resourceResolver.adaptTo(Session.class);
            queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);

            // get search arguments
            String searchRootPath = request.getParameter(SEARCH_ROOT_PATH);
            String fulltextSearchTerm = URLDecoder.decode(request.getParameter(FULLTEXT_SEARCH_TERM), "UTF-8")
                    .replace("%20", " ");
            log.info("Keyword to search : {}", fulltextSearchTerm);

            Gson gson = new Gson();
            String responseJSON = "not-set";

            // search for resources
            List<SearchResultBean> resources = getSearchResultItems(fulltextSearchTerm, searchRootPath);
            if (resources != null) {
                responseJSON = gson.toJson(resources);
                log.info("Here is the JSON object : {}", responseJSON);
            }

            // set the response type
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);

            PrintWriter writer = response.getWriter();
            writer.println(responseJSON);
            writer.flush();
            writer.close();

        } catch (UnsupportedEncodingException e) {
            log.error("Error while decoding the query term.", e);
        } catch (IOException e) {
            log.error("Error while writing the response object.", e);
        }

    }

    /**
     * Method to create a query and execute to get the results.
     *
     * @param fulltextSearchTerm
     * @param searchRootPath
     * @return List<SearchResultBean>
     */
    public List<SearchResultBean> getSearchResultItems(String fulltextSearchTerm, String searchRootPath) {
        log.info("Executing getSearchResultItems method.");
        Map<String, String> map = new HashMap<>();

        map.put("path", searchRootPath);
        map.put("type", "cq:Page");

        // Predicate for full text search if keyword is entered.
        if (!fulltextSearchTerm.isEmpty()) {
            map.put("fulltext", "\"" + fulltextSearchTerm + "\"");
        }

        //Excluding Error page template.
        map.put("1_property", "@jcr:content/cq:template");
        map.put("1_property.value", "/conf/publicweb/settings/wcm/templates/public-web-error-page");
        map.put("1_property.operation", "unequals");

        //Excluding pages which have Hide in Search selected.
        map.put("2_property", "@jcr:content/hideInSearch");
        map.put("2_property.value", "false");
        map.put("2_property.operation", "exists");

        map.put("p.limit", "-1");

        log.info("Here is the query PredicateGroup : {} ", PredicateGroup.create(map));

        Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
        SearchResult result = query.getResult();

        // paging metadata
        log.info("Total number of results : {}", result.getTotalMatches());
        List<SearchResultBean> resources = new LinkedList<>();
        if (result.getHits().isEmpty())
            return resources;

        // add all the items to the result list
        for (Hit hit : query.getResult().getHits()) {
            try {
                log.debug("Hit : {}", hit.getPath());
                resources.add(setSearchResultItemData(hit));
            } catch (RepositoryException e) {
                log.error("[performSearch] There was an issue getting the resource {}", hit);
            }
        }

        return resources;
    }

    /**
     * Method to set search result item with all data.
     *
     * @param hit
     * @return
     * @throws RepositoryException
     */
    private SearchResultBean setSearchResultItemData(Hit hit) throws RepositoryException {
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);

        SearchResultBean searchResultItem = new SearchResultBean();
        LinkedHashMap<String, String> tagsMap = new LinkedHashMap<>();
        LinkedHashMap<String, String> breadcrumbMap = new LinkedHashMap<>();

        searchResultItem.setPath(hit.getPath());
        searchResultItem.setTitle(hit.getTitle());

        if (pageManager != null) {
            Page page = pageManager.getPage(hit.getPath());
            ValueMap properties = page.getContentResource().getValueMap();
            Page breadcrumbParent1 = page.getParent(1);
            Page breadcrumbParent2 = page.getParent(2);
            breadcrumbMap.put(breadcrumbParent1.getTitle(), breadcrumbParent1.getPath());
            breadcrumbMap.put(breadcrumbParent2.getTitle(), breadcrumbParent2.getPath());
            searchResultItem.setBreadcrumbMap(breadcrumbMap);

            if (properties.containsKey("cq:tags") && tagManager != null) {
                String[] tags = properties.get("cq:tags", String[].class);
                for (String tagVal : tags) {
                    log.info("Tag ID : {}", tagVal);
                    Tag tag = tagManager.resolve(tagVal);
                    tagsMap.put(tag.getTitle(), tagVal);
                }
                searchResultItem.setTagsMap(tagsMap);
            }

            if (properties.containsKey("jcr:description")) {
                String description = properties.get("jcr:description", String.class);
                log.info("Decription : {}", description);
                searchResultItem.setDescription(description);
            }

            String template = properties.get("cq:template", String.class);
            log.info("Template : {}", template);
            String productType = template.replace(TEMPLATE_BASE_PATH, "");
            searchResultItem.setProductType(productType);

        }

        return searchResultItem;
    }

    @Activate
    protected void activate(final Config config) {
        this.SEARCH_ROOT_PATH = (String.valueOf(config.search_rootpath()) != null)
                ? String.valueOf(config.search_rootpath())
                : null;
        log.info("configure: SEARCH_ROOT_PATH='{}'", this.SEARCH_ROOT_PATH);
        this.FULLTEXT_SEARCH_TERM = (String.valueOf(config.fulltext_searchterm()) != null)
                ? String.valueOf(config.fulltext_searchterm())
                : null;
        log.info("configure: FULLTEXT_SEARCH_TERM='{}'", this.FULLTEXT_SEARCH_TERM);
    }
}
