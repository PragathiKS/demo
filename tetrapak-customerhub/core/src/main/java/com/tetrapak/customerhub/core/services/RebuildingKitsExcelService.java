package com.tetrapak.customerhub.core.services;

import com.tetrapak.customerhub.core.beans.rebuildingkits.RebuildingKits;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Equipment List Excel Service
 */
public interface RebuildingKitsExcelService {

	/**
	 * Method to generate CSV file
	 *
	 * @return boolean result
	 */
	boolean generateCSV(List<RebuildingKits> rbk, SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws IOException;
}
