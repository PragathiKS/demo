package com.tetrapak.customerhub.core.beans.aip;

import com.google.gson.annotations.SerializedName;

public class CotsSupportFormBean {

    public static final String QUERY_TYPE = "logQueryType";
    public static final String COMPANY = "company";
    public static final String CUSTOMER_SITE = "customerSite";
    public static final String AFFECTED_SYSTEMS = "affectedSystems";
    public static final String PRODUCT_INVOLVED = "productInvolved";
    public static final String SOFTWARE_VERSION = "softwareVersion";
    public static final String LICENSE_NUMBER = "licensenumber";
    public static final String DESCRIPTION = "description";
    public static final String QUESTIONS = "questions";
    public static final String NAME = "name";
    public static final String EMAIL_ADDRESS = "emailAddress";
    public static final String TELEPHONE = "telephone";
    public static final String FILES = "files";

    @SerializedName(QUERY_TYPE)
    private String logQueryType;
    @SerializedName(COMPANY)
    private String company;
    @SerializedName(CUSTOMER_SITE)
    private String customerSite;
    @SerializedName(AFFECTED_SYSTEMS)
    private String affectedSystems;
    @SerializedName(PRODUCT_INVOLVED)
    private String productInvolved;
    @SerializedName(SOFTWARE_VERSION)
    private String softwareVersion;
    @SerializedName(LICENSE_NUMBER)
    private String licenseNumber;
    @SerializedName(DESCRIPTION)
    private String description;
    @SerializedName(QUESTIONS)
    private String questions;
    @SerializedName(NAME)
    private String name;
    @SerializedName(EMAIL_ADDRESS)
    private String emailAddress;
    @SerializedName(TELEPHONE)
    private String telephone;
    @SerializedName(FILES)
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
