package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Model class for page reference components
 */
@Model(adaptables = Resource.class)
public class PageReferencesModel {

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    private String pageContentPath;

    private List<String> componentsReference = new LinkedList<>();
    private String locale;

    @PostConstruct
    protected void init() {
        if (Objects.nonNull(pageContentPath)) {
            GlobalUtil.setPageReferences(resourceResolver, componentsReference, locale, pageContentPath);
        }
    }

    public List<String> getComponentsReference() {
        return new LinkedList<>(componentsReference);
    }

    public String getPageContentPath() {
        return pageContentPath;
    }

}
