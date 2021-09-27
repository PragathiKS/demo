package com.tetralaval.models.sitemap;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * SitemapIndex
 */
@XmlRootElement(name = "sitemapindex")
public class SitemapIndex {
    /** xmlns */
    private String xmlns;
    /** sitemaps */
    private List<Sitemap> sitemaps;

    /**
     * xmlns getter
     * @return xmlns
     */
    @XmlAttribute
    public String getXmlns() {
        return xmlns;
    }

    /**
     * xmlns setter
     * @param xmlns
     */
    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    /**
     * sitemaps getter
     * @return sitemaps
     */
    @XmlElement(name = "sitemap")
    public List<Sitemap> getSitemaps() {
        return sitemaps;
    }

    /**
     * sitemaps setter
     * @param sitemaps
     */
    public void setSitemaps(List<Sitemap> sitemaps) {
        this.sitemaps = sitemaps;
    }
}
