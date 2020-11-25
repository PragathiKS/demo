
package com.tetrapak.publicweb.core.beans.pxp;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class ProcessingEquipement.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "name", "technology", "technologytypes", "categories", "header", "benefits", "features",
        "options" })
public class ProcessingEquipement {

    /** The id. */
    @JsonProperty("id")
    private String id;

    /** The name. */
    @JsonProperty("name")
    private String name;

    /** The technology. */
    @JsonProperty("technology")
    private Technology technology;

    /** The technology type. */
    @JsonProperty("technologytypes")
    private TechnologyType technologyType;

    /** The categories. */
    @JsonProperty("categories")
    private List<Category> categories = null;

    /** The header. */
    @JsonProperty("header")
    private String header;

    /** The benefits. */
    @JsonProperty("benefits")
    private String benefits;

    /** The benefitsimage. */
    @JsonProperty("benefitsimage")
    private String benefitsimage;

    /** The features. */
    @JsonProperty("features")
    private List<FeatureOption> features = null;

    /** The options. */
    @JsonProperty("options")
    private List<FeatureOption> options = null;

    /**
     * Gets the id.
     *
     * @return the id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the technology.
     *
     * @return the technology
     */
    @JsonProperty("technology")
    public Technology getTechnology() {
        return technology;
    }

    /**
     * Sets the technology.
     *
     * @param technology
     *            the new technology
     */
    @JsonProperty("technology")
    public void setTechnology(Technology technology) {
        this.technology = technology;
    }

    /**
     * Gets the technology type.
     *
     * @return the technology type
     */
    @JsonProperty("technologytypes")
    public TechnologyType getTechnologyType() {
        return technologyType;
    }

    /**
     * Sets the technology type.
     *
     * @param technologyType
     *            the new technology type
     */
    @JsonProperty("technologytypes")
    public void setTechnologyType(TechnologyType technologyType) {
        this.technologyType = technologyType;
    }

    /**
     * Gets the categories.
     *
     * @return the categories
     */
    @JsonProperty("categories")
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * Sets the categories.
     *
     * @param categories
     *            the new categories
     */
    @JsonProperty("categories")
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    /**
     * Gets the header.
     *
     * @return the header
     */
    @JsonProperty("header")
    public String getHeader() {
        return header;
    }

    /**
     * Sets the header.
     *
     * @param header
     *            the new header
     */
    @JsonProperty("header")
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Gets the benefits.
     *
     * @return the benefits
     */
    @JsonProperty("benefits")
    public String getBenefits() {
        return benefits;
    }

    /**
     * Sets the benefits.
     *
     * @param benefits
     *            the new benefits
     */
    @JsonProperty("benefits")
    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    /**
     * Gets the benefitsimage.
     *
     * @return the benefitsimage
     */
    @JsonProperty("benefitsimage")
    public String getBenefitsimage() {
        return benefitsimage;
    }

    /**
     * Sets the benefitsimage.
     *
     * @param benefitsimage
     *            the new benefitsimage
     */
    @JsonProperty("benefitsimage")
    public void setBenefitsimage(String benefitsimage) {
        this.benefitsimage = benefitsimage;
    }

    /**
     * Gets the features.
     *
     * @return the features
     */
    @JsonProperty("features")
    public List<FeatureOption> getFeatures() {
        return features;
    }

    /**
     * Sets the features.
     *
     * @param features
     *            the new features
     */
    @JsonProperty("features")
    public void setFeatures(List<FeatureOption> features) {
        this.features = features;
    }

    /**
     * Gets the options.
     *
     * @return the options
     */
    @JsonProperty("options")
    public List<FeatureOption> getOptions() {
        return options;
    }

    /**
     * Sets the options.
     *
     * @param options
     *            the new options
     */
    @JsonProperty("options")
    public void setOptions(List<FeatureOption> options) {
        this.options = options;
    }

}
