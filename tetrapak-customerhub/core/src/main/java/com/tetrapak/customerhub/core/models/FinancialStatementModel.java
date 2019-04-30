package com.tetrapak.customerhub.core.models;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.google.gson.Gson;

/**
 * Model class for financial statement component
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FinancialStatementModel {

    @Self
    private Resource resource;

    @Inject
    private String selectDates;

    @Inject
    private String closeBtn;

    @Inject
    private String setDates;

    @Inject
    private String statementOfAccount;

    /**
     * @return the statementOfAccount
     */
    public String getStatementOfAccount() {
        return statementOfAccount;
    }

    /**
     * @return the accountNumber
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * @return the accountService
     */
    public String getAccountService() {
        return accountService;
    }

    /**
     * @return the selectDocumentTypeLabel
     */
    public String getSelectDocumentTypeLabel() {
        return selectDocumentTypeLabel;
    }

    /**
     * @return the placeholderForDocumentNumber
     */
    public String getPlaceholderForDocumentNumber() {
        return placeholderForDocumentNumber;
    }

    /**
     * @return the summaryHeadingI18n
     */
    public String getSummaryHeadingI18n() {
        return summaryHeadingI18n;
    }

    /**
     * @return the documentHeadingI18n
     */
    public String getDocumentHeadingI18n() {
        return documentHeadingI18n;
    }

    @Inject
    private String findCustomer;

    @Inject
    private String accountNumber;

    @Inject
    private String accountService;
    
    @Inject
    private String selectCustomerLabel;

    @Inject
    private String selectStatusLabel;

    @Inject
    private String selectDateRangeLabel;
    
    @Inject
    private String selectDocumentTypeLabel;

    @Inject
    private String placeholderForDocumentNumber;
    
    @Inject
    private String searchInputLabel;
    
    @Inject
    private String resetButtonText;

    @Inject
    private String searchButtonText;
    
    @Inject
    private String summaryHeadingI18n;
    
    @Inject
    private String documentHeadingI18n;
    
    @Inject
    private String createExcelBtnI18n;
    
    @Inject
    private String createPdfBtnI18n;
    
    private String i18nKeys;
    
    private String downloadPdfExcelServletUrl;
    
    /**
     * init method
     * @return config 
     */
    @PostConstruct
    protected void init() {
   
        Map<String, Object> i18KeyMap = new HashMap<String, Object>();
        i18KeyMap.put("selectDates", selectDates);
        i18KeyMap.put("closeBtn", closeBtn);
        i18KeyMap.put("setDates", setDates);
        i18KeyMap.put("statementOfAccount", statementOfAccount);
        i18KeyMap.put("findCustomer", findCustomer);
        i18KeyMap.put("accountNumber", accountNumber);
        i18KeyMap.put("accountService", accountService);
        i18KeyMap.put("selectCustomerLabel", selectCustomerLabel);
        i18KeyMap.put("selectStatusLabel", selectStatusLabel);
        i18KeyMap.put("selectDateRangeLabel", selectDateRangeLabel);
        i18KeyMap.put("selectDocumentTypeLabel", selectDocumentTypeLabel);
        i18KeyMap.put("placeholderForDocumentNumber", placeholderForDocumentNumber);
        i18KeyMap.put("searchInputLabel", searchInputLabel);
        i18KeyMap.put("resetButtonText", resetButtonText);
        i18KeyMap.put("searchButtonText", searchButtonText);
        i18KeyMap.put("summaryHeadingI18n", summaryHeadingI18n);
        i18KeyMap.put("documentHeadingI18n", documentHeadingI18n);
        i18KeyMap.put("createExcelBtnI18n", createExcelBtnI18n);
        i18KeyMap.put("createPdfBtnI18n", createPdfBtnI18n);
        
        
        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);
        downloadPdfExcelServletUrl = resource.getPath() + ".{extnType}";
       
    }

    /**
     * Get i18nkeys method.
     * @return i18nkeys 
     */   
    public String getI18nKeys() {
        return i18nKeys;
    }

    /**
     * @return the downloadPdfExcelServletUrl
     */
    public String getDownloadPdfExcelServletUrl() {
        return downloadPdfExcelServletUrl;
    }

}
