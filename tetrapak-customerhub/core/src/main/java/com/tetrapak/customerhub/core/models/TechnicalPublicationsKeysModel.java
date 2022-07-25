package com.tetrapak.customerhub.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.google.gson.annotations.Expose;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TechnicalPublicationsKeysModel {

    @Expose
    @ValueMapValue
    private String documentNumber;

    @Expose
    @ValueMapValue
    private String documentType;

    @Expose
    @ValueMapValue
    private String serialNumber;

    @Expose
    @ValueMapValue
    private String rebuildingKitNumber;

    @Expose
    @ValueMapValue
    private String rebuildingKitName;

    public String getDocumentNumber() {
	return documentNumber;
    }

    public String getDocumentType() {
	return documentType;
    }

    public String getSerialNumber() {
	return serialNumber;
    }

    public String getRebuildingKitNumber() {
	return rebuildingKitNumber;
    }

    public String getRebuildingKitName() {
	return rebuildingKitName;
    }

}
