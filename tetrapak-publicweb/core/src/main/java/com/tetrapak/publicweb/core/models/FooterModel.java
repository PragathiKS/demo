package com.tetrapak.publicweb.core.models;

import com.tetrapak.publicweb.core.beans.FooterBean;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = Resource.class)
public class FooterModel {

    @Self
    private Resource resource;

    @Inject
    private String logoImagePath;
    
    @Inject
    private String altTextI18n;

    private List<FooterBean> getFooterList = new ArrayList<>();

    @PostConstruct
    protected void init() {
        Resource childResource = resource.getChild("list");
        Iterator<Resource> itr = childResource.listChildren();
        while (itr.hasNext()) {
            Resource res = itr.next();
            ValueMap valueMap = res.getValueMap();
            FooterBean bean = new FooterBean();
            bean.setImageAltI18n((String) valueMap.get("imageAltI18n"));
            bean.setTitleI18n((String) valueMap.get("titleI18n"));
            bean.setDescriptionI18n((String) valueMap.get("descriptionI18n"));
            getFooterList.add(bean);
        }
    }

    public List<FooterBean> getFooterList() {
        return getFooterList;
    }

    public String getLogoImagePath() {
        return logoImagePath;
    }
    
    public String getAltTextI18n() {
        return altTextI18n;
    }
}
