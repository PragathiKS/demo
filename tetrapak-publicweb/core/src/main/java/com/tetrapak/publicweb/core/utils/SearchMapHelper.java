package com.tetrapak.publicweb.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.day.cq.search.PredicateGroup;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.models.SearchResultsModel;
import com.tetrapak.publicweb.core.models.multifield.SearchPathModel;

/**
 * The Class SearchMapHelper.
 */
public final class SearchMapHelper {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchMapHelper.class);

    /** The Constant GROUP. */
    private static final String GROUP = "_group.";

    /** The Constant GROUP_3. */
    private static final String GROUP_101 = "101_group.";

    /** The Constant GROUP_TYPE. */
    private static final String GROUP_TYPE = "_group.type";

    /** The Constant GROUP_PATH. */
    private static final String GROUP_PATH = "_group.path";

    /** The Constant GROUP_PROPERTY. */
    private static final String GROUP_PROPERTY = "_group.1_property";

    /**
     * Instantiates a new resource util.
     */
    private SearchMapHelper() {
        /*
         * adding a private constructor to hide the implicit one
         */
    }

    /**
     * Gets the media results.
     *
     * @param map            the map
     * @param index            the index
     * @param searchResultsModel the search results model
     * @return the media results
     */
    public static int setMediaMap(Map<String, String> map, int index, SearchResultsModel searchResultsModel) {
        List<SearchPathModel> structure = searchResultsModel.getMediaStructureList();
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
     * @param type            the type
     * @param map            the map
     * @param index            the index
     * @param searchResultsModel the search results model
     * @return List<SearchResultBean>
     */
    public static int setPageseMap(String type, Map<String, String> map, int index, SearchResultsModel searchResultsModel) {
        LOGGER.info("Executing setContentTypeValToMap method.");
        List<SearchPathModel> structure = getStructureList(type.toLowerCase(),searchResultsModel);
        List<SearchPathModel> templates = getTemplateList(type.toLowerCase(),searchResultsModel);

        if (!CollectionUtils.isEmpty(structure)) {
            for (SearchPathModel path : structure) {
                map.put("1" + GROUP + index + GROUP_TYPE, "cq:Page");
                String pathKey = "1" + GROUP + index + GROUP_PATH;
                String templatekey = "1" + GROUP + index + GROUP_PROPERTY;
                map.put(pathKey, path.getPath());
                if (!CollectionUtils.isEmpty(templates)) {
                    map.put(templatekey, "@jcr:content/cq:template");
                    int templateIndex = 1;
                    for (SearchPathModel template : templates) {
                        map.put(templatekey + "." + templateIndex + "_value", template.getPath());
                        templateIndex++;
                    }
                }
                index++;
            }
        }
        return index;
    }

    /**
     * Sets the common map.
     *
     * @param fulltextSearchTerm            the fulltext search term
     * @param map            the map
     * @param pageParam            the page param
     * @param noOfResultsPerHit the no of results per hit
     */
    public static void setCommonMap(String fulltextSearchTerm, String[] contentType, Map<String, String> map, int pageParam,
            int noOfResultsPerHit, int guessTotal) {
        if (StringUtils.isNotBlank(fulltextSearchTerm)) {
            map.put("fulltext", fulltextSearchTerm);
        }
        map.put("p.guessTotal", String.valueOf(guessTotal));
        map.put("104_orderby", "@jcr:score");
        if(ArrayUtils.isNotEmpty(contentType) && Boolean.TRUE.equals(isValidContentType(contentType)) && ArrayUtils.contains( contentType, "media" )){
            map.put("105_orderby", "@jcr:content/jcr:lastModified");
        }else{
            map.put("105_orderby", "@jcr:content/articleDate");
        }
        map.put("104_orderby.sort", "desc");
        map.put("105_orderby.sort", "desc");

        // Excluding pages which have Hide in Search selected.
        map.put(GROUP_101 + "property", "@jcr:content/hideInSearch");
        map.put(GROUP_101 + "property.value", "false");
        map.put(GROUP_101 + "property.operation", "exists");
        map.put("p.limit", String.valueOf(noOfResultsPerHit));
        map.put("p.offset", String.valueOf((pageParam - 1) * noOfResultsPerHit));
    }

    /**
     * Sets the themes map.
     *
     * @param themes            the themes
     * @param map            the map
     * @param searchResultsModel the search results model
     */
    public static void setThemesPredicates(String[] themes, PredicateGroup tagsPredicateGroup , Map<String, String> themeMap) {
        if (themes != null && themes.length > 0) {
            for (int i = 0; i < themes.length; i++) {
                String tag = themeMap.get(themes[i]);
                if (StringUtils.isNotBlank(tag)) {
                    Map<String, String> tagMap = new HashMap<>();
                    tagMap.put("tagid.property", "jcr:content/cq:tags");
                    tagMap.put("tagid", tag);
                    tagsPredicateGroup.add(PredicateGroup.create(tagMap));
                }
            }
            for (int i = 0; i < themes.length; i++) {
                String tag = themeMap.get(themes[i]);
                if (StringUtils.isNotBlank(tag)) {
                    Map<String, String> tagMap = new HashMap<>();
                    tagMap.put("tagid.property", "jcr:content/metadata/cq:tags");
                    tagMap.put("tagid", tag);
                    tagsPredicateGroup.add(PredicateGroup.create(tagMap));
                }
            }
        }
    }

    /**
     * Sets the all pages map.
     *
     * @param map the map
     * @param searchResultsModel the search results model
     */
    public static void setAllPagesMap(Map<String, String> map, SearchResultsModel searchResultsModel) {
        map.put("1" + GROUP + 1 + GROUP_TYPE, "cq:Page");
        String pathKey = "1" + GROUP + 1 + GROUP_PATH;
        map.put(pathKey, PageUtil.getLanguagePage(searchResultsModel.getCurrentPage()).getPath());
    }

    /**
     * Filter gated content.
     *
     * @param map the map
     * @param index the index
     * @param searchResultsModel the search results model
     * @return the int
     */
    public static int filterGatedContent(Map<String, String> map, int index, SearchResultsModel searchResultsModel) {
        List<SearchPathModel> gatedContentList = searchResultsModel.getGatedContentList();
        if (!CollectionUtils.isEmpty(gatedContentList)) {
            for (SearchPathModel path : gatedContentList) {
                map.put(index + GROUP + "path", path.getPath());
                map.put(index + GROUP + "path.self", "true");
                map.put(index + GROUP + "p.not", "true");
                index++;
            }
        }
        return index;
    }

    /**
     * Gets the structure list.
     *
     * @param type
     *            the type
     * @return the structure list
     */
    private static List<SearchPathModel> getStructureList(String type, SearchResultsModel searchResultsModel) {
        List<SearchPathModel> structureList = new ArrayList<>();
        switch (type) {
            case PWConstants.NEWS:
                structureList = searchResultsModel.getNewsStructureList();
                break;
            case PWConstants.EVENTS:
                structureList = searchResultsModel.getEventStructureList();
                break;
            case PWConstants.PRODUCTS:
                structureList = searchResultsModel.getProductStructureList();
                break;
            case PWConstants.CASES:
                structureList = searchResultsModel.getCaseStructureList();
                break;
            default:
                LOGGER.debug("Not a valid content type");
        }
        return structureList;
    }

    /**
     * Gets the template list.
     *
     * @param type
     *            the type
     * @return the template list
     */
    private static List<SearchPathModel> getTemplateList(String type, SearchResultsModel searchResultsModel) {
        List<SearchPathModel> templateList = new ArrayList<>();
        switch (type) {
            case PWConstants.NEWS:
                templateList = searchResultsModel.getNewsTemplateList();
                break;
            case PWConstants.EVENTS:
                templateList = searchResultsModel.getEventTemplateList();
                break;
            case PWConstants.PRODUCTS:
                templateList = searchResultsModel.getProductTemplateList();
                break;
            case PWConstants.CASES:
                templateList = searchResultsModel.getCaseTemplateList();
                break;
            default:
                LOGGER.debug("No template available");
        }
        return templateList;
    }

    /**
     * Checks if is valid content type.
     *
     * @param contentTypes
     *            the content types
     * @return the boolean
     */
    public static Boolean isValidContentType(String[] contentTypes) {
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

}
