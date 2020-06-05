package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

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

    /** The download link. */
    @ValueMapValue
    private String downloadLink;

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
     * Gets the download link.
     *
     * @return the download link
     */
    public String getDownloadLink() {
        return downloadLink;
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

}
