package com.tetrapak.publicweb.core.models;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetrapak.publicweb.core.models.multifield.SearchPathModel;
import com.tetrapak.publicweb.core.models.multifield.ThemeModel;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SearchConfigModel {

    @Inject
    private List<SearchPathModel> productTemplateList;

    @Inject
    private List<SearchPathModel> newsTemplateList;

    @Inject
    private List<SearchPathModel> eventsTemplateList;

    @Inject
    private List<SearchPathModel> caseTemplateList;

    @Inject
    private List<SearchPathModel> productStructureList;

    @Inject
    private List<SearchPathModel> newsStructureList;

    @Inject
    private List<SearchPathModel> eventsStructureList;

    @Inject
    private List<SearchPathModel> caseStructureList;

    @Inject
    private List<ThemeModel> themeList;

    @ValueMapValue
    private String productLabel;

    @ValueMapValue
    private String eventsLabel;

    @ValueMapValue
    private String newsLabel;

    @ValueMapValue
    private String caseLabel;

    private Map<String, List<SearchPathModel>> templateMap = new LinkedHashMap<>();

    private Map<String, List<SearchPathModel>> structureMap = new LinkedHashMap<>();

    @PostConstruct
    public void init() {
        templateMap.put(productLabel, productTemplateList);
        templateMap.put(newsLabel, newsTemplateList);
        templateMap.put(eventsLabel, eventsTemplateList);
        templateMap.put(caseLabel, caseTemplateList);
        structureMap.put(productLabel, productStructureList);
        structureMap.put(newsLabel, newsStructureList);
        structureMap.put(eventsLabel, eventsStructureList);
        structureMap.put(caseLabel, caseStructureList);
    }

    public List<SearchPathModel> getProductTemplateList() {
        return productTemplateList;
    }

    public List<SearchPathModel> getNewsTemplateList() {
        return newsTemplateList;
    }

    public List<SearchPathModel> getEventsTemplateList() {
        return eventsTemplateList;
    }

    public List<SearchPathModel> getCaseTemplateList() {
        return caseTemplateList;
    }

    public List<SearchPathModel> getProductStructureList() {
        return productStructureList;
    }

    public List<SearchPathModel> getNewsStructureList() {
        return newsStructureList;
    }

    public List<SearchPathModel> getEventsStructureList() {
        return eventsStructureList;
    }

    public List<SearchPathModel> getCaseStructureList() {
        return caseStructureList;
    }

    public List<ThemeModel> getThemeList() {
        return themeList;
    }

    public String getProductLabel() {
        return productLabel;
    }

    public String getEventsLabel() {
        return eventsLabel;
    }

    public String getNewsLabel() {
        return newsLabel;
    }

    public String getCaseLabel() {
        return caseLabel;
    }

    public Map<String, List<SearchPathModel>> getTemplateMap() {
        return templateMap;
    }

    public Map<String, List<SearchPathModel>> getStructureMap() {
        return structureMap;
    }

}
