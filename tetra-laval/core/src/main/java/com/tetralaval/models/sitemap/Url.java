package com.tetralaval.models.sitemap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Url
 */
@XmlRootElement(name = "url")
@XmlType(propOrder = { "location", "lastModification" })
public class Url {
    /** location */
    private String location;
    /** lastModification */
    private String lastModification;

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

    /**
     * lastModification getter
     * @return lastModification
     */
    @XmlElement(name = "lastmod")
    public String getLastModification() {
        return lastModification;
    }

    /**
     * lastModification setter
     * @param lastModification
     */
    public void setLastModification(String lastModification) {
        this.lastModification = lastModification;
    }
}
