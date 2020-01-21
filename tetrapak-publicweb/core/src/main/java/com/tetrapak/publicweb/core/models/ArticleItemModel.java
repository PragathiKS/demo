package com.tetrapak.publicweb.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleItemModel {

    @Self
    private Resource resource;

    @Inject
    private String contentType;

    @Inject
    private String articlePath;

    @Inject
    private String articleTitle;

    @Inject
    private String vanityDescription;

    @Inject
    private String ctaTexti18nKey;

    @Inject
    private Boolean openInNewWindow;

    @Inject
    private Boolean showImage;

    @Inject
    private String articleImagePath;

    @Inject
    private String articleImageAltI18n;

    @Inject
    private String pwButtonTheme;

    @PostConstruct
    protected void init() {
        ResourceResolver resolver = resource.getResourceResolver();
        PageManager pageManager = resolver.adaptTo(PageManager.class);

        if ("automatic".equals(contentType) && pageManager != null) {
            Page articlePage = pageManager.getPage(articlePath);
            if (articlePage != null) {
                Resource jcrContentResource = articlePage.getContentResource();
                ArticlePageModel articlePageModel = jcrContentResource.adaptTo(ArticlePageModel.class);
                if (articlePageModel != null) {
                    articleTitle = articlePageModel.getArticleTitle();
                    vanityDescription = articlePageModel.getDescription();
                    ctaTexti18nKey = articlePageModel.getLinkText();
                    articleImagePath = articlePageModel.getArticleImagePath();
                    articleImageAltI18n = articlePageModel.getImageAltTextI18n();
                }
            }

        }
    }

    public Resource getResource() {
        return resource;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getVanityDescription() {
        return vanityDescription;
    }

    public String getCtaTexti18nKey() {
        return ctaTexti18nKey;
    }

    public String getContentType() {
        return contentType;
    }

    public String getArticlePath() {
        return LinkUtils.sanitizeLink(articlePath);
    }

    public String getLinkType() {
        return LinkUtils.linkType(articlePath);
    }

    public Boolean getOpenInNewWindow() {
        return openInNewWindow;
    }

    public Boolean getShowImage() {
        return showImage;
    }

    public String getArticleImagePath() {
        return articleImagePath;
    }

    public String getArticleImageAltI18n() {
        return articleImageAltI18n;
    }

    public String getPwButtonTheme() {
        return pwButtonTheme;
    }
}
