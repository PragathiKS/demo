package com.tetrapak.publicweb.core.beans.pxp;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "files" })
public class Files {

    @JsonProperty("files")
    private List<File> filesList;

    @JsonProperty("files")
    public List<File> getFiles() {
        return new ArrayList<>(filesList);
    }

    @JsonProperty("files")
    public void setFiles(List<File> files) {
        this.filesList = new ArrayList<>(filesList);
    }
}
