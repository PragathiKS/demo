
package com.tetrapak.publicweb.core.beans.pxp;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "thumbnail",
    "type",
    "principle",
    "benefits"
})
public class Openingclosure {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("thumbnail")
    private String thumbnail;
    @JsonProperty("type")
    private String type;
    @JsonProperty("principle")
    private String principle;
    @JsonProperty("benefits")
    private List<String> benefits = null;

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

    @JsonProperty("thumbnail")
    public String getThumbnail() {
        return thumbnail;
    }

    @JsonProperty("thumbnail")
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("principle")
    public String getPrinciple() {
        return principle;
    }

    @JsonProperty("principle")
    public void setPrinciple(String principle) {
        this.principle = principle;
    }

    @JsonProperty("benefits")
    public List<String> getBenefits() {
        return benefits;
    }

    @JsonProperty("benefits")
    public void setBenefits(List<String> benefits) {
        this.benefits = benefits;
    }

}
