package com.tetralaval.models;


import com.tetralaval.utils.GlobalUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = { SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageLoadAnalyticsModel {
    /**
     * Checks if is publisher.
     *
     * @return the boolean
     */
    public Boolean isPublisher() {
        return GlobalUtil.isPublish();
    }

}