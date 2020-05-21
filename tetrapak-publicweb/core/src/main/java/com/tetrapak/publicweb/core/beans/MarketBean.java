package com.tetrapak.publicweb.core.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Class MarketBean.
 */
public class MarketBean implements Comparable<MarketBean>{

    /** The market name. */
    private String marketName;
    
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
