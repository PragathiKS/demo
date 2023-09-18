package com.tetrapak.customerhub.core.beans.powerbi;

import java.util.List;

public class BPIdentity {

    private String username;

    private List<String> roles;

    private List<String> datasets;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getDatasets() {
        return datasets;
    }

    public void setDatasets(List<String> datasets) {
        this.datasets = datasets;
    }
}
