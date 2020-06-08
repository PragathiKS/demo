package com.tetrapak.publicweb.core.models.multifield;

import java.util.List;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The Class SearchTemplatePathBean.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContentTypeModel {

    /** The template path. */
    @ValueMapValue
    private String label;
    
    @ValueMapValue
    private String key;

    @Inject
    private List<SearchPathModel> templateList;

    @Inject
    private List<SearchPathModel> structureList;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<SearchPathModel> getTemplateList() {
        return templateList;
    }

    public void setTemplateList(List<SearchPathModel> templateList) {
        this.templateList = templateList;
    }

    public List<SearchPathModel> getStructureList() {
        return structureList;
    }

    public void setStructureList(List<SearchPathModel> structureList) {
        this.structureList = structureList;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
