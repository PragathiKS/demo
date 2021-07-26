package com.tetrapak.publicweb.core.beans;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class LanguageBean.
 */
public class LanguageBean implements Comparable<LanguageBean>{
    
    /** The language name. */
    private String languageName;
    
    /** The link path. */
    private String linkPath;
    
    /** The language index. */
    private int languageIndex;

    /** The country title. */
    private String countryTitle;

    /** The is current language. */
    private boolean isCurrentLanguage;

    /**
     * Gets the language name.
     *
     * @return the language name
     */
    public String getLanguageName() {
        return languageName;
    }

    /**
     * Sets the language name.
     *
     * @param languageName the new language name
     */
    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    /**
     * Gets the link path.
     *
     * @return the link path
     */
    public String getLinkPath() {
        return linkPath;
    }

    /**
     * Sets the link path.
     *
     * @param pagePath the new link path
     */
    public void setLinkPath(String pagePath) {
        this.linkPath = pagePath;
    }

    /**
     * Gets the language index.
     *
     * @return the language index
     */
    public int getLanguageIndex() {
        return languageIndex;
    }

    /**
     * Sets the language index.
     *
     * @param languageIndex
     *            the new language index
     */
    public void setLanguageIndex(int languageIndex) {
        this.languageIndex = languageIndex;
    }

    /**
     * Gets the country title.
     *
     * @return the country title
     */
    public String getCountryTitle() {
        return countryTitle;
    }

    /**
     * Sets the country title.
     *
     * @param countryTitle
     *            the new country title
     */
    public void setCountryTitle(String countryTitle) {
        this.countryTitle = countryTitle;
    }

    /**
     * Checks if is current language.
     *
     * @return true, if is current language
     */
    public boolean isCurrentLanguage() {
        return isCurrentLanguage;
    }

    /**
     * Sets the current language.
     *
     * @param isCurrentLanguage
     *            the new current language
     */
    public void setCurrentLanguage(boolean isCurrentLanguage) {
        this.isCurrentLanguage = isCurrentLanguage;
    }

    /**
     * Compare to.
     *
     * @param obj the obj
     * @return the int
     */
    @Override
    public int compareTo(LanguageBean obj) {
        if(StringUtils.isBlank(languageName)){
            return 0;
        }
        return languageName.compareTo(obj.getLanguageName());
    }    
    
    /**
     * Equals.
     *
     * @param obj the obj
     * @return true, if successful
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LanguageBean)) {
            return false;
        }
        return languageName.equalsIgnoreCase(((LanguageBean) obj).getLanguageName());
    }
    
    /**
     * Hash code.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return languageName.hashCode();
    }
    
    

}
