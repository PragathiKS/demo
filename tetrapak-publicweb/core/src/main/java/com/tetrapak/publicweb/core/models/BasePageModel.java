package com.tetrapak.publicweb.core.models;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BasePageModel {

    protected PageContentModel pageContent;

    @Self
    protected Resource jcrResource;

    @PostConstruct
    public void init() {
        if (jcrResource != null) {
            Page page = this.jcrResource.adaptTo(Page.class);
            if (page == null) {
                this.jcrResource = getPageManager().getContainingPage(jcrResource).getContentResource();
            } else {
                if (this.jcrResource != null)
                    this.jcrResource = this.jcrResource.getChild(JcrConstants.JCR_CONTENT);
            }
        }
        if (jcrResource != null)
            pageContent = jcrResource.adaptTo(PageContentModel.class);
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
}
