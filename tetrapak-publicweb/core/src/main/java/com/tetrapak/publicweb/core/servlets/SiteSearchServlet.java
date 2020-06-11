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
import com.tetrapak.publicweb.core.utils.LinkUtils;
import com.tetrapak.publicweb.core.utils.SearchMapHelper;

/**
 * The Class SiteSearchServlet.
 */
@Component(
        service = Servlet.class,
        property = { Constants.SERVICE_DESCRIPTION + "=Tetra Pak - Public Web Search service",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "publicweb/components/content/searchresults" })
@Designate(ocd = SiteSearchServlet.Config.class)
public class SiteSearchServlet extends SlingSafeMethodsServlet {

    /**
     * The Interface Config.
     */
    @ObjectClassDefinition(
            name = "Tetra Pak - Public Web Search Servlet",
            description = "Tetra Pak - Public Web Search servlet")
    public static @interface Config {

        /**
         * No of results per hit.
         *
         * @return the int
         */
        @AttributeDefinition(
                name = "Search Root Path Variable Name",
                description = "Name of variable being sent by Front end to the servlet, that tells about the search root path.")
        int noOfResultsPerHit() default 10;
        
        /**
         * No of results per hit.
         *
         * @return the int
         */
        @AttributeDefinition(
                name = "Default Max Result Suggestion",
                description = "Default Max Result Suggestion.")
        int defaultMaxResultSuggestion() default 5000;
    }

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5220677543550980049L;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SiteSearchServlet.class);

    /** The resolver factory. */
    @Reference
    private transient ResourceResolverFactory resolverFactory;

    /** The search results model. */
    private transient SearchResultsModel searchResultsModel;

    /** The query builder. */
    @Reference
    private transient QueryBuilder queryBuilder;

    /** The xss API. */
    @Reference
    private transient XSSAPI xssAPI;

    /** The session. */
    private transient Session session;

    /** The search bean. */
    private transient SearchBean searchBean;

    /**  The template map. */
    private Map<String, String> templatesMap;

    /** The no of results per hit. */
    private int noOfResultsPerHit;
    
    /** The no of total max guess */
    private int guessTotal;

    /**
     * Do get.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        LOGGER.info("Executing doGet method.");
        try {
            searchResultsModel = request.adaptTo(SearchResultsModel.class);
            templatesMap = searchResultsModel.getTemplateMap();

            // get resource resolver, session and queryBuilder objects.
            ResourceResolver resourceResolver = request.getResourceResolver();
            session = resourceResolver.adaptTo(Session.class);
            queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
            searchBean = new SearchBean();

            // get search arguments
            final String contentTypeParam = xssAPI.getValidHref(request.getParameter("contentType"));
            String[] contentType = null;
            if (StringUtils.isNoneBlank(contentTypeParam)) {
                contentType = contentTypeParam.split(",");
            }
            final String themesParam = xssAPI.getValidHref(request.getParameter("theme"));
            String[] themes = null;
            if (StringUtils.isNoneBlank(themesParam)) {
                themes = themesParam.split(",");
            }
            final int pageParam = xssAPI.getValidInteger(request.getParameter("page"), 1);
            String fulltextSearchTerm = URLDecoder
                    .decode(xssAPI.getValidHref(request.getParameter("searchTerm")), "UTF-8")
                    .replace("%20", PWConstants.SPACE);
            LOGGER.info("Keyword to search : {}", fulltextSearchTerm);

            // search for resources
            int index = 1;
            Map<String, String> map = new HashMap<>();
            map.put("1_group.p.or", "true");
            if (!(ArrayUtils.isEmpty(contentType) && ArrayUtils.isEmpty(themes)
                    && StringUtils.isBlank(fulltextSearchTerm))) {
                if (ArrayUtils.isNotEmpty(contentType)) {
                    for (String type : contentType) {
                        if (type.equalsIgnoreCase("media")) {
                            index = SearchMapHelper.setMediaMap(map, index,searchResultsModel);
                        } else {
                            index = SearchMapHelper.setPageseMap(type, map, index, searchResultsModel);
                        }
                    }
                } else {
                    SearchMapHelper.setAllPagesMap(map,searchResultsModel);
                    index = SearchMapHelper.setMediaMap(map, 2, searchResultsModel);
                }
                SearchMapHelper.setCommonMap(fulltextSearchTerm, map, pageParam, noOfResultsPerHit, guessTotal);
                SearchMapHelper.setThemesMap(themes, map, searchResultsModel);
                SearchMapHelper.filterGatedContent(map, index, searchResultsModel);
                setSearchBean(map);
            }
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

    /**
     * Sets the search bean.
     *
     * @param map
     *            the map
     */
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

    /**
     * Method to set search result item with all data.
     *
     * @param hit
     *            the hit
     * @return the search result bean
     * @throws RepositoryException
     *             the repository exception
     */
    private SearchResultBean setSearchResultItemData(Hit hit) throws RepositoryException {

        SearchResultBean searchResultItem = new SearchResultBean();
        if (hit.getProperties().containsKey("cq:template")) {
            searchResultItem.setType(getProductContentType(hit.getProperties().get("cq:template").toString()));
        } else {
            searchResultItem.setType("pw.searchResults.media");
        }
        searchResultItem.setPath(LinkUtils.sanitizeLink(hit.getPath()));
        searchResultItem.setTitle(hit.getTitle());
        Resource resource = hit.getResource();

        if (null != resource) {
            // Add Metadata properties
            Resource metadataResource = resource.getChild("jcr:content/metadata");
            if (Objects.nonNull(metadataResource)) {
                searchResultItem.setType("pw.searchResults.contentPage");
                ValueMap assetMetadataProperties = ResourceUtil.getValueMap(metadataResource);
                searchResultItem.setDescription(assetMetadataProperties.get("dc:description", StringUtils.EMPTY));
                searchResultItem = setMediaSize(searchResultItem,assetMetadataProperties);
                searchResultItem.setAssetExtension(hit.getPath().substring(hit.getPath().lastIndexOf('.') + 1));
                searchResultItem.setAssetType(getMediaType(assetMetadataProperties));

            } else {
                searchResultItem.setDescription(hit.getProperties().get("jcr:description", StringUtils.EMPTY));
                if (Objects.nonNull(hit.getProperties().get("cq:lastReplicated"))) {
                    searchResultItem.setDate(formatDate(hit.getProperties().get("cq:lastReplicated", Date.class)));
                }
            }
        }

        return searchResultItem;
    }
    
    /**
     * Sets the media size.
     *
     * @param searchResultItem the search result item
     * @param assetMetadataProperties the asset metadata properties
     * @return the search result bean
     */
    private SearchResultBean setMediaSize(SearchResultBean searchResultItem, ValueMap assetMetadataProperties) {
        int size = Integer.parseInt(assetMetadataProperties.get("dam:size", StringUtils.EMPTY));
        if (size < 1024) {
            searchResultItem.setSize(String.valueOf(size));
            searchResultItem.setSizeType("pw.searchResults.bype");
            return searchResultItem;
        }
        int convertedSize = size / 1024;
        if (convertedSize > 1024) {
            convertedSize = convertedSize / 1024;
            searchResultItem.setSize(String.valueOf(convertedSize));
            searchResultItem.setSizeType("pw.searchResults.mbype");
        } else {
            searchResultItem.setSize(String.valueOf(convertedSize));
            searchResultItem.setSizeType("pw.searchResults.kbype");
        }

        return searchResultItem;
    }

    /**
     * Gets the product content type.
     *
     * @param template
     *            the template
     * @return the product content type
     */
    private String getProductContentType(String template) {
        String contentType = "pw.searchResults.contentPage";
        if (templatesMap != null && templatesMap.containsKey("news")
                && templatesMap.get("news").indexOf(template) >= 0) {
            contentType = "pw.searchResults.news";
        }
        if (templatesMap != null && templatesMap.containsKey("events")
                && templatesMap.get("events").indexOf(template) >= 0) {
            contentType = "pw.searchResults.event";
        }
        if (templatesMap != null && templatesMap.containsKey("products")
                && templatesMap.get("products").indexOf(template) >= 0) {
            contentType = "pw.searchResults.product";
        }
        if (templatesMap != null && templatesMap.containsKey("cases")
                && templatesMap.get("cases").indexOf(template) >= 0) {
            contentType = "pw.searchResults.case";
        }
        return contentType;
    }

    /**
     * Gets the media type.
     *
     * @param assetMetadataProperties
     *            the asset metadata properties
     * @return the media type
     */
    private String getMediaType(ValueMap assetMetadataProperties) {
        if (assetMetadataProperties.containsKey("dc:format")) {
            String dcFormat = assetMetadataProperties.get("dc:format", StringUtils.EMPTY);
            if (dcFormat.contains("image")) {
                return "image";
            } else if (dcFormat.contains("video")) {
                return "video";
            } else {
                return "document";
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * Format date.
     *
     * @param date
     *            the date
     * @return the string
     */
    public String formatDate(Date date) {
        final DateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        return formatter.format(date);
    }

    /**
     * Activate.
     *
     * @param config
     *            the config
     */
    @Activate
    protected void activate(final Config config) {
        this.noOfResultsPerHit = config.noOfResultsPerHit();
        this.guessTotal = config.defaultMaxResultSuggestion();

    }
}
