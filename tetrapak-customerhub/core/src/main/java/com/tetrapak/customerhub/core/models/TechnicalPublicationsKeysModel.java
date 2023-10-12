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
    private String allFiles;

    @Expose
    @ValueMapValue
    private String country;

    @Expose
    @ValueMapValue
    private String customer;

    @Expose
    @ValueMapValue
    private String documentNumber;

    @Expose
    @ValueMapValue
    private String documentType;

    @Expose
    @ValueMapValue
    private String issueDate;

    @Expose
    @ValueMapValue
    private String line;

    @Expose
    @ValueMapValue
    private String lineEquipment;
    
    @Expose
    @ValueMapValue
    private String materialNumber;

    @Expose
    @ValueMapValue
    private String name;

    @Expose
    @ValueMapValue
    private String rebuildingKitName;

    @Expose
    @ValueMapValue
    private String rebuildingKitNumber;

    @Expose
    @ValueMapValue
    private String searchResults;

    @Expose
    @ValueMapValue
    private String serialNumber;

    @Expose
    @ValueMapValue
    private String technicalPublications;

    @Expose
    @ValueMapValue
    private String description;

    public String getAllFiles() {
	return allFiles;
    }

    public String getCountry() {
	return country;
    }

    public String getCustomer() {
	return customer;
    }

    public String getDocumentNumber() {
	return documentNumber;
    }

    public String getDocumentType() {
	return documentType;
    }

    public String getIssueDate() {
	return issueDate;
    }

    public String getLine() {
	return line;
    }

    public String getLineEquipment() {
	return lineEquipment;
    }
    
    public String getMaterialNumber() {
        return materialNumber;
    }

    public String getName() {
	return name;
    }

    public String getRebuildingKitName() {
	return rebuildingKitName;
    }

    public String getRebuildingKitNumber() {
	return rebuildingKitNumber;
    }

    public String getSearchResults() {
	return searchResults;
    }

    public String getSerialNumber() {
	return serialNumber;
    }

    public String getTechnicalPublications() {
	return technicalPublications;
    }
    
    /**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

}
