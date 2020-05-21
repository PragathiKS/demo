package com.tetrapak.publicweb.core.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class MarketBean.
 */
public class MarketBean implements Comparable<MarketBean>{

    private String marketName;
    
    private List<LanguageBean> languages;
    
    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public List<LanguageBean> getLanguages() {
        return new ArrayList<>(languages);
    }

    public void setLanguages(List<LanguageBean> languages) {
        this.languages = new ArrayList<>(languages);
    }

    @Override
    public int compareTo(MarketBean obj) {
        return marketName.compareTo(obj.getMarketName());
    }    
    
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
    
    @Override
    public int hashCode() {
        return marketName.hashCode();
    }

}
