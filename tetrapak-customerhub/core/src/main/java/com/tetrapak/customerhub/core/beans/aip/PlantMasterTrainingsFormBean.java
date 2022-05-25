package com.tetrapak.customerhub.core.beans.aip;

import com.google.gson.annotations.SerializedName;

/**
 * The Class PlantMasterTrainingsFormBean.
 */
public class PlantMasterTrainingsFormBean {

    /** The Constant TRAINING_NAME. */
    public static final String TRAINING_NAME = "trainingName";
    
    /** The Constant TRAINING_ID. */
    public static final String TRAINING_ID = "trainingId";

    /** The Constant NO_OF_PARTICIPANTS. */
    public static final String NO_OF_PARTICIPANTS = "noOfParticipants";

    /** The Constant PREFERRED_LOCATION. */
    public static final String PREFERRED_LOCATION = "preferredLocation";

    /** The Constant PREFERRED_DATE. */
    public static final String PREFERRED_DATE = "preferredDate";

    /** The Constant COMMENTS. */
    public static final String COMMENTS_PARAMETER = "comments";

    /** The Constant CONSENT. */
    public static final String CONSENT_PARAMETER = "consent";

    /** The Constant NAME_PARAMETER. */
    public static final String NAME_PARAMETER = "name";

    /** The Constant EMAIL_ADDRESS_PARAMETER. */
    public static final String EMAIL_ADDRESS_PARAMETER = "emailAddress";

    /** The training name. */
    @SerializedName(TRAINING_NAME)
    private String trainingName;
    
    /** The training ID. */
    @SerializedName(TRAINING_ID)
    private String trainingId;

    /** The no of participants. */
    @SerializedName(NO_OF_PARTICIPANTS)
    private String noOfParticipants;

    /** The preferred location. */
    @SerializedName(PREFERRED_LOCATION)
    private String preferredLocation;

    /** The preferred date. */
    @SerializedName(PREFERRED_DATE)
    private String preferredDate;

    /** The comments. */
    @SerializedName(COMMENTS_PARAMETER)
    private String comments;

    /** The consent. */
    @SerializedName(CONSENT_PARAMETER)
    private String consent;

    /** The name. */
    @SerializedName(NAME_PARAMETER)
    private String name;

    /** The email address. */
    @SerializedName(EMAIL_ADDRESS_PARAMETER)
    private String emailAddress;

    /**
     * Gets the training name.
     *
     * @return the training name
     */
    public String getTrainingName() {
        return trainingName;
    }

    /**
     * Gets the training id.
     *
     * @return the training id
     */
    public String getTrainingId() {
        return trainingId;
    }

    /**
     * Gets the no of participants.
     *
     * @return the no of participants
     */
    public String getNoOfParticipants() {
        return noOfParticipants;
    }

    /**
     * Gets the preferred location.
     *
     * @return the preferred location
     */
    public String getPreferredLocation() {
        return preferredLocation;
    }

    /**
     * Gets the preferred date.
     *
     * @return the preferred date
     */
    public String getPreferredDate() {
        return preferredDate;
    }

    /**
     * Gets the comments.
     *
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * Gets the consent.
     *
     * @return the consent
     */
    public String getConsent() {
        return consent;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the email address.
     *
     * @return the email address
     */
    public String getEmailAddress() {
        return emailAddress;
    }

}
