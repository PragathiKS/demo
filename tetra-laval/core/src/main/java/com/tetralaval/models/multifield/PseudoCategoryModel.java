package com.tetralaval.models.multifield;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The Class PseudoCategoryModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PseudoCategoryModel {

    /** The pseudo category key. */
    @ValueMapValue
    private String pseudoCategoryKey;

    /** The pseudo category value. */
    @ValueMapValue
    private String pseudoCategoryValue;

    /**
     * Gets the pseudo category key.
     *
     * @return the pseudo category key
     */
    public String getPseudoCategoryKey() {
        return pseudoCategoryKey;
    }

    /**
     * Gets the pseudo category value.
     *
     * @return the pseudo category value
     */
    public String getPseudoCategoryValue() {
        return pseudoCategoryValue;
    }

    
}
