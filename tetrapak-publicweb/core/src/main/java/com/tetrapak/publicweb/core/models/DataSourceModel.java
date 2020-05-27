package com.tetrapak.publicweb.core.models;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.tetrapak.publicweb.core.services.PseudoCategoryService;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * The Class DataSourceModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DataSourceModel {

    /** The request. */
    @Self
    private SlingHttpServletRequest request;

    /** The resource resolver. */
    @SlingObject
    private ResourceResolver resourceResolver;

    /** The pseudo category service. */
    @Inject
    private PseudoCategoryService pseudoCategoryService;

    /**
     * The init method.
     */
    @PostConstruct
    protected void init() {
        final Map<String, String> pseudoCategories = pseudoCategoryService.fetchPseudoCategories(resourceResolver);
        final DataSource dataSource = new SimpleDataSource(getResourceList(pseudoCategories).iterator());
        request.setAttribute(DataSource.class.getName(), dataSource);
    }

    /**
     * Gets the resource list.
     *
     * @param pseudoCategories the pseudo categories
     * @return the resource list
     */
    private List<Resource> getResourceList(final Map<String, String> pseudoCategories) {
        final List<Resource> resourceList = new ArrayList<>();
        final Iterator<String> iterator = pseudoCategories.keySet().iterator();
        while (iterator.hasNext()) {
            final ValueMap valueMap = new ValueMapDecorator(new HashMap<String, Object>());
            final String pseudoCategory = iterator.next();
            if (null != pseudoCategory) {
                valueMap.put("value", pseudoCategories.get(pseudoCategory));
                valueMap.put("text", pseudoCategory);
                resourceList.add(new ValueMapResource(resourceResolver, new ResourceMetadata(), "nt:unstructured", valueMap));
            }
        }
        return resourceList;
    }
}
