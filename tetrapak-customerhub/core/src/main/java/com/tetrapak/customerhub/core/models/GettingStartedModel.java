package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.beans.GettingStartedBean;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = Resource.class) public class GettingStartedModel {

    @Inject private Resource resource;

    private List<GettingStartedBean> gettingStartedList = new ArrayList<>();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostConstruct protected void init() {
        logger.info("post construct started");
        logger.info("resource: {}", resource);
        logger.info("resourceType: {}", resource.getName());
        Resource childResource = resource.getChild("list");
        Iterator<Resource> itr = childResource.listChildren();
        while (itr.hasNext()) {
            Resource res = itr.next();
            ValueMap valueMap = res.getValueMap();
            GettingStartedBean bean = new GettingStartedBean();
            bean.setImagePath((String) valueMap.get("imagePath"));
            bean.setImageAltI18n((String) valueMap.get("imageAltI18n"));
            bean.setTitleI18n((String) valueMap.get("titleI18n"));
            bean.setDescriptionI18n((String) valueMap.get("descriptionI18n"));
            gettingStartedList.add(bean);
        }
    }

    public List<GettingStartedBean> getGettingStartedList() {
        return gettingStartedList;
    }
}
