package com.tetrapak.customerhub.core.beans.licenses;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class EngineeringLicenseFormBean {

    private String comments;
    private List<Users> users;

    public String getComments() {
        return comments;
    }

    public List<Users> getUsers() {
        return users;
    }

    public String toString(){
        return new ToStringBuilder(this).
                append("comments", comments).
                append("users", users.toString()).
                toString();
    }

    public class Users{
        private String name;
        private String date;
        private List<String> licenses;

        public String getName() {
            return name;
        }

        public String getDate() {
            return date;
        }

        public List<String> getLicenses() {
            return licenses;
        }

        public String toString(){
            return new ToStringBuilder(this).
                    append("name", name).
                    append("date", date).
                    append("licenses", ArrayUtils.toString(licenses)).
                    toString();
        }
    }
}
