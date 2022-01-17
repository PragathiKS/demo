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
}
