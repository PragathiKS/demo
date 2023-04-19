package com.tetrapak.publicweb.core.multifield;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AccordionListModel {

    /**
     * The resource.
     */
    @Self
    private Resource request;

    /**
     * The heading.
     */
    @ValueMapValue
    private String accordionHeading;

    /**
     * Gets the heading.
     *
     * @return the heading.
     */
    public String getHeading() {
        return accordionHeading;
    }

}
