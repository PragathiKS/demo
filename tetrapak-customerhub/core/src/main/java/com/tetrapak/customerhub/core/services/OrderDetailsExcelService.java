package com.tetrapak.customerhub.core.services;

import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetailsData;
import com.tetrapak.customerhub.core.models.OrderDetailsModel;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

/**
 * Service to create Excel file for order details.
 *
 * @author Tushar
 */
@FunctionalInterface
public interface OrderDetailsExcelService {
    /**
     * Method to generate the excel file for the order details page.
     *
     * @param request           SlingHttpServletRequest
     * @param response          SlingHttpServletResponse
     * @param orderType         parts and packmat
     * @param orderDetailData   data from the api
     * @param orderDetailsModel data from the dialog
     * @return true if successful
     */
    boolean generateOrderDetailsExcel(SlingHttpServletRequest request, SlingHttpServletResponse response,
                                      String orderType, OrderDetailsData orderDetailData, OrderDetailsModel orderDetailsModel);

}
