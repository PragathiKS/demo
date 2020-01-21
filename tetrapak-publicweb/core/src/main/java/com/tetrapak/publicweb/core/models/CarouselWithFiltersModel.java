package com.tetrapak.publicweb.core.models;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.tetrapak.publicweb.core.services.BestPracticeLineService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CarouselWithFiltersModel {

    private static final Logger log = LoggerFactory.getLogger(CarouselWithFiltersModel.class);

    @Self
    Resource resource;

    @Inject
    private String titleI18n;

    @Inject
    private String titleAlignment;

    @Inject
    private String[] productType;

    @Inject
    private String[] category;

    @Inject
    private String rootPath;

    @Inject
    private String errorMessage;

    @Inject
    private String pwTheme;

    @Inject
    private String pwButtonTheme;

    @Inject
    private String pwPadding;

    private Integer carouselId = (int) (Math.random() * 1000 + 1);

    private List<String> prodTypeList = new ArrayList<>();
    private Map<String, String> categoryTagsMap = new HashMap<>();
    Map<String, Map<String, String>> tagsMap = new HashMap<>();

    private ResourceResolver resourceResolver;
    private TagManager tagManager;
    private String prodType;

    @Inject
    private BestPracticeLineService bestPracticeLineService;

    @PostConstruct
    protected void init() {
        log.info("Executing init method.");
        resourceResolver = resource.getResourceResolver();
        tagManager = resourceResolver.adaptTo(TagManager.class);
        if (tagManager != null) {
            if (!getTagList(productType).isEmpty()) {
                prodType = getTagList(productType).get(0);
                getCategoryTagsMap(category);
            }
        }
    }

    /**
     * This method is used to get the map of tags and its title
     * based on the tagID.
     *
     * @param tagID String[]
     */
    private void getCategoryTagsMap(String[] tagID) {
        log.info("Executing getCategoryTagsMap method.");
        if (tagID != null) {
            for (String tag : tagID) {
                Tag categoryTag = tagManager.resolve(tag);
                String tagTitle = categoryTag.getTitle();
                log.info("Category tags map : {} - {}", tag, tagTitle);
                categoryTagsMap.put(tag, tagTitle);
                Map<String, String> subCategoryTagsMap = getSubCategories(categoryTag);
                tagsMap.put(tag, subCategoryTagsMap);
            }
        }
    }

    /**
     * This method is used to get a map of sub tags for the category tag
     * passed as the parameter.
     *
     * @param categoryTag
     * @return
     */
    private Map<String, String> getSubCategories(Tag categoryTag) {
        log.info("Executing getSubCategories method.");
        Map<String, String> subCategoryTagsMap = new HashMap<>();
        Iterator<Tag> subCategoryTags = categoryTag.listChildren();
        if (subCategoryTags != null) {
            while (subCategoryTags.hasNext()) {
                Tag subCategTag = subCategoryTags.next();
                log.info("Sub Category tag : {}", subCategTag.getTagID());
                Boolean practiceLineExists = bestPracticeLineService.checkIfPracticeLineExists(resourceResolver, prodType, subCategTag.getTagID(), rootPath);
                if (practiceLineExists) {
                    log.info("Practice line exists for sub category : {}. Hence adding it to the list of dropdowns.", subCategTag);
                    String tagTitle = subCategTag.getTitle();
                    subCategoryTagsMap.put(tagTitle, subCategTag.getTagID());
                } else {
                    log.warn("No practice lines exist for sub category : {}", subCategTag.getTagID());
                }

            }
        }
        return subCategoryTagsMap;
    }

    private List<String> getTagList(String[] tagID) {
        log.info("Executing getTagList method.");
        if (tagID != null) {
            for (String tag : tagID) {
                String tagPath = tagManager.resolve(tag).getTagID();
                log.info("Tag Path : {}", tagPath);
                prodTypeList.add(tagPath);
            }
        }

        return prodTypeList;
    }

    public String getTitleI18n() {
        return titleI18n;
    }

    public String getTitleAlignment() {
        return titleAlignment;
    }

    public String getPwTheme() {
        return pwTheme;
    }

    public String getPwButtonTheme() {
        return pwButtonTheme;
    }

    public String getPwPadding() {
        return pwPadding;
    }

    public Integer getCarouselId() {
        return carouselId;
    }

    public String getProdType() {
        return prodTypeList.get(0);
    }

    public Map<String, String> getCategoryTagsMap() {
        return categoryTagsMap;
    }

    public String getRootPath() {
        return rootPath;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Map<String, Map<String, String>> getTagsMap() {
        return tagsMap;
    }

}
