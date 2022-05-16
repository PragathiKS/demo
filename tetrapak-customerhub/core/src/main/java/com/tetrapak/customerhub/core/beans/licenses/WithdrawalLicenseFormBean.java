package com.tetrapak.customerhub.core.beans.licenses;

/**
 * Withdrawal License Form Bean
 *
 * @author Pawan Kumar
 */
public class WithdrawalLicenseFormBean {
    public static final String License_KEY = "licenseKey";
    public static final String Platform_KEY = "platform";
    public static final String Country_KEY = "countryName";
    public static final String COMMENTS_JSON_KEY = "comments";
    public static final String REQUESTOR_NAME_KEY = "requestorName";

    private String subject;
    private String salutation;
    private String bodyText;
    private String userName;
    private String licenseKey;
    private String platform;
    private String country;
    private String comments;
    private String requestor;
    private String requestorEmailAddress;
    private String getContactPerson;

    public String getSubject() {
        return subject;
    }

    public String getSalutation() {
        return salutation;
    }

    public String getBodyText() {
        return bodyText;
    }

    public String getUserName() {
        return userName;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public String getPlatform() {
        return platform;
    }

    public String getCountry() {
        return country;
    }

    public String getComments() {
        return comments;
    }

    public String getRequestor() {
        return requestor;
    }

    public String getRequestorEmailAddress() {
        return requestorEmailAddress;
    }

    public String getGetContactPerson() {
        return getContactPerson;
    }
}
