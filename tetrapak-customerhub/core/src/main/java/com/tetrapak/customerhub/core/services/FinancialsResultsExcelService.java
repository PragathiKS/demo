package com.tetrapak.customerhub.core.services;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import com.tetrapak.customerhub.core.beans.financials.results.Params;
import com.tetrapak.customerhub.core.beans.financials.results.Results;

/**
 * Tetra Pak Financial Results Excel Service
 *
 * @author Swati Lamba
 */
@FunctionalInterface
public interface FinancialsResultsExcelService {

	/**
	 * Method to generate financial results excel
	 * 
	 * @param req          SlingHttpServletRequest the POST request call from the financials page frontend
	 * @param response     SlingHttpServletResponse to send the excel file to the browser
	 * @param apiResponse  searchFinancials API response containing statement of accounts
	 * @param paramRequest data from the frontend got from the POST request
	 *                     regarding the customer details
	 * @return true if successful in generation of the excel otherwise false
	 */

	boolean generateFinancialsResultsExcel(SlingHttpServletRequest req, SlingHttpServletResponse response,
			Results apiResponse, Params paramRequest);
}
