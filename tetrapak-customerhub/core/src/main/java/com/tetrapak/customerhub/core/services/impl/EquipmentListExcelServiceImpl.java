package com.tetrapak.customerhub.core.services.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;

import com.tetrapak.customerhub.core.beans.equipmentlist.Equipments;
import com.tetrapak.customerhub.core.services.EquipmentListExcelService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;

@Component(immediate = true, service = EquipmentListExcelService.class)
public class EquipmentListExcelServiceImpl implements EquipmentListExcelService {

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
			"eofsConfirmationDate", "eofsValidFromDate" };

	/**
	 * @return String array of excel columns
	 */
	public String[][] getColumnHeaderArray() {
		String[][] columnNames = new String[1][37];
		columnNames[0][0] = getI18nVal(COLFIELDS[0]);
		columnNames[0][1] = getI18nVal(COLFIELDS[1]);
		columnNames[0][2] = getI18nVal(COLFIELDS[2]);
		columnNames[0][3] = getI18nVal(COLFIELDS[3]);
		columnNames[0][4] = getI18nVal(COLFIELDS[4]);
		columnNames[0][5] = getI18nVal(COLFIELDS[5]);
		columnNames[0][6] = getI18nVal(COLFIELDS[6]);
		columnNames[0][7] = getI18nVal(COLFIELDS[7]);
		columnNames[0][8] = getI18nVal(COLFIELDS[8]);
		columnNames[0][9] = getI18nVal(COLFIELDS[9]);
		columnNames[0][10] = getI18nVal(COLFIELDS[10]);
		columnNames[0][11] = getI18nVal(COLFIELDS[11]);
		columnNames[0][12] = getI18nVal(COLFIELDS[12]);
		columnNames[0][13] = getI18nVal(COLFIELDS[13]);
		columnNames[0][14] = getI18nVal(COLFIELDS[14]);
		columnNames[0][15] = getI18nVal(COLFIELDS[15]);
		columnNames[0][16] = getI18nVal(COLFIELDS[16]);
		columnNames[0][17] = getI18nVal(COLFIELDS[17]);
		columnNames[0][18] = getI18nVal(COLFIELDS[18]);
		columnNames[0][19] = getI18nVal(COLFIELDS[19]);
		columnNames[0][20] = getI18nVal(COLFIELDS[20]);
		columnNames[0][21] = getI18nVal(COLFIELDS[21]);
		columnNames[0][22] = getI18nVal(COLFIELDS[22]);
		columnNames[0][23] = getI18nVal(COLFIELDS[23]);
		columnNames[0][24] = getI18nVal(COLFIELDS[24]);
		columnNames[0][25] = getI18nVal(COLFIELDS[25]);
		columnNames[0][26] = getI18nVal(COLFIELDS[26]);
		columnNames[0][27] = getI18nVal(COLFIELDS[27]);
		columnNames[0][28] = getI18nVal(COLFIELDS[28]);
		columnNames[0][29] = getI18nVal(COLFIELDS[29]);
		columnNames[0][30] = getI18nVal(COLFIELDS[30]);
		columnNames[0][31] = getI18nVal(COLFIELDS[31]);
		columnNames[0][32] = getI18nVal(COLFIELDS[32]);
		columnNames[0][33] = getI18nVal(COLFIELDS[33]);
		columnNames[0][34] = getI18nVal(COLFIELDS[34]);
		columnNames[0][35] = getI18nVal(COLFIELDS[35]);
		columnNames[0][36] = getI18nVal(COLFIELDS[36]);
		return columnNames;
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
	public String[][] getEquipmentsList(List<Equipments> equipments) {
		String[][] equipmentList = new String[equipments.size()][37];
		int counter = 0;
		Iterator<Equipments> itr = equipments.iterator();
		while (itr.hasNext()) {
			Equipments equipment = itr.next();
			equipmentList[counter][0] = equipment.getId();
			equipmentList[counter][1] = equipment.getCountryCode();
			equipmentList[counter][2] = equipment.getCountryName();
			equipmentList[counter][3] = equipment.getCustomer();
			equipmentList[counter][4] = equipment.getCustomerNumber();
			equipmentList[counter][5] = equipment.getCustomerName();
			equipmentList[counter][6] = equipment.getLocation();
			equipmentList[counter][7] = equipment.getLineName();
			equipmentList[counter][8] = equipment.getEquipmentStatus();
			equipmentList[counter][9] = equipment.getIsSecondhand();
			equipmentList[counter][10] = equipment.getEquipmentType();
			equipmentList[counter][11] = equipment.getBusinessType();
			equipmentList[counter][12] = equipment.getEquipmentTypeDesc();
			equipmentList[counter][13] = equipment.getFunctionalLocation();
			equipmentList[counter][14] = equipment.getFunctionalLocationDesc();
			equipmentList[counter][15] = equipment.getSerialNumber();
			equipmentList[counter][16] = equipment.getSiteName();
			equipmentList[counter][17] = equipment.getSiteDesc();
			equipmentList[counter][18] = equipment.getPermanentVolumeConversion();
			equipmentList[counter][19] = equipment.getPosition();
			equipmentList[counter][20] = equipment.getMachineSystem();
			equipmentList[counter][21] = equipment.getMaterial();
			equipmentList[counter][22] = equipment.getMaterialDesc();
			equipmentList[counter][23] = equipment.getManufacturerModelNumber();
			equipmentList[counter][24] = equipment.getManufacturerSerialNumber();
			equipmentList[counter][25] = equipment.getSuperiorEquipment();
			equipmentList[counter][26] = equipment.getSuperiorEquipmentName();
			equipmentList[counter][27] = equipment.getSuperiorEquipmentSerialNumber();
			equipmentList[counter][28] = equipment.getManufacturer();
			equipmentList[counter][29] = equipment.getManufacturerCountry();
			equipmentList[counter][30] = equipment.getConstructionYear();
			equipmentList[counter][31] = equipment.getCustomerWarrantyStartDate();
			equipmentList[counter][32] = equipment.getCustomerWarrantyEndDate();
			equipmentList[counter][33] = equipment.getEquipmentCategory();
			equipmentList[counter][34] = equipment.getEquipmentCategoryDesc();
			equipmentList[counter][35] = equipment.getEofsConfirmationDate();
			equipmentList[counter][36] = equipment.getEofsValidFromDate();
			counter++;
		}
		return equipmentList;
	}
}
