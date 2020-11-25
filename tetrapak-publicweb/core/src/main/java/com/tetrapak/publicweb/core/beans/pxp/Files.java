package com.tetrapak.publicweb.core.beans.pxp;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class Files.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "files" })
public class Files {

    /** The files. */
    @JsonProperty("files")
    private List<File> files = null;

    /**
     * Gets the files.
     *
     * @return the files
     */
    @JsonProperty("files")
    public List<File> getFiles() {
        return files;
    }

    /**
     * Sets the files.
     *
     * @param files
     *            the new files
     */
    @JsonProperty("files")
    public void setFiles(List<File> files) {
        this.files = files;
    }
}
