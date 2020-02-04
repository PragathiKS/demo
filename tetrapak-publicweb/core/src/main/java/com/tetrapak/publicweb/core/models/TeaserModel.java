package com.tetrapak.publicweb.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.beans.TeaserBean;
import com.tetrapak.publicweb.core.services.TeaserSearchService;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TeaserModel {

    @Self
    private Resource resource;

    @Inject
    private TeaserSearchService teaserSearchService;

    @Inject
    private String contentType;

    @Inject
    private String pwButtonTheme;

    @Inject
    private String[] tags;

    @Inject
    private int maxTeasers;

    private List<TeaserBean> teaserList = new ArrayList<>();

    @PostConstruct
    protected void init() {
        ResourceResolver resolver = resource.getResourceResolver();
        PageManager pageManager = resolver.adaptTo(PageManager.class);

        if ("automatic".equals(contentType) && pageManager != null) {
            //todo this path is to be made dynamic later when we have language and country specific pages
            String rootPath = "/content";
            List<Page> pagePaths = teaserSearchService.getListOfTeasers(resolver, tags, rootPath, maxTeasers);
            if (null == pagePaths) {
                return;
            }
            for (Page page : pagePaths) {
                Resource jcrContentResource = page.getContentResource();
                if (null != jcrContentResource) {
                    ArticlePageModel articlePageModel = jcrContentResource.adaptTo(ArticlePageModel.class);
                    addToList(page.getPath(), articlePageModel);
                }
            }
        } else if ("semi-automatic".equals(contentType) && pageManager != null) {
            Resource listResource = resource.getChild("semiAutomaticList");
            if (null != listResource && !ResourceUtil.isNonExistingResource(listResource)) {
                Iterator<Resource> itr = listResource.listChildren();
                while (itr.hasNext()) {
                    Resource itemResource = itr.next();
                    addToTeaserList(pageManager, itemResource);
                }
            }
        } else {
            Resource listResource = resource.getChild("manualList");
            if (null != listResource && !ResourceUtil.isNonExistingResource(listResource)) {
                Iterator<Resource> itr = listResource.listChildren();
                while (itr.hasNext()) {
                    Resource itemResource = itr.next();
                    ArticlePageModel articlePageModel = itemResource.adaptTo(ArticlePageModel.class);
                    addToList(articlePageModel);
                }
            }
        }
    }

    private void addToTeaserList(PageManager pageManager, Resource itemResource) {
        ValueMap vMap = itemResource.getValueMap();
        if (vMap.containsKey("pagePath")) {
            String pagePath = (String) vMap.get("pagePath");
            Page articlePage = pageManager.getPage(pagePath);
            if (articlePage != null) {
                Resource jcrContentResource = articlePage.getContentResource();
                if (null != jcrContentResource) {
                    ArticlePageModel articlePageModel = jcrContentResource.adaptTo(ArticlePageModel.class);
                    addToList(pagePath, articlePageModel);
                }
            }
        }
    }

    private void addToList(ArticlePageModel articlePageModel) {
        if (articlePageModel != null) {
            addToList(articlePageModel.getLinkPath(), articlePageModel);
        }
    }

    private void addToList(String pagePath, ArticlePageModel articlePageModel) {
        if (articlePageModel != null) {
            TeaserBean teaserBean = new TeaserBean();
            teaserBean.setTitle(articlePageModel.getArticleTitle());
            teaserBean.setDescription(articlePageModel.getDescription());
            teaserBean.setImagePath(articlePageModel.getArticleImagePath());
            teaserBean.setAltText(articlePageModel.getImageAltTextI18n());
            teaserBean.setLinkText(articlePageModel.getLinkText());
            teaserBean.setLinkPath(LinkUtils.sanitizeLink(pagePath));
            teaserList.add(teaserBean);
        }
    }

    public Resource getResource() {
        return resource;
    }

    public String getContentType() {
        return contentType;
    }

    public String getPwButtonTheme() {
        return pwButtonTheme;
    }

    public List<TeaserBean> getTeaserList() {
        return teaserList;
    }
}
