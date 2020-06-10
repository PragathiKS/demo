package com.tetrapak.publicweb.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.xss.XSSAPI;
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
import com.tetrapak.publicweb.core.beans.SearchBean;
import com.tetrapak.publicweb.core.beans.SearchResultBean;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.models.SearchResultsModel;
import com.tetrapak.publicweb.core.models.multifield.SearchPathModel;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import com.tetrapak.publicweb.core.utils.PageUtil;

@Component(
        service = Servlet.class,
        property = { Constants.SERVICE_DESCRIPTION + "=Tetra Pak - Public Web Search service",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "publicweb/components/content/searchresults" })
@Designate(ocd = SiteSearchServlet.Config.class)
public class SiteSearchServlet extends SlingSafeMethodsServlet {
    @ObjectClassDefinition(
            name = "Tetra Pak - Public Web Search Servlet",
            description = "Tetra Pak - Public Web Search servlet")
    public static @interface Config {

        @AttributeDefinition(
                name = "Search Root Path Variable Name",
                description = "Name of variable being sent by Front end to the servlet, that tells about the search root path.")
        int noOfResultsPerHit() default 10;
    }

    private static final String GROUP = "_group.";

    private static final String GROUP_2 = "2_group.";

    private static final String GROUP_3 = "3_group.";
    
    private static final String GROUP_TYPE = "_group.type";
    
    private static final String GROUP_PATH = "_group.path";

    private static final long serialVersionUID = 5220677543550980049L;

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteSearchServlet.class);

    @Reference
    private transient ResourceResolverFactory resolverFactory;

    private transient SearchResultsModel searchResultsModel;

    @Reference
    private transient QueryBuilder queryBuilder;

    @Reference
    private transient XSSAPI xssAPI;

    private transient Session session;

    private transient SearchBean searchBean;

    private int noOfResultsPerHit;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        LOGGER.info("Executing doGet method.");
        try {
            searchResultsModel = request.adaptTo(SearchResultsModel.class);

            // get resource resolver, session and queryBuilder objects.
            ResourceResolver resourceResolver = request.getResourceResolver();
            session = resourceResolver.adaptTo(Session.class);
            queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
            searchBean = new SearchBean();

            // get search arguments
            final String contentTypeParam = xssAPI.getValidJSON(request.getParameter("contentType"), StringUtils.EMPTY);
            String[] contentType = null;
            if (StringUtils.isNoneBlank(contentTypeParam)) {
                contentType = contentTypeParam.split(",");
            }
            final String themesParam = xssAPI.getValidJSON(request.getParameter("theme"), StringUtils.EMPTY);
            String[] themes = null;
            if (StringUtils.isNoneBlank(themesParam)) {
                themes = themesParam.split(",");
            }
            final int pageParam = xssAPI.getValidInteger(request.getParameter("page"), 1);
            String fulltextSearchTerm = URLDecoder
                    .decode(xssAPI.getValidJSON(request.getParameter("searchTerm"), StringUtils.EMPTY), "UTF-8")
                    .replace("%20", PWConstants.SPACE);
            LOGGER.info("Keyword to search : {}", fulltextSearchTerm);

            // search for resources
            Map<String, String> map = new HashMap<>();
            map.put("1_group.p.or", "true");
            if (ArrayUtils.isNotEmpty(contentType)) {
                int index = 1;
                for (String type : contentType) {
                    if (type.equalsIgnoreCase("media")) {
                        index = getMediaResults(map, index);

                    } else {
                        index = setContentTypeValToMap(type, map, index);
                    }
                }
            } else {
                map.put("1" + GROUP + 1 + GROUP_TYPE, "cq:Page");
                String pathKey = "1" + GROUP + 1 + GROUP_PATH;
                map.put(pathKey, PageUtil.getLanguagePage(searchResultsModel.getCurrentPage()).getPath());
                getMediaResults(map, 2);
            }
            setThemesMap(themes, map);
            setCommonMap(fulltextSearchTerm, map, pageParam);
            setSearchBean(map);
            Gson gson = new Gson();
            String responseJSON;
            responseJSON = gson.toJson(searchBean);
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
                map.put("1" + GROUP + index + GROUP_TYPE, "dam:Asset");
                String pathKey = "1" + GROUP + index + GROUP_PATH;
                map.put(pathKey, path.getPath());
                index++;
            }
        }
        return index;

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
                map.put("1" + GROUP + index + GROUP_TYPE, "cq:Page");
                String pathKey = "1" + GROUP + index + GROUP_PATH;
                map.put(pathKey, path.getPath());
                index++;
            }
        }
        return index;
    }

    private void setSearchBean(Map<String, String> map) {
        Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
        SearchResult result = query.getResult();
        long noOfResults = result.getTotalMatches();
        // paging metadata
        LOGGER.info("Total number of results : {}", noOfResults);

        searchBean.setTotalResults(noOfResults);
        int numberofPages = (int) Math.ceil((double) noOfResults / noOfResultsPerHit);
        searchBean.setTotalPages(numberofPages);
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
        searchBean.setSearchResults(resource);
    }

    private void setCommonMap(String fulltextSearchTerm, Map<String, String> map, int pageParam) {
        if (StringUtils.isNotBlank(fulltextSearchTerm)) {
            map.put("fulltext", "\"" + fulltextSearchTerm + "\"");
        }
        map.put("p.guessTotal", "true");
        map.put("orderby", "@jcr:score");

        // Excluding pages which have Hide in Search selected.
        map.put(GROUP_3 + "property", "@jcr:content/hideInSearch");
        map.put(GROUP_3 + "property.value", "false");
        map.put(GROUP_3 + "property.operation", "exists");
        map.put("p.limit", String.valueOf(noOfResultsPerHit));
        map.put("p.offset", String.valueOf(pageParam));
    }

    private void setThemesMap(String[] themes, Map<String, String> map) {
        if (themes != null && themes.length > 0) {
            map.put("2_group.p.or", "true");
            int index = 1;
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
        Resource resource = hit.getResource();

        if (null != resource) {
            // Add Metadata properties
            Resource metadataResource = resource.getChild("jcr:content/metadata");
            if (Objects.nonNull(metadataResource)) {
                searchResultItem.setType("MEDIA");
                ValueMap assetMetadataProperties = ResourceUtil.getValueMap(metadataResource);
                searchResultItem.setDescription(assetMetadataProperties.get("dc:description", StringUtils.EMPTY));
                searchResultItem.setSize(assetMetadataProperties.get("dam:size", StringUtils.EMPTY));
            } else {
                searchResultItem.setDescription(hit.getProperties().get("jcr:description", StringUtils.EMPTY));
                if (Objects.nonNull(hit.getProperties().get("cq:lastReplicated"))) {
                    searchResultItem.setDate(formatDate(hit.getProperties().get("cq:lastReplicated", Date.class)));
                }
            }
        }

        return searchResultItem;
    }

    public String formatDate(Date date) {
        final DateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        return formatter.format(date);
    }

    @Activate
    protected void activate(final Config config) {
        this.noOfResultsPerHit = config.noOfResultsPerHit();

    }
}
