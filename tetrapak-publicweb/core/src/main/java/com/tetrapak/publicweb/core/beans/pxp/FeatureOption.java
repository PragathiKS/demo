
package com.tetrapak.publicweb.core.beans.pxp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class FeatureOption.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "name", "header", "body", "image", "video" })
public class FeatureOption {

    /** The name. */
    @JsonProperty("name")
    private String name;

    /** The header. */
    @JsonProperty("header")
    private String header;

    /** The body. */
    @JsonProperty("body")
    private String body;

    /** The image. */
    @JsonProperty("image")
    private String image;

    /** The video. */
    @JsonProperty("video")
    private Video video;

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
     * Gets the header.
     *
     * @return the header
     */
    @JsonProperty("header")
    public String getHeader() {
        return header;
    }

    /**
     * Sets the header.
     *
     * @param header
     *            the new header
     */
    @JsonProperty("header")
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Gets the body.
     *
     * @return the body
     */
    @JsonProperty("body")
    public String getBody() {
        return body;
    }

    /**
     * Sets the body.
     *
     * @param body
     *            the new body
     */
    @JsonProperty("body")
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Gets the image.
     *
     * @return the image
     */
    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    /**
     * Sets the image.
     *
     * @param image
     *            the new image
     */
    @JsonProperty("image")
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Gets the video.
     *
     * @return the video
     */
    @JsonProperty("video")
    public Video getVideo() {
        return video;
    }

    /**
     * Sets the video.
     *
     * @param video
     *            the new video
     */
    @JsonProperty("video")
    public void setVideo(Video video) {
        this.video = video;
    }

}
