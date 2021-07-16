package com.tetrapak.publicweb.core.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class MarketBean.
 */
public class MarketBean implements Comparable<MarketBean>{

    /** The market name. */
    private String marketName;

    /** The country name. */
    private String countryName;

    /** The is active. */
    private boolean isActive;

    /** The languages. */
    private List<LanguageBean> languages;
    
    /**
     * Gets the market name.
     *
     * @return the market name
     */
    public String getMarketName() {
        return marketName;
    }

    /**
     * Sets the market name.
     *
     * @param marketName the new market name
     */
    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    /**
     * Gets the country name.
     *
     * @return the country name
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Sets the country name.
     *
     * @param countryName
     *            the new country name
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * Checks if is active.
     *
     * @return true, if is active
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the active.
     *
     * @param isActive
     *            the new active
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Gets the languages.
     *
     * @return the languages
     */
    public List<LanguageBean> getLanguages() {
        Collections.sort(languages);
        return new ArrayList<>(languages);
    }

    /**
     * Sets the languages.
     *
     * @param languages the new languages
     */
    public void setLanguages(List<LanguageBean> languages) {
        this.languages = new ArrayList<>(languages);
    }

    /**
     * Compare to.
     *
     * @param obj the obj
     * @return the int
     */
    @Override
    public int compareTo(MarketBean obj) {
        if(StringUtils.isBlank(marketName)){
            return 0;
        }
        return marketName.compareTo(obj.getMarketName());
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
        if (!(obj instanceof MarketBean)) {
            return false;
        }
        return marketName.equalsIgnoreCase(((MarketBean) obj).getMarketName());
    }
    
    /**
     * Hash code.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return marketName.hashCode();
    }

}
