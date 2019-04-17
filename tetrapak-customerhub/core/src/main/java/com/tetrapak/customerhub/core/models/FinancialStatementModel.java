package com.tetrapak.customerhub.core.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

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
        
        try {
        	//NOSONAR
        	JSONObject  json=new JSONObject ();
        	json.put("i18nKeys", i18KeyMap);
        	i18nKeys=json.getString("i18nKeys");
        } catch (Exception e) {
        	e.printStackTrace();;
        }
    }

    /**
     * Get i18nkeys method.
     * @return i18nkeys 
     */   
    public String getI18nKeys() {
        return i18nKeys;
    }

}
