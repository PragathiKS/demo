package com.tetrapak.customerhub.core.models;

import com.day.crx.JcrConstants;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.Iterator;
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
            locale = StringUtils.isNotBlank(locale) ? locale : "en";
            String pagePath = String.valueOf(pageContentPath);
            pagePath = pagePath.replace("/en", CustomerHubConstants.PATH_SEPARATOR + locale);
            String resGridPathWithoutJcrContent = CustomerHubConstants.PATH_SEPARATOR +
                    CustomerHubConstants.ROOT_NODE + CustomerHubConstants.PATH_SEPARATOR +
                    CustomerHubConstants.RESPONSIVE_GRID_NODE;
            String resGridPath = pagePath.endsWith(JcrConstants.JCR_CONTENT) ? resGridPathWithoutJcrContent :
                    CustomerHubConstants.PATH_SEPARATOR + JcrConstants.JCR_CONTENT + resGridPathWithoutJcrContent;
            pagePath = pagePath + resGridPath;
            pageReferenceComponents(pagePath);
        }
    }

    private void pageReferenceComponents(String path) {
        Resource componentResources = resourceResolver.getResource(path);
        if (Objects.nonNull(componentResources)) {
            Iterator<Resource> iterators = componentResources.listChildren();
            while (iterators.hasNext()) {
                componentsReference.add(iterators.next().getPath());
            }
        }
    }

    public List<String> getComponentsReference() {
        return new LinkedList<>(componentsReference);
    }

    public String getPageContentPath() {
        return pageContentPath;
    }

}
