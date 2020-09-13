package com.tetrapak.publicweb.core.mock;

import java.net.URL;
import java.util.Set;

import org.apache.sling.settings.SlingSettingsService;

public class MockSlingSettingService implements SlingSettingsService{

    private Set<String> runModes;
    
    public MockSlingSettingService(Set<String> runModes) {
        this.runModes.addAll(runModes);
    }
    
    @Override
    public String getAbsolutePathWithinSlingHome(String relativePath) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<String> getRunModes() {
        // TODO Auto-generated method stub
        return runModes;
    }

    @Override
    public String getSlingDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public URL getSlingHome() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSlingHomePath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSlingId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSlingName() {
        // TODO Auto-generated method stub
        return null;
    }

}
