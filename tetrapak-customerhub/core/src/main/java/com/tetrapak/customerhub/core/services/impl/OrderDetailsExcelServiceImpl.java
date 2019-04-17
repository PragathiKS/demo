
package com.tetrapak.customerhub.core.services.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.customerhub.core.beans.excel.ExcelFileData;
import com.tetrapak.customerhub.core.beans.oderdetails.DeliveryList;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetails;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetailsData;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderSummary;
import com.tetrapak.customerhub.core.models.OrderDetailsModel;
import com.tetrapak.customerhub.core.services.OrderDetailsExcelService;
import com.tetrapak.customerhub.core.utils.ExcelUtil;

/**
 * Implemention class for Odrder Details Excel Service
 *
 * @author Tushar
 */
@Component(immediate = true, service = OrderDetailsExcelService.class)
public class OrderDetailsExcelServiceImpl implements OrderDetailsExcelService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderDetailsExcelServiceImpl.class);

	/*@Override
	public void generateOrderDetailsExcel(SlingHttpServletResponse response, String orderType,
			OrderDetailsData orderDetailData, OrderDetailsModel orderDetailsModel) {
		getOrderDetailsSection(orderDetailsModel, orderDetailData.getOrderDetails());
		getOrderSummary(orderDetailData.getOrderSummary());
		getDeliveryList(orderDetailData.getDeliveryList());

	}*/
	
	@Override
    public void generateOrderDetailsExcel(SlingHttpServletResponse response, String orderType,
                  OrderDetailsData orderDetailData, OrderDetailsModel orderDetailsModel) {
           //getOrderDetailsSection(orderDetailData.getOrderDetails());
           String[][] data = getOrderSummary(orderDetailData.getOrderSummary());
           getDeliveryList(orderDetailData.getDeliveryList());
           ExcelFileData excelReportData = new ExcelFileData();
           excelReportData.setFileName("Packmat Order Details_Excel_"+orderDetailData.getOrderDetails().getOrderNumber());
           // excelReportData.setFontColor(1);
           excelReportData.setExcelSheetName("Sheet1");
           // excelReportData.setColumns(columns);
           excelReportData.setData(data);
           ExcelUtil.generateExcelReport(response, excelReportData);}

/**
    * Order Summary section in the excel
    * Only in packaging material excel
    * 
     * @return OrderSummary 2 d array
    */
    private String[][] getOrderSummary(List<OrderSummary> orderSummary) {
           String[][] data = new String[4][4];
           data[0][0] = StringUtils.EMPTY;
           data[0][1] = "<b>Product</b>";
           data[0][2] = "<html><u>Order Quantity</u></html>";
           data[0][3] = "Quantity Delivered so far";
           int counter = 1;
           Iterator<OrderSummary> itr = orderSummary.iterator();
           while (itr.hasNext()) {
                  counter++;
                  OrderSummary summaryRow = itr.next();
                  data[counter][0] = summaryRow.getProduct();
                  data[counter][1] = summaryRow.getOrderQuantity();
                  data[counter][2] = summaryRow.getDeliveredQuantity();
           }
           data[3][0] = "Only show above items in the deliverables : YES/No (from api)";
           return data;
    };


	/**
	 * The first section of the excel having order details used in both parts and
	 * packaging.
	 * @param orderDetailsModel order detail model
	 * 
	 * @return the first section of the excel having order details
	 */
	private String[] getOrderDetailsSection(OrderDetailsModel orderDetailsModel, OrderDetails orderDetails) {
		String[] orderDetailsSection = new String [10];
		orderDetailsSection[0] = orderDetails.getOrderNumber();
		orderDetailsSection[1] = orderDetails.getStatus();
		orderDetailsSection[2] = orderDetails.getCustomerName();
		orderDetailsSection[3] = orderDetails.getCustomerNumber().toString();
		orderDetailsSection[4] = orderDetails.getPurchaseOrderNumber().toString();
		orderDetailsSection[5] = orderDetails.getCustomerReference().toString();
		orderDetailsSection[6] = orderDetails.getPlacedOn();
		orderDetailsSection[7] = orderDetails.getWebRefID().toString();		
		return orderDetailsSection;
	};

	/**
	 * 
	 * Only in packaging material excel
	 * 
	 * @return OrderSummary 2D array
	 */
	/*private String[][] getOrderSummary(List<OrderSummary> orderSummary) {
		return null;
	};
*/
	/**
	 * 
	 * Used in both parts and packaging material excel
	 * 
	 * @return DeliveryList 2D array
	 */
	private String[][] getDeliveryList(List<DeliveryList> deliveryList) {

		String[][] productDetailsList = getProductTable(deliveryList);

		return productDetailsList;
	}


	/**
	 * 
	 * List of Product details table
	 * 
	 * @param deliveryList
	 * @return
	 */
	private String[][] getProductTable(List<DeliveryList> deliveryList) {
		Iterator<DeliveryList> deliveryListIterator = deliveryList.iterator();
		while(deliveryListIterator.hasNext()) {
			DeliveryList deliveryItem = deliveryListIterator.next();
			String[] deliveryDetails = getDeliveryDetails(deliveryItem);
		}
		return null;
	}

	/**
	 * 
	 * All the delivery details
	 * 
	 * @param deliveryList
	 * @return
	 */
	private String[] getDeliveryDetails(DeliveryList deliveryList) {
		// TODO Auto-generated method stub
		return null;
	};

}
