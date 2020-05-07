
package com.tetrapak.publicweb.core.beans.pxp;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "thumbnail",
    "volumes"
})
public class Shape {

    @JsonProperty("name")
    private String name;
    @JsonProperty("thumbnail")
    private String thumbnail;
    @JsonProperty("volumes")
    private List<String> volumes;

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

    @JsonProperty("volumes")
    public List<String> getVolumes() {
        return new ArrayList<>(volumes);
    }

    @JsonProperty("volumes")
    public void setVolumes(List<String> volumes) {
        this.volumes = new ArrayList<>(volumes);
    }

}
