
package com.tetrapak.publicweb.core.beans.pxp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class Video.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "poster", "src" })
public class Video {

    /** The poster. */
    @JsonProperty("poster")
    private String poster;

    /** The src. */
    @JsonProperty("src")
    private String src;

    /**
     * Gets the poster.
     *
     * @return the poster
     */
    @JsonProperty("poster")
    public String getPoster() {
        return poster;
    }

    /**
     * Sets the poster.
     *
     * @param poster
     *            the new poster
     */
    @JsonProperty("poster")
    public void setPoster(String poster) {
        this.poster = poster;
    }

    /**
     * Gets the src.
     *
     * @return the src
     */
    @JsonProperty("src")
    public String getSrc() {
        return src;
    }

    /**
     * Sets the src.
     *
     * @param src
     *            the new src
     */
    @JsonProperty("src")
    public void setSrc(String src) {
        this.src = src;
    }

}
