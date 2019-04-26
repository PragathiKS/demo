package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.publicweb.core.utils.LinkUtils;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ImageTextBannerModel {
	
	private static final Logger log = LoggerFactory.getLogger(ImageTextBannerModel.class);

    @Self
    private Resource resource;

    @Inject
    private String bannerSubtitleI18n;

    @Inject
    private String titleI18n;

    @Inject
    private String desktopImage;

    @Inject
    private String desktopImageAltI18n;

    @Inject
    private String mobileImage;

    @Inject
    private String mobileImageAltI18n;

    @Inject
    private String bannerDescriptionI18n;

    @Inject
    private String contentAlignment;

    @Inject
    private String contrastLayer;

    @Inject
    private String bannerCtaTextI18n;

    @Inject
    private String bannerCtaPath;

    @Inject
    private String targetBlank;

    @Inject
    private String bannerCtaTooltipI18n;

    @Inject
    private String targetSoftConversion;
    
	@Inject
	private String softConversionTitle;

	@Inject
	private String softConversionDescription;

	@Inject
	private String softConversionHeadline;

	@Inject
	private String[] radioButtonGroups;
	
	@Inject
	private String softConversionDocPath;

    private Boolean isHeaderBanner = false;

    @PostConstruct
    protected void init() {

        String parentName = resource.getParent().getName();
        if (parentName.equalsIgnoreCase("header")) {
            isHeaderBanner = true;
        }

    }

    public String getBannerSubtitleI18n() {
        return bannerSubtitleI18n;
    }

    public String getTitleI18n() {
        return titleI18n;
    }

    public String getDesktopImage() {
        return desktopImage;
    }

    public String getDesktopImageAltI18n() {
        return desktopImageAltI18n;
    }

    public String getMobileImage() {
        return mobileImage;
    }

    public String getMobileImageAltI18n() {
        return mobileImageAltI18n;
    }

    public String getBannerDescriptionI18n() {
        return bannerDescriptionI18n;
    }

    public String getContentAlignment() {
        return contentAlignment;
    }

    public String getContrastLayer() {
        return contrastLayer;
    }

    public String getBannerCtaTextI18n() {
        return bannerCtaTextI18n;
    }

    public String getBannerCtaPath() {
        return LinkUtils.sanitizeLink(bannerCtaPath);
    }

    public String getTargetBlank() {
        return targetBlank;
    }

    public String getBannerCtaTooltipI18n() {
        return bannerCtaTooltipI18n;
    }

    public Boolean getIsHeaderBanner() {
        return isHeaderBanner;
    }

    public String getTargetSoftConversion() {
        return targetSoftConversion;
    }
    
	public String getSoftConversionTitle() {
		return softConversionTitle;
	}

	public String getSoftConversionDescription() {
		return softConversionDescription;
	}

	public String getSoftConversionHeadline() {
		return softConversionHeadline;
	}

	public List<String> getRadioButtonGroups() {
		return getRadioButtonGroups(radioButtonGroups);
	}

	public String getSoftConversionDocPath() {
		return softConversionDocPath;
	}

	/**
	 * Method to get the tab link text from the multifield property saved in CRX for
	 * each of the radio button groups.
	 *
	 * @param tabLinks String[]
	 * @return List<String>
	 */
	public static List<String> getRadioButtonGroups(String[] radioButtonGroups) {
		@SuppressWarnings("deprecation")
		List<String> radioButtons = new ArrayList<String>();
		JSONObject jObj;
		try {
			if (radioButtonGroups == null) {
				log.error("Radio Button Groups value is NULL");
			} else {
				for (int i = 0; i < radioButtonGroups.length; i++) {
					jObj = new JSONObject(radioButtonGroups[i]);

					if (jObj.has("radiobuttonTitle")) {
						radioButtons.add(jObj.getString("radiobuttonTitle"));
					}
				}
			}
		} catch (Exception e) {
			log.error("Exception while Multifield data {}", e.getMessage(), e);
		}
		return radioButtons;
	}

}
