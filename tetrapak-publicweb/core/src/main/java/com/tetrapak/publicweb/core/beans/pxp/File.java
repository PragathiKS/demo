package com.tetrapak.publicweb.core.beans.pxp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class File.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "name", "lastModified" })
public class File {

    /** The name. */
    @JsonProperty("name")
    private String name;

    /** The last modified. */
    @JsonProperty("lastModified")
    private String lastModified;

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
     * Gets the last modified.
     *
     * @return the last modified
     */
    @JsonProperty("lastModified")
    public String getLastModified() {
        return lastModified;
    }

    /**
     * Sets the last modified.
     *
     * @param lastModified
     *            the new last modified
     */
    @JsonProperty("lastModified")
    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

}
