package com.tetralaval.models.sitemap;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * UrlSet
 */
@XmlRootElement(name = "urlset")
public class UrlSet {
    /** xmlns */
    private String xmlns;
    /** urls */
    private List<Url> urls;

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
     * urls getter
     * @return urls
     */
    @XmlElement(name = "url")
    public List<Url> getUrls() {
        return urls;
    }

    /**
     * urls setter
     * @param urls
     */
    public void setUrls(List<Url> urls) {
        this.urls = urls;
    }
}
