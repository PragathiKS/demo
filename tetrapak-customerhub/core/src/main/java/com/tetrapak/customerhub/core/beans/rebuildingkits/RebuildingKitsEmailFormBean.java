package com.tetrapak.customerhub.core.beans.rebuildingkits;

/**
 * Withdrawal License Form Bean
 *
 * @author Pawan Kumar
 */

public class RebuildingKitsEmailFormBean {
    private String rkTbNumber;
    private String mcon;
    private String functionalLocation;
    private String requestedCTILanguage;
    private String userComment;

    public String getRkTbNumber() {
        return rkTbNumber;
    }

    public String getMcon() {
        return mcon;
    }

    public String getFunctionalLocation() {
        return functionalLocation;
    }

    public String getRequestedCTILanguage() {
        return requestedCTILanguage;
    }

    public String getUserComment() {
        return userComment;
    }
}
