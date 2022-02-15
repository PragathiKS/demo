package com.tetrapak.customerhub.core.models;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.servlets.CotsSupportEmailServlet;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CotsSupportModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(CotsSupportModel.class);

    public enum COTSSupportComponentDialog {
        TITLE("title"),
        SUBTITLE("subTitle"),
        SELECT_REQUEST("selectRequest"),
        TECHNICAL_ISSUES("technicalIssues"),
        PRODUCT_SUPPORT("productSupport"),
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
        SUBMIT_BUTTON("submitButtonLabel"),
        COMMENTS_ERROR_MESSAGE("commentsErrorMsg"),
        EMAIL_ERROR_MESSAGE("emailErrorMsg"),
        PHONE_ERROR_MESSAGE("phoneErrorMsg"),
        SELECT_ERROR_MESSAGE("selectErrorMsg"),
        INVALID_FILE_ERROR_MESSAGE("invalidFileErrorMsg"),
        REMOVE_FILE_LABEL("removeFileLabel"),
        DRAG_DROP_TITLE("dragAndDropTitle"),
        DRAG_DROP_SUBTITLE("dragAndDropSubtitle");

        public final String i18nJsonKey;

        COTSSupportComponentDialog(String jsonKey) {
            this.i18nJsonKey = jsonKey;
        }

        private String getI18nJsonKey(){
            return i18nJsonKey;
        }

    }

    /** The resource. */
    @SlingObject
    private Resource resource;

    @SlingObject
    private SlingHttpServletRequest request;

    @ScriptVariable
    private ResourceResolver resolver;
    
    /** The title */
    @ValueMapValue
    private String title;
    
    /** The subtitle */
    @ValueMapValue
    private String subTitle;
    
    /** The select type of request label. */
    @ValueMapValue
    private String selectRequest;
    
    /** The technical issues label. */
    @ValueMapValue
    private String technicalIssues;

    /** The technical issues label. */
    @ValueMapValue
    private String productSupport;
    
    /** The company field label. */
    @ValueMapValue
    private String company;
    
    /** The customer site field label. */
    @ValueMapValue
    private String customerSite;
    
    @ValueMapValue
    private String affectedSystemsLabel;
    
    @Inject @Via("resource")
    private List<AffectedSystemsModel> affectedSystems;
    
    @ValueMapValue
    private String productInvolvedLabel;
    
    /** The Software Version field label. */
    @ValueMapValue
    private String softwareVersion;
    
    /** The Engineering License Serial Number field label. */
    @ValueMapValue
    private String engineeringLicenseSerialNumber;
    
    /** The Short description field label. */
    @ValueMapValue
    private String shortDescription;
    
    /** The Select File button field label */
    @ValueMapValue
    private String selectFile;
    
    /** The question button label. */
    @ValueMapValue
    private String question;
    
    /** The name label */
    @ValueMapValue
    private String name;
    
    /** The email address label */
    @ValueMapValue
    private String emailAddress;
    
    /** The telephone label. */
    @ValueMapValue
    private String telephone;
    
    /** The dropdown placeholder label. */
    @ValueMapValue
    private String dropdownPlaceholder;
    
    /** The input error message label. */
    @ValueMapValue
    private String inputErrorMsg;
    
    /** The success message */
    @ValueMapValue
    private String successMessage;
    
    /** The salutation text in email */
    @ValueMapValue
    private String salutation;
    
    /** The body text in email */
    @ValueMapValue
    private String body;
    
    /** The contact details text in email */
    @ValueMapValue
    private String contactDetails;
    
    /** The submit button label */
    @ValueMapValue
    private String submitButtonLabel;

    /** The comments field error message */
    @ValueMapValue
    private String commentsErrorMsg;

    /** The email Error message */
    @ValueMapValue
    private String emailErrorMsg;

    /** The phone field Error message */
    @ValueMapValue
    private String phoneErrorMsg;

    /** The select field Error message */
    @ValueMapValue
    private String selectErrorMsg;

    /** The invalid file Error message */
    @ValueMapValue
    private String invalidFileErrorMsg;

    /** The remove File Label Error message */
    @ValueMapValue
    private String removeFileLabel;

    /** The drag and drop title */
    @ValueMapValue
    private String dragAndDropTitle;

    /** The drag and drop subtitle */
    @ValueMapValue
    private String dragAndDropSubtitle;

    private String i18nKeys;
    
    private String componentPath;
    
    private String componentPathExtension;

    private String userName;

    private String userEmailAddress;
    
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
        i18KeyMap.put(COTSSupportComponentDialog.PRODUCT_SUPPORT.getI18nJsonKey(), getProductSupport());
        i18KeyMap.put(COTSSupportComponentDialog.COMPANY.getI18nJsonKey(), getCompany());
        i18KeyMap.put(COTSSupportComponentDialog.CUSTOMER_SITE.getI18nJsonKey(), getCustomerSite());
        i18KeyMap.put(COTSSupportComponentDialog.AFFECTED_SYSTEMS_LABEL.getI18nJsonKey(), getAffectedSystemsLabel());
        i18KeyMap.put(COTSSupportComponentDialog.AFFECTED_SYSTEMS.getI18nJsonKey(), getAffectedSystems());
        i18KeyMap.put(COTSSupportComponentDialog.PRODUCT_INVOLVED.getI18nJsonKey(), getProductInvolvedLabel());
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
        i18KeyMap.put(COTSSupportComponentDialog.SUBMIT_BUTTON.getI18nJsonKey(), getSubmitButtonLabel());
        i18KeyMap.put(COTSSupportComponentDialog.COMMENTS_ERROR_MESSAGE.getI18nJsonKey(), getCommentsErrorMsg());
        i18KeyMap.put(COTSSupportComponentDialog.EMAIL_ERROR_MESSAGE.getI18nJsonKey(), getEmailErrorMsg());
        i18KeyMap.put(COTSSupportComponentDialog.PHONE_ERROR_MESSAGE.getI18nJsonKey(), getPhoneErrorMsg());
        i18KeyMap.put(COTSSupportComponentDialog.SELECT_ERROR_MESSAGE.getI18nJsonKey(), getSelectErrorMsg());
        i18KeyMap.put(COTSSupportComponentDialog.INVALID_FILE_ERROR_MESSAGE.getI18nJsonKey(), getInvalidFileErrorMsg());
        i18KeyMap.put(COTSSupportComponentDialog.REMOVE_FILE_LABEL.getI18nJsonKey(), getRemoveFileLabel());
        i18KeyMap.put(COTSSupportComponentDialog.DRAG_DROP_TITLE.getI18nJsonKey(), getDragAndDropTitle());
        i18KeyMap.put(COTSSupportComponentDialog.DRAG_DROP_SUBTITLE.getI18nJsonKey(), getDragAndDropSubtitle());

        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);
        LOGGER.debug("i18nKeys : {}",i18nKeys);
        
        this.componentPath = resolver.map(this.resource.getPath());
        LOGGER.debug("Resource mapped url : {}",this.componentPath);
        this.componentPathExtension = CustomerHubConstants.DOT + CotsSupportEmailServlet.SLING_SERVLET_SELECTOR
                + CustomerHubConstants.DOT + CotsSupportEmailServlet.SLING_SERVLET_EXTENSION;
        this.setUserEmailAddress();
        if (request.getCookie(CustomerHubConstants.CUSTOMER_COOKIE_NAME) != null) {
            this.userName = request.getCookie(CustomerHubConstants.CUSTOMER_COOKIE_NAME).getValue();
        }
    }

    public void setUserEmailAddress() {
        this.userEmailAddress = GlobalUtil.getCustomerEmailAddress(this.request);
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

    public String getProductSupport() {
        return productSupport;
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

    public String getSubmitButtonLabel() {
        return submitButtonLabel;
    }

    public String getCommentsErrorMsg() {
        return commentsErrorMsg;
    }

    public String getEmailErrorMsg() {
        return emailErrorMsg;
    }

    public String getPhoneErrorMsg() {
        return phoneErrorMsg;
    }

    public String getSelectErrorMsg() {
        return selectErrorMsg;
    }

    public String getInvalidFileErrorMsg() {
        return invalidFileErrorMsg;
    }

    public String getRemoveFileLabel() {
        return removeFileLabel;
    }

    public String getDragAndDropTitle() {
        return dragAndDropTitle;
    }

    public String getDragAndDropSubtitle() {
        return dragAndDropSubtitle;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmailAddress() {
        return userEmailAddress;
    }
}
