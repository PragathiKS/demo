package com.tetrapak.publicweb.core.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "statusMessage", "statusCode" })
public class ContactUsResponse {

    @JsonProperty("statusMessage")
    private String statusMessage;
    @JsonProperty("statusCode")
    private String statusCode;

    @JsonProperty("statusMessage")
    public String getStatusMessage() {
        return statusMessage;
    }

    @JsonProperty("statusMessage")
    public void setStatusMessage(final String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @JsonProperty("statusCode")
    public String getStatusCode() {
        return statusCode;
    }

    @JsonProperty("statusCode")
    public void setStatusCode(final String statusCode) {
        this.statusCode = statusCode;
    }

}
