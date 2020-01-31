package com.tetrapak.publicweb.core.models;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ImageCarouselModel {

    @Self
    private Resource resource;

    private List<Map<String, String>> imagesList = new ArrayList<>();
    private String resourceID;

    @PostConstruct
    protected void init() {

        Resource imageDetails = resource.getChild("imageDetails");
        if (null != imageDetails) {
            Iterator<Resource> itr = imageDetails.listChildren();
            while (itr.hasNext()) {
                Resource imageRes = itr.next();
                ValueMap valueMap = imageRes.getValueMap();

                String imagePath = valueMap.get("imagePath", StringUtils.EMPTY);
                if (StringUtils.isNotEmpty(imagePath)) {
                    String imageAltText = valueMap.get("imageAltI18n", StringUtils.EMPTY);
                    Map<String, String> imageItem = new HashMap<>();
                    imageItem.put("imagePath", imagePath);
                    imageItem.put("imageAltI18n", imageAltText);

                    imagesList.add(imageItem);
                }
            }
        }
        resourceID = resource.getName();
    }

    public String getResourceID() {
        return resourceID;
    }

    public List<Map<String, String>> getImagesList() {
        return imagesList;
    }
}
