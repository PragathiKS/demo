
package com.tetrapak.publicweb.core.beans.pxp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "poster",
    "src"
})
public class FeatureVideo {

    @JsonProperty("poster")
    private Object poster;
    @JsonProperty("src")
    private Object src;

    @JsonProperty("poster")
    public Object getPoster() {
        return poster;
    }

    @JsonProperty("poster")
    public void setPoster(Object poster) {
        this.poster = poster;
    }

    @JsonProperty("src")
    public Object getSrc() {
        return src;
    }

    @JsonProperty("src")
    public void setSrc(Object src) {
        this.src = src;
    }

}
