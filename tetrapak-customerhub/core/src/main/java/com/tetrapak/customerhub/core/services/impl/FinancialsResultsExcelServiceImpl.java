
package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.beans.excel.ExcelFileData;
import com.tetrapak.customerhub.core.beans.financials.results.Document;
import com.tetrapak.customerhub.core.beans.financials.results.Params;
import com.tetrapak.customerhub.core.beans.financials.results.Record;
import com.tetrapak.customerhub.core.beans.financials.results.Results;
import com.tetrapak.customerhub.core.beans.financials.results.Summary;
import com.tetrapak.customerhub.core.services.FinancialsResultsExcelService;
import com.tetrapak.customerhub.core.utils.ExcelUtil;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Implementation class for Financial Results Excel Service
 *
 * @author swalamba
 */
@Component(immediate = true, service = FinancialsResultsExcelService.class)
public class FinancialsResultsExcelServiceImpl implements FinancialsResultsExcelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FinancialsResultsExcelServiceImpl.class);
    private SlingHttpServletRequest request;
    private static final String[] COLFIELDS = {
            "documentNumber", "documentType", "invoiceStatus", "invoiceReference", "poNumber", "docDate", "dueDate", "clearedDate",
            "currency", "dueDays", "orgAmount", "remAmount", "salesOffice", "companyCode", "salesLocalData"
    };
    private static final String I18N_PREFIX = "cuhu.financials.";

    @Override
    public boolean generateFinancialsResultsExcel(SlingHttpServletRequest req, SlingHttpServletResponse response,
                                                  Results apiResponse, Params paramRequest) {

        String customerName = (paramRequest != null && paramRequest.getCustomerData() != null)
                ? paramRequest.getCustomerData().getCustomerName()
                : StringUtils.EMPTY;
        if (paramRequest != null && null != apiResponse) {
            request = req;
            ExcelFileData excelReportData = new ExcelFileData();
            String dateRange = paramRequest.getStartDate() + (paramRequest.getEndDate() == null ? StringUtils.EMPTY
                    : " " + "-" + " " + paramRequest.getEndDate());
            excelReportData.setFileName("Financial-" + customerName + "-" + dateRange);
            excelReportData.setExcelSheetName("Sheet");
            excelReportData.setCellBorderColor(IndexedColors.GREY_25_PERCENT.index);
            excelReportData.setHasMargin(false);
            String[][] data = getColumnHeaderArray();
            data = ArrayUtils.addAll(data, getSalesOfficeStatementList(apiResponse.getDocuments()));
            data = ArrayUtils.addAll(data, getSummaryRow(apiResponse.getSummary()));
            excelReportData.setData(data);
            if (ExcelUtil.generateExcelReport(response, excelReportData)) {
                LOGGER.debug("Excel file: {} generated successfully!", excelReportData.getExcelSheetName());
                return true;
            }
        }

        LOGGER.error("Excel file could not be generated for customer {}", customerName);
        return false;
    }

    private String[][] getColumnHeaderArray() {
        String[][] columnNames = new String[1][16];
        String[] tags = new String[]{
                ExcelUtil.DARK_GREY_BG_TAG
        };
        columnNames[0][0] = addTagToContent(getI18nVal(COLFIELDS[0]), tags);
        columnNames[0][1] = addTagToContent(StringUtils.EMPTY, new String[]{
                ExcelUtil.MERGE_2_CELLS_TAG, ExcelUtil.DARK_GREY_BG_TAG
        });
        columnNames[0][2] = addTagToContent(getI18nVal(COLFIELDS[1]), tags);
        columnNames[0][3] = addTagToContent(getI18nVal(COLFIELDS[2]), tags);
        columnNames[0][4] = addTagToContent(getI18nVal(COLFIELDS[3]), tags);
        columnNames[0][5] = addTagToContent(getI18nVal(COLFIELDS[4]), tags);
        columnNames[0][6] = addTagToContent(getI18nVal(COLFIELDS[5]), tags);
        columnNames[0][7] = addTagToContent(getI18nVal(COLFIELDS[6]), tags);
        columnNames[0][8] = addTagToContent(getI18nVal(COLFIELDS[7]), tags);
        columnNames[0][9] = addTagToContent(getI18nVal(COLFIELDS[8]), tags);
        columnNames[0][10] = addTagToContent(getI18nVal(COLFIELDS[9]), tags);
        columnNames[0][11] = addTagToContent(getI18nVal(COLFIELDS[10]), tags);
        columnNames[0][12] = addTagToContent(getI18nVal(COLFIELDS[11]), tags);
        columnNames[0][13] = addTagToContent(getI18nVal(COLFIELDS[12]), tags);
        columnNames[0][14] = addTagToContent(getI18nVal(COLFIELDS[13]), tags);
        columnNames[0][15] = addTagToContent(getI18nVal(COLFIELDS[14]), tags);
        return columnNames;
    }

    /**
     * @param summaryList summaryList
     * @return String array
     */
    private String[][] getSummaryRow(List<Summary> summaryList) {
        String[][] summaryRow = new String[1][10];
        String[] tags = new String[]{
                ExcelUtil.LIME_BG_TAG
        };
        String totalSummary = getTotalFromSummary(summaryList);
        summaryRow[0][0] = addTagToContent(StringUtils.EMPTY, tags);
        summaryRow[0][1] = addTagToContent(StringUtils.EMPTY, new String[]{
                ExcelUtil.MERGE_2_CELLS_TAG, ExcelUtil.LIME_BG_TAG
        });
        summaryRow[0][2] = addTagToContent(StringUtils.EMPTY, tags);
        summaryRow[0][3] = addTagToContent(StringUtils.EMPTY, tags);
        summaryRow[0][4] = addTagToContent(StringUtils.EMPTY, tags);
        summaryRow[0][5] = addTagToContent(StringUtils.EMPTY, tags);
        summaryRow[0][6] = addTagToContent(StringUtils.EMPTY, tags);
        summaryRow[0][7] = addTagToContent(StringUtils.EMPTY, tags);
        summaryRow[0][8] = addTagToContent(StringUtils.EMPTY, tags);
        summaryRow[0][9] = addTagToContent(totalSummary, tags);

        return summaryRow;
    }

    /**
     * @param summaryList summary list
     * @return total
     */
    private String getTotalFromSummary(List<Summary> summaryList) {

        Set<String> currency = new HashSet<>();
        StringBuilder sb = new StringBuilder(StringUtils.EMPTY);
        if (null != summaryList) {
            Iterator<Summary> itr = summaryList.iterator();
            while (itr.hasNext()) {
                Summary summary = itr.next();
                currency.add(summary.getCurrency().toLowerCase());
                sb.append(summary.getTotal());
            }
        }
        String totalSummary;
        if (currency.size() > 1) {
            totalSummary = "N/A for Multiple Currencies";
        } else {
            totalSummary = sb.toString();
        }

        return totalSummary;
    }

    /**
     * @param documents documents
     * @return doc list
     */
    private String[][] getSalesOfficeStatementList(List<Document> documents) {
        String[][] docList = null;
        if (null != documents) {
            Iterator<Document> itr = documents.iterator();
            while (itr.hasNext()) {
                Document doc = itr.next();
                docList = ArrayUtils.addAll(docList, getEachSalesOfcStatement(doc));
            }
        }
        return docList;
    }

    /**
     * @param doc document
     * @return array
     */
    private String[][] getEachSalesOfcStatement(Document doc) {
        String[][] data = null;
        if (null != doc) {
            String[][] firstRow = {
                    {
                            setSalesOfficeRow(doc.getSalesOffice())
                    }
            };
            String[][] middleRows = setStatementData(doc.getRecords());
            String[][] lastRow = {
                    setTotalAmountRow(doc.getTotalAmount())
            };
            data = ArrayUtils.addAll(data, firstRow);
            data = ArrayUtils.addAll(data, middleRows);
            data = ArrayUtils.addAll(data, lastRow);
        }
        return data;
    }

    /**
     * @param records list of records
     * @return StatementData rows
     */
    private String[][] setStatementData(List<Record> records) {
        String[][] data = null;
        if (null != records) {
            data = new String[records.size()][16];
            Iterator<Record> itr = records.iterator();
            byte counter = 0;
            String[] tags = new String[]{};
            while (itr.hasNext()) {
                Record record = itr.next();
                data[counter][0] = addTagToContent(StringUtils.EMPTY, new String[]{
                        ExcelUtil.NO_BORDER_TAG
                });
                data[counter][1] = addTagToContent(record.getDocumentNumber(), tags);
                data[counter][2] = addTagToContent(record.getDocumentType(), tags);
                data[counter][3] = addTagToContent(record.getInvoiceStatus(), tags);
                data[counter][4] = addTagToContent(record.getInvoiceReference(), tags);
                data[counter][5] = addTagToContent(record.getPoNumber(), tags);
                data[counter][6] = addTagToContent(record.getDocDate(), tags);
                data[counter][7] = addTagToContent(record.getDueDate(), tags);
                data[counter][8] = addTagToContent(record.getClearedDate(), tags);
                data[counter][9] = addTagToContent(record.getCurrency(), tags);
                data[counter][10] = addTagToContent(record.getDueDays(), tags);
                data[counter][11] = addTagToContent(record.getOrgAmount(), new String[]{
                        ExcelUtil.RIGHT_ALIGN_TAG
                });
                data[counter][12] = addTagToContent(record.getRemAmount(), new String[]{
                        ExcelUtil.RIGHT_ALIGN_TAG
                });
                data[counter][13] = addTagToContent(record.getSalesOffice(), tags);
                data[counter][14] = addTagToContent(record.getCompanyCode(), tags);
                data[counter][15] = addTagToContent(record.getSalesLocalData(), tags);

                counter++;
            }
        }
        return data;
    }

    /**
     * @param totalAmount total amount
     * @return array
     */
    private String[] setTotalAmountRow(String totalAmount) {
        String[] tags = new String[]{
                ExcelUtil.LIME_BG_TAG
        };
        String[] totalAmountRow = new String[10];
        totalAmountRow[0] = addTagToContent(StringUtils.EMPTY, new String[]{
                ExcelUtil.NO_BORDER_TAG
        });
        totalAmountRow[1] = addTagToContent(StringUtils.EMPTY, tags);
        totalAmountRow[2] = addTagToContent(StringUtils.EMPTY, tags);
        totalAmountRow[3] = addTagToContent(StringUtils.EMPTY, tags);
        totalAmountRow[4] = addTagToContent(StringUtils.EMPTY, tags);
        totalAmountRow[5] = addTagToContent(StringUtils.EMPTY, tags);
        totalAmountRow[6] = addTagToContent(StringUtils.EMPTY, tags);
        totalAmountRow[7] = addTagToContent(StringUtils.EMPTY, tags);
        totalAmountRow[8] = addTagToContent(StringUtils.EMPTY, tags);
        totalAmountRow[9] = addTagToContent(totalAmount, tags);
        return totalAmountRow;
    }

    /**
     * @param salesOffice sales office
     * @return salesOffice value
     */
    private String setSalesOfficeRow(String salesOffice) {
        return addTagToContent("Sales Office: " + (StringUtils.isBlank(salesOffice) ? StringUtils.EMPTY : salesOffice),
                new String[]{
                        ExcelUtil.LIGHT_GREY_LEFT_TAG, ExcelUtil.MERGE_ROW_TAG
                });
    }

    /**
     * Appending the tags to the field values so that it can be processed for the styling
     *
     * @param rawData raw data
     * @param tags tags
     * @return string
     */
    private String addTagToContent(String rawData, String[] tags) {
        StringBuilder processedData = new StringBuilder();
        if (null != rawData) {
            processedData.append(rawData);
            for (String tag : tags) {
                processedData.append(tag);
            }
        }
        return processedData.toString();
    }

    /**
     * getI18nVal
     *
     * @param value value
     * @return string
     */
    private String getI18nVal(String value) {
        if (null != request && !StringUtils.isBlank(value)) {
            value = GlobalUtil.getI18nValue(request, I18N_PREFIX, value);
        }
        return value;
    }

}
