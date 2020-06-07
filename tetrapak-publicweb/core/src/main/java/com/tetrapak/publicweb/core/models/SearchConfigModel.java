package com.tetrapak.publicweb.core.models;

import java.util.List;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetrapak.publicweb.core.models.multifield.ContentTypeModel;
import com.tetrapak.publicweb.core.models.multifield.ThemeModel;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SearchConfigModel {

    @Inject
    private List<ContentTypeModel> contentTypeList;

    @Inject
    private List<ThemeModel> themeList;

    @ValueMapValue
    private String mediaLabel;

    @ValueMapValue
    private String gatedPath;

    public List<ThemeModel> getThemeList() {
        return themeList;
    }

    public List<ContentTypeModel> getContentTypeList() {
        return contentTypeList;
    }

    public String getMediaLabel() {
        return mediaLabel;
    }

    public String getGatedPath() {
        return gatedPath;
    }

}
