package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetrapak.publicweb.core.beans.pxp.FeatureOption;
import com.tetrapak.publicweb.core.models.multifield.TabModel;

/**
 * The Class PXPFeaturesModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PXPFeaturesModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** The heading. */
    @ValueMapValue
    @Default(values = "Features")
    private String heading;

    /** The pw theme. */
    @ValueMapValue
    @Default(values = "grayscale-white")
    private String pwTheme;

    /** The pw display. */
    @ValueMapValue
    @Default(values = "display-row")
    private String pwDisplay;

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;

    /** The tabs. */
    private List<TabModel> tabs = new ArrayList<>();

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        ProductModel productModel = resource.adaptTo(ProductModel.class);
        if (Objects.nonNull(productModel)) {
            List<FeatureOption> featureOptions = productModel.getFeatures();
            for (FeatureOption feature : featureOptions) {
                TabModel tab = new TabModel();
                tab.setTitle(feature.getHeader());
                tab.setSubTitle(feature.getName());
                tab.setDescription(feature.getBody());
                if (StringUtils.isNotBlank(feature.getImage())) {
                    tab.setFileReference(feature.getImage());
                    tab.setAlt(feature.getName());
                    tab.setTabType("imageText");
                } else if (Objects.nonNull(feature.getVideo())) {
                    tab.setDamVideoPath(feature.getVideo().getSrc());
                    tab.setThumbnailPath(feature.getVideo().getPoster());
                    tab.setTabType("videoText");
                    tab.setVideoSource("damVideo");
                }
                tabs.add(tab);
            }
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
     * Gets the pw theme.
     *
     * @return the pw theme
     */
    public String getPwTheme() {
        return pwTheme;
    }

    /**
     * Gets the pw display.
     *
     * @return the pw display
     */
    public String getPwDisplay() {
        return pwDisplay;
    }

    /**
     * Gets the anchor id.
     *
     * @return the anchor id
     */
    public String getAnchorId() {
        return anchorId;
    }

    /**
     * Gets the anchor title.
     *
     * @return the anchor title
     */
    public String getAnchorTitle() {
        return anchorTitle;
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
