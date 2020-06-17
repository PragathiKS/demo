package com.tetrapak.publicweb.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import org.apache.sling.settings.SlingSettingsService;
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
import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import com.tetrapak.publicweb.core.utils.PageUtil;
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
        @AttributeDefinition(name = "Default Max Result Suggestion", description = "Default Max Result Suggestion.")
        int defaultMaxResultSuggestion() default 3000;
    }

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5220677543550980049L;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SiteSearchServlet.class);

    /** The resolver factory. */
    @Reference
    private transient ResourceResolverFactory resolverFactory;

    /** The query builder. */
    @Reference
    private transient QueryBuilder queryBuilder;

    /** The sling Settings Service. */
    @Reference
    private transient SlingSettingsService slingSettingsService;

    /** The dynamic Media Service. */
    @Reference
    private transient DynamicMediaService dynamicMediaService;

    /** The xss API. */
    @Reference
    private transient XSSAPI xssAPI;

    /** The session. */
    private transient Session session;

    /** The search bean. */
    private transient SearchBean searchBean;

    /** The template map. */
    private Map<String, String> templatesMap;

    /** The theme map. */
    private Map<String, String> themeMap;

    /** The no of results per hit. */
    private int noOfResultsPerHit;

    /** The no of total max guess. */
    private int guessTotal;

    /** The resource resolver. */
    private transient ResourceResolver resourceResolver;

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
            SearchResultsModel searchResultsModel = request.adaptTo(SearchResultsModel.class);
            templatesMap = searchResultsModel.getTemplateMap();
            themeMap = searchResultsModel.getThemeMap();

            // get resource resolver, session and queryBuilder objects.
            resourceResolver = request.getResourceResolver();
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

            if (isValidRequest(contentType, themes, fulltextSearchTerm)) {
                index = setContentMap(map, searchResultsModel, contentType, index);
                SearchMapHelper.setCommonMap(fulltextSearchTerm, map, pageParam, noOfResultsPerHit, guessTotal);
                SearchMapHelper.setThemesMap(themes, map, themeMap);
                SearchMapHelper.filterGatedContent(map, index, searchResultsModel);
                setSearchBean(map, searchResultsModel);
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
     * Checks if is valid request.
     *
     * @param contentTypes
     *            the content types
     * @param themes
     *            the themes
     * @param searchTerm
     *            the search term
     * @return true, if is valid request
     */
    private boolean isValidRequest(String[] contentTypes, String[] themes, String searchTerm) {
        boolean isValidRequest = true;
        if (StringUtils.isBlank(searchTerm) && ArrayUtils.isEmpty(contentTypes) && ArrayUtils.isEmpty(themes)) {
            isValidRequest = false;
        }
        if (ArrayUtils.isNotEmpty(contentTypes) && Boolean.FALSE.equals(isValidContentType(contentTypes))) {
            isValidRequest = false;
        }
        if (ArrayUtils.isNotEmpty(themes) && Boolean.FALSE.equals(isValidThemeType(themes))) {
            isValidRequest = false;
        }

        return isValidRequest;
    }

    /**
     * Sets the content map.
     *
     * @param map
     *            the map
     * @param searchResultsModel
     *            the search results model
     * @param contentType
     *            the content type
     * @param index
     *            the index
     * @return the int
     */
    private int setContentMap(Map<String, String> map, SearchResultsModel searchResultsModel, String[] contentType,
            int index) {
        if (ArrayUtils.isNotEmpty(contentType) && Boolean.TRUE.equals(isValidContentType(contentType))) {
            for (String type : contentType) {
                if (type.equalsIgnoreCase("media")) {
                    index = SearchMapHelper.setMediaMap(map, index, searchResultsModel);
                } else {
                    index = SearchMapHelper.setPageseMap(type, map, index, searchResultsModel);
                }
            }
        } else {
            SearchMapHelper.setAllPagesMap(map, searchResultsModel);
            index = SearchMapHelper.setMediaMap(map, 2, searchResultsModel);
        }
        return index;
    }

    /**
     * Checks if is valid content type.
     *
     * @param contentTypes
     *            the content types
     * @return the boolean
     */
    private Boolean isValidContentType(String[] contentTypes) {
        Boolean isValidContentType = false;
        for (String contentType : contentTypes) {
            if (PWConstants.NEWS.equalsIgnoreCase(contentType.toLowerCase())
                    || PWConstants.EVENTS.equalsIgnoreCase(contentType)
                    || PWConstants.PRODUCTS.equalsIgnoreCase(contentType)
                    || PWConstants.CASES.equalsIgnoreCase(contentType)
                    || PWConstants.MEDIA.equalsIgnoreCase(contentType)) {
                isValidContentType = true;
            }
        }
        return isValidContentType;
    }

    /**
     * Checks if is valid theme type.
     *
     * @param themes
     *            the themes
     * @return the boolean
     */
    private Boolean isValidThemeType(String[] themes) {
        Boolean isValidThemeType = false;
        for (String theme : themes) {
            if (themeMap != null && themeMap.containsKey(theme)) {
                isValidThemeType = true;
            }
        }
        return isValidThemeType;
    }

    /**
     * Sets the search bean.
     *
     * @param map
     *            the map
     * @param searchResultsModel
     *            the search results model
     */
    private void setSearchBean(Map<String, String> map, SearchResultsModel searchResultsModel) {
        Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
        SearchResult result = query.getResult();
        long noOfResults = result.getTotalMatches();

        // paging metadata
        LOGGER.info("Total number of results : {}", noOfResults);

        // 1 Extra Result Count is Coming
        if (noOfResults > 1) {
            noOfResults = noOfResults - 1;
        }

        searchBean.setTotalResults(noOfResults);
        int numberofPages = (int) Math.ceil((double) noOfResults / noOfResultsPerHit);
        searchBean.setTotalPages(numberofPages);
        List<SearchResultBean> resource = new LinkedList<>();

        // add all the items to the result list
        for (Hit hit : query.getResult().getHits()) {
            try {
                LOGGER.debug("Hit : {}", hit.getPath());
                resource.add(setSearchResultItemData(hit, searchResultsModel));
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
     * @param searchResultsModel
     *            the search results model
     * @return the search result bean
     * @throws RepositoryException
     *             the repository exception
     */
    private SearchResultBean setSearchResultItemData(Hit hit, SearchResultsModel searchResultsModel)
            throws RepositoryException {

        SearchResultBean searchResultItem = new SearchResultBean();       
        Resource resource = hit.getResource();

        if (null != resource) {
            // Add Metadata properties
            Resource metadataResource = resource.getChild("jcr:content/metadata");
            if (Objects.nonNull(metadataResource)) {
                searchResultItem.setType(searchResultsModel.getMediaLabel());
                ValueMap assetMetadataProperties = ResourceUtil.getValueMap(metadataResource);
                String mediaType = getMediaType(assetMetadataProperties);
                setMediaPath(searchResultItem, hit.getPath(), mediaType);
                String mediaTitle = assetMetadataProperties.get("dc:title", StringUtils.EMPTY);
                if(StringUtils.isBlank(mediaTitle)) {
                    mediaTitle = hit.getTitle(); 
                }
                searchResultItem.setTitle(mediaTitle);
                setMediaSize(searchResultItem, assetMetadataProperties);
                searchResultItem.setAssetExtension(hit.getPath().substring(hit.getPath().lastIndexOf('.') + 1));
                searchResultItem.setAssetType(mediaType);
                if(PWConstants.VIDEO.equalsIgnoreCase(mediaType)) {
                    searchResultItem.setAssetThumbnail(searchResultsModel.getVideoThumbnail());
                }
                if(PWConstants.DOCUMENT.equalsIgnoreCase(mediaType)) {
                    searchResultItem.setAssetThumbnail(searchResultsModel.getDocumentThumbnail());
                }
            } else {
                searchResultItem.setTitle(PageUtil.getCurrentPage(hit.getResource()).getTitle());
                searchResultItem.setPath(LinkUtils.sanitizeLink(hit.getPath()));
                searchResultItem.setDescription(hit.getProperties().get("jcr:description", StringUtils.EMPTY));
                setContentFields(searchResultItem, hit, searchResultsModel);
            }
        }
        return searchResultItem;

    }

    /**
     * Sets the media path.
     *
     * @param searchResultItem
     *            the search result item
     * @param path
     *            the path
     * @param mediaType
     *            the media type
     */
    private void setMediaPath(SearchResultBean searchResultItem, String path, String mediaType) {
        if (!slingSettingsService.getRunModes().contains("author") && null != dynamicMediaService) {
            if (PWConstants.VIDEO.equalsIgnoreCase(mediaType)) {
                searchResultItem.setPath(GlobalUtil.getVideoUrlFromScene7(resourceResolver, path, dynamicMediaService));
            } else if (PWConstants.IMAGE.equalsIgnoreCase(mediaType)) {
                searchResultItem.setPath(GlobalUtil.getImageUrlFromScene7(resourceResolver, path, dynamicMediaService));
            } else {
                searchResultItem.setPath(LinkUtils.sanitizeLink(path));
            }
        } else {
            searchResultItem.setPath(LinkUtils.sanitizeLink(path));
        }
    }

    /**
     * Sets the media size.
     *
     * @param searchResultItem
     *            the search result item
     * @param assetMetadataProperties
     *            the asset metadata properties
     * @return the search result bean
     */
    private SearchResultBean setMediaSize(SearchResultBean searchResultItem, ValueMap assetMetadataProperties) {
        double size = 0;
        try {
            size = Integer.parseInt(assetMetadataProperties.get("dam:size", StringUtils.EMPTY));
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid Asset Size", e.getMessage(), e);
        }
        double convertedSize = size / 1024;
        if (convertedSize > 1024) {
            convertedSize = convertedSize / 1024;
            searchResultItem
                    .setSize(String.valueOf(BigDecimal.valueOf(convertedSize).setScale(1, RoundingMode.HALF_UP)));
            searchResultItem.setSizeType("pw.searchResults.mbyte");
        } else {
            searchResultItem
                    .setSize(String.valueOf(BigDecimal.valueOf(convertedSize).setScale(1, RoundingMode.HALF_UP)));
            searchResultItem.setSizeType("pw.searchResults.kbyte");
        }
        return searchResultItem;
    }

    /**
     * Gets the product content type.
     *
     * @param searchResultItem
     *            the search result item
     * @param hit
     *            the hit
     * @param searchResultsModel
     *            the search results model
     * @return the product content type
     * @throws RepositoryException
     *             the repository exception
     */
    private void setContentFields(SearchResultBean searchResultItem, Hit hit, SearchResultsModel searchResultsModel)
            throws RepositoryException {

        String contentType = getContentTypeFromTemplate(hit);
        if (StringUtils.isBlank(contentType)) {
            searchResultItem.setType("pw.searchResults.contentPage");
        } else {
            setContentTypeAndDate(searchResultItem, hit, contentType, searchResultsModel);
        }

    }

    /**
     * Sets the content type and date.
     *
     * @param searchResultItem
     *            the search result item
     * @param hit
     *            the hit
     * @param contentType
     *            the content type
     * @param searchResultsModel
     *            the search results model
     * @throws RepositoryException
     *             the repository exception
     */
    private void setContentTypeAndDate(SearchResultBean searchResultItem, Hit hit, String contentType,
            SearchResultsModel searchResultsModel) throws RepositoryException {
        if (PWConstants.PRODUCTS.equalsIgnoreCase(contentType)) {
            searchResultItem.setType(searchResultsModel.getProductLabel());
        } else {
            if (Objects.nonNull(hit.getProperties().get("cq:lastModified"))) {
                searchResultItem.setDate(formatDate(hit.getProperties().get("cq:lastModified", Date.class)));
            }
            if (PWConstants.EVENTS.equalsIgnoreCase(contentType)) {
                searchResultItem.setType(searchResultsModel.getEventLabel());
            }
            if (PWConstants.NEWS.equalsIgnoreCase(contentType)) {
                searchResultItem.setType(searchResultsModel.getNewsLabel());
            }
            if (PWConstants.CASES.equalsIgnoreCase(contentType)) {
                searchResultItem.setType(searchResultsModel.getCaseLabel());
            }
        }

    }

    /**
     * Gets the content type from template.
     *
     * @param hit
     *            the hit
     * @return the content type from template
     * @throws RepositoryException
     *             the repository exception
     */
    private String getContentTypeFromTemplate(Hit hit) throws RepositoryException {
        if (hit.getProperties().containsKey(PWConstants.CQ_TEMPLATE)) {
            String template = hit.getProperties().get(PWConstants.CQ_TEMPLATE).toString();
            if (templatesMap != null && templatesMap.containsKey(PWConstants.NEWS)
                    && templatesMap.get(PWConstants.NEWS).indexOf(template) >= 0) {
                return PWConstants.NEWS;
            }
            if (templatesMap != null && templatesMap.containsKey(PWConstants.EVENTS)
                    && templatesMap.get(PWConstants.EVENTS).indexOf(template) >= 0) {
                return PWConstants.EVENTS;
            }
            if (templatesMap != null && templatesMap.containsKey(PWConstants.PRODUCTS)
                    && templatesMap.get(PWConstants.PRODUCTS).indexOf(template) >= 0) {
                return PWConstants.PRODUCTS;
            }
            if (templatesMap != null && templatesMap.containsKey(PWConstants.CASES)
                    && templatesMap.get(PWConstants.CASES).indexOf(template) >= 0) {
                return PWConstants.CASES;
            }
        }
        return StringUtils.EMPTY;
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
