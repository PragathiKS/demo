package com.tetrapak.customerhub.core.beans.licenses;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class EngineeringLicenseFormBean {
    
    private static final String COMMENTS_FIELD = "comments";
    private static final String USERS_FIELD = "users";
    
    private String comments;
    private List<Users> users;
    
    public String getComments() {
        return comments;
    }
    
    public List<Users> getUsers() {
        return users;
    }
    
    public String toString() {
        return new ToStringBuilder(this).append(COMMENTS_FIELD, comments).append(USERS_FIELD, users.toString())
                .toString();
    }
    
    public class Users {
        
        private static final String NAME_FIELD = "licenseHolderName";
        private static final String DATE_FIELD = "activationDate";
        private static final String LICENSES_FIELD = "licenses";
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
        
        public String toString() {
            return new ToStringBuilder(this).append(NAME_FIELD, licenseHolderName).append(DATE_FIELD, activationDate)
                    .append(LICENSES_FIELD, ArrayUtils.toString(licenses)).toString();
        }
    }
}
