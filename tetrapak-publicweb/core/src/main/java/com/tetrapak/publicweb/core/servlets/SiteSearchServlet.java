package com.tetrapak.publicweb.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
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
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.Gson;
import com.tetrapak.publicweb.core.beans.SearchResultBean;
import com.tetrapak.publicweb.core.models.SearchConfigModel;
import com.tetrapak.publicweb.core.models.SearchResultsModel;
import com.tetrapak.publicweb.core.models.multifield.SearchPathModel;

@Component(
        service = Servlet.class,
        property = { Constants.SERVICE_DESCRIPTION + "=Tetra Pak - Public Web Search service",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "publicweb/components/content/searchresults" })
public class SiteSearchServlet extends SlingSafeMethodsServlet {

    private static final String _1_GROUP = "1_group.";

    private static final long serialVersionUID = 5220677543550980049L;

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteSearchServlet.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    private SearchResultsModel searchResultsModel;

    @Reference
    private QueryBuilder queryBuilder;

    private Session session;
    private ResourceResolver resourceResolver;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        LOGGER.info("Executing doGet method.");
        try {
            searchResultsModel = request.adaptTo(SearchResultsModel.class);
            // get resource resolver, session and queryBuilder objects.
            resourceResolver = request.getResourceResolver();
            session = resourceResolver.adaptTo(Session.class);
            queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);

            // get search arguments
            String[] contentType = request.getParameterValues("contentType");
            String[] themes = request.getParameterValues("themes");
            String fulltextSearchTerm = URLDecoder.decode(request.getParameter("searchTerm"), "UTF-8").replace("%20",
                    " ");
            LOGGER.info("Keyword to search : {}", fulltextSearchTerm);

            Gson gson = new Gson();
            String responseJSON = "not-set";

            // search for resources
            List<SearchResultBean> resources = getSearchResultItems(fulltextSearchTerm, contentType, themes);
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

        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Error while decoding the query term.", e);
        } catch (IOException e) {
            LOGGER.error("Error while writing the response object.", e);
        }

    }

    /**
     * Method to create a query and execute to get the results.
     *
     * @param fulltextSearchTerm
     * @param themes
     * @param searchRootPath
     * @return List<SearchResultBean>
     */
    public List<SearchResultBean> getSearchResultItems(String fulltextSearchTerm, String[] contentType,
            String[] themes) {
        LOGGER.info("Executing getSearchResultItems method.");
        List<SearchResultBean> resources = new LinkedList<>();
        for (String type : contentType) {
            Map<String, String> map = new HashMap<>();
            List<SearchPathModel> structure = searchResultsModel.getStructureMap().get(type);
            int index = 1;
            for (SearchPathModel path : structure) {

                String pathKey = _1_GROUP + index + "_path";
                map.put(pathKey, path.getPath());
                index++;
            }
            map.put("type", "cq:Page");
            map.put("orderby", "@jcr:score");

            // Predicate for full text search if keyword is entered.
            if (!fulltextSearchTerm.isEmpty()) {
                map.put("fulltext", "\"" + fulltextSearchTerm + "\"");
            }
            if (themes != null && themes.length > 0) {
                map.put("1_group.p.or", "true");
                for (int i = 0; i < themes.length; i++) {
                    map.put(_1_GROUP + (i + 1) + "_group.property", "jcr:content/cq:tags");
                    map.put(_1_GROUP + (i + 1) + "_group.property.value", themes[i]);
                }
            }
            List<SearchPathModel> template = searchResultsModel.getTemplateMap().get(type);
            if (template != null && template.isEmpty()) {
                map.put("1_group.p.or", "true");
                for (int i = 0; i < template.size(); i++) {
                    map.put(_1_GROUP + (i + 1) + "_group.property", "jcr:content/cq:template");
                    map.put(_1_GROUP + (i + 1) + "_group.property.value", template.get(i).getPath());
                }
            }
            // Excluding Error page template.
            map.put("1_property", "@jcr:content/cq:template");
            map.put("1_property.value", "/conf/publicweb/settings/wcm/templates/public-web-landing-page");

            // Excluding pages which have Hide in Search selected.
            map.put("2_property", "@jcr:content/hideInSearch");
            map.put("2_property.value", "false");
            map.put("2_property.operation", "exists");

            map.put("p.limit", "-1");

            LOGGER.info("Here is the query PredicateGroup : {} ", PredicateGroup.create(map));

            Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
            SearchResult result = query.getResult();

            // paging metadata
            LOGGER.info("Total number of results : {}", result.getTotalMatches());
            List<SearchResultBean> resource = new LinkedList<>();
            if (result.getHits().isEmpty()) {
                return resource;
            }

            // add all the items to the result list
            for (Hit hit : query.getResult().getHits()) {
                try {
                    LOGGER.debug("Hit : {}", hit.getPath());
                    resources.add(setSearchResultItemData(hit));
                } catch (RepositoryException e) {
                    LOGGER.error("[performSearch] There was an issue getting the resource", e);
                }
            }
            resources.addAll(resource);
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

        SearchResultBean searchResultItem = new SearchResultBean();

        searchResultItem.setPath(hit.getPath());
        searchResultItem.setTitle(hit.getTitle());

        if (pageManager != null) {
            Page page = pageManager.getPage(hit.getPath());
            searchResultItem.setDescription(page.getDescription());

        }

        return searchResultItem;
    }
}
