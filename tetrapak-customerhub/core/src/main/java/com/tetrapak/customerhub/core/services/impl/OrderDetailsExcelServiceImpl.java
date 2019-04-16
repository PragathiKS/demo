
package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.beans.oderdetails.DeliveryList;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetails;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetailsData;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderSummary;
import com.tetrapak.customerhub.core.models.OrderDetailsModel;
import com.tetrapak.customerhub.core.services.OrderDetailsExcelService;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * Implemention class for Odrder Details Excel Service
 *
 * @author Tushar
 */
@Component(immediate = true, service = OrderDetailsExcelService.class)
public class OrderDetailsExcelServiceImpl implements OrderDetailsExcelService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderDetailsExcelServiceImpl.class);

	@Override
	public void generateOrderDetailsExcel(SlingHttpServletResponse response, String orderType,
			OrderDetailsData orderDetailData, OrderDetailsModel orderDetailsModel) {
		getOrderDetailsSection(orderDetailsModel, orderDetailData.getOrderDetails());
		getOrderSummary(orderDetailData.getOrderSummary());
		getDeliveryList(orderDetailData.getDeliveryList());

	}

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
	private String[][] getOrderSummary(List<OrderSummary> orderSummary) {
		return null;
	};

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
