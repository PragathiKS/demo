package com.tetrapak.supplierportal.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.msm.api.BlueprintManager;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import com.tetrapak.supplierportal.core.services.UserPreferenceService;
import com.tetrapak.supplierportal.core.utils.GlobalUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Model class for language selector component
 *
 * @author Nitin Kumar
 */
@Model(adaptables = { SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LanguageSelectorModel {

    @SlingObject private SlingHttpServletRequest request;

    @OSGiService private UserPreferenceService userPreferenceService;

    private String headingI18n;

    private String closeBtnTextI18n;

    private String selectedLanguage;

    private Map<String, String> listOfLanguages = new HashMap<>();

    @PostConstruct protected void init() throws WCMException {
        BlueprintManager manager = request.getResourceResolver().adaptTo(BlueprintManager.class);
        PageManager pageManager = request.getResourceResolver().adaptTo(PageManager.class);

        List<Page> pages = manager.getBlueprints().stream()
                .filter(x -> x.getSitePath().startsWith("/content/tetrapak/supplierportal/global"))
                .map(x -> pageManager.getPage(x.getSitePath())).collect(Collectors.toList());

        pages.forEach(p -> listOfLanguages.put(p.getName(), p.getTitle()));
        listOfLanguages = listOfLanguages.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        selectedLanguage = GlobalUtil.getSelectedLanguage(request, userPreferenceService);

        Resource navigationConfigurationResource = GlobalUtil.getNavigationConfigurationResource(request);
        if (null != navigationConfigurationResource) {
            ValueMap map = navigationConfigurationResource.getValueMap();
            headingI18n = (String) map.get("headingI18n");
            closeBtnTextI18n = (String) map.get("closeBtnText");
        }
    }

    public String getHeadingI18n() {
        return headingI18n;
    }

    public String getCloseBtnTextI18n() {
        return closeBtnTextI18n;
    }

    public String getLocale() {
        return StringUtils.isEmpty(selectedLanguage) ? SupplierPortalConstants.DEFAULT_LOCALE : selectedLanguage;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public Map<String, String> getListOfLanguages() {
        return listOfLanguages;
    }
}
