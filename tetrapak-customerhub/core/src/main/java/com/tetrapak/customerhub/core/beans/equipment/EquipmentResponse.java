package com.tetrapak.customerhub.core.beans.equipment;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class EquipmentResponse {

    /** The status message. */
    @JsonProperty("statusMessage")
    private String statusMessage;

    /** The status code. */
    @JsonProperty("statusCode")
    private int statusCode;

    /**
     * Instantiates a Update Equipment response.
     *
     * @param statusCode
     *            the status code
     * @param statusMessage
     *            the status message
     */
    public EquipmentResponse(String statusMessage, int statusCode) {
        this.statusMessage = statusMessage;
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
