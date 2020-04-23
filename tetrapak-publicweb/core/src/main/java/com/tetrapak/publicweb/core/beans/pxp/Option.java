
package com.tetrapak.publicweb.core.beans.pxp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "header",
    "body",
    "image",
    "video"
})
public class Option {

    @JsonProperty("name")
    private String name;
    @JsonProperty("header")
    private String header;
    @JsonProperty("body")
    private String body;
    @JsonProperty("image")
    private Object image;
    @JsonProperty("video")
    private OptionVideo video;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("header")
    public String getHeader() {
        return header;
    }

    @JsonProperty("header")
    public void setHeader(String header) {
        this.header = header;
    }

    @JsonProperty("body")
    public String getBody() {
        return body;
    }

    @JsonProperty("body")
    public void setBody(String body) {
        this.body = body;
    }

    @JsonProperty("image")
    public Object getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(Object image) {
        this.image = image;
    }

    @JsonProperty("video")
    public OptionVideo getVideo() {
        return video;
    }

    @JsonProperty("video")
    public void setVideo(OptionVideo video) {
        this.video = video;
    }

}
