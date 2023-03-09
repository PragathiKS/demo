package com.tetrapak.customerhub.core.models;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RKLiabilityConditionsModel {

    @ValueMapValue
    private String rkMandatoryKitsText;

    private String i18nKeys;

    @PostConstruct
    protected void init() throws UnsupportedEncodingException {
        Map<String, Object> i18KeyMap = new HashMap<>();
        i18KeyMap.put(CustomerHubConstants.RK_MANDATORY_KITS_TEXT, getRkMandatoryKitsText());
        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);
    }

    public String getRkMandatoryKitsText() {
        return rkMandatoryKitsText;
    }

    public String getI18nKeys() {

        return i18nKeys;
    }
}
