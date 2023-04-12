package com.tetralaval.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class ContactUsResponse.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "statusMessage", "statusCode" })
public class ContactUsResponse {

    /** The status message. */
    @JsonProperty("statusMessage")
    private String statusMessage;

    /** The status code. */
    @JsonProperty("statusCode")
    private int statusCode;

    @JsonProperty("type")
    private String type;

    @JsonProperty("redirectURL")
    private String redirectURL;

    /**
     * Instantiates a new contact us response.
     *
     * @param statusCode    the status code
     * @param statusMessage the status message
     */
    public ContactUsResponse(final int statusCode, final String statusMessage) {
	this.statusCode = statusCode;
	this.statusMessage = statusMessage;
    }

    /**
     * Instantiates a new contact us response.
     *
     * @param statusCode    the status code
     * @param statusMessage the status message
     * @param type          the Type
     * @param redirectURL   the Redirect URL
     */
    public ContactUsResponse(final int statusCode, final String statusMessage, String type, String redirectURL) {
	this.statusCode = statusCode;
	this.statusMessage = statusMessage;
	this.type = type;
	this.redirectURL = redirectURL;
    }

    /**
     * Instantiates a new contact us response.
     *
     * @param statusCode    the status code
     * @param statusMessage the status message
     * @param type          the Type
     */
    public ContactUsResponse(final int statusCode, final String statusMessage, String type) {
	this.statusCode = statusCode;
	this.statusMessage = statusMessage;
	this.type = type;
    }

    /**
     * Gets the status message.
     *
     * @return the status message
     */
    @JsonProperty("statusMessage")
    public String getStatusMessage() {
	return statusMessage;
    }

    /**
     * Sets the status message.
     *
     * @param statusMessage the new status message
     */
    @JsonProperty("statusMessage")
    public void setStatusMessage(final String statusMessage) {
	this.statusMessage = statusMessage;
    }

    /**
     * Gets the status code.
     *
     * @return the status code
     */
    @JsonProperty("statusCode")
    public int getStatusCode() {
	return statusCode;
    }

    /**
     * Sets the status code.
     *
     * @param statusCode the new status code
     */
    @JsonProperty("statusCode")
    public void setStatusCode(final int statusCode) {
	this.statusCode = statusCode;
    }

    /**
     * Gets the Type
     * 
     * @return the type
     */
    @JsonProperty("type")
    public String getType() {
	return type;
    }

    /**
     * Sets the Type: redirect/message
     * 
     * @param the Type
     */
    @JsonProperty("type")
    public void setType(String type) {
	this.type = type;
    }

    /**
     * Gets the redirect URL
     * 
     * @return Redirect URL
     */
    @JsonProperty("redirectURL")
    public String getRedirectURL() {
	return redirectURL;
    }

    /**
     * Sets the Redirect URL.
     *
     * @param the RedirectURL
     */
    @JsonProperty("redirectURL")
    public void setRedirectURL(String redirectURL) {
	this.redirectURL = redirectURL;
    }

}
