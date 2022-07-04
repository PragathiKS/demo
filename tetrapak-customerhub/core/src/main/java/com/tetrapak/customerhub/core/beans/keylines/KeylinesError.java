package com.tetrapak.customerhub.core.beans.keylines;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KeylinesError {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("message")
    @Expose
    private String message;

    public int getStatus() {
	return status;
    }

    public void setStatus(int httpBadRequest) {
	this.status = httpBadRequest;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

}
