
package com.tetrapak.publicweb.core.beans.pxp;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class Openingclosure.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "name", "thumbnail", "type", "principle", "benefits" })
public class Openingclosure {

    /** The id. */
    @JsonProperty("id")
    private String id;

    /** The name. */
    @JsonProperty("name")
    private String name;

    /** The thumbnail. */
    @JsonProperty("thumbnail")
    private String thumbnail;

    /** The type. */
    @JsonProperty("type")
    private String type;

    /** The principle. */
    @JsonProperty("principle")
    private String principle;

    /** The benefits. */
    @JsonProperty("benefits")
    private List<String> benefits = null;

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
     * Gets the type.
     *
     * @return the type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *            the new type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the principle.
     *
     * @return the principle
     */
    @JsonProperty("principle")
    public String getPrinciple() {
        return principle;
    }

    /**
     * Sets the principle.
     *
     * @param principle
     *            the new principle
     */
    @JsonProperty("principle")
    public void setPrinciple(String principle) {
        this.principle = principle;
    }

    /**
     * Gets the benefits.
     *
     * @return the benefits
     */
    @JsonProperty("benefits")
    public List<String> getBenefits() {
        return benefits;
    }

    /**
     * Sets the benefits.
     *
     * @param benefits
     *            the new benefits
     */
    @JsonProperty("benefits")
    public void setBenefits(List<String> benefits) {
        this.benefits = benefits;
    }

}
