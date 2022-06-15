package com.tetrapak.customerhub.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Model class for Plant Master Engineering Trainings child node of PlantMaster training component.
 */
@Model(adaptables = {
        Resource.class, SlingHttpServletRequest.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PlantMasterEngineeringTrainingsModel {

    /** The title. */
    @ValueMapValue
    private String title;

    /** The available trainings. */
    @ValueMapValue
    private String availableTrainings;

    /** The course description. */
    @ValueMapValue
    private String courseDescription;

    /** The principle objectives. */
    @ValueMapValue
    private String principleObjectives;

    /** The target groups. */
    @ValueMapValue
    private String targetGroups;

    /** The duration. */
    @ValueMapValue
    private String duration;

    /** The hours. */
    @ValueMapValue
    private String hours;

    /** The max participants. */
    @ValueMapValue
    private String maxParticipants;

    /** The knowledge requirements. */
    @ValueMapValue
    private String knowledgeRequirements;

    /** The no of participants label. */
    @ValueMapValue
    private String noOfParticipantsLabel;

    /** The preferred location label. */
    @ValueMapValue
    private String preferredLocationLabel;

    /** The preferred date label. */
    @ValueMapValue
    private String preferredDateLabel;

    /** The preferred date placeholder. */
    @ValueMapValue
    private String preferredDatePlaceholder;

    /** The comments label. */
    @ValueMapValue
    private String commentsLabel;

    /** The confirmation text. */
    @ValueMapValue
    private String confirmationText;

    /** The submit button label. */
    @ValueMapValue
    private String submitButtonLabel;

    /** The input error message label. */
    @ValueMapValue
    private String inputErrorMsg;

    /** The confirmation error message label. */
    @ValueMapValue
    private String confirmationErrorMsg;

    /** The input format error message label. */
    @ValueMapValue
    private String inputFormatErrorMsg;

    /** The input format error message label. */
    @ValueMapValue
    private String numberFieldErrorMsg;

    /** The success message. */
    @ValueMapValue
    private String successMessage;

    /** The subject. */
    @ValueMapValue
    private String subject;

    /** The salutation text in email. */
    @ValueMapValue
    private String salutation;

    /** The body text in email. */
    @ValueMapValue
    private String body;

    /** The consent label. */
    @ValueMapValue
    private String consentLabel;

    /** The contact details. */
    @ValueMapValue
    private String contactDetails;

    /** The training id label. */
    @ValueMapValue
    private String trainingIdLabel;

    /** The training name label. */
    @ValueMapValue
    private String trainingNameLabel;

    /** The form title. */
    @ValueMapValue
    private String formTitle;

    public String getTitle() {
        return title;
    }

    public String getAvailableTrainings() {
        return availableTrainings;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public String getPrincipleObjectives() {
        return principleObjectives;
    }

    public String getTargetGroups() {
        return targetGroups;
    }

    public String getDuration() {
        return duration;
    }

    public String getHours() {
        return hours;
    }

    public String getMaxParticipants() {
        return maxParticipants;
    }

    public String getKnowledgeRequirements() {
        return knowledgeRequirements;
    }

    public String getNoOfParticipantsLabel() {
        return noOfParticipantsLabel;
    }

    public String getPreferredLocationLabel() {
        return preferredLocationLabel;
    }

    public String getPreferredDateLabel() {
        return preferredDateLabel;
    }

    public String getPreferredDatePlaceholder() {
        return preferredDatePlaceholder;
    }

    public String getCommentsLabel() {
        return commentsLabel;
    }

    public String getConfirmationText() {
        return confirmationText;
    }

    public String getSubmitButtonLabel() {
        return submitButtonLabel;
    }

    public String getInputErrorMsg() {
        return inputErrorMsg;
    }

    public String getConfirmationErrorMsg() {
        return confirmationErrorMsg;
    }

    public String getInputFormatErrorMsg() {
        return inputFormatErrorMsg;
    }

    public String getNumberFieldErrorMsg() {
        return numberFieldErrorMsg;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public String getSubject() {
        return subject;
    }

    public String getSalutation() {
        return salutation;
    }

    public String getBody() {
        return body;
    }

    public String getConsentLabel() {
        return consentLabel;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public String getTrainingIdLabel() {
        return trainingIdLabel;
    }

    public String getTrainingNameLabel() {
        return trainingNameLabel;
    }

    public String getFormTitle() {
        return formTitle;
    }
}
