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
 * Model class for Maintenance Filtering component
 *
 * @author Nitin Kumar
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MaintenanceModel {

    @Self
    private Resource resource;

    @Inject
    private String siteFilterLabel;
    @Inject
    private String lineFilterLabel;
    @Inject
    private String equipmentFilterLabel;
    @Inject
    private String allOptionText;
    @Inject
    private String contactsHeading;
    @Inject
    private String scheduleHeading;
    @Inject
    private String eventsHeading;
    @Inject
    private String statusLabel;
    @Inject
    private String serviceAgreementLabel;
    @Inject
    private String plannedDurationLabel;
    @Inject
    private String plannedStartLabel;
    @Inject
    private String plannedFinishedLabel;
    @Inject
    private String lineLabel;
    @Inject
    private String equipmentLabel;
    @Inject
    private String serviceOrderLabel;
    @Inject
    private String operationShortTextLabel;
    @Inject
    private String noDataMsg;
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
        i18KeyMap.put("equipmentFilterLabel", getEquipmentFilterLabel());
        i18KeyMap.put("allOptionText", getAllOptionText());
        i18KeyMap.put("contactsHeading", getContactsHeading());
        i18KeyMap.put("scheduleHeading", getScheduleHeading());
        i18KeyMap.put("eventsHeading", getEventsHeading());
        i18KeyMap.put("statusLabel", getStatusLabel());
        i18KeyMap.put("serviceAgreementLabel", getServiceAgreementLabel());
        i18KeyMap.put("plannedDurationLabel", getPlannedDurationLabel());
        i18KeyMap.put("plannedStartLabel", getPlannedStartLabel());
        i18KeyMap.put("plannedFinishedLabel", getPlannedFinishedLabel());
        i18KeyMap.put("lineLabel", getLineLabel());
        i18KeyMap.put("equipmentLabel", getEquipmentLabel());
        i18KeyMap.put("serviceOrderLabel", getServiceOrderLabel());
        i18KeyMap.put("operationShortTextLabel", getOperationShortTextLabel());
        i18KeyMap.put("noDataMsg", getNoDataMsg());
        i18KeyMap.put("errorMsg", getErrorMsg());

        i18nKeys = new Gson().toJson(i18KeyMap);
    }

    /**
     * @return the i18nKeys
     */
    public String getI18nKeys() {
        return i18nKeys;
    }

    public String getSiteFilterLabel() {
        return siteFilterLabel;
    }

    public String getLineFilterLabel() {
        return lineFilterLabel;
    }

    public String getEquipmentFilterLabel() {
        return equipmentFilterLabel;
    }

    public String getAllOptionText() {
        return allOptionText;
    }

    public String getContactsHeading() {
        return contactsHeading;
    }

    public String getScheduleHeading() {
        return scheduleHeading;
    }

    public String getEventsHeading() {
        return eventsHeading;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public String getServiceAgreementLabel() {
        return serviceAgreementLabel;
    }

    public String getPlannedDurationLabel() {
        return plannedDurationLabel;
    }

    public String getPlannedStartLabel() {
        return plannedStartLabel;
    }

    public String getPlannedFinishedLabel() {
        return plannedFinishedLabel;
    }

    public String getLineLabel() {
        return lineLabel;
    }

    public String getEquipmentLabel() {
        return equipmentLabel;
    }

    public String getServiceOrderLabel() {
        return serviceOrderLabel;
    }

    public String getOperationShortTextLabel() {
        return operationShortTextLabel;
    }

    public String getNoDataMsg() {
        return noDataMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
