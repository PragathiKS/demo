package com.tetrapak.publicweb.core.models;

import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.tetrapak.publicweb.core.beans.NavigationLinkBean;
import com.tetrapak.publicweb.core.beans.SocialLinkBean;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class)
public class FooterModel {

    private static final Logger log = LoggerFactory.getLogger(FooterModel.class);

    @Self
    private Resource resource;

    private String ctaLabelI18n;
    private List<SocialLinkBean> footerSocialLinks = new ArrayList<>();
    private List<NavigationLinkBean> footerNavigationLinks = new ArrayList<>();

    @PostConstruct
    protected void init() {
        InheritanceValueMap inheritanceValueMap1 = new HierarchyNodeInheritanceValueMap(resource);
        ctaLabelI18n = inheritanceValueMap1.getInherited("ctaLabelI18n", String.class);
        String[] footerNavLinks = inheritanceValueMap1.getInherited("footerNavigationLinks", String[].class);
        String[] socialLinks = inheritanceValueMap1.getInherited("footerSocialLinks", String[].class);
        setSocialLinks(socialLinks);
        LinkUtils.setMultifieldNavLinkItems(footerNavLinks, footerNavigationLinks, log);
    }

    /**
     * Method to get multi field social link items.
     */
    public void setSocialLinks(String[] socialLinks) {
        @SuppressWarnings("deprecation")
        JSONObject jObj;
        try {
            if (socialLinks == null) {
                log.error("socialLinks is NULL");
                return;
            }

            if (socialLinks != null) {
                for (int i = 0; i < socialLinks.length; i++) {
                    jObj = new JSONObject(socialLinks[i]);
                    SocialLinkBean bean = new SocialLinkBean();

                    String socialMedia = "";
                    String socialMediaLinkPath = "";
                    if (jObj.has("socialMedia")) {
                        socialMedia = jObj.getString("socialMedia");
                    }
                    if (jObj.has("socialMediaLinkPath")) {
                        socialMediaLinkPath = jObj.getString("socialMediaLinkPath");
                    }

                    bean.setSocialMediaLinkPath(socialMediaLinkPath);
                    bean.setSocialMediaIconClass("icon-" + socialMedia);
                    bean.setSocialMediaName(socialMedia);
                    footerSocialLinks.add(bean);

                }
            }
        } catch (Exception e) {
            log.error("Exception while Multifield data {}", e.getMessage(), e);
        }
    }

    public String getCtaLabelI18n() {
        return ctaLabelI18n;
    }

    public List<SocialLinkBean> getFooterSocialLinks() {
        return footerSocialLinks;
    }

    public List<NavigationLinkBean> getFooterNavigationLinks() {
        return footerNavigationLinks;
    }

}
