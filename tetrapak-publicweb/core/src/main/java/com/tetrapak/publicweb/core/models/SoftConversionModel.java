package com.tetrapak.publicweb.core.models;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetrapak.publicweb.core.utils.LinkUtils;
import com.tetrapak.publicweb.core.utils.PageUtil;

/**
 * The Class SoftConversionModel.
 *
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SoftConversionModel extends FormModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** The welcome back heading. */
    @ValueMapValue
    private String welcomeBackHeading;

    /** The welcome back description text. */
    @ValueMapValue
    private String welcomeBackDescriptionText;

    /** The download ready heading. */
    @ValueMapValue
    private String downloadReadyHeading;

    /** The download ready description text. */
    @ValueMapValue
    private String downloadReadyDescriptionText;

    /** The more link action. */
    @ValueMapValue
    private String moreButtonAction;

    /** The more link label. */
    @ValueMapValue
    private String moreButtonLabel;

    @ValueMapValue
    private String yesButtonLabel;

    @ValueMapValue
    private String noButtonLabel;

    @ValueMapValue
    private String padrotUrl;

    /**
     * The init method.
     */
    @PostConstruct
    protected void init() {
        if (StringUtils.isNotEmpty(moreButtonAction)) {
            moreButtonAction = LinkUtils.sanitizeLink(moreButtonAction);
        }
    }

    /**
     * Gets the welcome back heading.
     *
     * @return the welcome back heading
     */
    public String getWelcomeBackHeading() {
        return welcomeBackHeading;
    }

    /**
     * Gets the welcome back description text.
     *
     * @return the welcome back description text
     */
    public String getWelcomeBackDescriptionText() {
        return welcomeBackDescriptionText;
    }

    /**
     * Gets the download ready heading.
     *
     * @return the download ready heading
     */
    public String getDownloadReadyHeading() {
        return downloadReadyHeading;
    }

    /**
     * Gets the download ready description text.
     *
     * @return the download ready description text
     */
    public String getDownloadReadyDescriptionText() {
        return downloadReadyDescriptionText;
    }

    /**
     * Gets the more link action.
     *
     * @return the more link action
     */
    public String getMoreButtonAction() {
        return moreButtonAction;
    }

    /**
     * Gets the more button label.
     *
     * @return the more button label
     */
    public String getMoreButtonLabel() {
        return moreButtonLabel;
    }

    /**
     * Gets the yes button label.
     *
     * @return the yes button label
     */
    public String getYesButtonLabel() {
        return yesButtonLabel;
    }

    /**
     * Gets the no button label.
     *
     * @return the no button label
     */
    public String getNoButtonLabel() {
        return noButtonLabel;
    }

    /**
     * Gets the api url.
     *
     * @return the api url
     */
    @Override
    public String getApiUrl() {
        return resource.getPath() + ".padrotsoftconversion.json";
    }

    /**
     * Gets the site language.
     *
     * @return the site language
     */
    public String getSiteLanguage() {
        return PageUtil.getLanguageCodeFromResource(resource);
    }

    /**
     * Gets the site country.
     *
     * @return the site country
     */
    public String getSiteCountry() {
        return PageUtil.getCountryCodeFromResource(resource);
    }

    public String getPadrotUrl() {
        return padrotUrl;
    }
}
