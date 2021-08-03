package com.tetralaval.models.sitemap;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "sitemapindex")
public class SitemapIndex {
    private String xmlns;
    private List<Sitemap> sitemaps;

    @XmlAttribute
    public String getXmlns() {
        return xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    @XmlElement(name = "sitemap")
    public List<Sitemap> getSitemaps() {
        return sitemaps;
    }

    public void setSitemaps(List<Sitemap> sitemaps) {
        this.sitemaps = sitemaps;
    }
}
