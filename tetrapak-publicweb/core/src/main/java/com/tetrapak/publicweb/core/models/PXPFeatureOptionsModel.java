package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import com.tetrapak.publicweb.core.beans.pxp.FeatureOption;
import com.tetrapak.publicweb.core.models.multifield.TabModel;
import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;

/**
 * The Class PXPFeatureOptionsModel.
 */
public class PXPFeatureOptionsModel {

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

    /** The sling settings service. */
    @OSGiService
    private SlingSettingsService slingSettingsService;

    /** The dynamic media service. */
    @OSGiService
    private DynamicMediaService dynamicMediaService;

    /** The Constant AUTHOR. */
    private static final String AUTHOR = "author";

    /**
     * Sets the tab list.
     *
     * @param featureOptions
     *            the feature options
     * @return the list
     */
    public List<TabModel> setTabList(List<FeatureOption> featureOptions) {
        List<TabModel> tabs = new ArrayList<>();
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
                String damVideoPath = feature.getVideo().getSrc();
                damVideoPath = setDynamicMediaVideoPath(damVideoPath);
                tab.setDamVideoPath(damVideoPath);
                tab.setThumbnailPath(feature.getVideo().getPoster());
                tab.setThumbnailAltText(feature.getName());
                tab.setTabType("videoText");
                tab.setVideoSource("damVideo");
            }
            tabs.add(tab);
        }
        return tabs;
    }

    /**
     * Sets the dynamic media video path.
     *
     * @param damVideoPath
     *            the dam video path
     * @return the string
     */
    private String setDynamicMediaVideoPath(String damVideoPath) {
        if (!slingSettingsService.getRunModes().contains(AUTHOR) && null != dynamicMediaService) {
            damVideoPath = GlobalUtil.getVideoUrlFromScene7(damVideoPath, dynamicMediaService);
        }
        return damVideoPath;
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

}
