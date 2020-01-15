package com.tetrapak.customerhub.core.models;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.utils.LinkUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Model class for Maintenance Card component
 *
 * @author ruhsharm
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MaintenanceCardModel extends BaseMaintenanceModel {

    @Self
    private Resource resource;

    @Inject
    private String maintenanceHeading;
    @Inject
    private String contactsHeading;

    @Inject
    private String viewAllText;
    @Inject
    private String serviceOrderLabel;
    @Inject
    private String operationShortTextLabel;
    @Inject
    private String noDataMsg;
    @Inject
    private String errorMsg;
    @Inject
    private String viewAllLink;

    private String i18nKeys;

    /**
     * Populating the i18n keys to a JSON object string getting values from the
     * dialog
     */
    @PostConstruct
    protected void init() {
        Map<String, Object> i18KeyMap = new HashMap<String, Object>();
        i18KeyMap.put("maintenanceHeading", getMaintenanceHeading());
        i18KeyMap.put("contactsHeading", getContactsHeading());
        i18KeyMap.put("statusLabel", getStatusLabel());
        i18KeyMap.put("serviceAgreementLabel", getServiceAgreementLabel());
        i18KeyMap.put("plannedDurationLabel", getPlannedDurationLabel());
        i18KeyMap.put("plannedStartLabel", getPlannedStartLabel());
        i18KeyMap.put("plannedFinishedLabel", getPlannedFinishedLabel());
        i18KeyMap.put("viewAllText", getViewAllText());
        i18KeyMap.put("serviceOrderLabel", getServiceOrderLabel());
        i18KeyMap.put("operationShortTextLabel", getOperationShortTextLabel());
        i18KeyMap.put("noDataMsg", getNoDataMsg());
        i18KeyMap.put("errorMsg", getErrorMsg());
        i18KeyMap.put("viewAllLink", getViewAllLink());

        i18nKeys = new Gson().toJson(i18KeyMap);
    }

    /**
     * @return the i18nKeys
     */
    public String getI18nKeys() {
        return i18nKeys;
    }

    /**
     * @return the maintenanceHeading
     */
    public String getMaintenanceHeading() {
        return maintenanceHeading;
    }

    /**
     * @return the viewAllText
     */
    public String getViewAllText() {
        return viewAllText;
    }

    /**
     * @return the viewAllLink
     */
    public String getViewAllLink() {
        return LinkUtil.getValidLink(resource, viewAllLink);
    }

    /**
     * @return the contactsHeading
     */
    public String getContactsHeading() {
        return contactsHeading;
    }

    /**
     * @return the serviceOrderLabel
     */
    public String getServiceOrderLabel() {
        return serviceOrderLabel;
    }

    /**
     * @return the operationShortTextLabel
     */
    public String getOperationShortTextLabel() {
        return operationShortTextLabel;
    }

    /**
     * @return the noDataMsg
     */
    public String getNoDataMsg() {
        return noDataMsg;
    }

    /**
     * @return the errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

}
