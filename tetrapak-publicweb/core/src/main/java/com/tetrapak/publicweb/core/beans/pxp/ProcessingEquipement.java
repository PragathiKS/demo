
package com.tetrapak.publicweb.core.beans.pxp;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "technology",
    "technologytypes",
    "categories",
    "header",
    "benefits",
    "features",
    "options"
})
public class ProcessingEquipement {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("technology")
    private Technology technology;
    @JsonProperty("technologytypes")
    private TechnologyType technologyType;
    @JsonProperty("categories")
    private List<Category> categories = null;
    @JsonProperty("header")
    private String header;
    @JsonProperty("benefits")
    private String benefits;
    @JsonProperty("features")
    private List<FeatureOption> features = null;
    @JsonProperty("options")
    private List<FeatureOption> options = null;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("technology")
    public Technology getTechnology() {
        return technology;
    }

    @JsonProperty("technology")
    public void setTechnology(Technology technology) {
        this.technology = technology;
    }

    @JsonProperty("technologytypes")
    public TechnologyType getTechnologyType() {
        return technologyType;
    }

    @JsonProperty("technologytypes")
    public void setTechnologyType(TechnologyType technologyType) {
        this.technologyType = technologyType;
    }

    @JsonProperty("categories")
    public List<Category> getCategories() {
        return categories;
    }

    @JsonProperty("categories")
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @JsonProperty("header")
    public String getHeader() {
        return header;
    }

    @JsonProperty("header")
    public void setHeader(String header) {
        this.header = header;
    }

    @JsonProperty("benefits")
    public String getBenefits() {
        return benefits;
    }

    @JsonProperty("benefits")
    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    @JsonProperty("features")
    public List<FeatureOption> getFeatures() {
        return features;
    }

    @JsonProperty("features")
    public void setFeatures(List<FeatureOption> features) {
        this.features = features;
    }

    @JsonProperty("options")
    public List<FeatureOption> getOptions() {
        return options;
    }

    @JsonProperty("options")
    public void setOptions(List<FeatureOption> options) {
        this.options = options;
    }

}
