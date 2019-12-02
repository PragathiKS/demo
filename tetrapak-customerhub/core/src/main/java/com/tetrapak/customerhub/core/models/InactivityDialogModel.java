package com.tetrapak.customerhub.core.models;

import com.day.cq.wcm.api.Page;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.util.Iterator;

/**
 * Model class for Inactivity Dialog Component
 *
 * @author Nitin Kumar
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class InactivityDialogModel {

    @SlingObject
    private SlingHttpServletRequest request;

    @OSGiService
    private UserPreferenceService userPreferenceService;

    private String messageI18n;
    private String continueBtnI18n;
    private String logoutBtnI18n;
    private String closeBtnI18n;
    private String idleTimeoutMinutes;
    private String selectedLanguage;
    private String logoutURL;
    private String autoRefreshSession;

    @PostConstruct
    protected void init() {
        selectedLanguage = GlobalUtil.getSelectedLanguage(request, userPreferenceService);

        Resource globalConfigResource = GlobalUtil.getGlobalConfigurationResource(request);
        if (null != globalConfigResource) {
            ValueMap map = globalConfigResource.getValueMap();
            messageI18n = (String) map.get("messageI18n");
            continueBtnI18n = (String) map.get("continueBtnI18n");
            logoutBtnI18n = (String) map.get("logoutBtnI18n");
            closeBtnI18n = (String) map.get("closeBtnI18n");
            idleTimeoutMinutes = (String) map.get("idleTimeoutMinutes");
            autoRefreshSession = (String) map.get("autoRefreshSession");
            fetchLogoutURL(globalConfigResource);
        }
    }

    private void fetchLogoutURL(Resource globalConfigResource) {
        Page enPage = GlobalUtil.getCustomerhubConfigPage(globalConfigResource);
        if (null == enPage) {
            return;
        }
        if (!enPage.hasChild("logout")) {
            return;
        }
        Iterator<Page> childPages = enPage.listChildren();
        while (childPages.hasNext()) {
            Page logoutPage = childPages.next();
            if (logoutPage.getPath().contains("logout")) {
                ValueMap vMap = logoutPage.getContentResource().getValueMap();
                logoutURL = (String) vMap.get("cq:redirectTarget");
            }
        }
    }

    public String getLocale() {
        return StringUtils.isEmpty(selectedLanguage) ? CustomerHubConstants.DEFAULT_LOCALE : selectedLanguage;
    }

    public String getMessageI18n() {
        return messageI18n;
    }

    public String getContinueBtnI18n() {
        return continueBtnI18n;
    }

    public String getLogoutBtnI18n() {
        return logoutBtnI18n;
    }

    public String getCloseBtnI18n() {
        return closeBtnI18n;
    }

    public String getIdleTimeoutMinutes() {
        return idleTimeoutMinutes;
    }

    public String getAutoRefreshSession() {
        return autoRefreshSession;
    }

    public String getLogoutURL() {
        return logoutURL;
    }
}
