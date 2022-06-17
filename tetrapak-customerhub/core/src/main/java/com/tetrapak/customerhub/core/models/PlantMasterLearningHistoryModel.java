package com.tetrapak.customerhub.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Model class for Learning History child node of PlantMaster training component.
 */
@Model(adaptables = {
        Resource.class, SlingHttpServletRequest.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PlantMasterLearningHistoryModel {

    /** The learning history. */
    @ValueMapValue
    private String learninghistorytext;

    /** The diploma. */
    @ValueMapValue
    private String diplomatext;

    /** The accredited. */
    @ValueMapValue
    private String accreditedtext;

    /** The authenticated. */
    @ValueMapValue
    private String authenticatedtext;

    /** The user text. */
    @ValueMapValue
    private String usertext;

    /** The item text. */
    @ValueMapValue
    private String itemtext;

    /** The completion date text. */
    @ValueMapValue
    private String completiondatetext;

    public String getLearninghistorytext() {
        return learninghistorytext;
    }

    public String getDiplomatext() {
        return diplomatext;
    }

    public String getAccreditedtext() {
        return accreditedtext;
    }

    public String getAuthenticatedtext() {
        return authenticatedtext;
    }

    public String getUsertext() {
        return usertext;
    }

    public String getItemtext() {
        return itemtext;
    }

    public String getCompletiondatetext() {
        return completiondatetext;
    }
}
