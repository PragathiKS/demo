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
 * Model class for Documents Filtering component
 *
 * @author ruhsharma
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DocumentsModel {

    @Self
    private Resource resource;

    @Inject
    private String siteFilterLabel;
    @Inject
    private String lineFilterLabel;
    @Inject
    private String documentHeading;
    @Inject
    private String documentTypeLabel;
    @Inject
    private String noDocumentsMsg;
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
        i18KeyMap.put("siteFilterLabel", getSiteFilterLabel());
        i18KeyMap.put("lineFilterLabel", getLineFilterLabel());
        i18KeyMap.put("documentHeading", getDocumentHeading());
        i18KeyMap.put("documentTypeLabel", getDocumentTypeLabel());
        i18KeyMap.put("noDocumentsMsg", getNoDocumentsMsg());
        i18KeyMap.put("scheduleHeading", getErrorMsg());

        i18nKeys = new Gson().toJson(i18KeyMap);
    }

    /**
     * @return the siteFilterLabel
     */
    public String getSiteFilterLabel() {
        return siteFilterLabel;
    }

    /**
     * @return the i18nKeys
     */
    public String getI18nKeys() {
        return i18nKeys;
    }

    /**
     * @return the lineFilterLabel
     */
    public String getLineFilterLabel() {
        return lineFilterLabel;
    }

    /**
     * @return the documentTypeLabel
     */
    public String getDocumentTypeLabel() {
        return documentTypeLabel;
    }

    /**
     * @return the noDocumentsMsg
     */
    public String getNoDocumentsMsg() {
        return noDocumentsMsg;
    }

    /**
     * @return the errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * @return the documentHeading
     */
    public String getDocumentHeading() {
        return documentHeading;
    }

}
