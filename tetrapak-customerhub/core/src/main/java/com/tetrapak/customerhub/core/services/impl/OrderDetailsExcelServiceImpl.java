
package com.tetrapak.customerhub.core.services.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;
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
import com.tetrapak.customerhub.core.beans.oderdetails.Product;
import com.tetrapak.customerhub.core.models.OrderDetailsModel;
import com.tetrapak.customerhub.core.services.OrderDetailsExcelService;
import com.tetrapak.customerhub.core.utils.ExcelUtil;

/**
 * Implemention class for Order Details Excel Service
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
			data = ArrayUtils.addAll(data,
					getDeliverySection(orderType, orderDetailsModel, orderDetailData.getDeliveryList()));
		}

		ExcelFileData excelReportData = new ExcelFileData();
		excelReportData
				.setFileName(orderType + " Order Details_Excel_" + orderDetailData.getOrderDetails().getOrderNumber());
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
		String[][] orderDetailsSection = new String[11][10];
		orderDetailsSection[0][0] = ExcelUtil.HALF_BOLD_TAG + "Tetra Pak Order Number:"
				+ getProcessedValue(orderDetails.getOrderNumber());
		orderDetailsSection[1][0] = ExcelUtil.HALF_BOLD_TAG + "Status: " + getProcessedValue(orderDetails.getStatus());
		orderDetailsSection[2][0] = StringUtils.EMPTY + ExcelUtil.REGULAR_STYLE_TAG;
		orderDetailsSection[3][0] = ExcelUtil.BOLD_TAG + "Order Details<lightGreyBG>";
		orderDetailsSection[4][0] = ExcelUtil.HALF_BOLD_TAG + "Customer Name: "
				+ getProcessedValue(orderDetails.getCustomerName());
		orderDetailsSection[5][0] = ExcelUtil.HALF_BOLD_TAG + "Customer Number: "
				+ getProcessedValue(orderDetails.getCustomerNumber());
		orderDetailsSection[6][0] = ExcelUtil.HALF_BOLD_TAG + "Purchase Order: "
				+ getProcessedValue(orderDetails.getPurchaseOrderNumber());
		orderDetailsSection[7][0] = ExcelUtil.HALF_BOLD_TAG + "Customer Reference: "
				+ getProcessedValue(orderDetails.getCustomerReference());
		orderDetailsSection[8][0] = ExcelUtil.HALF_BOLD_TAG + "Order Date : "
				+ getProcessedValue(orderDetails.getPlacedOn());
		orderDetailsSection[9][0] = ExcelUtil.HALF_BOLD_TAG + "Web Reference :"
				+ getProcessedValue(orderDetails.getWebRefID());
		orderDetailsSection[10][0] = StringUtils.EMPTY + ExcelUtil.REGULAR_STYLE_TAG;

		return orderDetailsSection;
	}

	/**
	 * Order Summary section in the excel Only in packaging material excel
	 * 
	 * @return OrderSummary 2 d array
	 */
	private String[][] getOrderSummary(List<OrderSummary> orderSummary) {
		String[][] data = new String[5][4];
		data[0][0] = StringUtils.EMPTY + ExcelUtil.REGULAR_STYLE_TAG;
		data[0][1] = ExcelUtil.BOLD_TAG + "Product<aligncenter>";
		data[0][2] = ExcelUtil.BOLD_TAG + "Order Quantity<aligncenter>";
		data[0][3] = ExcelUtil.BOLD_TAG + "Quantity Delivered so far<aligncenter>";
		int counter = 1;
		Iterator<OrderSummary> itr = orderSummary.iterator();
		while (itr.hasNext()) {
			OrderSummary summaryRow = itr.next();
			data[counter][1] = StringUtils.EMPTY + ExcelUtil.REGULAR_STYLE_TAG;
			data[counter][1] = getProcessedValue(summaryRow.getProduct());
			data[counter][2] = getProcessedValue(summaryRow.getOrderQuantity());
			data[counter][3] = getProcessedValue(summaryRow.getDeliveredQuantity());
			counter++;
		}
		data[3][0] = ExcelUtil.HALF_BOLD_TAG + "Only show above items in the deliverables : YES/NO (from api)";
		data[4][0] = StringUtils.EMPTY + ExcelUtil.REGULAR_STYLE_TAG;
		return data;
	}

	/**
	 * 
	 * Used in both parts and packaging material excel
	 * 
	 * @param orderType
	 * 
	 * @param DeliveryList 2D array
	 * @return
	 */
	private String[][] getDeliverySection(String orderType, OrderDetailsModel orderDetailsModel,
			List<DeliveryList> deliveryList) {

		String[][] deliveryDetails = null;
		Iterator<DeliveryList> deliveryListIterator = deliveryList.iterator();
		while (deliveryListIterator.hasNext()) {
			DeliveryList deliveryItem = deliveryListIterator.next();
			deliveryDetails = ArrayUtils.addAll(deliveryDetails,
					getDeliveryDetails(orderType, orderDetailsModel, deliveryItem));
		}
		return deliveryDetails;
	}

	/**
	 * 
	 * All the delivery details
	 * 
	 * @param orderType
	 * 
	 * @param deliveryList
	 * @return
	 */
	private String[][] getDeliveryDetails(String orderType, OrderDetailsModel orderDetailsModel,
			DeliveryList deliveryList) {
		String[][] deliveryDetailsSection1 = getDeliveryDetailsInitialSection(orderType, deliveryList);
		String[][] productDetails = getProductDetails(orderType, orderDetailsModel, deliveryList);

		String[][] deliveryDetails = ArrayUtils.addAll(deliveryDetailsSection1, productDetails);
		return deliveryDetails;
	}

	/**
	 * DeliveryDetails data based on the orderType
	 * 
	 * @param orderType
	 * @param deliveryList
	 * @return
	 */
	private String[][] getDeliveryDetailsInitialSection(String orderType, DeliveryList deliveryList) {
		String[][] deliveryDetailsSection;
		if (orderType.equalsIgnoreCase("parts")) {
			deliveryDetailsSection = new String[9][10];
			deliveryDetailsSection[0][0] = ExcelUtil.BOLD_TAG + ExcelUtil.MERGE_ROW_TAG
					+ "<lightGreyBG>Delivery number: " + getProcessedValue(deliveryList.getDeliveryOrder());
			deliveryDetailsSection[1][0] = ExcelUtil.HALF_BOLD_TAG + "Shipping: "
					+ getProcessedValue(deliveryList.getCarrier());
			deliveryDetailsSection[2][0] = ExcelUtil.HALF_BOLD_TAG + "Track Order: "
					+ getProcessedValue(deliveryList.getCarrierTrackingID());
			deliveryDetailsSection[3][0] = ExcelUtil.BOLD_TAG + "Delivery Address";
			deliveryDetailsSection[4][0] = getProcessedValue(deliveryList.getDeliveryAddress().getName()) + " "
					+ getProcessedValue(deliveryList.getDeliveryAddress().getName2()) + "\n"
					+ getProcessedValue(deliveryList.getDeliveryAddress().getCity()) + "\n"
					+ getProcessedValue(deliveryList.getDeliveryAddress().getState()) + " , "
					+ getProcessedValue(deliveryList.getDeliveryAddress().getPostalcode()) + " "
					+ getProcessedValue(deliveryList.getDeliveryAddress().getCountry());
			deliveryDetailsSection[5][0] = StringUtils.EMPTY + ExcelUtil.REGULAR_STYLE_TAG;
			deliveryDetailsSection[6][0] = ExcelUtil.BOLD_TAG + "Invoice Address";
			deliveryDetailsSection[7][0] = getProcessedValue(deliveryList.getInvoiceAddress().getName()) + " "
					+ deliveryList.getInvoiceAddress().getName2() + "\n"
					+ getProcessedValue(deliveryList.getInvoiceAddress().getCity()) + "\n"
					+ deliveryList.getInvoiceAddress().getState() + " , "
					+ getProcessedValue(deliveryList.getInvoiceAddress().getPostalcode()) + "  "
					+ getProcessedValue((deliveryList.getInvoiceAddress().getCountry()));
			deliveryDetailsSection[8][0] = StringUtils.EMPTY + ExcelUtil.REGULAR_STYLE_TAG;
		} else {
			deliveryDetailsSection = new String[11][10];
			deliveryDetailsSection[0][0] = ExcelUtil.BOLD_TAG + ExcelUtil.MERGE_ROW_TAG
					+ "<lightGreyBG>Delivery number: " + getProcessedValue(deliveryList.getDeliveryOrder()) + "-Status";
			deliveryDetailsSection[1][0] = ExcelUtil.HALF_BOLD_TAG + "Delivery Order: "
					+ getProcessedValue(deliveryList.getDeliveryOrder());
			deliveryDetailsSection[2][0] = ExcelUtil.HALF_BOLD_TAG + "Production Place: "
					+ getProcessedValue(deliveryList.getProductPlace());
			deliveryDetailsSection[3][0] = ExcelUtil.HALF_BOLD_TAG + "Requested: "
					+ getProcessedValue(deliveryList.getDeliveryOrder());
			deliveryDetailsSection[4][0] = ExcelUtil.HALF_BOLD_TAG + "ETD: "
					+ getProcessedValue(deliveryList.getDeliveryOrder());
			deliveryDetailsSection[5][0] = ExcelUtil.BOLD_TAG + "Delivery Address";
			deliveryDetailsSection[6][0] = getProcessedValue(deliveryList.getDeliveryAddress().getName()) + " "
					+ getProcessedValue(deliveryList.getDeliveryAddress().getName2()) + "\n"
					+ getProcessedValue(deliveryList.getDeliveryAddress().getCity()) + "\n"
					+ getProcessedValue(deliveryList.getDeliveryAddress().getState()) + " , "
					+ getProcessedValue(deliveryList.getDeliveryAddress().getPostalcode()) + " "
					+ getProcessedValue(deliveryList.getDeliveryAddress().getCountry());
			deliveryDetailsSection[7][0] = StringUtils.EMPTY + ExcelUtil.REGULAR_STYLE_TAG;
			deliveryDetailsSection[8][0] = ExcelUtil.BOLD_TAG + "Invoice Address";
			deliveryDetailsSection[9][0] = getProcessedValue(deliveryList.getInvoiceAddress().getName()) + " "
					+ deliveryList.getInvoiceAddress().getName2() + "\n"
					+ getProcessedValue(deliveryList.getInvoiceAddress().getCity()) + "\n"
					+ deliveryList.getInvoiceAddress().getState() + " , "
					+ getProcessedValue(deliveryList.getInvoiceAddress().getPostalcode()) + "  "
					+ getProcessedValue((deliveryList.getInvoiceAddress().getCountry()));
			deliveryDetailsSection[10][0] = StringUtils.EMPTY + ExcelUtil.REGULAR_STYLE_TAG;
		}
		return deliveryDetailsSection;
	}

	private String[][] getProductDetails(String orderType, OrderDetailsModel orderDetailsModel,
			DeliveryList deliveryList) {
		List<Product> products = deliveryList.getProducts();
		String[][] productDetails = null;
		if (null != products) {
			int rows = products.size();
			productDetails = new String[rows + 5][10];
			productDetails[0][0] = "#<darkGreyBG>";
			productDetails[0][1] = "Product<darkGreyBG>";
			productDetails[0][2] = "Product ID<darkGreyBG>";
			productDetails[0][3] = "Quantity<darkGreyBG>";
			productDetails[0][4] = "Weight<darkGreyBG>";
			productDetails[0][5] = "Sent<darkGreyBG>";
			productDetails[0][6] = "Open<darkGreyBG>";
			productDetails[0][7] = "ETA<darkGreyBG>";
			productDetails[0][8] = "Unit Price<darkGreyBG>";
			productDetails[0][9] = "Price<darkGreyBG>";

			Iterator<Product> productsIterator = products.iterator();
			int counter = 1;
			while (productsIterator.hasNext()) {
				Product product = productsIterator.next();
				productDetails[counter][0] = "<aligncenter>" + Integer.toString(counter);
				productDetails[counter][1] = "<aligncenter>" + getProcessedValue(product.getProductName());
				productDetails[counter][2] = "<aligncenter>" + getProcessedValue(product.getProductID());
				productDetails[counter][3] = "<aligncenter>" + getProcessedValue(product.getOrderQuantity());
				productDetails[counter][4] = "<aligncenter>" + getProcessedValue(product.getWeight());
				productDetails[counter][5] = "<aligncenter>" + getProcessedValue(product.getDeliveredQuantity());
				productDetails[counter][6] = "<aligncenter>" + getProcessedValue(product.getRemainingQuantity());
				productDetails[counter][7] = "<aligncenter>" + getProcessedValue(product.getETA());
				productDetails[counter][8] = "<aligncenter>" + getProcessedValue(product.getUnitPrice());
				productDetails[counter][9] = "<aligncenter>" + getProcessedValue(product.getPrice());
				if (!orderType.equalsIgnoreCase("packmat")) {
					productDetails[counter + 1][8] = ExcelUtil.BOLD_TAG + "<aligncenter>Total Weight";
					productDetails[counter + 1][9] = "<aligncenter>" + getProcessedValue(deliveryList.getTotalWeight());
					productDetails[counter + 2][8] = ExcelUtil.BOLD_TAG + "<aligncenter>Total Pre VAT";
					productDetails[counter + 2][9] = "<aligncenter>"
							+ getProcessedValue(deliveryList.getTotalPricePreVAT());
					productDetails[counter + 3][8] = ExcelUtil.BOLD_TAG + "<aligncenter>VAT";
					productDetails[counter + 3][9] = "<aligncenter>" + getProcessedValue(deliveryList.getTotalVAT());
				}
				counter++;
			}

			while (counter < (rows + 5)) {
				for (int col = 0; col < 10; col++) {
					if (StringUtils.isBlank(productDetails[counter][col])) {
						productDetails[counter][col] = ExcelUtil.REGULAR_STYLE_TAG;
					}
				}
				counter++;
			}

		}
		return productDetails;
	}

	/**
	 * 
	 * if the string parameter is null then return empty string otherwise the
	 * original string
	 * 
	 * @param rawData
	 * @return
	 */
	private String getProcessedValue(String rawData) {
		String processedData = StringUtils.EMPTY;
		if (!StringUtils.isBlank(rawData)) {
			processedData = rawData;
		}
		return processedData + ExcelUtil.REGULAR_STYLE_TAG;
	}

	/**
	 * 
	 * if the integer parameter is null then return empty string otherwise the
	 * original integer as string
	 * 
	 * @param rawData
	 * @return
	 */
	private String getProcessedValue(Integer rawData) {
		String processedData = StringUtils.EMPTY;
		if (null != rawData) {
			processedData = rawData.toString();
		}
		return processedData + ExcelUtil.REGULAR_STYLE_TAG;
	}

}
