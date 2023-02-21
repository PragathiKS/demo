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
    private String statusCode;

    /**
     * Instantiates a new contact us response.
     *
     * @param statusCode
     *            the status code
     * @param statusMessage
     *            the status message
     */
    public ContactUsResponse(final String statusCode, final String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
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
     * @param statusMessage
     *            the new status message
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
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the status code.
     *
     * @param statusCode
     *            the new status code
     */
    @JsonProperty("statusCode")
    public void setStatusCode(final String statusCode) {
        this.statusCode = statusCode;
    }

}
