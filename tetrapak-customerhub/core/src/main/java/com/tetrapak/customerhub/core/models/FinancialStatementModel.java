package com.tetrapak.customerhub.core.models;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.sling.api.resource.Resource;
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
    private String datePlaceholder;

    @Inject
    private String dateRangePlaceholder;

    @Inject
    private String dateRangeErrorLabel;

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
    private String excelPdfDownloadErrorText;

    @Inject
    private String fileDownloadErrorClose;

    @Inject
    private String noCustomerErrorText;

    public String getStatementOfAccount() {
        return statementOfAccount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountService() {
        return accountService;
    }

    public String getSelectDocumentTypeLabel() {
        return selectDocumentTypeLabel;
    }

    public String getPlaceholderForDocumentNumber() {
        return placeholderForDocumentNumber;
    }

    public String getSummaryHeadingI18n() {
        return summaryHeadingI18n;
    }

    public String getDocumentHeadingI18n() {
        return documentHeadingI18n;
    }

    public String getSelectDates() {
        return selectDates;
    }

    public String getCloseBtn() {
        return closeBtn;
    }

    public String getSetDates() {
        return setDates;
    }

    public String getDatePlaceholder() {
        return datePlaceholder;
    }

    public String getDateRangePlaceholder() {
        return dateRangePlaceholder;
    }

    public String getDateRangeErrorLabel() {
        return dateRangeErrorLabel;
    }

    public String getFindCustomer() {
        return findCustomer;
    }

    public String getSelectCustomerLabel() {
        return selectCustomerLabel;
    }

    public String getSelectStatusLabel() {
        return selectStatusLabel;
    }

    public String getSelectDateRangeLabel() {
        return selectDateRangeLabel;
    }

    public String getSearchInputLabel() {
        return searchInputLabel;
    }

    public String getResetButtonText() {
        return resetButtonText;
    }

    public String getSearchButtonText() {
        return searchButtonText;
    }

    public String getCreateExcelBtnI18n() {
        return createExcelBtnI18n;
    }

    public String getCreatePdfBtnI18n() {
        return createPdfBtnI18n;
    }

    public String getFileDownloadErrorText() {
        return fileDownloadErrorText;
    }

    public String getExcelPdfDownloadErrorText() {
        return excelPdfDownloadErrorText;
    }

    public String getFileDownloadErrorClose() {
        return fileDownloadErrorClose;
    }

    public String getNoCustomerErrorText() {
        return noCustomerErrorText;
    }

    public String getI18nKeys() {
        return i18nKeys;
    }

    public String getDownloadPdfExcelServletUrl() {
        return downloadPdfExcelServletUrl;
    }

    public String getDownloadInvoice() {
        return "/bin/customerhub/invoice/document.{docId}.pdf";
    }

    /**
     * init method
     */
    @PostConstruct
    protected void init() {
        Map<String, Object> i18KeyMap = new HashMap<>();
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
        i18KeyMap.put("excelPdfDownloadErrorText", getExcelPdfDownloadErrorText());
        i18KeyMap.put("fileDownloadErrorClose", getFileDownloadErrorClose());
        i18KeyMap.put("datePlaceholder", getDatePlaceholder());
        i18KeyMap.put("dateRangePlaceholder", getDateRangePlaceholder());
        i18KeyMap.put("dateRangeErrorLabel", getDateRangeErrorLabel());
        i18KeyMap.put("noCustomerErrorText", getNoCustomerErrorText());
        i18KeyMap.put("apiErrorCodes", GlobalUtil.getApiErrorCodes(resource));

        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);
        downloadPdfExcelServletUrl = resource.getPath() + ".download.{extnType}";
    }
}
