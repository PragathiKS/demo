package com.tetrapak.customerhub.core.services;

/**
 * Equipment List Excel Service
 */
public interface EquipmentListExcelService {

	/**
	 * Method to generate CSV file header array
	 *
	 * @return String[][] header array
	 */
	String[][] getColumnHeaderArray();
}
