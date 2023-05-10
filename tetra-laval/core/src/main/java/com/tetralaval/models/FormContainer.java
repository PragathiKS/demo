package com.tetralaval.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FormContainer {

    @ValueMapValue
    private String actionType;

    @ValueMapValue
    private String emailTemplate;

    @ValueMapValue
    private String redirectUrl;

    @ValueMapValue
    private String subject;

    @ValueMapValue
    private String thankYouMessage;

    @ValueMapValue
    private String thankYouTitle;

    @ValueMapValue
    private String thankYouType;

    @ValueMapValue
    private String to;

    public String getActionType() {
        return actionType;
    }

    public String getEmailTemplate() {
        return emailTemplate;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getSubject() {
        return subject;
    }

    public String getThankYouMessage() {
        return thankYouMessage;
    }

    public String getThankYouTitle() {
        return thankYouTitle;
    }

    public String getThankYouType() {
        return thankYouType;
    }

    public String getTo() {
        return to;
    }

}
