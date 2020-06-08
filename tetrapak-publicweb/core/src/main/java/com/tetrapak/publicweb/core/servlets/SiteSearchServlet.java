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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.gson.Gson;
import com.tetrapak.publicweb.core.beans.SearchResultBean;
import com.tetrapak.publicweb.core.models.SearchResultsModel;
import com.tetrapak.publicweb.core.models.multifield.SearchPathModel;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import com.tetrapak.publicweb.core.utils.PageUtil;

@Component(
        service = Servlet.class,
        property = { Constants.SERVICE_DESCRIPTION + "=Tetra Pak - Public Web Search service",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "publicweb/components/content/searchresults" })
public class SiteSearchServlet extends SlingSafeMethodsServlet {

    private static final String GROUP_1 = "1_group.";

    private static final String GROUP_2 = "2_group.";

    private static final String GROUP_3 = "3_group.";

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
            String contentTypeParam = request.getParameter("contentType");
            String[] contentType = null;
            if (StringUtils.isNoneBlank(contentTypeParam)) {
                contentType = contentTypeParam.split(",");
            }
            String themesParam = request.getParameter("theme");
            String[] themes = null;
            if (StringUtils.isNoneBlank(themesParam)) {
                themes = themesParam.split(",");
            }
            String fulltextSearchTerm = URLDecoder.decode(request.getParameter("searchTerm"), "UTF-8").replace("%20",
                    " ");
            LOGGER.info("Keyword to search : {}", fulltextSearchTerm);
            List<SearchResultBean> resources = new LinkedList<>();
            // search for resources
            if (ArrayUtils.isNotEmpty(contentType) && ArrayUtils.isNotEmpty(themes)) {
                for (String type : contentType) {
                    Map<String, String> map = new HashMap<>();
                    if (type.equalsIgnoreCase(searchResultsModel.getMediaLabel())) {
                        getMediaResults(map);
                    } else {
                        setContentTypeValToMap(type, map);
                    }
                    setThemesMap(themes, map);
                    setCommonMap(fulltextSearchTerm, map);
                    resources.addAll(getResources(map, type));

                }
            } else if (ArrayUtils.isNotEmpty(themes)) {
                Map<String, String> map = new HashMap<>();
                setThemesMap(themes, map);
                setCommonMap(fulltextSearchTerm, map);
                resources.addAll(getResources(map, StringUtils.EMPTY));
            } else if (ArrayUtils.isNotEmpty(contentType)) {
                for (String type : contentType) {
                    Map<String, String> map = new HashMap<>();
                    if (type.equalsIgnoreCase(searchResultsModel.getMediaLabel())) {
                        getMediaResults(map);
                    } else {
                        setContentTypeValToMap(type, map);
                    }
                    setCommonMap(fulltextSearchTerm, map);
                    resources.addAll(getResources(map, type));
                }
            } else {
                Map<String, String> map = new HashMap<>();
                map.put("type", "cq:Page");
                map.put("path", PageUtil.getLanguagePage(searchResultsModel.getCurrentPage()).getPath());
                setCommonMap(fulltextSearchTerm, map);
                resources.addAll(getResources(map, StringUtils.EMPTY));

            }
            Gson gson = new Gson();
            String responseJSON;
            responseJSON = gson.toJson(resources);
            LOGGER.info("Here is the JSON object : {}", responseJSON);

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

    private void getMediaResults(Map<String, String> map) {
        map.put("type", "dam:Asset");
        map.put("1_group.p.or", "true");
        map.put(GROUP_1 + "1_path", "/content/dam/tetrapak/publicweb/global/en");
        map.put(GROUP_1 + "2_path", searchResultsModel.getGatedPath());

    }

    /**
     * Method to create a query and execute to get the results.
     *
     * @param fulltextSearchTerm
     * @param map
     * @param searchRootPath
     * @return List<SearchResultBean>
     */
    public void setContentTypeValToMap(String type, Map<String, String> map) {
        LOGGER.info("Executing setContentTypeValToMap method.");
        List<SearchPathModel> structure = searchResultsModel.getStructureMap().get(type);
        map.put("type", "cq:Page");
        int index = 1;
        if (!CollectionUtils.isEmpty(structure)) {
            for (SearchPathModel path : structure) {
                map.put("1_group.p.or", "true");
                String pathKey = GROUP_1 + index + "_path";
                map.put(pathKey, path.getPath());
                index++;
            }
            // hide in navigation
            /*
             * map.put("2_property", "@jcr:content/hideInSearch"); map.put("2_property.value", "false");
             * map.put("2_property.operation", "exists");
             */
        }
        List<SearchPathModel> template = searchResultsModel.getTemplateMap().get(type);
        if (!CollectionUtils.isEmpty(template)) {
            map.put("3_group.p.or", "true");
            for (int i = 0; i < template.size(); i++) {
                map.put(GROUP_3 + (i + 1) + "_group.property", "jcr:content/cq:template");
                map.put(GROUP_3 + (i + 1) + "_group.property.value", template.get(i).getPath());
            }
        }

    }

    private List<SearchResultBean> getResources(Map<String, String> map, String type) {
        Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
        SearchResult result = query.getResult();

        // paging metadata
        LOGGER.info("Total number of results : {}", result.getTotalMatches());
        List<SearchResultBean> resource = new LinkedList<>();

        // add all the items to the result list
        for (Hit hit : query.getResult().getHits()) {
            try {
                LOGGER.debug("Hit : {}", hit.getPath());
                resource.add(setSearchResultItemData(hit, type));
            } catch (RepositoryException e) {
                LOGGER.error("[performSearch] There was an issue getting the resource", e);
            }
        }
        return resource;
    }

    private void setCommonMap(String fulltextSearchTerm, Map<String, String> map) {
        if (!fulltextSearchTerm.isEmpty()) {
            map.put("fulltext", "\"" + fulltextSearchTerm + "\"");
        }
        map.put("p.guessTotal", "true");
        map.put("orderby", "@jcr:score");
        map.put("p.limit", "-1");
    }

    private void setThemesMap(String[] themes, Map<String, String> map) {
        if (themes != null && themes.length > 0) {
            map.put("2_group.p.or", "true");
            for (int i = 0; i < themes.length; i++) {
                String tag = searchResultsModel.getThemeMap().get(themes[i]);
                map.put(GROUP_2 + (i + 1) + "_group.property", "jcr:content/cq:tags");
                map.put(GROUP_2 + (i + 1) + "_group.property.value", tag);
            }
        }
    }

    /**
     * Method to set search result item with all data.
     *
     * @param hit
     * @param type
     * @return
     * @throws RepositoryException
     */
    private SearchResultBean setSearchResultItemData(Hit hit, String type) throws RepositoryException {

        SearchResultBean searchResultItem = new SearchResultBean();
        searchResultItem.setType(type);
        searchResultItem.setPath(LinkUtils.sanitizeLink(hit.getPath()));
        searchResultItem.setTitle(hit.getTitle());
        searchResultItem.setDescription(hit.getProperties().get("jcr:description", StringUtils.EMPTY));

        return searchResultItem;
    }
}
