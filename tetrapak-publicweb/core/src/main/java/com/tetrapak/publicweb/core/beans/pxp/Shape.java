
package com.tetrapak.publicweb.core.beans.pxp;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class Shape.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "name", "thumbnail", "volumes" })
public class Shape {

    /** The name. */
    @JsonProperty("name")
    private String name;

    /** The thumbnail. */
    @JsonProperty("thumbnail")
    private String thumbnail;

    /** The volumes. */
    @JsonProperty("volumes")
    private List<String> volumes = null;

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
     * Gets the volumes.
     *
     * @return the volumes
     */
    @JsonProperty("volumes")
    public List<String> getVolumes() {
        return volumes;
    }

    /**
     * Sets the volumes.
     *
     * @param volumes
     *            the new volumes
     */
    @JsonProperty("volumes")
    public void setVolumes(List<String> volumes) {
        this.volumes = volumes;
    }

}
