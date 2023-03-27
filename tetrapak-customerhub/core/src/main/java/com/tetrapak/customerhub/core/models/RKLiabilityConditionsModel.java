package com.tetrapak.customerhub.core.models;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.tetrapak.customerhub.core.constants.CustomerHubConstants.DOT;
import static com.tetrapak.customerhub.core.servlets.RKLiabilityConditionsServlet.JSON_EXTENSION;
import static com.tetrapak.customerhub.core.servlets.RKLiabilityConditionsServlet.PDF_LINKS_SELECTOR;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RKLiabilityConditionsModel {

    @ValueMapValue
    private String rkMandatoryKitsText;

    @Self
    private Resource resource;

    private String i18nKeys;

    @PostConstruct
    protected void init() throws UnsupportedEncodingException {
        Map<String, Object> i18KeyMap = new HashMap<>();
        i18KeyMap.put(CustomerHubConstants.RK_MANDATORY_KITS_TEXT, getRkMandatoryKitsText());
        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);
    }

    public String getApiPath() {
        ResourceResolver resolver = resource.getResourceResolver();
        return resolver.map(resource.getPath()+DOT+PDF_LINKS_SELECTOR+DOT+JSON_EXTENSION);
    }

    public String getRkMandatoryKitsText() {
        return rkMandatoryKitsText;
    }

    public String getI18nKeys() {

        return i18nKeys;
    }
}
