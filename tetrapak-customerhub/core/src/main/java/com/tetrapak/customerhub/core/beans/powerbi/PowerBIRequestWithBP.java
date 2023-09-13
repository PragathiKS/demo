package com.tetrapak.customerhub.core.beans.powerbi;

import java.util.List;

public class PowerBIRequestWithBP {

    private String accessLevel;

    private boolean allowSaveAs;

    private List<BPIdentity> identities;

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public boolean isAllowSaveAs() {
        return allowSaveAs;
    }

    public void setAllowSaveAs(boolean allowSaveAs) {
        this.allowSaveAs = allowSaveAs;
    }

    public List<BPIdentity> getIdentities() {
        return identities;
    }

    public void setIdentities(List<BPIdentity> identities) {
        this.identities = identities;
    }
}
