package com.tetrapak.publicweb.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.beans.TeaserBean;
import com.tetrapak.publicweb.core.services.TeaserSearchService;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.commons.lang.StringUtils;
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
            generateListAutomaticWay(resolver);
        } else if ("semi-automatic".equals(contentType) && pageManager != null) {
            generateListSemiAutomatically(pageManager);
        } else if ("manual".equals(contentType) && pageManager != null) {
            generateListManually();
        }
    }

    private void generateListAutomaticWay(ResourceResolver resolver) {
        //todo this path is to be made dynamic later when we have language and country specific pages
        String rootPath = "/content";
        List<Page> pagePaths = teaserSearchService.getListOfTeasers(resolver, tags, rootPath, maxTeasers);
        if (null == pagePaths) {
            return;
        }
        for (Page page : pagePaths) {
            Resource jcrContentResource = page.getContentResource();
            if (null != jcrContentResource) {
                BasePageModel basePageModel = jcrContentResource.adaptTo(BasePageModel.class);
                addToList(page.getPath(), basePageModel);
            }
        }
    }

    private void generateListSemiAutomatically(PageManager pageManager) {
        Resource listResource = resource.getChild("semiAutomaticList");
        if (null != listResource && !ResourceUtil.isNonExistingResource(listResource)) {
            Iterator<Resource> itr = listResource.listChildren();
            while (itr.hasNext()) {
                Resource itemResource = itr.next();
                addToTeaserList(pageManager, itemResource);
            }
        }
    }

    private void generateListManually() {
        Resource listResource = resource.getChild("manualList");
        if (null != listResource && !ResourceUtil.isNonExistingResource(listResource)) {
            Iterator<Resource> itr = listResource.listChildren();
            while (itr.hasNext()) {
                Resource itemResource = itr.next();
                ValueMap valueMap = itemResource.getValueMap();
                TeaserBean teaserBean = new TeaserBean();
                teaserBean.setTitle(valueMap.get("title", String.class));
                teaserBean.setDescription(valueMap.get("description", String.class));
                teaserBean.setImagePath(valueMap.get("imagePath", String.class));
                teaserBean.setAltText(valueMap.get("altText", String.class));
                teaserBean.setLinkText(valueMap.get("linkText", String.class));
                teaserBean.setLinkPath(LinkUtils.sanitizeLink(valueMap.get("linkPath", String.class)));
                teaserBean.setTargetNew(valueMap.get("targetNew", String.class));
                teaserList.add(teaserBean);
            }
        }
    }

    private void addToTeaserList(PageManager pageManager, Resource itemResource) {
        ValueMap vMap = itemResource.getValueMap();
        if (vMap.containsKey("pagePath")) {
            String pagePath = (String) vMap.get("pagePath");
            Page page = pageManager.getPage(pagePath);
            if (page != null) {
                Resource jcrContentResource = page.getContentResource();
                if (null != jcrContentResource) {
                    BasePageModel basePageModel = jcrContentResource.adaptTo(BasePageModel.class);
                    addToList(pagePath, basePageModel);
                }
            }
        }
    }

    private void addToList(String pagePath, BasePageModel basePageModel) {
        if (basePageModel != null) {
            TeaserBean teaserBean = new TeaserBean();
            teaserBean.setTitle(basePageModel.getTitle());
            teaserBean.setDescription(basePageModel.getDescription());
            teaserBean.setImagePath(basePageModel.getImagePath());
            teaserBean.setAltText(basePageModel.getAltText());
            teaserBean.setLinkText(basePageModel.getLinkText());
            if(StringUtils.isNotEmpty(basePageModel.getLinkPath())) {
                teaserBean.setLinkPath(basePageModel.getLinkPath());
            }else{
                teaserBean.setLinkPath(LinkUtils.sanitizeLink(pagePath));
            }
            teaserBean.setTargetNew(basePageModel.getLinkTarget());
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
