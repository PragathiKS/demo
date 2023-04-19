package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.tetrapak.publicweb.core.multifield.AccordionListModel;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AccordionModel {

    /**
     * The request.
     */
    @Self
    private Resource resource;

    /**
     * Heading.
     */
    @Inject
    private String heading;

    /**
     * Description.
     */
    @Inject
    private String description;

    @Inject
    private List<AccordionListModel> accordionList;

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

    /**
     * Gets the accordion List.
     *
     * @return lists
     */
    public List<AccordionListModel> getAccordionList() {
        final List<AccordionListModel> lists = new ArrayList<>();
        if (Objects.nonNull(accordionList)) {
            lists.addAll(accordionList);
        }
        return lists;
    }
}
