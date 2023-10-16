package com.tetrapak.customerhub.core.beans.spareparts;

import org.apache.http.HttpResponse;

public class ImageResponse {

    private String imageLink;

    private HttpResponse httpResponse;

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }
}
