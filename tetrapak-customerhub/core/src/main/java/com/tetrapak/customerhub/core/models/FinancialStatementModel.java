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
    
    @Inject
    private String fileDownloadErrorText;
    
    @Inject
    private String fileDownloadErrorClose;
        

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
    /**
     * @return the selectDates
     */
    public String getSelectDates() {
        return selectDates;
    }

    /**
     * @return the closeBtn
     */
    public String getCloseBtn() {
        return closeBtn;
    }

    /**
     * @return the setDates
     */
    public String getSetDates() {
        return setDates;
    }

    /**
     * @return the findCustomer
     */
    public String getFindCustomer() {
        return findCustomer;
    }

    /**
     * @return the selectCustomerLabel
     */
    public String getSelectCustomerLabel() {
        return selectCustomerLabel;
    }

    /**
     * @return the selectStatusLabel
     */
    public String getSelectStatusLabel() {
        return selectStatusLabel;
    }

    /**
     * @return the selectDateRangeLabel
     */
    public String getSelectDateRangeLabel() {
        return selectDateRangeLabel;
    }

    /**
     * @return the searchInputLabel
     */
    public String getSearchInputLabel() {
        return searchInputLabel;
    }

    /**
     * @return the resetButtonText
     */
    public String getResetButtonText() {
        return resetButtonText;
    }

    /**
     * @return the searchButtonText
     */
    public String getSearchButtonText() {
        return searchButtonText;
    }

    /**
     * @return the createExcelBtnI18n
     */
    public String getCreateExcelBtnI18n() {
        return createExcelBtnI18n;
    }

    /**
     * @return the createPdfBtnI18n
     */
    public String getCreatePdfBtnI18n() {
        return createPdfBtnI18n;
    }

    /**
     * @return the fileDownloadErrorText
     */
    public String getFileDownloadErrorText() {
        return fileDownloadErrorText;
    }

    /**
     * @return the fileDownloadErrorClose
     */
    public String getFileDownloadErrorClose() {
        return fileDownloadErrorClose;
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

    /**
     * init method
     * @return config 
     */
    @PostConstruct
    protected void init() {
   
        Map<String, Object> i18KeyMap = new HashMap<String, Object>();
        i18KeyMap.put("selectDates", getSelectDates());
        i18KeyMap.put("closeBtn", getCloseBtn());
        i18KeyMap.put("setDates", getSetDates());
        i18KeyMap.put("statementOfAccount", getStatementOfAccount());
        i18KeyMap.put("findCustomer", getFindCustomer());
        i18KeyMap.put("accountNumber", getAccountNumber());
        i18KeyMap.put("accountService", getAccountService());
        i18KeyMap.put("selectCustomerLabel", getSelectCustomerLabel());
        i18KeyMap.put("selectStatusLabel", getSelectStatusLabel());
        i18KeyMap.put("selectDateRangeLabel", getSelectDateRangeLabel());
        i18KeyMap.put("selectDocumentTypeLabel", getSelectDocumentTypeLabel());
        i18KeyMap.put("placeholderForDocumentNumber", getPlaceholderForDocumentNumber());
        i18KeyMap.put("searchInputLabel", getSearchInputLabel());
        i18KeyMap.put("resetButtonText", getResetButtonText());
        i18KeyMap.put("searchButtonText", getSearchButtonText());
        i18KeyMap.put("summaryHeadingI18n", getSummaryHeadingI18n());
        i18KeyMap.put("documentHeadingI18n", getDocumentHeadingI18n());
        i18KeyMap.put("createExcelBtnI18n", getCreateExcelBtnI18n());
        i18KeyMap.put("createPdfBtnI18n", getCreatePdfBtnI18n());
        i18KeyMap.put("fileDownloadErrorText", getFileDownloadErrorText());
        i18KeyMap.put("fileDownloadErrorClose", getFileDownloadErrorClose());
        
        
        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);
        downloadPdfExcelServletUrl = resource.getPath() + ".download.{extnType}";
       
    }

}
