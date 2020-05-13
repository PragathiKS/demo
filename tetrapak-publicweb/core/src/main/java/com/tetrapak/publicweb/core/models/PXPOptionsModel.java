package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetrapak.publicweb.core.beans.pxp.FeatureOption;
import com.tetrapak.publicweb.core.models.multifield.TabModel;

/**
 * The Class PXPOptionsModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PXPOptionsModel extends PXPFeatureOptionsModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** The heading. */
    @ValueMapValue
    private String heading;

    /** The tabs. */
    private List<TabModel> tabs = new ArrayList<>();

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        ProductModel productModel = resource.adaptTo(ProductModel.class);
        if (Objects.nonNull(productModel)) {
            List<FeatureOption> options = productModel.getOptions();
            tabs = setTabList(options);
        }
    }

    /**
     * Gets the heading.
     *
     * @return the heading
     */
    public String getHeading() {
        return heading;
    }

    /**
     * Get tabs.
     *
     * @return the list
     */
    public List<TabModel> getTabs() {
        return new ArrayList<>(tabs);
    }
}
