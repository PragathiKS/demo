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

@Component(immediate = true, service = SearchResultsService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = SearchResultsConfiguration.class)
public class SearchResultsServiceImpl implements SearchResultsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchResultsServiceImpl.class);

    private final static String FULLTEXT_PARAM = "searchTerm";
    private final static String ARTICLE_TYPE_PARAM = "contentType";
    private final static String TAGS_PARAM = "theme";
    private final static String PAGE_PARAM = "page";

    private final static String HIDE_IN_SEARCH_PROP = "hideInSearch";
    private final static String ARTICLE_DATE_PROP = "articleDate";
    private final static String ARTICLE_TYPE_PROP = "articleType";
    private final static String MEDIA_LABEL_PROP = "mediaLabel";
    private final static String VIDEO_THUMBNAIL_PROP = "videoThumbnail";
    private final static String DOCUMENT_THUMBNAIL_PROP = "documentThumbnail";

    private final static String RESOURCES_GROUP = "3_group";
    private final static String PAGES_GROUP = String.format("%s.1_group", RESOURCES_GROUP);
    private final static String ASSETS_GROUP = String.format("%s.2_group", RESOURCES_GROUP);
    private final static String TAGS_GROUP = "1_group";
    private final static String TEMPLATES_GROUP = "2_group";

    private final static String SORT_DESCENDING = "desc";

    private final static String ARTICLE_TEMPLATE = "/conf/tetra-laval/settings/wcm/templates/article-page-template";

    @Reference
    private XSSAPI xssAPI;

    @Reference
    private XSSFilter xssFilter;

    @Reference
    private QueryBuilder queryBuilder;

    @Reference
    private ArticleService articleService;


    private String path;
    private String mediaLabel;
    private String videoThumbnail;
    private String documentThumbnail;

    private SearchResultsConfiguration config;

    @Activate
    public void activate(SearchResultsConfiguration config) {
        this.config = config;
    }

    @Override
    public int getItemsPerPage() {
        return config.itemsPerPage();
    }

    @Override
    public int getMaxResultSuggesions() {
        return config.maxResultSuggesions();
    }

    @Override
    public Map<String, String> setSearchQueryMap(SlingHttpServletRequest request, RequestParameterMap params) {
        path = getLanguagePagePath(request);

        Map<String, String> map = new HashMap<>();
        List<FilterModel> filterModelList = getTags(request, params);

        map.putAll(getFulltext(params));
        map.putAll(setResultsOrder());
        map.putAll(setResultsAmount(params));

        map.put(String.format("%s.p.or", RESOURCES_GROUP), "true");
        map.put(String.format("%s.%s.p.or", PAGES_GROUP, TAGS_GROUP), "true");
        map.put(String.format("%s.%s.p.or", PAGES_GROUP, TEMPLATES_GROUP), "true");
        map.put(String.format("%s.%s.p.or", ASSETS_GROUP, TAGS_GROUP), "true");

        map.putAll(setResources());
        map.putAll(setTags(PAGES_GROUP, String.format("%s/%s", JcrConstants.JCR_CONTENT,
                TagConstants.PN_TAGS), filterModelList));
        map.putAll(setTags(ASSETS_GROUP, String.format("%s/%s/%s", JcrConstants.JCR_CONTENT,
                DamConstants.METADATA_FOLDER, TagConstants.PN_TAGS), filterModelList));
        map.putAll(setTemplates(params));
        return map;
    }

    @Override
    public List<FilterModel> getFilters(SlingHttpServletRequest request, String[] tags) {
        List<FilterModel> filterModels = new ArrayList<>();
        TagManager tagManager = request.getResource().getResourceResolver().adaptTo(TagManager.class);
        if (tagManager != null && tags != null) {
            for (String tagString : tags) {
                Tag tag = tagManager.resolve(tagString);
                FilterModel filterModel = new FilterModel();
                filterModel.setLabel(tag.getTitle());
                filterModel.setKey(tagString);
                filterModels.add(filterModel);
            }
        }
        return filterModels;
    }

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
        map.put("fulltext", fulltext != null ? fulltext : StringUtils.EMPTY);
        return map;
    }

    private String[] getArticleTypes(RequestParameterMap params) {
        String[] articleTypes = null;
        RequestParameter articleTypesParam = params.getValue(ARTICLE_TYPE_PARAM);
        if (articleTypesParam != null) {
            articleTypes = articleTypesParam.getString().split(TLConstants.COMA);
        }
        return articleTypes;
    }

    private List<FilterModel> getTags(SlingHttpServletRequest request, RequestParameterMap params) {
        List<FilterModel> filters = null;
        RequestParameter tagsParam = params.getValue(TAGS_PARAM);
        if (tagsParam != null) {
            filters = getFilters(request, tagsParam.getString().split(TLConstants.COMA));
        }
        return filters;
    }

    private int getPageNumber(RequestParameterMap params) {
        int page = 1;
        RequestParameter pageParam = params.getValue(PAGE_PARAM);
        if (pageParam != null) {
            page = xssAPI.getValidInteger(pageParam.getString(), 1);
        }
        return page;
    }

    private Map<String, String> setResultsOrder() {
        Map<String, String> map = new HashMap<>();

        // set sorting logic for search results
        map.put("1_orderby", String.format("@%s/%s", JcrConstants.JCR_CONTENT, ARTICLE_DATE_PROP));
        map.put("1_orderby.sort", SORT_DESCENDING);
        map.put("2_orderby", String.format("@%s/%s", JcrConstants.JCR_CONTENT, NameConstants.PN_PAGE_LAST_MOD));
        map.put("2_orderby.sort", SORT_DESCENDING);
        return map;
    }

    private Map<String, String> setResultsAmount(RequestParameterMap params) {
        Map<String, String> map = new HashMap<>();

        // set amount of results
        map.put("p.guessTotal", String.valueOf(getMaxResultSuggesions()));
        map.put("p.offset", String.valueOf((getPageNumber(params) - 1) * getItemsPerPage()));
        map.put("p.limit", String.valueOf(getItemsPerPage()));
        return map;
    }

    private Map<String, String> setResources() {
        Map<String, String> map = new HashMap<>();

        // creating map configuration for different resources
        map.put(String.format("%s.type", PAGES_GROUP), NameConstants.NT_PAGE);
        map.put(String.format("%s.path", PAGES_GROUP), path);

        map.put(String.format("%s.type", ASSETS_GROUP), DamConstants.NT_DAM_ASSET);
        map.put(String.format("%s.path", ASSETS_GROUP), TLConstants.DAM_ROOT_PATH);

        // fetched only these results which do not have checked hideInSearch flag
        map.put("1_property.operation", "exists");
        map.put("1_property.value", "false");
        map.put(String.format("%s.property", PAGES_GROUP),
                String.format("@%s/%s", JcrConstants.JCR_CONTENT, HIDE_IN_SEARCH_PROP));
        map.put(String.format("%s.property.operation", PAGES_GROUP), "exists");
        map.put(String.format("%s.property.value", PAGES_GROUP), "false");
        return map;
    }

    private Map<String, String> setTags(String resourceGroup, String cqTagsProp, List<FilterModel> filterModelList) {
        Map<String, String> map = new HashMap<>();

        // generating map using tags selected in search result filters
        int tagIndex = 0;
        for (FilterModel filterModel : filterModelList) {
            tagIndex++;
            map.put(String.format("%s.1_group.%d_property", resourceGroup, tagIndex), cqTagsProp);
            map.put(String.format("%s.1_group.%d_property.value", resourceGroup, tagIndex), filterModel.getKey());
        }
        return map;
    }

    private Map<String, String> setTemplates(RequestParameterMap params) {
        Map<String, String> map = new HashMap<>();

        // fetched only these pages which have the same type as selected in filters
        int articleIndex = 0;
        for (String articleType : getArticleTypes(params)) {
            articleIndex++;
            map.put(String.format("%s.%s.%d_group.1_property", PAGES_GROUP, TEMPLATES_GROUP, articleIndex),
                    String.format("@%s/%s", JcrConstants.JCR_CONTENT, NameConstants.NN_TEMPLATE));
            map.put(String.format("%s.%s.%d_group.1_property.value", PAGES_GROUP, TEMPLATES_GROUP, articleIndex), ARTICLE_TEMPLATE);
            map.put(String.format("%s.%s.%d_group.2_property", PAGES_GROUP, TEMPLATES_GROUP, articleIndex),
                    String.format("@%s/articleType", JcrConstants.JCR_CONTENT));
            map.put(String.format("%s.%s.%d_group.2_property.value", PAGES_GROUP, TEMPLATES_GROUP, articleIndex), articleType);
        }
        return map;
    }

    private String getLanguagePagePath(SlingHttpServletRequest request) {
        return String.join(TLConstants.SLASH, Arrays.stream(request.getPathInfo()
                .split(TLConstants.SLASH))
                .collect(Collectors.toList())
                .subList(0, TLConstants.CHAPTER_LEVEL + 1));
    }

    private ResultItem setSearchResultItemData(SlingHttpServletRequest request, Hit hit) throws RepositoryException {
        ResultItem resultItem = new ResultItem();
        setAssetsProperties(request);

        Resource resource = hit.getResource();
        if (resource != null) {
            Resource metadataResource = resource.getChild(String.format("%s/%s", JcrConstants.JCR_CONTENT, DamConstants.METADATA_FOLDER));
            if (metadataResource != null) {
                ValueMap assetMetadataProperties = ResourceUtil.getValueMap(metadataResource);
                String mediaTitle = assetMetadataProperties.get(DamConstants.DC_TITLE, StringUtils.EMPTY);
                String mediaType = getMediaType(assetMetadataProperties);

                resultItem.setType(mediaLabel);
                resultItem.setTitle(StringUtils.isBlank(mediaTitle) ? hit.getTitle() : mediaTitle);
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
                setContentFields(resultItem, hit.getResource().getChild(JcrConstants.JCR_CONTENT));
            }
            resultItem.setPath(LinkUtils.sanitizeLink(hit.getPath(), request));
        }
        return resultItem;
    }

    private void setContentFields(ResultItem resultItem, Resource resource) throws RepositoryException {
        ValueMap valueMap = resource.getValueMap();
        String articleTypeName = null;

        String articleType = valueMap.get(ARTICLE_TYPE_PROP, String.class);
        String date = valueMap.get(valueMap.containsKey(ARTICLE_DATE_PROP) ?
                ARTICLE_DATE_PROP : NameConstants.PN_PAGE_LAST_MOD, String.class);

        List<FilterModel> filters = articleService.getFilterTypes().stream()
                .filter(filterModel -> filterModel.getKey().equals(articleType))
                .collect(Collectors.toList());

        if (filters.size() > 0) {
            articleTypeName = filters.get(0).getLabel();
        }
        resultItem.setType(articleTypeName);
        resultItem.setDate(formatDate(date));
    }

    private String getMediaType(ValueMap assetMetadataProperties) {
        if (assetMetadataProperties.containsKey(DamConstants.DC_FORMAT)) {
            String dcFormat = assetMetadataProperties.get(DamConstants.DC_FORMAT, StringUtils.EMPTY);
            if (dcFormat.contains(TLConstants.IMAGE)) {
                return TLConstants.IMAGE;
            } else if (dcFormat.contains(TLConstants.VIDEO)) {
                return TLConstants.VIDEO;
            } else {
                return TLConstants.DOCUMENT;
            }
        }
        return StringUtils.EMPTY;
    }

    private ResultItem setMediaSize(ResultItem resultItem, ValueMap assetMetadataProperties) {
        double size = 0;
        try {
            size = Integer.parseInt(assetMetadataProperties.get(DamConstants.DAM_SIZE, StringUtils.EMPTY));
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid Asset Size", e.getMessage(), e);
        }
        double convertedSize = size / 1024;
        if (convertedSize > 1024) {
            convertedSize = convertedSize / 1024;
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

    private void setAssetsProperties(SlingHttpServletRequest request) {
        Resource resource = request.getResource().getResourceResolver()
                .resolve(String.format("%s%s", request.getResource().getPath(), "/root/responsivegrid/searchresults"));
        if (resource != null && resource.adaptTo(Node.class) != null) {
            Node node = resource.adaptTo(Node.class);
            try {
                mediaLabel = node.hasProperty(MEDIA_LABEL_PROP) ? node.getProperty(MEDIA_LABEL_PROP).getString() : null;
                videoThumbnail = node.hasProperty(VIDEO_THUMBNAIL_PROP) ? node.getProperty(VIDEO_THUMBNAIL_PROP).getString() : null;
                documentThumbnail = node.hasProperty(DOCUMENT_THUMBNAIL_PROP) ? node.getProperty(DOCUMENT_THUMBNAIL_PROP).getString() : null;
            } catch (RepositoryException re) {
                LOGGER.error("setAssetsProperties:: RepositoryException", re.getMessage(), re);
            }
        }
    }

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
