package com.tetralaval.models.sitemap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Sitemap
 */
@XmlRootElement(name = "sitemap")
public class Sitemap {
    /** location */
    private String location;

    /**
     * location getter
     * @return location
     */
    @XmlElement(name = "loc")
    public String getLocation() {
        return location;
    }

    /**
     * location setter
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }
}
