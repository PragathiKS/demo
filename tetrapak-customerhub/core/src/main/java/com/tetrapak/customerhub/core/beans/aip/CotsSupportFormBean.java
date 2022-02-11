package com.tetrapak.customerhub.core.beans.aip;

import com.google.gson.annotations.SerializedName;

public class CotsSupportFormBean {

    public static final String QUERY_TYPE_PARAMETER = "logQueryType";
    public static final String COMPANY_PARAMETER = "company";
    public static final String CUSTOMER_SITE_PARAMETER = "customerSite";
    public static final String AFFECTED_SYSTEMS_PARAMETER = "affectedSystems";
    public static final String PRODUCT_INVOLVED_PARAMETER = "productInvolved";
    public static final String SOFTWARE_VERSION_PARAMETER = "softwareVersion";
    public static final String LICENSE_NUMBER_PARAMETER = "licensenumber";
    public static final String DESCRIPTION_PARAMETER = "description";
    public static final String QUESTIONS_PARAMETER = "questions";
    public static final String NAME_PARAMETER = "name";
    public static final String EMAIL_ADDRESS_PARAMETER = "emailAddress";
    public static final String TELEPHONE_PARAMETER = "telephone";
    public static final String FILES_PARAMETER = "files";

    @SerializedName(QUERY_TYPE_PARAMETER)
    private String logQueryType;
    @SerializedName(COMPANY_PARAMETER)
    private String company;
    @SerializedName(CUSTOMER_SITE_PARAMETER)
    private String customerSite;
    @SerializedName(AFFECTED_SYSTEMS_PARAMETER)
    private String affectedSystems;
    @SerializedName(PRODUCT_INVOLVED_PARAMETER)
    private String productInvolved;
    @SerializedName(SOFTWARE_VERSION_PARAMETER)
    private String softwareVersion;
    @SerializedName(LICENSE_NUMBER_PARAMETER)
    private String licenseNumber;
    @SerializedName(DESCRIPTION_PARAMETER)
    private String description;
    @SerializedName(QUESTIONS_PARAMETER)
    private String questions;
    @SerializedName(NAME_PARAMETER)
    private String name;
    @SerializedName(EMAIL_ADDRESS_PARAMETER)
    private String emailAddress;
    @SerializedName(TELEPHONE_PARAMETER)
    private String telephone;
    @SerializedName(FILES_PARAMETER)
    private String files;

    public String getLogQueryType() {
        return logQueryType;
    }

    public String getCompany() {
        return company;
    }

    public String getCustomerSite() {
        return customerSite;
    }

    public String getAffectedSystems() {
        return affectedSystems;
    }

    public String getProductInvolved() {
        return productInvolved;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getDescription() {
        return description;
    }

    public String getQuestions() {
        return questions;
    }

    public String getName() {
        return name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getFiles() {
        return files;
    }
}
