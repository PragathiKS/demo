package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The Class FormModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SoftConversionFormConfigModel extends FormConfigModel {

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

    @ValueMapValue
    private String yesButtonLabel;

    @ValueMapValue
    private String noButtonLabel;
    
    @ValueMapValue
    private String positionTagsPath;
    
    @ValueMapValue
    private String functionTagsPath;


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
     * Gets the position tag path.
     *
     * @return the position tag path
     */
    public String getPositionTagsPath() {
    	return positionTagsPath;
    }
    
    /**
     * Gets the function tag path.
     *
     * @return the function tag path
     */
    public String getFunctionTagsPath() {
    	return functionTagsPath;
    }

}
