package com.tetrapak.publicweb.core.models.multifield;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The Class SearchPathModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SearchPathModel {

    /** The path. */
    @ValueMapValue
    private String path;

    /**
     * Gets the path.
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

}
