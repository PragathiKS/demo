package com.tetrapak.customerhub.core.services;

import com.tetrapak.customerhub.core.beans.equipmentlist.Equipments;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Equipment List Excel Service
 */
public interface EquipmentListExcelService {

	/**
	 * Method to generate CSV file
	 *
	 * @return boolean result
	 */
	boolean generateCSV(List<Equipments> equipments, SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws IOException;

	/**
	 * Method to generate CSV file header array
	 *
	 * @return String[][] header array
	 */
	String[][] getColumnHeaderArray();

	/**
	 * This method converts an Equipment object to comma separated String
	 * representation. The order of adding properties to list determines the order
	 * of columns in CSV file.
	 *
	 * @param Equipments equipment
	 * @return String
	 */
	String convertToCSVRow(Equipments equipment);

	/**
	 * This method converts an Equipment object to comma separated String
	 * representation. The order of adding properties to list determines the order
	 * of columns in CSV file.
	 *
	 * @param Equipments equipment
	 * @return String
	 */
	List<Equipments> sortEquipmentRecordsinCSV(List<Equipments> unsortedEquipments);
}
