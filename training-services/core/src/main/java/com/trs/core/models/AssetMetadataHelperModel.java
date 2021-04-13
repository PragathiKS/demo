package com.trs.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.trs.core.utils.TrsConstants;

@Model(adaptables = SlingHttpServletRequest.class)
public class AssetMetadataHelperModel {

    @SlingObject
    private ResourceResolver resourceResolver;

    @Inject
    private SlingHttpServletRequest request;

    private String videoPagePublicURL;

    private String videoPageAuthoringURL;

    public String getVideoPagePublicURL() {
        return videoPagePublicURL;
    }

    public void setVideoPagePublicURL(String videoPagePublicURL) {
        this.videoPagePublicURL = videoPagePublicURL;
    }

    public String getVideoPageAuthoringURL() {
        return videoPageAuthoringURL;
    }

    public void setVideoPageAuthoringURL(String videoPageAuthoringURL) {
        this.videoPageAuthoringURL = videoPageAuthoringURL;
    }

    @PostConstruct
    protected void init() {

        String assetPath = request.getParameter("item");
        if (StringUtils.isNotBlank(assetPath)) {
            ValueMap properties = resourceResolver.getResource(assetPath + TrsConstants.ASSET_METADATA_RELATIVE_PATH)
                    .adaptTo(ValueMap.class);
            this.videoPagePublicURL = properties.get("dam:pagePublishUrl", StringUtils.EMPTY);
            this.videoPageAuthoringURL = properties.get("dam:pageAuthorUrl", StringUtils.EMPTY);
        }
    }

}
