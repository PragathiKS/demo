package com.tetrapak.customerhub.core.models;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Model(adaptables = { Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PlantMasterLicensesModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlantMasterLicensesModel.class);

    /** The resource. */
    @SlingObject
    private Resource resource;

    @SlingObject
    private SlingHttpServletRequest request;

    private String i18nKeys;

    private String userName;

    private String userEmailAddress;

    /**
     * init method.
     */
    @PostConstruct
    protected void init() {

        EngineeringLicensesModel engineeringLicensesModel = request.adaptTo(EngineeringLicensesModel.class);
        Map<String, Object> i18KeyMap = new HashMap<>();
        i18KeyMap.put("engineeringLicense",engineeringLicensesModel);
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        i18nKeys = gson.toJson(i18KeyMap);
        LOGGER.debug("i18nKeys : {}",i18nKeys);
    }

    public void setUserEmailAddress() {
        this.userEmailAddress = GlobalUtil.getCustomerEmailAddress(this.request);
    }

    public String getI18nKeys() {
        return i18nKeys;
    }
}
