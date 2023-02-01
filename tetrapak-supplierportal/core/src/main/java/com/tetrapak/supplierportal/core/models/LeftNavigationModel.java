package com.tetrapak.supplierportal.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import com.tetrapak.supplierportal.core.services.UserPreferenceService;
import com.tetrapak.supplierportal.core.utils.GlobalUtil;
import com.tetrapak.supplierportal.core.utils.NavigationUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = { SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LeftNavigationModel {

    private static final String NAV_HEADING_I18N_PROPERTY = "navHeadingI18n";

    private static final String CLOSE_BTN_TEXT_PROPERTY = "closeBtnText";

    @SlingObject private SlingHttpServletRequest request;

    @OSGiService private UserPreferenceService userPreferenceService;

    private String navHeading;

    private String closeBtnText;

    private List<LeftNavigationBean> leftNavItems = new ArrayList<>();

    private String selectedLanguage;

    @PostConstruct protected void init() {
        selectedLanguage = GlobalUtil.getSelectedLanguage(request, userPreferenceService);
        Resource navigationConfigResource = GlobalUtil.getNavigationConfigurationResource(request);
        PageManager pageManager = request.getResourceResolver().adaptTo(PageManager.class);
        if (null != navigationConfigResource && null != pageManager) {
            ValueMap map = navigationConfigResource.getValueMap();
            navHeading = (String) map.get(NAV_HEADING_I18N_PROPERTY);
            closeBtnText = (String) map.get(CLOSE_BTN_TEXT_PROPERTY);

            Page navigationPage = pageManager.getContainingPage(navigationConfigResource);
            if (null != navigationPage) {
                Iterator<Page> itr = navigationPage.listChildren();
                while (itr.hasNext()) {
                    Page childPage = itr.next();
                    NavigationUtil.populateLeftNavItems(request.getResourceResolver(), request.getResource(),
                            leftNavItems, childPage);
                }
            }
        }
    }

    public List<LeftNavigationBean> getLeftNavItems() {
        return new ArrayList<>(this.leftNavItems);
    }

    public String getNavHeading() {
        return navHeading;
    }

    public String getCloseBtnText() {
        return closeBtnText;
    }

    public String getLocale() {
        if (StringUtils.isEmpty(selectedLanguage)) {
            return SupplierPortalConstants.DEFAULT_LOCALE;
        }
        return selectedLanguage;
    }
}
