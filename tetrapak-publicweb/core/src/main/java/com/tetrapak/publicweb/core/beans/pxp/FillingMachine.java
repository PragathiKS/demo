
package com.tetrapak.publicweb.core.beans.pxp;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class FillingMachine.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "benefits", "features", "name", "benefitsimage", "header", "thumbnail", "id", "packagetypes",
        "options" })
public class FillingMachine {

    /** The benefits. */
    @JsonProperty("benefits")
    private String benefits;

    /** The features. */
    @JsonProperty("features")
    private List<FeatureOption> features = null;

    /** The name. */
    @JsonProperty("name")
    private String name;

    /** The benefitsimage. */
    @JsonProperty("benefitsimage")
    private String benefitsimage;

    /** The header. */
    @JsonProperty("header")
    private String header;

    /** The thumbnail. */
    @JsonProperty("thumbnail")
    private String thumbnail;

    /** The id. */
    @JsonProperty("id")
    private String id;

    /** The packagetypes. */
    @JsonProperty("packagetypes")
    private List<Packagetype> packagetypes = null;

    /** The options. */
    @JsonProperty("options")
    private List<FeatureOption> options = null;

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
     * Gets the thumbnail.
     *
     * @return the thumbnail
     */
    @JsonProperty("thumbnail")
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * Sets the thumbnail.
     *
     * @param thumbnail
     *            the new thumbnail
     */
    @JsonProperty("thumbnail")
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

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
     * Gets the packagetypes.
     *
     * @return the packagetypes
     */
    @JsonProperty("packagetypes")
    public List<Packagetype> getPackagetypes() {
        return packagetypes;
    }

    /**
     * Sets the packagetypes.
     *
     * @param packagetypes
     *            the new packagetypes
     */
    @JsonProperty("packagetypes")
    public void setPackagetypes(List<Packagetype> packagetypes) {
        this.packagetypes = packagetypes;
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
