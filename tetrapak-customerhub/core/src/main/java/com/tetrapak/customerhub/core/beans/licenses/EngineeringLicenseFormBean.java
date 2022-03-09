package com.tetrapak.customerhub.core.beans.licenses;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class EngineeringLicenseFormBean {

    private String comments;

    public String getComments() {
        return comments;
    }

    public String toString(){
        return new ToStringBuilder(this).
                append(comments, comments).
                toString();
    }
}
