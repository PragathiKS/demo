package com.tetrapak.supplierportal.core.models;

import com.day.cq.wcm.api.Page;
import com.tetrapak.supplierportal.core.beans.HeaderBean;
import com.tetrapak.supplierportal.core.utils.LinkUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderModel {

    private static final String LOGO_TEXT_I18N_KEY = "sp.logotext.text";
    private static final String USER_INFO_I18N = "Charlie Svensson";

    @SlingObject private SlingHttpServletRequest request;

    @Inject private Page currentPage;

    private String logoUrl;

    private String mLogoLink;

    private String dLogoLink;

    private List<HeaderBean> headerNavLinks = new ArrayList<>();

    @PostConstruct protected void init() {
        Resource headerConfigurationResource = request.getResourceResolver()
                .getResource(currentPage.getPath() + "/jcr:content/root/responsivegrid/headerconfiguration");
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
        return LOGO_TEXT_I18N_KEY;
    }

    public String getGetUserInfoI18n() {
        return USER_INFO_I18N;
    }

    public boolean getRightNavDisplay() {
        return true;
    }
}
