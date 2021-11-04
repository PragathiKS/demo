package com.tetrapak.customerhub.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AddEquipmentModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** The title label. */
    @Inject
    private String title;

    /** The subtitle label. */
    @Inject
    private String subTitle;

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }
}
