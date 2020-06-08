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

    private static final String GROUP = "_group.";

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
                Map<String, String> map = new HashMap<>();
                map.put("1_group.p.or", "true");
                if (ArrayUtils.isNotEmpty(contentType)) {
                    int index = 1;
                    for (String type : contentType) {
                        if (type.equals("media")) {
                            index = getMediaResults(map, index);
                            
                        } else {
                            index = setContentTypeValToMap(type, map, index);
                        }
                    }
                }else {
                    map.put("1" + GROUP + 1 + "_group.type", "cq:Page");
                    String pathKey = "1" + GROUP + 1 + "_group.path";
                    map.put(pathKey, PageUtil.getLanguagePage(searchResultsModel.getCurrentPage()).getPath());
                    getMediaResults(map,2);
                }
                setThemesMap(themes, map);
                setCommonMap(fulltextSearchTerm, map);
                resources.addAll(getResources(map));

            /*
             * else { Map<String, String> map = new HashMap<>(); map.put("type", "cq:Page"); map.put("path",
             * PageUtil.getLanguagePage(searchResultsModel.getCurrentPage()).getPath());
             * setCommonMap(fulltextSearchTerm, map); resources.addAll(getResources(map));
             * 
             * }
             */
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

    private int getMediaResults(Map<String, String> map, int index) {

        List<SearchPathModel> structure = searchResultsModel.getStructureMap().get("media");
        if (!CollectionUtils.isEmpty(structure)) {
            for (SearchPathModel path : structure) {
                map.put("1" + GROUP + index + "_group.type", "dam:Asset");
                String pathKey = "1" + GROUP + index + "_group.path";
                map.put(pathKey, path.getPath());
                index++;
            }
        }
        return index;
        /*
         * map.put("2_group.p.not", "false"); map.put(GROUP_2 + "1_path", searchResultsModel.getGatedPath());
         */

    }

    /**
     * Method to create a query and execute to get the results.
     *
     * @param fulltextSearchTerm
     * @param map
     * @param index2
     * @param searchRootPath
     * @return List<SearchResultBean>
     */
    public int setContentTypeValToMap(String type, Map<String, String> map, int index) {
        LOGGER.info("Executing setContentTypeValToMap method.");
        List<SearchPathModel> structure = searchResultsModel.getStructureMap().get(type);

        if (!CollectionUtils.isEmpty(structure)) {
            for (SearchPathModel path : structure) {
                map.put("1" + GROUP + index + "_group.type", "cq:Page");
                String pathKey = "1" + GROUP + index + "_group.path";
                map.put(pathKey, path.getPath());
                index++;
            }
        }
        return index;
    }

    private List<SearchResultBean> getResources(Map<String, String> map) {
        Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
        SearchResult result = query.getResult();

        // paging metadata
        LOGGER.info("Total number of results : {}", result.getTotalMatches());
        List<SearchResultBean> resource = new LinkedList<>();

        // add all the items to the result list
        for (Hit hit : query.getResult().getHits()) {
            try {
                LOGGER.debug("Hit : {}", hit.getPath());
                resource.add(setSearchResultItemData(hit));
            } catch (RepositoryException e) {
                LOGGER.error("[performSearch] There was an issue getting the resource", e);
            }
        }
        return resource;
    }

    private void setCommonMap(String fulltextSearchTerm, Map<String, String> map) {
        if (StringUtils.isNotBlank(fulltextSearchTerm)) {
            map.put("fulltext", "\"" + fulltextSearchTerm + "\"");
        }
        map.put("p.guessTotal", "true");
        map.put("orderby", "@jcr:score");

        // Excluding pages which have Hide in Search selected.
        map.put(GROUP_3+"property", "@jcr:content/hideInSearch");
        map.put(GROUP_3+"property.value", "false");
        map.put(GROUP_3+"property.operation", "exists");   
        map.put("p.limit", "-1");
    }

    private void setThemesMap(String[] themes, Map<String, String> map) {
        if (themes != null && themes.length > 0) {
            map.put("2_group.p.or", "true");
            int index=1;
            for (int i = 0; i < themes.length; i++) {
                String tag = searchResultsModel.getThemeMap().get(themes[i]);
                map.put(GROUP_2 + (i + 1) + "_group.property", "jcr:content/cq:tags");
                map.put(GROUP_2 + (i + 1) + "_group.property.value", tag);
                index++;
            }
            for (int i = 0; i < themes.length; i++) {
                String tag = searchResultsModel.getThemeMap().get(themes[i]);
                map.put(GROUP_2 + (index) + "_group.property", "jcr:content/metadata/cq:tags");
                map.put(GROUP_2 + (index) + "_group.property.value", tag);
                index++;
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
    private SearchResultBean setSearchResultItemData(Hit hit) throws RepositoryException {

        SearchResultBean searchResultItem = new SearchResultBean();
        searchResultItem.setType("type");
        searchResultItem.setPath(LinkUtils.sanitizeLink(hit.getPath()));
        searchResultItem.setTitle(hit.getTitle());
        searchResultItem.setDescription(hit.getProperties().get("jcr:description", StringUtils.EMPTY));

        return searchResultItem;
    }
}
