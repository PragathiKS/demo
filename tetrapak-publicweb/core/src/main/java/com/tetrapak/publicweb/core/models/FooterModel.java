package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.tetrapak.publicweb.core.beans.FooterBean;

@Model(adaptables=Resource.class, defaultInjectionStrategy=DefaultInjectionStrategy.OPTIONAL)
public class FooterModel {

    @Self
    private Resource resource;

    @Inject
    private String imagePath;
    
    @Inject
    private String imageAltI18n;
    
    @Inject
    private String imageLink;
    
    @Inject
    private String imageTitleI18n;
    
    @Inject
    private String ctaLabelI18n;
    
    @Inject
    private String socialMediaLinkedin;
    
    @Inject
    private String socialMediaFacebook;
    
    @Inject
    private String socialMediaTwitter;
    
    @Inject
    private String socialMediaYoutube;

    private List<FooterBean> footerNavigationLinkList = new ArrayList<>();

    @PostConstruct
    protected void init() {
        Resource childResource = resource.getChild("footerNavigationLinks");
        if (childResource != null) {
        	Iterator<Resource> itr = childResource.listChildren();
            while (itr.hasNext()) {
                Resource res = itr.next();
                ValueMap valueMap = res.getValueMap();
                FooterBean bean = new FooterBean();
                bean.setLinkTextI18n((String) valueMap.get("linkTextI18n"));
                bean.setLinkTooltipI18n((String) valueMap.get("linkTooltipI18n"));
                bean.setLinkPath((String) valueMap.get("linkPath"));
                bean.setTargetBlank((String) valueMap.get("targetBlank"));
                footerNavigationLinkList.add(bean);
            }
        }        
    }

	public Resource getResource() {
		return resource;
	}

	public String getImagePath() {
		return imagePath;
	}

	public String getImageAltI18n() {
		return imageAltI18n;
	}

	public String getImageLink() {
		return imageLink;
	}

	public String getImageTitleI18n() {
		return imageTitleI18n;
	}

	public String getCtaLabelI18n() {
		return ctaLabelI18n;
	}

	public String getSocialMediaLinkedin() {
		return socialMediaLinkedin;
	}

	public String getSocialMediaFacebook() {
		return socialMediaFacebook;
	}

	public String getSocialMediaTwitter() {
		return socialMediaTwitter;
	}

	public String getSocialMediaYoutube() {
		return socialMediaYoutube;
	}

	public List<FooterBean> getFooterNavigationLinkList() {
		return footerNavigationLinkList;
	}

}
