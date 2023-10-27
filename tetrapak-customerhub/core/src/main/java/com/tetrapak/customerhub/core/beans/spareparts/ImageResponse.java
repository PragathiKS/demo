package com.tetrapak.customerhub.core.beans.spareparts;

import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;

public class ImageResponse {

    private String imageLink;

    private HttpResponse binaryResponse;

    public HttpResponse getBinaryResponse() {
        return binaryResponse;
    }

    public void setBinaryResponse(HttpResponse binaryResponse) {
        this.binaryResponse = binaryResponse;
    }

    private JsonObject errorResponse;

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public JsonObject getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(JsonObject errorResponse) {
        this.errorResponse = errorResponse;
    }
}
