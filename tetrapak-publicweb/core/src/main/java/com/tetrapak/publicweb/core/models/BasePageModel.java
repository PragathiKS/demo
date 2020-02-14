package com.tetrapak.publicweb.core.models;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BasePageModel {

    protected PageContentModel pageContent;

    private ValueMap jcrMap;

    private String title;
    private String description;
    private String imagePath;
    private String altText;
    private String linkText;
    private String linkPath;
    private String linkTarget;

    @Self
    protected Resource jcrResource;

    @PostConstruct
    public void init() {
        if (jcrResource != null) {
            Page page = this.jcrResource.adaptTo(Page.class);
            if (page == null) {
                this.jcrResource = getPageManager().getContainingPage(jcrResource).getContentResource();
            } else {
                this.jcrResource = this.jcrResource.getChild(JcrConstants.JCR_CONTENT);
            }
        }
        if (jcrResource != null) {
            pageContent = jcrResource.adaptTo(PageContentModel.class);
        }
        if (jcrMap != null) {
            title = jcrMap.get("jcr:title", String.class);
            description = jcrMap.get("jcr:description", String.class);

            imagePath = jcrMap.get("imagePath", String.class);
            altText = jcrMap.get("altText", String.class);
            linkText = jcrMap.get("linkText", String.class);
            linkTarget = jcrMap.get("linkTarget", String.class);
            if (StringUtils.isEmpty(linkText)) {
                linkText = title;
            }

            linkPath = jcrMap.get("linkPath", String.class);
        }
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder("BasePageModel{");
        if (null != pageContent) {
            sb.append(pageContent.toString());
        }
        sb.append('}');
        return sb.toString();
    }

    public PageContentModel getPageContent() {
        return pageContent;
    }

    protected PageManager getPageManager() {
        return jcrResource.getResourceResolver().adaptTo(PageManager.class);
    }

    protected TagManager getTagManager() {
        return jcrResource.getResourceResolver().adaptTo(TagManager.class);
    }

    public Resource getJcrResource() {
        return jcrResource;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }


    public String getImagePath() {
        return LinkUtils.sanitizeLink(imagePath);
    }

    public String getAltText() {
        return altText;
    }

    public String getLinkText() {
        return linkText;
    }

    public String getLinkPath() {
        return linkPath;
    }

    public String getLinkTarget() {
        return linkTarget;
    }
}
