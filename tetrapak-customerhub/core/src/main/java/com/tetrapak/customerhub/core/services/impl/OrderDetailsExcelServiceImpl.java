
package com.tetrapak.customerhub.core.services.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.lang.reflect.Field;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.customerhub.core.beans.excel.ExcelFileData;
import com.tetrapak.customerhub.core.beans.oderdetails.DeliveryList;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetails;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetailsData;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderSummary;
import com.tetrapak.customerhub.core.beans.oderdetails.Product;
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

	@Override
	public void generateOrderDetailsExcel(SlingHttpServletResponse response, String orderType,
			OrderDetailsData orderDetailData, OrderDetailsModel orderDetailsModel) {
		String[][] data = null;
		data = getOrderDetailsSection(orderDetailsModel, orderDetailData.getOrderDetails());

		if (Objects.nonNull(orderDetailData.getOrderSummary())) {
			data = ArrayUtils.addAll(data, getOrderSummary(orderDetailData.getOrderSummary()));
		}

		if (Objects.nonNull(orderDetailData.getDeliveryList())) {
			data = ArrayUtils.addAll(data, getDeliverySection(orderDetailsModel, orderDetailData.getDeliveryList()));
		}

		ExcelFileData excelReportData = new ExcelFileData();
		excelReportData
				.setFileName(orderType+" Order Details_Excel_" + orderDetailData.getOrderDetails().getOrderNumber());
		excelReportData.setExcelSheetName("Order Details");
		excelReportData.setData(data);
		ExcelUtil.generateExcelReport(response, excelReportData);
	}

	/**
	 * The first section of the excel having order details used in both parts and
	 * packaging.
	 * 
	 * @param orderDetailsModel order detail model
	 * 
	 * @return the first section of the excel having order details
	 */
	private String[][] getOrderDetailsSection(OrderDetailsModel orderDetailsModel, OrderDetails orderDetails) {
		Font bold = ExcelUtil.getFont();
		bold.setBold(true);
		String[][] orderDetailsSection = new String[9][10];
		orderDetailsSection[0][0] = "Tetra Pak Order Number:" + orderDetails.getOrderNumber();
		orderDetailsSection[1][0] = "Status: " + orderDetails.getStatus();
		orderDetailsSection[2][0] = "Customer Name: " + orderDetails.getCustomerName();
		orderDetailsSection[3][0] = "Customer Number" + orderDetails.getCustomerNumber();
		orderDetailsSection[4][0] = "Purchasse Order: " + orderDetails.getPurchaseOrderNumber();
		orderDetailsSection[5][0] = "Customer Reference: " + orderDetails.getCustomerReference().toString();
		orderDetailsSection[6][0] = "Order Date" + orderDetails.getPlacedOn();
		orderDetailsSection[7][0] = "Web Refrence " + orderDetails.getWebRefID();

		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 10; col++) {
				if (col != 0) {
					orderDetailsSection[row][col] = StringUtils.EMPTY;
				}
			}
		}

		return orderDetailsSection;
	}

	/**
	 * Order Summary section in the excel Only in packaging material excel
	 * 
	 * @return OrderSummary 2 d array
	 */
	private String[][] getOrderSummary(List<OrderSummary> orderSummary) {
		String[][] data = new String[4][4];
		data[0][0] = StringUtils.EMPTY;
		data[0][1] = "Product<b>";
		data[0][2] = "Order Quantity<b>";
		data[0][3] = "Quantity Delivered so far<b>";
		int counter = 1;
		Iterator<OrderSummary> itr = orderSummary.iterator();
		while (itr.hasNext()) {
			OrderSummary summaryRow = itr.next();
			data[counter][1] = summaryRow.getProduct();
			data[counter][2] = summaryRow.getOrderQuantity();
			data[counter][3] = summaryRow.getDeliveredQuantity();
			counter++;
		}
		data[3][0] = "Only show above items in the deliverables : YES/NO (from api)";
		return data;
	}

	/**
	 * 
	 * Used in both parts and packaging material excel
	 * 
	 * @param DeliveryList 2D array
	 * @return
	 */
	private String[][] getDeliverySection(OrderDetailsModel orderDetailsModel, List<DeliveryList> deliveryList) {

		String[][] deliveryDetails = null;
		Iterator<DeliveryList> deliveryListIterator = deliveryList.iterator();
		while (deliveryListIterator.hasNext()) {
			DeliveryList deliveryItem = deliveryListIterator.next();
			deliveryDetails = ArrayUtils.addAll(deliveryDetails, getDeliveryDetails(orderDetailsModel, deliveryItem));
		}
		return deliveryDetails;
	}

	/**
	 * 
	 * All the delivery details
	 * 
	 * @param deliveryList
	 * @return
	 */
	private String[][] getDeliveryDetails(OrderDetailsModel orderDetailsModel, DeliveryList deliveryList) {
		String[][] deliveryDetails = new String[9][10];

		deliveryDetails[0][0] = "Delivery number: " + deliveryList.getDeliveryOrder();
		deliveryDetails[1][0] = "Shipping: " + deliveryList.getCarrier();
		deliveryDetails[2][0] = "Track Order: " + deliveryList.getCarrierTrackingID();
		deliveryDetails[3][0] = "Delivery Address<b>";
		deliveryDetails[4][0] = deliveryList.getDeliveryAddress().getName() + " "
				+ deliveryList.getDeliveryAddress().getName2() + ", " + deliveryList.getDeliveryAddress().getCity()
				+ ", " + deliveryList.getDeliveryAddress().getState() + ", "
				+ deliveryList.getDeliveryAddress().getPostalcode() + deliveryList.getDeliveryAddress().getCountry();
		deliveryDetails[5][0] = StringUtils.EMPTY;
		deliveryDetails[6][0] = "Invoice Address<b>";
		deliveryDetails[7][0] = deliveryList.getInvoiceAddress().getName() + " "
				+ deliveryList.getInvoiceAddress().getName2() + ", " + deliveryList.getInvoiceAddress().getCity()
				+ ", " + deliveryList.getInvoiceAddress().getState() + ", "
				+ deliveryList.getInvoiceAddress().getPostalcode() + deliveryList.getInvoiceAddress().getCountry();
		deliveryDetails[8][0] = StringUtils.EMPTY;

		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 10; col++) {
				if (col != 0) {
					deliveryDetails[row][col] = StringUtils.EMPTY;
				}
			}
		}

		String[][] productDetails = getProductDetails(orderDetailsModel, deliveryList);

		deliveryDetails = ArrayUtils.addAll(deliveryDetails, productDetails);
		return deliveryDetails;
	}

	private String[][] getProductDetails(OrderDetailsModel orderDetailsModel, DeliveryList deliveryList) {
		List<Product> products = deliveryList.getProducts();
		String[][] productDetails = null;
		if (null != products) {
			// int columns = Product.class.getDeclaredFields().length;
			int rows = products.size();
			productDetails = new String[rows + 5][10];
			productDetails[0][0] = "#<w>";
			productDetails[0][1] = "Product<w>";
			productDetails[0][2] = "Product ID<w>";
			productDetails[0][3] = "Quantity<w>";
			productDetails[0][4] = "Weight<w>";
			productDetails[0][5] = "Sent<w>";
			productDetails[0][6] = "Open<w>";
			productDetails[0][7] = "ETA<w>";
			productDetails[0][8] = "Unit Price<w>";
			productDetails[0][9] = "Price<w>";

			Iterator<Product> productsIterator = products.iterator();
			int counter = 1;
			while (productsIterator.hasNext()) {
				Product product = productsIterator.next();
				productDetails[counter][0] = Integer.toString(counter);
				productDetails[counter][1] = product.getProductName();
				productDetails[counter][2] = product.getProductID();
				productDetails[counter][3] = product.getOrderQuantity();
				productDetails[counter][4] = product.getWeight();
				productDetails[counter][5] = product.getDeliveredQuantity();
				productDetails[counter][6] = product.getRemainingQuantity();
				productDetails[counter][7] = product.getETA();
				productDetails[counter][8] = product.getUnitPrice();
				productDetails[counter][9] = product.getPrice();

				productDetails[counter + 1][8] = "Total Weight<b>";
				productDetails[counter + 1][9] = deliveryList.getTotalWeight();
				productDetails[counter + 2][8] = "Total Pre VAT<b>";
				productDetails[counter + 2][9] = deliveryList.getTotalPricePreVAT();
				productDetails[counter + 3][8] = "VAT<b>";
				productDetails[counter + 3][9] = deliveryList.getTotalVAT();

				counter++;
			}
		}
		return productDetails;
	}

	private int getActualFieldsCount(Object obj) {
		for (Field field : obj.getClass().getDeclaredFields()) {
			try {
				obj.getClass().getField(field.getName());
			} catch (NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

}
