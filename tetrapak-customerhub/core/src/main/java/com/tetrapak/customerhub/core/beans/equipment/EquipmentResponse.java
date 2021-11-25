package com.tetrapak.customerhub.core.beans.equipment;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Update Equipment response class.
 */
public class EquipmentResponse {

    /** The status message. */
    @JsonProperty("result")
    private String result;

    /** The status code. */
    @JsonProperty("status")
    private int status;

    /**
     * Instantiates a Update Equipment response.
     *
     * @param status
     *            the status code
     * @param result
     *            the status message
     */
    public EquipmentResponse(String result, int status) {
        this.result = result;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getResult() {
        return result;
    }
}
