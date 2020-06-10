package com.tetrapak.publicweb.core.models;

import java.util.List;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import com.tetrapak.publicweb.core.models.multifield.SearchPathModel;
import com.tetrapak.publicweb.core.models.multifield.ThemeModel;

/**
 * The Class SearchConfigModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SearchConfigModel {

    /** The product template list. */
    @Inject
    private List<SearchPathModel> productTemplateList;

    /** The product structure list. */
    @Inject
    private List<SearchPathModel> productStructureList;
    
    /** The news template list. */
    @Inject
    private List<SearchPathModel> newsTemplateList;

    /** The news structure list. */
    @Inject
    private List<SearchPathModel> newsStructureList;
    
    /** The event template list. */
    @Inject
    private List<SearchPathModel> eventTemplateList;

    /** The event structure list. */
    @Inject
    private List<SearchPathModel> eventStructureList;
    
    /** The case template list. */
    @Inject
    private List<SearchPathModel> caseTemplateList;

    /** The case structure list. */
    @Inject
    private List<SearchPathModel> caseStructureList;

    /** The media structure list. */
    @Inject
    private List<SearchPathModel> mediaStructureList;

    /** The theme list. */
    @Inject
    private List<ThemeModel> themeList;

    /** The gated content list. */
    @Inject
    private List<SearchPathModel> gatedContentList;

    /**
     * Gets the theme list.
     *
     * @return the theme list
     */
    public List<ThemeModel> getThemeList() {
        return themeList;
    }

    /**
     * Gets the product template list.
     *
     * @return the product template list
     */
    public List<SearchPathModel> getProductTemplateList() {
        return productTemplateList;
    }



    /**
     * Gets the product structure list.
     *
     * @return the product structure list
     */
    public List<SearchPathModel> getProductStructureList() {
        return productStructureList;
    }



    /**
     * Gets the news template list.
     *
     * @return the news template list
     */
    public List<SearchPathModel> getNewsTemplateList() {
        return newsTemplateList;
    }



    /**
     * Gets the news structure list.
     *
     * @return the news structure list
     */
    public List<SearchPathModel> getNewsStructureList() {
        return newsStructureList;
    }



    /**
     * Gets the event template list.
     *
     * @return the event template list
     */
    public List<SearchPathModel> getEventTemplateList() {
        return eventTemplateList;
    }



    /**
     * Gets the event structure list.
     *
     * @return the event structure list
     */
    public List<SearchPathModel> getEventStructureList() {
        return eventStructureList;
    }



    /**
     * Gets the case template list.
     *
     * @return the case template list
     */
    public List<SearchPathModel> getCaseTemplateList() {
        return caseTemplateList;
    }



    /**
     * Gets the case structure list.
     *
     * @return the case structure list
     */
    public List<SearchPathModel> getCaseStructureList() {
        return caseStructureList;
    }



    /**
     * Gets the media structure list.
     *
     * @return the media structure list
     */
    public List<SearchPathModel> getMediaStructureList() {
        return mediaStructureList;
    }



    /**
     * Gets the gated content list.
     *
     * @return the gated content list
     */
    public List<SearchPathModel> getGatedContentList() {
        return gatedContentList;
    }

}
