package com.tetrapak.customerhub.core.services;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.rebuildingkits.RKLiabilityConditionsPDF;
import org.apache.sling.api.resource.ResourceResolver;

public interface RKLiabilityConditionsService {

    public RKLiabilityConditionsPDF getPDFLinksJSON(ResourceResolver resourceResolver, String preferredLanguage);
}
