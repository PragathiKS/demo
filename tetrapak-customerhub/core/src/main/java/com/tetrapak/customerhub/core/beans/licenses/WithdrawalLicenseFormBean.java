package com.tetrapak.customerhub.core.beans.licenses;

/**
 * Withdrawal License Form Bean
 *
 * @author Pawan Kumar
 */
public class WithdrawalLicenseFormBean {

    private String licenseKey;
    private String platform;
    private String comments;
    private String name;
    private String startDate;
    private String endDate;

    public String getLicenseKey() {
        return licenseKey;
    }

    public String getPlatform() {
        return platform;
    }

    public String getComments() {
        return comments;
    }

    public String getName() {
        return name;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

}
