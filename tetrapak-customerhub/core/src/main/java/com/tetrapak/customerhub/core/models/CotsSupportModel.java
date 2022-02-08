package com.tetrapak.customerhub.core.models;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.servlets.CotsSupportEmailServlet;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CotsSupportModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(CotsSupportModel.class);

    public enum COTSSupportComponentDialog {
        TITLE("title"),
        SUBTITLE("subTitle"),
        SELECT_REQUEST("selectRequest"),
        TECHNICAL_ISSUES("technicalIssues"),
        COMPANY("company"),
        CUSTOMER_SITE("customerSite"),
        AFFECTED_SYSTEMS_LABEL("affectedSystemsLabel"),
        AFFECTED_SYSTEMS("affectedSystems"),
        PRODUCT_INVOLVED("productInvolvedLabel"),
        SOFTWARE_VERSION("softwareVersion"),
        ENGINEERING_LICENSE_SERIAL_NUMBER("engineeringLicenseSerialNumber"),
        SHORT_DESCRIPTION("shortDescription"),
        SELECT_FILE("selectFile"),
        QUESTION("question"),
        NAME("name"),
        EMAIL_ADDRESS("emailAddress"),
        TELEPHONE("telephone"),
        DROPDOWN_PLACEHOLDER("dropdownPlaceholder"),
        INPUT_ERROR_MESSAGE("inputErrorMsg"),
        SUCCESS_MESSAGE("successMessage"),
        SALUTATION("salutation"),
        BODY("body"),
        CONTACT_DETAILS("contactDetails"),
        RECIPIENT_EMAIL_ADDRESS("recipientEmailAddress");

        public final String i18nJsonKey;

        private COTSSupportComponentDialog(String jsonKey) {
            this.i18nJsonKey = jsonKey;
        }

        private String getI18nJsonKey(){
            return i18nJsonKey;
        }

    }

    /** The resource. */
    @Self
    private Resource resource;
    
    /** The title */
    @Inject
    private String title;
    
    /** The subtitle */
    @Inject
    private String subTitle;
    
    /** The select type of request label. */
    @Inject
    private String selectRequest;
    
    /** The technical issues label. */
    @Inject
    private String technicalIssues;
    
    /** The company field label. */
    @Inject
    private String company;
    
    /** The customer site field label. */
    @Inject
    private String customerSite;
    
    @Inject
    private String affectedSystemsLabel;
    
    /** The affected systems field label. */
    @Inject
    private List<AffectedSystemsModel> affectedSystems;
    
    @Inject
    private String productInvolvedLabel;
    
    /** The Software Version field label. */
    @Inject
    private String softwareVersion;
    
    /** The Engineering License Serial Number field label. */
    @Inject
    private String engineeringLicenseSerialNumber;
    
    /** The Short description field label. */
    @Inject
    private String shortDescription;
    
    /** The Select File button field label */
    @Inject
    private String selectFile;
    
    /** The question button label. */
    @Inject
    private String question;
    
    /** The name label */
    @Inject
    private String name;
    
    /** The email address label */
    @Inject
    private String emailAddress;
    
    /** The telephone label. */
    @Inject
    private String telephone;
    
    /** The dropdown placeholder label. */
    @Inject
    private String dropdownPlaceholder;
    
    /** The input error message label. */
    @Inject
    private String inputErrorMsg;
    
    /** The success message */
    @Inject
    private String successMessage;
    
    /** The salutation text in email */
    @Inject
    private String salutation;
    
    /** The body text in email */
    @Inject
    private String body;
    
    /** The contact details text in email */
    @Inject
    private String contactDetails;
    
    @Inject
    private String recipientEmailAddress;
    
    private String i18nKeys;
    
    private String componentPath;
    
    private String componentPathExtension;
    
    /**
     * init method.
     */
    @PostConstruct
    protected void init() {
        Map<String, Object> i18KeyMap = new HashMap<>();
        i18KeyMap.put(COTSSupportComponentDialog.TITLE.getI18nJsonKey(), getTitle());
        i18KeyMap.put(COTSSupportComponentDialog.SUBTITLE.getI18nJsonKey(), getSubTitle());
        i18KeyMap.put(COTSSupportComponentDialog.SELECT_REQUEST.getI18nJsonKey(), getSelectRequest());
        i18KeyMap.put(COTSSupportComponentDialog.TECHNICAL_ISSUES.getI18nJsonKey(), getTechnicalIssues());
        i18KeyMap.put(COTSSupportComponentDialog.COMPANY.getI18nJsonKey(), getCompany());
        i18KeyMap.put(COTSSupportComponentDialog.CUSTOMER_SITE.getI18nJsonKey(), getCustomerSite());
        i18KeyMap.put(COTSSupportComponentDialog.AFFECTED_SYSTEMS.getI18nJsonKey(), getAffectedSystems());
        i18KeyMap.put(COTSSupportComponentDialog.SOFTWARE_VERSION.getI18nJsonKey(), getSoftwareVersion());
        i18KeyMap.put(COTSSupportComponentDialog.ENGINEERING_LICENSE_SERIAL_NUMBER.getI18nJsonKey(),
                getEngineeringLicenseSerialNumber());
        i18KeyMap.put(COTSSupportComponentDialog.SHORT_DESCRIPTION.getI18nJsonKey(), getShortDescription());
        i18KeyMap.put(COTSSupportComponentDialog.SELECT_FILE.getI18nJsonKey(), getSelectFile());
        i18KeyMap.put(COTSSupportComponentDialog.QUESTION.getI18nJsonKey(), getQuestion());
        i18KeyMap.put(COTSSupportComponentDialog.NAME.getI18nJsonKey(), getName());
        i18KeyMap.put(COTSSupportComponentDialog.EMAIL_ADDRESS.getI18nJsonKey(), getEmailAddress());
        i18KeyMap.put(COTSSupportComponentDialog.TELEPHONE.getI18nJsonKey(), getTelephone());
        i18KeyMap.put(COTSSupportComponentDialog.DROPDOWN_PLACEHOLDER.getI18nJsonKey(), getDropdownPlaceholder());
        i18KeyMap.put(COTSSupportComponentDialog.INPUT_ERROR_MESSAGE.getI18nJsonKey(), getInputErrorMsg());
        i18KeyMap.put(COTSSupportComponentDialog.SUCCESS_MESSAGE.getI18nJsonKey(), getSuccessMessage());
        
        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);
        LOGGER.debug("================" + i18nKeys.toString());
        
        this.componentPath = this.resource.getPath();
        this.componentPathExtension = "." + CotsSupportEmailServlet.SLING_SERVLET_SELECTOR + "."
                + CotsSupportEmailServlet.SLING_SERVLET_EXTENSION;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getSubTitle() {
        return subTitle;
    }
    
    public String getSelectRequest() {
        return selectRequest;
    }
    
    public String getTechnicalIssues() {
        return technicalIssues;
    }
    
    public String getCompany() {
        return company;
    }
    
    public String getCustomerSite() {
        return customerSite;
    }
    
    public String getAffectedSystemsLabel() {
        return affectedSystemsLabel;
    }
    
    public List<AffectedSystemsModel> getAffectedSystems() {
        return affectedSystems;
    }
    
    public String getProductInvolvedLabel() {
        return productInvolvedLabel;
    }
    
    public String getSoftwareVersion() {
        return softwareVersion;
    }
    
    public String getEngineeringLicenseSerialNumber() {
        return engineeringLicenseSerialNumber;
    }
    
    public String getShortDescription() {
        return shortDescription;
    }
    
    public String getSelectFile() {
        return selectFile;
    }
    
    public String getQuestion() {
        return question;
    }
    
    public String getName() {
        return name;
    }
    
    public String getEmailAddress() {
        return emailAddress;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public String getDropdownPlaceholder() {
        return dropdownPlaceholder;
    }
    
    public String getInputErrorMsg() {
        return inputErrorMsg;
    }
    
    public String getSuccessMessage() {
        return successMessage;
    }
    
    public String getI18nKeys() {
        return i18nKeys;
    }
    
    public String getComponentPath() {
        return componentPath;
    }
    
    public String getComponentPathExtension() {
        return componentPathExtension;
    }
    
    public String getSalutation() {
        return salutation;
    }
    
    public String getBody() {
        return body;
    }
    
    public String getContactDetails() {
        return contactDetails;
    }
    
    public String getRecipientEmailAddress() {
        return recipientEmailAddress;
    }
}
