package com.tetrapak.publicweb.core.models;

import com.day.cq.commons.LanguageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * The Class ThumbnailConfigModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ThumbnailConfigModel {

    private static final String THUMBNAIL_CONFIG_PATH = "/jcr:content/root/responsivegrid/thumbnailconfig";

    @Inject
    private Resource pageContent;

    @Inject
    private String fileReference;

    @Inject
    private String imagePath;

    private String openGraphImagePath;

    @PostConstruct
    private void init() {
        this.openGraphImagePath = StringUtils.defaultString(resolvePathFromPageProperties(), resolvePathFromLanguagePage());
    }

    public boolean isValid() {
        return StringUtils.isNotEmpty(openGraphImagePath);
    }

    private String resolvePathFromLanguagePage() {
        final String path = LanguageUtil.getLanguageRoot(pageContent.getPath()) + THUMBNAIL_CONFIG_PATH;
        Resource configResource = pageContent.getResourceResolver().getResource(path);
        return getPropertyValueFromResource(configResource, "fileReference");
    }

    private String resolvePathFromPageProperties() {
        return getPropertyValueFromResource(pageContent, "imagePath");
    }

    private String getPropertyValueFromResource(Resource resource, String key) {
        return resource != null ? resource.getValueMap().get(key, String.class) : null;
    }

    public String getFileReference() {
        return fileReference;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getOpenGraphImagePath() {
        return openGraphImagePath;
    }
}
