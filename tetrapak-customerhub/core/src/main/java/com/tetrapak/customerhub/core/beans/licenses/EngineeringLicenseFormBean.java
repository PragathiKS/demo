package com.tetrapak.customerhub.core.beans.licenses;

import java.util.List;

/**
 * Engineering License Form Bean
 *
 * @author Pathak Ankit
 */
public class EngineeringLicenseFormBean {

    /** comments */
    private String comments;
    /** users */
    private List<Users> users;

    /** get Comments */
    public String getComments() {
        return comments;
    }

    /** get Users */
    public List<Users> getUsers() {
        return users;
    }

    /**
     * User Bean
     */
    public class Users {
        private String licenseHolderName;
        private String activationDate;
        private List<String> licenses;

        public String getLicenseHolderName() {
            return licenseHolderName;
        }

        public String getActivationDate() {
            return activationDate;
        }

        public List<String> getLicenses() {
            return licenses;
        }
    }
}
