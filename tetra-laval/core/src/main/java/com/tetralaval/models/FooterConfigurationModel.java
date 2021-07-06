package com.tetralaval.models;

import com.tetralaval.constants.PWConstants;
import com.tetralaval.models.multifield.FooterLinkModel;
import com.tetralaval.models.multifield.SocialLinkModel;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * The Class FooterConfigurationModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterConfigurationModel {

    /** The request. */
    @Self
    private Resource resource;

    /** The logo image path. */
    @ValueMapValue
    private String logoImagePath;

    /** The logo link. */
    @ValueMapValue
    private String logoLink;

    /** The logo alt. */
    @ValueMapValue
    private String logoAlt;

    /** The logo background. */
    @ValueMapValue
    private String logoBackground;

    /** The social links. */
    @Inject
    private List<SocialLinkModel> socialLinks;

    /** The footer links. */
    @Inject
    private List<FooterLinkModel> footerLinks;

    /** The go to top label. */
    @ValueMapValue
    private String goToTopLabel;

    /**
     * Gets the logo image path.
     *
     * @return the logo image path
     */
    public String getLogoImagePath() {
        return logoImagePath;
    }

    /**
     * Gets the logo link.
     *
     * @return the logo link
     */
    public String getLogoLink() {
        return logoLink;
    }

    /**
     * Gets the logo alt.
     *
     * @return the logo alt
     */
    public String getLogoAlt() {
        return logoAlt;
    }

    /**
     * Gets the logo background.
     *
     * @return the logo background
     */
    public String getLogoBackground() {
        return logoBackground;
    }

    /**
     * Gets the social links.
     *
     * @return the social links
     */
    public List<SocialLinkModel> getSocialLinks() {
        final List<SocialLinkModel> lists = new ArrayList<>();
        String countryCode = resource.getPath().split(PWConstants.SLASH)[5];
        if (Objects.nonNull(socialLinks)) {
        	final String [] valueArray = {"Xing","Vkontakte"};
        	lists.addAll(socialLinks);
        	if (countryCode.equalsIgnoreCase(PWConstants.RU_COUNTRY_CODE)) {
        		modifyList(lists,valueArray[0]);
            } else if (countryCode.equalsIgnoreCase(PWConstants.DE_COUNTRY_CODE)) {
            	modifyList(lists,valueArray[1]);
            } else {
            	for (String value : valueArray) {
            		modifyList(lists, value);
            	}
            }
        }
        return lists;
    }
    
    /**
     * Modify list.
     *
     * @param lists the lists
     * @param socialName the social name
     */
    private void modifyList(final List<SocialLinkModel> lists, String socialName) {
		Iterator<SocialLinkModel> itr = lists.iterator();
		while(itr.hasNext()) {
			SocialLinkModel modelEntry = itr.next();
			if (modelEntry.getSocialMedia().equalsIgnoreCase(socialName)) {
				lists.remove(modelEntry);
				break;
			}
		}
	}

    /**
     * Gets the footer link.
     *
     * @return the footer link
     */
    public List<FooterLinkModel> getFooterLinks() {
        final List<FooterLinkModel> lists = new ArrayList<>();
        if (Objects.nonNull(footerLinks)) {
            lists.addAll(footerLinks);
        }
        return lists;

    }

    /**
     * Gets the go to top label.
     *
     * @return the go to top label
     */
    public String getGoToTopLabel() {
        return goToTopLabel;
    }

}
