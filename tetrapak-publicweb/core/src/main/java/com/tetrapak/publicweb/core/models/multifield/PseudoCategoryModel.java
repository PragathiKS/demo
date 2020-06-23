package com.tetrapak.publicweb.core.models.multifield;

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
     * Sets the pseudo category key.
     *
     * @param pseudoCategoryKey the new pseudo category key
     */
    public void setPseudoCategoryKey(String pseudoCategoryKey) {
        this.pseudoCategoryKey = pseudoCategoryKey;
    }

    /**
     * Gets the pseudo category value.
     *
     * @return the pseudo category value
     */
    public String getPseudoCategoryValue() {
        return pseudoCategoryValue;
    }

    /**
     * Sets the pseudo category value.
     *
     * @param pseudoCategoryValue the new pseudo category value
     */
    public void setPseudoCategoryValue(String pseudoCategoryValue) {
        this.pseudoCategoryValue = pseudoCategoryValue;
    }

    
}
