package com.tetrapak.customerhub.core.models;

import com.google.gson.Gson;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Model class for Contacts component
 *
 * @author ruhsharma
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactsModel {

    @Self
    private Resource resource;

    @Inject
    private String siteFilterHeading;
    @Inject
    private String noContactMsg;
    @Inject
    private String errorMsg;

    private String i18nKeys;

    /**
     * Populating the i18n keys to a JSON object string getting values from the
     * dialog
     */
    @PostConstruct
    protected void init() {
        Map<String, Object> i18KeyMap = new HashMap<String, Object>();
        i18KeyMap.put("siteFilterHeading", getSiteFilterHeading());
        i18KeyMap.put("noContactMsg", getNoContactMsg());
        i18KeyMap.put("errorMsg", getErrorMsg());

        i18nKeys = new Gson().toJson(i18KeyMap);
    }

    /**
     * @return the siteFilterHeading
     */
    public String getSiteFilterHeading() {
        return siteFilterHeading;
    }

    /**
     * @return the noContactMsg
     */
    public String getNoContactMsg() {
        return noContactMsg;
    }

    /**
     * @return the errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * @return the i18nKeys
     */
    public String getI18nKeys() {
        return i18nKeys;
    }


}
