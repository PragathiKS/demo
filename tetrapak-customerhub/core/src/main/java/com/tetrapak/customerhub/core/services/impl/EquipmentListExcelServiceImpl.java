package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.beans.equipmentlist.Equipments;
import com.tetrapak.customerhub.core.beans.equipmentlist.Results;
import com.tetrapak.customerhub.core.beans.excel.ExcelFileData;
import com.tetrapak.customerhub.core.services.EquipmentListExcelService;
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

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Component(immediate = true, service = EquipmentListExcelService.class) public class EquipmentListExcelServiceImpl
        implements EquipmentListExcelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentListExcelServiceImpl.class);
    private static final String WHITE = "<white>";
    private static final String SHEET_NAME = "Equipments List";
    private static final String FILE_NAME = "List of Equipments";
    private SlingHttpServletRequest request;
    private String language;
    private static final String I18N_PREFIX = "cuhu.equipment.";
    private static final String[] COLFIELDS = { "id", "countryCode", "countryName", "customer", "customerNumber",
            "customerName", "location", "lineName", "equipmentstatus", "isSecondhand", "equipmentType", "businessType",
            "equipmentTypeDesc", "functionalLocation", "functionalLocationDesc", "serialnumber", "siteName",
            "siteDescription", "permanentVolumeConversion", "position", "machineSystem", "material", "materialDesc",
            "manufacturerModelNumber", "manufacturerSerialNumber", "superiorEquipment", "superiorEquipmentName",
            "superiorEquipmentSerialNumber", "manufacturer", "manufacturerCountry", "constructionYear",
            "customerWarrantyStartDate", "customerWarrantyEndDate", "equipmentCategory", "equipmentCategoryDesc",
            "eofsConfirmationDate", "eofsValidFromDate"
    };

    /**
     * @param SlingHttpServletRequest  req
     * @param SlingHttpServletResponse response
     * @param Results                  apiResponse
     * @return boolean flag
     */
    @Override public boolean generateEquipmentListExcel(SlingHttpServletRequest req, SlingHttpServletResponse response,
            Results apiResponse) {
        if (Objects.nonNull(apiResponse)) {
            language = GlobalUtil.getLanguage(req);
            request = req;
            ExcelFileData excelReportData = new ExcelFileData();
            excelReportData.setFileName(FILE_NAME);
            excelReportData.setExcelSheetName(SHEET_NAME);
            excelReportData.setCellBorderColor(IndexedColors.GREY_25_PERCENT.index);
            excelReportData.setHasMargin(false);
            String[][] data = getColumnHeaderArray();
            data = ArrayUtils.addAll(data, getEquipmentsList(apiResponse.getData()));
            excelReportData.setData(data);
            if (ExcelUtil.generateExcelReport(response, excelReportData)) {
                LOGGER.debug("Excel file: {} generated successfully!", excelReportData.getExcelSheetName());
                return true;
            } else {
                LOGGER.error("Equipment Data is null so can not process the excel creation!");
            }
            return false;
        }
        return false;
    }

    /**
     * @return String array of excel columns
     */
    private String[][] getColumnHeaderArray() {
        String[][] columnNames = new String[1][37];
        String[] tags = new String[] { ExcelUtil.DARK_GREY_BG_TAG
        };
        columnNames[0][0] = addTagToContent(WHITE + getI18nVal(COLFIELDS[0]), tags);
        columnNames[0][1] = addTagToContent(WHITE + getI18nVal(COLFIELDS[1]), tags);
        columnNames[0][2] = addTagToContent(WHITE + getI18nVal(COLFIELDS[2]), tags);
        columnNames[0][3] = addTagToContent(WHITE + getI18nVal(COLFIELDS[3]), tags);
        columnNames[0][4] = addTagToContent(WHITE + getI18nVal(COLFIELDS[4]), tags);
        columnNames[0][5] = addTagToContent(WHITE + getI18nVal(COLFIELDS[5]), tags);
        columnNames[0][6] = addTagToContent(WHITE + getI18nVal(COLFIELDS[6]), tags);
        columnNames[0][7] = addTagToContent(WHITE + getI18nVal(COLFIELDS[7]), tags);
        columnNames[0][8] = addTagToContent(WHITE + getI18nVal(COLFIELDS[8]), tags);
        columnNames[0][9] = addTagToContent(WHITE + getI18nVal(COLFIELDS[9]), tags);
        columnNames[0][10] = addTagToContent(WHITE + getI18nVal(COLFIELDS[10]), tags);
        columnNames[0][11] = addTagToContent(WHITE + getI18nVal(COLFIELDS[11]), tags);
        columnNames[0][12] = addTagToContent(WHITE + getI18nVal(COLFIELDS[12]), tags);
        columnNames[0][13] = addTagToContent(WHITE + getI18nVal(COLFIELDS[13]), tags);
        columnNames[0][14] = addTagToContent(WHITE + getI18nVal(COLFIELDS[14]), tags);
        columnNames[0][15] = addTagToContent(WHITE + getI18nVal(COLFIELDS[15]), tags);
        columnNames[0][16] = addTagToContent(WHITE + getI18nVal(COLFIELDS[16]), tags);
        columnNames[0][17] = addTagToContent(WHITE + getI18nVal(COLFIELDS[17]), tags);
        columnNames[0][18] = addTagToContent(WHITE + getI18nVal(COLFIELDS[18]), tags);
        columnNames[0][19] = addTagToContent(WHITE + getI18nVal(COLFIELDS[19]), tags);
        columnNames[0][20] = addTagToContent(WHITE + getI18nVal(COLFIELDS[20]), tags);
        columnNames[0][21] = addTagToContent(WHITE + getI18nVal(COLFIELDS[21]), tags);
        columnNames[0][22] = addTagToContent(WHITE + getI18nVal(COLFIELDS[22]), tags);
        columnNames[0][23] = addTagToContent(WHITE + getI18nVal(COLFIELDS[23]), tags);
        columnNames[0][24] = addTagToContent(WHITE + getI18nVal(COLFIELDS[24]), tags);
        columnNames[0][25] = addTagToContent(WHITE + getI18nVal(COLFIELDS[25]), tags);
        columnNames[0][26] = addTagToContent(WHITE + getI18nVal(COLFIELDS[26]), tags);
        columnNames[0][27] = addTagToContent(WHITE + getI18nVal(COLFIELDS[27]), tags);
        columnNames[0][28] = addTagToContent(WHITE + getI18nVal(COLFIELDS[28]), tags);
        columnNames[0][29] = addTagToContent(WHITE + getI18nVal(COLFIELDS[29]), tags);
        columnNames[0][30] = addTagToContent(WHITE + getI18nVal(COLFIELDS[30]), tags);
        columnNames[0][31] = addTagToContent(WHITE + getI18nVal(COLFIELDS[31]), tags);
        columnNames[0][32] = addTagToContent(WHITE + getI18nVal(COLFIELDS[32]), tags);
        columnNames[0][33] = addTagToContent(WHITE + getI18nVal(COLFIELDS[33]), tags);
        columnNames[0][34] = addTagToContent(WHITE + getI18nVal(COLFIELDS[34]), tags);
        columnNames[0][35] = addTagToContent(WHITE + getI18nVal(COLFIELDS[35]), tags);
        columnNames[0][36] = addTagToContent(WHITE + getI18nVal(COLFIELDS[36]), tags);
        return columnNames;
    }

    /**
     * Appending the tags to the field values so that it can be processed for the styling
     *
     * @param rawData raw data
     * @param tags    tags
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
            value = GlobalUtil.getI18nValueForThisLanguage(request, I18N_PREFIX, value, language);
        }
        return value;
    }

    /**
     * @param documents documents
     * @return doc list
     */
    private String[][] getEquipmentsList(List<Equipments> equipments) {
        String[][] equipmentList = new String[equipments.size()][37];
        int counter = 0;
        String[] tags = new String[] {};
        Iterator<Equipments> itr = equipments.iterator();
        while (itr.hasNext()) {
            Equipments equipment = itr.next();
            equipmentList[counter][0] = addTagToContent(equipment.getId(), tags);
            equipmentList[counter][1] = addTagToContent(equipment.getCountryCode(), tags);
            equipmentList[counter][2] = addTagToContent(equipment.getCountryName(), tags);
            equipmentList[counter][3] = addTagToContent(equipment.getCustomer(), tags);
            equipmentList[counter][4] = addTagToContent(equipment.getCustomerNumber(), tags);
            equipmentList[counter][5] = addTagToContent(equipment.getCustomerName(), tags);
            equipmentList[counter][6] = addTagToContent(equipment.getLocation(), tags);
            equipmentList[counter][7] = addTagToContent(equipment.getLineName(), tags);
            equipmentList[counter][8] = addTagToContent(equipment.getEquipmentStatus(), tags);
            equipmentList[counter][9] = addTagToContent(equipment.getIsSecondhand(), tags);
            equipmentList[counter][10] = addTagToContent(equipment.getEquipmentType(), tags);
            equipmentList[counter][11] = addTagToContent(equipment.getBusinessType(), tags);
            equipmentList[counter][12] = addTagToContent(equipment.getEquipmentTypeDesc(), tags);
            equipmentList[counter][13] = addTagToContent(equipment.getFunctionalLocation(), tags);
            equipmentList[counter][14] = addTagToContent(equipment.getFunctionalLocationDesc(), tags);
            equipmentList[counter][15] = addTagToContent(equipment.getSerialNumber(), tags);
            equipmentList[counter][16] = addTagToContent(equipment.getSiteName(), tags);
            equipmentList[counter][17] = addTagToContent(equipment.getSiteDesc(), tags);
            equipmentList[counter][18] = addTagToContent(equipment.getPermanentVolumeConversion(), tags);
            equipmentList[counter][19] = addTagToContent(equipment.getPosition(), tags);
            equipmentList[counter][20] = addTagToContent(equipment.getMachineSystem(), tags);
            equipmentList[counter][21] = addTagToContent(equipment.getMaterial(), tags);
            equipmentList[counter][22] = addTagToContent(equipment.getMaterialDesc(), tags);
            equipmentList[counter][23] = addTagToContent(equipment.getManufacturerModelNumber(), tags);
            equipmentList[counter][24] = addTagToContent(equipment.getManufacturerSerialNumber(), tags);
            equipmentList[counter][25] = addTagToContent(equipment.getSuperiorEquipment(), tags);
            equipmentList[counter][26] = addTagToContent(equipment.getSuperiorEquipmentName(), tags);
            equipmentList[counter][27] = addTagToContent(equipment.getSuperiorEquipmentSerialNumber(), tags);
            equipmentList[counter][28] = addTagToContent(equipment.getManufacturer(), tags);
            equipmentList[counter][29] = addTagToContent(equipment.getManufacturerCountry(), tags);
            equipmentList[counter][30] = addTagToContent(equipment.getConstructionYear(), tags);
            equipmentList[counter][31] = addTagToContent(equipment.getCustomerWarrantyStartDate(), tags);
            equipmentList[counter][32] = addTagToContent(equipment.getCustomerWarrantyEndDate(), tags);
            equipmentList[counter][33] = addTagToContent(equipment.getEquipmentCategory(), tags);
            equipmentList[counter][34] = addTagToContent(equipment.getEquipmentCategoryDesc(), tags);
            equipmentList[counter][35] = addTagToContent(equipment.getEofsConfirmationDate(), tags);
            equipmentList[counter][36] = addTagToContent(equipment.getEofsValidFromDate(), tags);
            counter++;
        }
        return equipmentList;
    }
}
