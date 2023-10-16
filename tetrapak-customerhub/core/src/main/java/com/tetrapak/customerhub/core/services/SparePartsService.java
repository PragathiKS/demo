package com.tetrapak.customerhub.core.services;

import com.tetrapak.customerhub.core.beans.spareparts.ImageLinks;
import com.tetrapak.customerhub.core.beans.spareparts.ImageResponse;
import org.apache.http.HttpResponse;

public interface SparePartsService {

    public ImageLinks getImageLinks(String dimension, String partNumber);

    public HttpResponse getImage(String imageLink);

    public ImageResponse getImage(String dimension, String partNumber);
}
