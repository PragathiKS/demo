package com.tetralaval.services.impl;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagConstants;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.NameConstants;
import com.tetralaval.config.SearchResultsConfiguration;
import com.tetralaval.constants.TLConstants;
import com.tetralaval.models.search.FilterModel;
import com.tetralaval.models.search.ResultItem;
import com.tetralaval.models.search.ResultModel;
import com.tetralaval.services.ArticleService;
import com.tetralaval.services.SearchResultsService;
import com.tetralaval.utils.LinkUtils;
import com.tetralaval.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.xss.XSSAPI;
import org.apache.sling.xss.XSSFilter;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SearchResultsServiceImpl
 */
@Component(immediate = true, service = SearchResultsService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = SearchResultsConfiguration.class)
public class SearchResultsServiceImpl implements SearchResultsService {
    /** LOGGER constant */
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchResultsServiceImpl.class);

    /** FULLTEXT_PARAM constant */
    private static final String FULLTEXT_PARAM = "searchTerm";
    /** ARTICLE_TYPE_PARAM constant */
    private static final String ARTICLE_TYPE_PARAM = "contentType";
    /** TAGS_PARAM constant */
    private static final String TAGS_PARAM = "theme";
    /** PAGE_PARAM constant */
    private static final String PAGE_PARAM = "page";

    /** HIDE_IN_SEARCH_PROP constant */
    private static final String HIDE_IN_SEARCH_PROP = "hideInSearch";
    /** ARTICLE_DATE_PROP constant */
    private static final String ARTICLE_DATE_PROP = "articleDate";
    /** ARTICLE_TYPE_PROP constant */
    private static final String ARTICLE_TYPE_PROP = "articleType";
    /** MEDIA_LABEL_PROP constant */
    private static final String MEDIA_LABEL_PROP = "mediaLabel";
    /** ASSETS_PATH_PROP constant */
    private static final String ASSETS_PATH_PROP = "assetsPath";
    /** VIDEO_THUMBNAIL_PROP constant */
    private static final String VIDEO_THUMBNAIL_PROP = "videoThumbnail";
    /** DOCUMENT_THUMBNAIL_PROP constant */
    private static final String DOCUMENT_THUMBNAIL_PROP = "documentThumbnail";

    /** RESOURCES_GROUP constant */
    private static final String RESOURCES_GROUP = "3_group";
    /** PAGES_GROUP constant */
    private static final String PAGES_GROUP = String.format("%s.1_group", RESOURCES_GROUP);
    /** ASSETS_GROUP constant */
    private static final String ASSETS_GROUP = String.format("%s.2_group", RESOURCES_GROUP);
    /** TAGS_GROUP constant */
    private static final String TAGS_GROUP = "1_group";
    /** TEMPLATES_GROUP constant */
    private static final String TEMPLATES_GROUP = "2_group";

    /** SECOND_LEVEL_OF_GROUP_PLACEHOLDER constant */
    private static final String SECOND_LEVEL_OF_GROUP_PLACEHOLDER = "%s.%s.p.or";

    /** KILO_BYTES_VALUE constant */
    private static final int KILO_BYTES_VALUE = 1024;

    /** ARTICLE_TEMPLATE constant */
    private static final String ARTICLE_TEMPLATE = "/conf/tetra-laval/settings/wcm/templates/article-page-template";

    /** xssAPI */
    @Reference
    private XSSAPI xssAPI;

    /** xssFilter */
    @Reference
    private XSSFilter xssFilter;

    /** queryBuilder */
    @Reference
    private QueryBuilder queryBuilder;

    /** articleService */
    @Reference
    private ArticleService articleService;

    /** path */
    private String path;
    /** mediaLabel */
    private String mediaLabel;
    /** mediaId */
    private String mediaId;
    /** assetsPath */
    private String assetsPath;
    /** videoThumbnail */
    private String videoThumbnail;
    /** documentThumbnail */
    private String documentThumbnail;

    /** config */
    private SearchResultsConfiguration config;

    /**
     * activate method
     * @param config
     */
    @Activate
    public void activate(SearchResultsConfiguration config) {
        this.config = config;
    }

    /**
     * itemsPerPage getter
     * @return itemsPerPage
     */
    @Override
    public int getItemsPerPage() {
        return config.itemsPerPage();
    }

    /**
     * maxResultSuggestions getter
     * @return maxResultSuggestions
     */
    @Override
    public int getMaxResultSuggestions() {
        return config.maxResultSuggestions();
    }

    /**
     * Return search query map
     * @param request
     * @param params
     * @return
     */
    @Override
    public Map<String, String> setSearchQueryMap(SlingHttpServletRequest request, RequestParameterMap params) {
        setAssetsProperties(request);
        path = getLanguagePagePath(request);

        Map<String, String> map = new HashMap<>();
        map.put("104_orderby", "@jcr:content/articleDate");
        map.put("105_orderby", "@jcr:content/cq:lastModified");
        map.put("106_orderby", "@jcr:content/jcr:lastModified");
        map.put("104_orderby.sort", "desc");
        map.put("105_orderby.sort", "desc");
        map.put("106_orderby.sort", "desc");
        List<FilterModel> filterModelList = getTags(request, params);
        String[] articleTypes = getArticleTypes(params);
        boolean isMediaChecked = false;

        if (articleTypes != null && mediaId != null) {
            isMediaChecked = !Arrays.stream(articleTypes)
                    .filter(s -> mediaId.equals(s))
                    .collect(Collectors.toList()).isEmpty();
        }

        map.putAll(getFulltext(params));
        map.putAll(setResultsAmount(params));

        map.put(String.format("%s.p.or", RESOURCES_GROUP), "true");
        map.put(String.format(SECOND_LEVEL_OF_GROUP_PLACEHOLDER, PAGES_GROUP, TAGS_GROUP), "true");
        map.put(String.format(SECOND_LEVEL_OF_GROUP_PLACEHOLDER, PAGES_GROUP, TEMPLATES_GROUP), "true");

        if (isMediaChecked) {
            map.put(String.format(SECOND_LEVEL_OF_GROUP_PLACEHOLDER, ASSETS_GROUP, TAGS_GROUP), "true");
        }

        map.putAll(setResources(isMediaChecked));
        if (filterModelList != null) {
            map.putAll(setTags(PAGES_GROUP, String.format("%s/%s", JcrConstants.JCR_CONTENT,
                    TagConstants.PN_TAGS), filterModelList));

            if (isMediaChecked) {
                map.putAll(setTags(ASSETS_GROUP, String.format("%s/%s/%s", JcrConstants.JCR_CONTENT,
                        DamConstants.METADATA_FOLDER, TagConstants.PN_TAGS), filterModelList));
            }
        }
        map.putAll(setTemplates(articleTypes));
        return map;
    }

    /**
     * Get filters
     * @param request
     * @param tags
     * @return list of FilterModel
     */
    @Override
    public List<FilterModel> getFilters(SlingHttpServletRequest request, String[] tags) {
        List<FilterModel> filterModels = new ArrayList<>();
        TagManager tagManager = request.getResource().getResourceResolver().adaptTo(TagManager.class);
        if (tagManager != null && tags != null) {
            for (String tagString : tags) {
                Tag tag = tagManager.resolve(decodeColonInKey(tagString));
                FilterModel filterModel = new FilterModel();
                filterModel.setLabel(tag.getTitle());
                filterModel.setKey(encodeColonInKey(tagString));
                filterModels.add(filterModel);
            }
        }
        return filterModels;
    }

    /**
     * Encode colon in tag key
     * @param text
     * @return
     */
    private String encodeColonInKey(String text) {
        return text.replaceAll("\\:+", "-fc-");
    }

    /**
     * Decode colon in tag key
     * @param text
     * @return
     */
    private String decodeColonInKey(String text) {
        return text.replaceAll("(-fc-)+", TLConstants.COLON);
    }

    /**
     * Get results
     * @param request
     * @param map
     * @return ResultModel
     */
    @Override
    public ResultModel getResults(SlingHttpServletRequest request, Map<String, String> map) {
        ResultModel resultModel = new ResultModel();

        ResourceResolver resourceResolver = request.getResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);

        Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
        SearchResult results = query.getResult();
        long resultsAmount = results.getTotalMatches();

        resultModel.setTotalResults(resultsAmount);
        resultModel.setTotalPages((int) Math.ceil((double) resultsAmount / getItemsPerPage()));

        List<ResultItem> resource = new LinkedList<>();
        for (Hit hit : query.getResult().getHits()) {
            try {
                resource.add(setSearchResultItemData(request, hit));
            } catch (RepositoryException e) {
                LOGGER.error("[performSearch] There was an issue getting the resource", e);
            }
        }
        resultModel.setSearchResults(resource);
        return resultModel;
    }

    /**
     * Set mediaId based on mediaLabel
     * @param mediaLabel
     * @return mediaId
     */
    @Override
    public String setMediaId(String mediaLabel) {
        if (mediaLabel != null) {
            return mediaLabel.toLowerCase()
                    .replaceAll("\\s+", TLConstants.HYPHEN);
        }
        return null;
    }

    /**
     * Get fulltext
     * @param params
     * @return
     */
    private Map<String, String> getFulltext(RequestParameterMap params) {
        String fulltext = null;
        RequestParameter fulltextParam = params.getValue(FULLTEXT_PARAM);
        if (fulltextParam != null && StringUtils.isNotBlank(fulltextParam.getString())) {
            fulltext = xssFilter.filter(fulltextParam.getString());
            if (fulltext.contains("&lt;")) {
                fulltext = fulltext.replace("&lt;", StringUtils.EMPTY);
            }
            if (fulltext.contains("&gt;")) {
                fulltext = fulltext.replace("&gt;", StringUtils.EMPTY);
            }
            fulltext = String.format("\"%s\"", fulltext);
        }

        // fetched only these results which contains specific query text
        Map<String, String> map = new HashMap<>();

        if (fulltext != null) {
            map.put("fulltext", fulltext);
        } else {
            map.put("fulltext", StringUtils.EMPTY);
        }
        return map;
    }

    /**
     * Get articleTypes
     * @param params
     * @return array of articleTypes
     */
    private String[] getArticleTypes(RequestParameterMap params) {
        String[] articleTypes = null;
        RequestParameter articleTypesParam = params.getValue(ARTICLE_TYPE_PARAM);
        if (articleTypesParam != null) {
            articleTypes = articleTypesParam.getString().split(TLConstants.COMA);
        }
        return articleTypes;
    }

    /**
     * Get tags
     * @param request
     * @param params
     * @return list of FilterModel
     */
    private List<FilterModel> getTags(SlingHttpServletRequest request, RequestParameterMap params) {
        List<FilterModel> filters = null;
        RequestParameter tagsParam = params.getValue(TAGS_PARAM);
        if (tagsParam != null) {
            filters = getFilters(request, tagsParam.getString().split(TLConstants.COMA));
        }
        return filters;
    }

    /**
     * Get page number
     * @param params
     * @return pageNumber
     */
    private int getPageNumber(RequestParameterMap params) {
        int page = 1;
        RequestParameter pageParam = params.getValue(PAGE_PARAM);
        if (pageParam != null) {
            page = xssAPI.getValidInteger(pageParam.getString(), 1);
        }
        return page;
    }

    /**
     * Set results amount
     * @param params
     * @return
     */
    private Map<String, String> setResultsAmount(RequestParameterMap params) {
        Map<String, String> map = new HashMap<>();

        // set amount of results
        map.put("p.guessTotal", String.valueOf(getMaxResultSuggestions()));
        map.put("p.offset", String.valueOf((getPageNumber(params) - 1) * getItemsPerPage()));
        map.put("p.limit", String.valueOf(getItemsPerPage()));
        return map;
    }

    /**
     * Set resources
     * @param isMediaChecked
     * @return
     */
    private Map<String, String> setResources(boolean isMediaChecked) {
        Map<String, String> map = new HashMap<>();

        // creating map configuration for different resources
        map.put(String.format("%s.type", PAGES_GROUP), NameConstants.NT_PAGE);
        map.put(String.format("%s.path", PAGES_GROUP), path);

        if (isMediaChecked) {
            map.put(String.format("%s.type", ASSETS_GROUP), DamConstants.NT_DAM_ASSET);
            map.put(String.format("%s.path", ASSETS_GROUP), assetsPath);
        }

        // fetched only these results which do not have checked hideInSearch flag
        map.put(String.format("%s.property", PAGES_GROUP),
                String.format("@%s/%s", JcrConstants.JCR_CONTENT, HIDE_IN_SEARCH_PROP));
        map.put(String.format("%s.property.operation", PAGES_GROUP), "exists");
        map.put(String.format("%s.property.value", PAGES_GROUP), "false");
        return map;
    }

    /**
     * Set tags
     * @param resourceGroup
     * @param cqTagsProp
     * @param filterModelList
     * @return
     */
    private Map<String, String> setTags(String resourceGroup, String cqTagsProp, List<FilterModel> filterModelList) {
        Map<String, String> map = new HashMap<>();

        // generating map using tags selected in search result filters
        if (!filterModelList.isEmpty()) {
            int tagIndex = 0;
            for (FilterModel filterModel : filterModelList) {
                tagIndex++;
                map.put(String.format("%s.1_group.%d_property", resourceGroup, tagIndex), cqTagsProp);
                map.put(String.format("%s.1_group.%d_property.value", resourceGroup, tagIndex), decodeColonInKey(filterModel.getKey()));
            }
        }
        return map;
    }

    /**
     * Set templates
     * @param articleTypes
     * @return
     */
    private Map<String, String> setTemplates(String[] articleTypes) {
        Map<String, String> map = new HashMap<>();

        // fetched only these pages which have the same type as selected in filters
        if (articleTypes != null && articleTypes.length > 0) {
            int articleIndex = 0;
            for (String articleType : articleTypes) {
                articleIndex++;
                map.put(String.format("%s.%s.%d_group.1_property", PAGES_GROUP, TEMPLATES_GROUP, articleIndex),
                        String.format("@%s/%s", JcrConstants.JCR_CONTENT, NameConstants.NN_TEMPLATE));
                map.put(String.format("%s.%s.%d_group.1_property.value", PAGES_GROUP, TEMPLATES_GROUP, articleIndex), ARTICLE_TEMPLATE);
                map.put(String.format("%s.%s.%d_group.2_property", PAGES_GROUP, TEMPLATES_GROUP, articleIndex),
                        String.format("@%s/articleType", JcrConstants.JCR_CONTENT));
                map.put(String.format("%s.%s.%d_group.2_property.value", PAGES_GROUP, TEMPLATES_GROUP, articleIndex), articleType);
            }
        }
        return map;
    }

    /**
     * Get languagePage path
     * @param request
     * @return languagePage path
     */
    private String getLanguagePagePath(SlingHttpServletRequest request) {
        return String.join(TLConstants.SLASH, Arrays.stream(request.getPathInfo()
                .split(TLConstants.SLASH))
                .collect(Collectors.toList())
                .subList(0, TLConstants.CHAPTER_LEVEL + 1));
    }

    /**
     * Set search result item details
     * @param request
     * @param hit
     * @return resultItem
     * @throws RepositoryException
     */
    private ResultItem setSearchResultItemData(SlingHttpServletRequest request, Hit hit) throws RepositoryException {
        ResultItem resultItem = new ResultItem();

        Resource resource = hit.getResource();
        if (resource != null) {
            Resource metadataResource = resource.getChild(String.format("%s/%s", JcrConstants.JCR_CONTENT, DamConstants.METADATA_FOLDER));
            if (metadataResource != null) {
                ValueMap assetMetadataProperties = ResourceUtil.getValueMap(metadataResource);
                String mediaTitle = assetMetadataProperties.get(DamConstants.DC_TITLE, StringUtils.EMPTY);
                String mediaType = getMediaType(assetMetadataProperties);

                resultItem.setType(mediaLabel);

                if (StringUtils.isBlank(mediaTitle)) {
                    resultItem.setTitle(hit.getTitle());
                } else {
                    resultItem.setTitle(mediaTitle);
                }

                setMediaSize(resultItem, assetMetadataProperties);
                resultItem.setAssetExtension(hit.getPath().substring(hit.getPath().lastIndexOf(TLConstants.DOT) + 1));
                resultItem.setAssetType(mediaType);
                if (TLConstants.VIDEO.equalsIgnoreCase(mediaType)) {
                    resultItem.setAssetThumbnail(videoThumbnail);
                }
                if (TLConstants.DOCUMENT.equalsIgnoreCase(mediaType)) {
                    resultItem.setAssetThumbnail(documentThumbnail);
                }
            } else {
                resultItem.setTitle(PageUtil.getCurrentPage(resource).getTitle());
                resultItem.setDescription(hit.getProperties().get(JcrConstants.JCR_DESCRIPTION, StringUtils.EMPTY));
                setContentFields(resultItem, hit);
            }
            resultItem.setPath(LinkUtils.sanitizeLink(hit.getPath(), request));
        }
        return resultItem;
    }

    /**
     * Set sort date method
     * @param hit
     * @param cls
     * @param <T>
     * @return
     * @throws RepositoryException
     */
    private <T> T getSortDate(Hit hit, Class<T> cls) throws RepositoryException {
        T sortDate = null;
        if (hit.getProperties().containsKey(ARTICLE_DATE_PROP)) {
            sortDate = hit.getProperties().get(ARTICLE_DATE_PROP, cls);
        } else if (hit.getProperties().containsKey(NameConstants.PN_PAGE_LAST_MOD)) {
            sortDate = hit.getProperties().get(NameConstants.PN_PAGE_LAST_MOD, cls);
        } else if (hit.getProperties().containsKey(JcrConstants.JCR_LASTMODIFIED)) {
            sortDate = hit.getProperties().get(JcrConstants.JCR_LASTMODIFIED, cls);
        }
        return sortDate;
    }

    /**
     * Set content fields
     * @param resultItem
     * @param hit
     * @throws RepositoryException
     */
    private void setContentFields(ResultItem resultItem, Hit hit) throws RepositoryException {
        String articleTypeName = TLConstants.PAGE_TYPE;

        String articleType = hit.getProperties().get(ARTICLE_TYPE_PROP, String.class);
        String date = getSortDate(hit, String.class);

        List<FilterModel> filters = articleService.getFilterTypes().stream()
                .filter(filterModel -> filterModel.getKey().equals(articleType))
                .collect(Collectors.toList());

        if (!filters.isEmpty()) {
            articleTypeName = filters.get(0).getLabel();
        }
        resultItem.setType(articleTypeName);
        resultItem.setDate(formatDate(date));
    }

    /**
     * Get media type
     * @param assetMetadataProperties
     * @return
     */
    private String getMediaType(ValueMap assetMetadataProperties) {
        String mediaType = StringUtils.EMPTY;
        if (assetMetadataProperties.containsKey(DamConstants.DC_FORMAT)) {
            String dcFormat = assetMetadataProperties.get(DamConstants.DC_FORMAT, StringUtils.EMPTY);
            if (dcFormat.contains(TLConstants.IMAGE)) {
                mediaType = TLConstants.IMAGE;
            } else if (dcFormat.contains(TLConstants.VIDEO)) {
                mediaType = TLConstants.VIDEO;
            } else {
                mediaType = TLConstants.DOCUMENT;
            }
        }
        return mediaType;
    }

    /**
     * Set media size
     * @param resultItem
     * @param assetMetadataProperties
     * @return
     */
    private ResultItem setMediaSize(ResultItem resultItem, ValueMap assetMetadataProperties) {
        double size = 0;
        try {
            size = Integer.parseInt(assetMetadataProperties.get(DamConstants.DAM_SIZE, StringUtils.EMPTY));
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid Asset Size {} {}", e.getMessage(), e);
        }
        double convertedSize = size / KILO_BYTES_VALUE;
        if (convertedSize > KILO_BYTES_VALUE) {
            convertedSize = convertedSize / KILO_BYTES_VALUE;
            resultItem.setSize(String.valueOf(BigDecimal.valueOf(convertedSize)
                    .setScale(1, RoundingMode.HALF_UP)));
            resultItem.setSizeType("MB");
        } else {
            resultItem.setSize(String.valueOf(BigDecimal.valueOf(convertedSize)
                    .setScale(1, RoundingMode.HALF_UP)));
            resultItem.setSizeType("KB");
        }
        return resultItem;
    }

    /**
     * Set assets properties
     * @param request
     */
    private void setAssetsProperties(SlingHttpServletRequest request) {
        Resource resource = request.getResource().getResourceResolver()
                .resolve(String.format("%s%s", request.getResource().getPath(), "/root/responsivegrid/searchresults"));
        if (resource.adaptTo(Node.class) != null) {
            Node node = resource.adaptTo(Node.class);
            try {
                if (node.hasProperty(MEDIA_LABEL_PROP)) {
                    mediaLabel = node.getProperty(MEDIA_LABEL_PROP).getString();
                } else {
                    mediaLabel = null;
                }
                mediaId = setMediaId(mediaLabel);

                if (node.hasProperty(ASSETS_PATH_PROP)) {
                    assetsPath = node.getProperty(ASSETS_PATH_PROP).getString();
                } else {
                    assetsPath = TLConstants.DAM_ROOT_PATH;
                }

                if (node.hasProperty(VIDEO_THUMBNAIL_PROP)) {
                    videoThumbnail = node.getProperty(VIDEO_THUMBNAIL_PROP).getString();
                } else {
                    videoThumbnail = null;
                }

                if (node.hasProperty(DOCUMENT_THUMBNAIL_PROP)) {
                    documentThumbnail = node.getProperty(DOCUMENT_THUMBNAIL_PROP).getString();
                } else {
                    documentThumbnail = null;
                }
            } catch (RepositoryException re) {
                LOGGER.error("setAssetsProperties:: RepositoryException {} {}", re.getMessage(), re);
            }
        }
    }

    /**
     * Set proper format date
     * @param dateString
     * @return
     */
    private String formatDate(String dateString) {
        if (dateString != null && dateString.length() > 0 && dateString.contains("T")) {
            final String parsedDate = dateString.substring(0, dateString.indexOf("T"));
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date d = null;
            if (parsedDate.length() > 0) {
                try {
                    d = formatter.parse(parsedDate);
                } catch (final ParseException e) {
                    LOGGER.error("Error occurred while parsing date: {} ", e.getMessage(), e);
                }
            }
            formatter = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            return formatter.format(d);
        }
        return StringUtils.EMPTY;
    }

}
