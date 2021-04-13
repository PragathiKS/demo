package com.trs.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import com.trs.core.services.TrsConfigurationService;
import com.trs.core.utils.TrsConstants;

@Model(adaptables = Resource.class)
public class VideoPageModel {

    @Inject
    private TrsConfigurationService trsConfigurationService;

    private String dmDomain;

    @Inject
    @Named(TrsConstants.PAGE_SCENE7_FILENAME_PROPERTY)
    @Optional
    private String s7assetFile;

    @PostConstruct
    protected void init() {
        dmDomain = trsConfigurationService.getDynamicMediaDomain();
    }

    public String getS7assetFile() {
        return s7assetFile;
    }

    public void setS7assetFile(String s7assetFile) {
        this.s7assetFile = s7assetFile;
    }

    public String getDmDomain() {
        return dmDomain;
    }

    public void setDmDomain(String dmDomain) {
        this.dmDomain = dmDomain;
    }

}
