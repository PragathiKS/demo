package com.tetrapak.customerhub.core.services;

import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetailsData;
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
     * @param response        Response
     * @param orderType       Order Type
     * @param orderDetailData order details data
     */
    void generateOrderDetailsExcel(SlingHttpServletResponse response, String orderType, OrderDetailsData orderDetailData);

}
