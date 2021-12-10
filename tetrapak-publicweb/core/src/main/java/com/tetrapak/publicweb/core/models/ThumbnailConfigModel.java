package com.tetrapak.publicweb.core.models;

import com.day.cq.commons.LanguageUtil;
import com.tetrapak.publicweb.core.constants.PWConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * The Class ThumbnailConfigModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ThumbnailConfigModel {

    private static final String THUMBNAIL_CONFIG_PATH = "/jcr:content/root/responsivegrid/thumbnailconfig";
    private static final String IMAGE_PATH_PROPERTY_NAME = "imagePath";
    private static final String HASH_SIGN = "#";

    @Inject
    private Resource resource;

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
        return StringUtils.isNotEmpty(openGraphImagePath) && !StringUtils.equals(HASH_SIGN, openGraphImagePath);
    }

    private String resolvePathFromLanguagePage() {
        final String path = LanguageUtil.getLanguageRoot(resource.getPath()) + THUMBNAIL_CONFIG_PATH;
        Resource configResource = resource.getResourceResolver().getResource(path);
        return getPropertyValueFromResource(configResource, PWConstants.FILE_REFERENCE);
    }

    private String resolvePathFromPageProperties() {
        return getPropertyValueFromResource(resource, IMAGE_PATH_PROPERTY_NAME);
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
        return this.openGraphImagePath;
    }
}
