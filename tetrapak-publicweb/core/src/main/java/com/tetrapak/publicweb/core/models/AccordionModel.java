package com.tetrapak.publicweb.core.models;

import java.util.List;
import com.adobe.cq.wcm.core.components.models.Accordion;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.drew.lang.annotations.NotNull;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = Accordion.class,
        resourceType = "publicweb/components/content/accordion",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

public class AccordionModel implements Accordion {

    @Self @Via(type = ResourceSuperType.class)
    private Accordion accordion;

    @NotNull @Override
    public List<ListItem> getItems() {
        return accordion.getItems();
    }

    /**
     * The request.
     */
    @Self
    private Resource resource;

    /**
     * Heading.
     */
    @ValueMapValue
    private String heading;

    /**
     * Description.
     */
    @ValueMapValue
    private String description;


    /**
     * Gets the heading.
     *
     * @return the heading
     */
    public String getHeading() {
        return heading;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }


}
