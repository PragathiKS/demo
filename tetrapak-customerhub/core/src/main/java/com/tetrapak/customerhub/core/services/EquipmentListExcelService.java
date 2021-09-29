package com.tetrapak.customerhub.core.services;

import com.tetrapak.customerhub.core.beans.equipmentlist.Results;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

/**
 * Equipment List Excel Service
 */
public interface EquipmentListExcelService {

	/**
	 * Method to generate Equipment List excel
	 *
	 * @param req        SlingHttpServletRequest the POST request call from the My
	 *                   Equipment page frontend
	 * @param response   SlingHttpServletResponse to send the excel file to the
	 *                   browser
	 * @param equipments data from the frontend got from the POST request regarding
	 *                   the equipment details
	 * @return true if successful in generation of the excel otherwise false
	 */
	boolean generateEquipmentListExcel(SlingHttpServletRequest req, SlingHttpServletResponse response,
			Results apiResponse);

}
