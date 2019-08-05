package com.tetrapak.customerhub.core.models;

import com.day.cq.wcm.api.Page;
import com.tetrapak.customerhub.core.beans.HeaderBean;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.LinkUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Model class for Header Configurations Component
 *
 * @author Nitin Kumar
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderModel {

    @SlingObject
    private SlingHttpServletRequest request;

    @OSGiService
    private UserPreferenceService userPreferenceService;

    private String logoUrl;

    private String mLogoLink;

    private String dLogoLink;

    private List<HeaderBean> headerNavLinks = new ArrayList<>();

    @PostConstruct
    protected void init() {
        Page languagePage = GlobalUtil.getLanguagePage(request, userPreferenceService);
        if (null != languagePage) {
            Resource headerConfigurationResource = request.getResourceResolver().getResource(languagePage.getPath()
                    + "/jcr:content/root/responsivegrid/headerconfiguration");
            if (null != headerConfigurationResource) {
                ValueMap map = headerConfigurationResource.getValueMap();
                logoUrl = (String) map.get("logoUrl");
                mLogoLink = LinkUtil.getValidLink(request.getResource(), (String) map.get("mLogoLink"));
                dLogoLink = LinkUtil.getValidLink(request.getResource(), (String) map.get("dLogoLink"));
                Resource links = headerConfigurationResource.getChild("headerNavLinks");
                if (null == links) {
                    return;
                }
                Iterator<Resource> itr = links.listChildren();
                while (itr.hasNext()) {
                    Resource res = itr.next();
                    ValueMap valueMap = res.getValueMap();
                    HeaderBean headerBean = new HeaderBean();
                    headerBean.setHref(LinkUtil.getValidLink(request.getResource(), (String) valueMap.get("href")));
                    headerBean.setName((String) valueMap.get("name"));
                    headerBean.setTargetNew(StringUtils.equalsIgnoreCase("true", (String) valueMap.get("targetNew")));
                    headerNavLinks.add(headerBean);
                }

            }
        }
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getMLogoLink() {
        return mLogoLink;
    }

    public String getDLogoLink() {
        return dLogoLink;
    }

    public List<HeaderBean> getHeaderNavLinks() {
        return headerNavLinks;
    }

    public String getLogoTextI18n() {
        return "cuhu.logotext.text";
    }

    public String getGetUserInfoI18n() {
        return "Charlie Svensson";
    }

    public boolean getRightNavDisplay() {
        return true;
    }
}
