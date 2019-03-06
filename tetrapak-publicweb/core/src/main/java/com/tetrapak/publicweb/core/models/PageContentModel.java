package com.tetrapak.publicweb.core.models;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.xss.XSSAPI;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageContentModel {
	
    private String pagePath;

    @Inject
    private ResourceResolverFactory resolverFactory;

    @Inject
    @Named("jcr:description")
    @Default
    private String description;

    @Inject
    @Named("sling:resourceType")
    private String slingResourceType;

    @Inject
    @Named("cq:template")
    private String template;

    @Inject
    @Named("jcr:title")
    @Default
    protected String title;

    @Inject
    @Named("subtitle")
    @Default
    protected String subtitle;

    @Inject
    @Named("jcr:createdBy")
    @Default
    protected String createdBy;

    @Inject
    @Named(NameConstants.PN_PAGE_LAST_MOD)
    @Optional
    private Date lastUpdate;

    @Inject
    @Named(NameConstants.PN_PAGE_LAST_REPLICATED)
    @Optional
    private Date lastReplicated;

    @Inject
    @Named(NameConstants.PN_CREATED)
    @Optional
    private Date createdOn;

    @Inject
    @Named(NameConstants.PN_TAGS)
    @Optional
    private String[] cqTags;

    @Inject
    @Default
    protected String navTitle;

    @Inject
    @Default
    protected String pageTitle;

    @Inject
    @Default
    protected String author;
    
    private ValueMap jcrMap;

    private Resource resource;

    private static final Logger logger = LoggerFactory.getLogger(PageContentModel.class);
    
    @PostConstruct
    public void init() {
        if (resource == null)
            return;

        ResourceResolver resolver = resource.getResourceResolver();
        final TagManager tagManager = resolver.adaptTo(TagManager.class);
        pagePath = resource.getParent().getPath();
        
        jcrMap = resource.getValueMap();
    }

    public PageContentModel(Resource resource) {
        this.resource = resource;
    }

    public String getPath() {
        return pagePath;
    }

    public String getDescription() {
        return description;
    }

    public String getSlingResourceType() {
        return slingResourceType;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getAuthorName() {
        return author;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public Date getLastReplicated() {
        return lastReplicated;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public String[] getCqTags() {
        return cqTags;
    }

    public String toString() {

        final StringBuilder sb = new StringBuilder("");
        sb.append(", description='").append(description).append('\'');
        sb.append(", id='").append(pagePath).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", subtitle='").append(subtitle).append('\'');
        sb.append(", slingResourceType='").append(slingResourceType).append('\'');
        sb.append(", lastUpdate=").append(lastUpdate);
        sb.append(", tags=").append(Arrays.toString(cqTags));
        return sb.toString();
    }


    public String getPageTitle() {
        return pageTitle;
    }

    public String getNavTitle() {
        return navTitle;
    }

    public ValueMap getJcrMap() {
        return jcrMap;
    }

}