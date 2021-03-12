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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tetrapak.publicweb.core.models.multifield.TabModel;

/**
 * The Class PXPOptionsModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PXPOptionsModel extends PXPFeatureOptionsModel {
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(PXPOptionsModel.class);

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
    	LOGGER.debug("Inside init of {}", this.getClass().getName());
        final ProductModel productModel = resource.adaptTo(ProductModel.class);
        if (Objects.nonNull(productModel)) {
            tabs = setTabList(productModel.getOptions());
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
