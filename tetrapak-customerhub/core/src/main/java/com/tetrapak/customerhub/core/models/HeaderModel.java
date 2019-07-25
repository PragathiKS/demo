package com.tetrapak.customerhub.core.models;

import com.day.cq.wcm.api.Page;
import com.tetrapak.customerhub.core.beans.HeaderBean;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.LinkUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Model class for Header Configurations Component
 *
 * @author Nitin Kumar
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderModel {

    @Self
    private Resource resource;

    private String logoUrl;

    private String mLogoLink;

    private String dLogoLink;

    private List<HeaderBean> headerNavLinks = new ArrayList<>();

    @PostConstruct
    protected void init() {
        Page languagePage = GlobalUtil.getLanguagePage(resource);
        if (null != languagePage) {
            Resource headerConfigurationResource = resource.getResourceResolver().getResource(languagePage.getPath()
                    + "/jcr:content/root/responsivegrid/headerconfiguration");
            if (null != headerConfigurationResource) {
                ValueMap map = headerConfigurationResource.getValueMap();
                logoUrl = (String) map.get("logoUrl");
                mLogoLink = LinkUtil.getValidLink(resource, (String) map.get("mLogoLink"));
                dLogoLink = LinkUtil.getValidLink(resource, (String) map.get("dLogoLink"));
                Resource links = headerConfigurationResource.getChild("headerNavLinks");
                if (null == links) {
                    return;
                }
                Iterator<Resource> itr = links.listChildren();
                while (itr.hasNext()) {
                    Resource res = itr.next();
                    ValueMap valueMap = res.getValueMap();
                    HeaderBean headerBean = new HeaderBean();
                    headerBean.setHref(LinkUtil.getValidLink(resource, (String) valueMap.get("href")));
                    headerBean.setName((String) valueMap.get("name"));
                    headerBean.setTargetNew(StringUtils.equalsIgnoreCase("true", (String) valueMap.get("targetNew")));
                    headerNavLinks.add(headerBean);
                }

            }
        }
    }

    public Resource getResource() {
        return resource;
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
