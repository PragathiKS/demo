package com.tetrapak.publicweb.core.beans;

/**
 * The Class LanguageBean.
 */
public class LanguageBean {
    
    private String languageName;
    
    private String linkPath;
    
    private int languageIndex;

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLinkPath() {
        return linkPath;
    }

    public void setLinkPath(String pagePath) {
        this.linkPath = pagePath;
    }

    public int getLanguageIndex() {
        return languageIndex;
    }

    public void setLanguageIndex(int languageIndex) {
        this.languageIndex = languageIndex;
    }
    
    

}
