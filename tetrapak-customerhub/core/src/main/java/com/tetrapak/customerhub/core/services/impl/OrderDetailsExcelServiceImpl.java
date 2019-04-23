
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
import com.tetrapak.customerhub.core.beans.oderdetails.DeliveryAddress;
import com.tetrapak.customerhub.core.beans.oderdetails.DeliveryList;
import com.tetrapak.customerhub.core.beans.oderdetails.InvoiceAddress;
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
 * @author swalamba
 *
 */
@Component(immediate = true, service = OrderDetailsExcelService.class)
public class OrderDetailsExcelServiceImpl implements OrderDetailsExcelService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderDetailsExcelServiceImpl.class);
	private static final String SHEET_NAME = "Order Details";
	private static final String PARTS_ORDER_TYPE = "parts";
	private static final String PACKMAT_ORDER_TYPE = "packmat";

	@Override
	public void generateOrderDetailsExcel(SlingHttpServletResponse response, String orderType,
			OrderDetailsData orderDetailData, OrderDetailsModel orderDetailsModel) {
		if (Objects.nonNull(orderDetailData) && !StringUtils.isBlank(orderType)) {
			String[][] data = null;
			data = getOrderDetailsSection(orderDetailsModel, orderDetailData.getOrderDetails());
			LOGGER.debug("Order Details Section data added to the array..");
			if (Objects.nonNull(orderDetailData.getOrderSummary())) {
				data = ArrayUtils.addAll(data, getOrderSummary(orderDetailData.getOrderSummary()));
				LOGGER.debug("Order Summary data added to the array..");
			}

			if (Objects.nonNull(orderDetailData.getDeliveryList())) {
				data = ArrayUtils.addAll(data,
						getDeliverySection(orderType, orderDetailsModel, orderDetailData.getDeliveryList()));
				LOGGER.debug("Delivery Section data added to the array..");
			}
			LOGGER.debug("Raw Data for the Excel sheet has been formed having rows: {} and columns: {}", data.length,
					data[0].length);
			ExcelFileData excelReportData = new ExcelFileData();
			excelReportData.setFileName(orderDetailData.getOrderDetails().getOrderNumber());
			excelReportData.setExcelSheetName(SHEET_NAME);
			excelReportData.setData(data);
			ExcelUtil.generateExcelReport(response, excelReportData);
		} else {
			LOGGER.error("Order Details Data is null so can not process the excel creation!");
		}
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
		orderDetailsSection[0][0] = addTagToContent(
				"Tetra Pak Order Number:" + getProcessedValue(orderDetails.getOrderNumber()),
				new String[] { ExcelUtil.HALF_BOLD_TAG });
		orderDetailsSection[1][0] = addTagToContent("Status: " + getProcessedValue(orderDetails.getStatus()),
				new String[] { ExcelUtil.HALF_BOLD_TAG });
		orderDetailsSection[2][0] = addTagToContent(StringUtils.EMPTY, new String[] { ExcelUtil.REGULAR_STYLE_TAG });
		orderDetailsSection[3][0] = addTagToContent("Order Details",
				new String[] { ExcelUtil.BOLD_TAG, ExcelUtil.LIGHT_GREY_BG_TAG });
		orderDetailsSection[4][0] = addTagToContent(
				"Customer Name: " + getProcessedValue(orderDetails.getCustomerName()),
				new String[] { ExcelUtil.HALF_BOLD_TAG });
		orderDetailsSection[5][0] = addTagToContent(
				"Customer Number: " + getProcessedValue(orderDetails.getCustomerNumber()),
				new String[] { ExcelUtil.HALF_BOLD_TAG });
		orderDetailsSection[6][0] = addTagToContent(
				"Purchase Order: " + getProcessedValue(orderDetails.getPurchaseOrderNumber()),
				new String[] { ExcelUtil.HALF_BOLD_TAG });
		orderDetailsSection[7][0] = addTagToContent(
				"Customer Reference: " + getProcessedValue(orderDetails.getCustomerReference()),
				new String[] { ExcelUtil.HALF_BOLD_TAG });
		orderDetailsSection[8][0] = addTagToContent("Order Date : " + getProcessedValue(orderDetails.getPlacedOn()),
				new String[] { ExcelUtil.HALF_BOLD_TAG });
		orderDetailsSection[9][0] = addTagToContent("Web Reference :" + getProcessedValue(orderDetails.getWebRefID()),
				new String[] { ExcelUtil.HALF_BOLD_TAG });
		orderDetailsSection[10][0] = addTagToContent(StringUtils.EMPTY, new String[] { ExcelUtil.REGULAR_STYLE_TAG });

		return orderDetailsSection;
	}

	/**
	 * Order Summary section in the excel Only in packaging material excel
	 * 
	 * @return OrderSummary 2 d array
	 */
	private String[][] getOrderSummary(List<OrderSummary> orderSummary) {
		String[][] data = new String[5][4];
		data[0][0] = addTagToContent(StringUtils.EMPTY, new String[] { ExcelUtil.REGULAR_STYLE_TAG });
		data[0][1] = addTagToContent("Product", new String[] { ExcelUtil.BOLD_TAG, ExcelUtil.ALIGN_CENTER_TAG });
		data[0][2] = addTagToContent("Order Quantity", new String[] { ExcelUtil.BOLD_TAG, ExcelUtil.ALIGN_CENTER_TAG });
		data[0][3] = addTagToContent("Quantity Delivered so far",
				new String[] { ExcelUtil.BOLD_TAG, ExcelUtil.ALIGN_CENTER_TAG });
		int counter = 1;
		Iterator<OrderSummary> itr = orderSummary.iterator();
		while (itr.hasNext()) {
			OrderSummary summaryRow = itr.next();
			data[counter][1] = addTagToContent(StringUtils.EMPTY, new String[] { ExcelUtil.REGULAR_STYLE_TAG });
			data[counter][1] = getProcessedValue(summaryRow.getProduct());
			data[counter][2] = getProcessedValue(summaryRow.getOrderQuantity());
			data[counter][3] = getProcessedValue(summaryRow.getDeliveredQuantity());
			counter++;
		}
		data[3][0] = addTagToContent("Only show above items in the deliverables : YES/NO (from api)",
				new String[] { ExcelUtil.HALF_BOLD_TAG });
		data[4][0] = addTagToContent(StringUtils.EMPTY, new String[] { ExcelUtil.REGULAR_STYLE_TAG });
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

		String[][] deliveryDetailsList = null;
		Iterator<DeliveryList> deliveryListIterator = deliveryList.iterator();
		while (deliveryListIterator.hasNext()) {
			DeliveryList deliveryItem = deliveryListIterator.next();
			deliveryDetailsList = ArrayUtils.addAll(deliveryDetailsList,
					getDeliveryDetails(orderType, orderDetailsModel, deliveryItem));
		}
		return deliveryDetailsList;
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

		return ArrayUtils.addAll(deliveryDetailsSection1, productDetails);
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
		if (orderType.equalsIgnoreCase(PARTS_ORDER_TYPE)) {
			deliveryDetailsSection = new String[9][10];
			deliveryDetailsSection[0][0] = addTagToContent(
					"Delivery number: " + getProcessedValue(deliveryList.getDeliveryOrder()),
					new String[] { ExcelUtil.BOLD_TAG, ExcelUtil.MERGE_ROW_TAG, ExcelUtil.LIGHT_GREY_BG_TAG });
			deliveryDetailsSection[1][0] = addTagToContent("Shipping: " + getProcessedValue(deliveryList.getCarrier()),
					new String[] { ExcelUtil.HALF_BOLD_TAG });
			deliveryDetailsSection[2][0] = addTagToContent(
					"Track Order: " + getProcessedValue(deliveryList.getCarrierTrackingID()),
					new String[] { ExcelUtil.HALF_BOLD_TAG });
			deliveryDetailsSection[3][0] = addTagToContent("Delivery Address", new String[] { ExcelUtil.BOLD_TAG });
			deliveryDetailsSection[4][0] = getDeliveryAddress(deliveryList.getDeliveryAddress());
			deliveryDetailsSection[5][0] = addTagToContent(StringUtils.EMPTY,
					new String[] { ExcelUtil.REGULAR_STYLE_TAG });
			deliveryDetailsSection[6][0] = addTagToContent("Invoice Address", new String[] { ExcelUtil.BOLD_TAG });
			deliveryDetailsSection[7][0] = getInvoiceAddress(deliveryList.getInvoiceAddress());
			deliveryDetailsSection[8][0] = addTagToContent(StringUtils.EMPTY,
					new String[] { ExcelUtil.REGULAR_STYLE_TAG });
		} else {
			deliveryDetailsSection = new String[11][10];
			deliveryDetailsSection[0][0] = addTagToContent(
					"Delivery number: " + getProcessedValue(deliveryList.getDeliveryOrder()) + "-Status",
					new String[] { ExcelUtil.BOLD_TAG, ExcelUtil.MERGE_ROW_TAG, ExcelUtil.LIGHT_GREY_BG_TAG });
			deliveryDetailsSection[1][0] = addTagToContent(
					"Delivery Order: " + getProcessedValue(deliveryList.getDeliveryOrder()),
					new String[] { ExcelUtil.HALF_BOLD_TAG });
			deliveryDetailsSection[2][0] = addTagToContent(
					"Production Place: " + getProcessedValue(deliveryList.getProductPlace()),
					new String[] { ExcelUtil.HALF_BOLD_TAG });
			deliveryDetailsSection[3][0] = addTagToContent(
					"Requested: " + getProcessedValue(deliveryList.getDeliveryOrder()),
					new String[] { ExcelUtil.HALF_BOLD_TAG });
			deliveryDetailsSection[4][0] = addTagToContent("ETD: " + getProcessedValue(deliveryList.getDeliveryOrder()),
					new String[] { ExcelUtil.HALF_BOLD_TAG });
			deliveryDetailsSection[5][0] = addTagToContent("Delivery Address", new String[] { ExcelUtil.BOLD_TAG });
			deliveryDetailsSection[6][0] = getDeliveryAddress(deliveryList.getDeliveryAddress());
			deliveryDetailsSection[7][0] = addTagToContent(StringUtils.EMPTY,
					new String[] { ExcelUtil.REGULAR_STYLE_TAG });
			deliveryDetailsSection[8][0] = addTagToContent("Invoice Address", new String[] { ExcelUtil.BOLD_TAG });
			deliveryDetailsSection[9][0] = getInvoiceAddress(deliveryList.getInvoiceAddress());
			deliveryDetailsSection[10][0] = addTagToContent(StringUtils.EMPTY,
					new String[] { ExcelUtil.REGULAR_STYLE_TAG });
		}
		return deliveryDetailsSection;
	}

	/**
	 * @param invoiceAddress
	 * @return
	 */
	private String getInvoiceAddress(InvoiceAddress invoiceAddress) {
		return getProcessedValue(invoiceAddress.getName()) + " " + invoiceAddress.getName2() + "\n"
				+ getProcessedValue(invoiceAddress.getCity()) + "\n" + invoiceAddress.getState() + " , "
				+ getProcessedValue(invoiceAddress.getPostalcode()) + "  "
				+ getProcessedValue((invoiceAddress.getCountry()));
	}

	/**
	 * @param deliveryAddress
	 * @return
	 */
	private String getDeliveryAddress(DeliveryAddress deliveryAddress) {
		return getProcessedValue(deliveryAddress.getName()) + " " + getProcessedValue(deliveryAddress.getName2())
				+ ExcelUtil.NEW_LINE_DETECTOR + getProcessedValue(deliveryAddress.getCity())
				+ ExcelUtil.NEW_LINE_DETECTOR + getProcessedValue(deliveryAddress.getState()) + " , "
				+ getProcessedValue(deliveryAddress.getPostalcode()) + " "
				+ getProcessedValue(deliveryAddress.getCountry());
	}

	/**
	 * Appending the tags to the field values so that it can be processed for the
	 * styling
	 * 
	 * @param rawData
	 * @param tags
	 * @return
	 */
	private String addTagToContent(String rawData, String[] tags) {
		StringBuilder processedData = new StringBuilder(rawData);
		for (String tag : tags) {
			processedData.append(tag);
		}
		return processedData.toString();
	}

	/**
	 * ProductDetails data in the 2d string array based on the orderType
	 * 
	 * @param orderType
	 * @param orderDetailsModel
	 * @param deliveryList
	 * @return
	 */
	private String[][] getProductDetails(String orderType, OrderDetailsModel orderDetailsModel,
			DeliveryList deliveryList) {
		List<Product> products = deliveryList.getProducts();
		String[][] productDetails = null;
		if (null != products) {
			int rows = products.size();
			productDetails = new String[rows + 5][10];
			productDetails[0][0] = addTagToContent("#", new String[] { ExcelUtil.DARK_GREY_BG_TAG });
			productDetails[0][1] = addTagToContent("Product", new String[] { ExcelUtil.DARK_GREY_BG_TAG });
			productDetails[0][2] = addTagToContent("Product ID", new String[] { ExcelUtil.DARK_GREY_BG_TAG });
			productDetails[0][3] = addTagToContent("Quantity", new String[] { ExcelUtil.DARK_GREY_BG_TAG });
			productDetails[0][4] = addTagToContent("Weight", new String[] { ExcelUtil.DARK_GREY_BG_TAG });
			productDetails[0][5] = addTagToContent("Sent", new String[] { ExcelUtil.DARK_GREY_BG_TAG });
			productDetails[0][6] = addTagToContent("Open", new String[] { ExcelUtil.DARK_GREY_BG_TAG });
			productDetails[0][7] = addTagToContent("ETA", new String[] { ExcelUtil.DARK_GREY_BG_TAG });
			productDetails[0][8] = addTagToContent("Unit Price", new String[] { ExcelUtil.DARK_GREY_BG_TAG });
			productDetails[0][9] = addTagToContent("Price", new String[] { ExcelUtil.DARK_GREY_BG_TAG });

			Iterator<Product> productsIterator = products.iterator();
			int counter = 1;
			while (productsIterator.hasNext()) {
				Product product = productsIterator.next();
				productDetails[counter][0] = addTagToContent(Integer.toString(counter),
						new String[] { ExcelUtil.ALIGN_CENTER_TAG });
				productDetails[counter][1] = addTagToContent(getProcessedValue(product.getProductName()),
						new String[] { ExcelUtil.ALIGN_CENTER_TAG });
				productDetails[counter][2] = addTagToContent(getProcessedValue(product.getProductID()),
						new String[] { ExcelUtil.ALIGN_CENTER_TAG });
				productDetails[counter][3] = addTagToContent(getProcessedValue(product.getOrderQuantity()),
						new String[] { ExcelUtil.ALIGN_CENTER_TAG });
				productDetails[counter][4] = addTagToContent(getProcessedValue(product.getWeight()),
						new String[] { ExcelUtil.ALIGN_CENTER_TAG });
				productDetails[counter][5] = addTagToContent(getProcessedValue(product.getDeliveredQuantity()),
						new String[] { ExcelUtil.ALIGN_CENTER_TAG });
				productDetails[counter][6] = addTagToContent(getProcessedValue(product.getRemainingQuantity()),
						new String[] { ExcelUtil.ALIGN_CENTER_TAG });
				productDetails[counter][7] = addTagToContent(getProcessedValue(product.getETA()),
						new String[] { ExcelUtil.ALIGN_CENTER_TAG });
				productDetails[counter][8] = addTagToContent(getProcessedValue(product.getUnitPrice()),
						new String[] { ExcelUtil.ALIGN_CENTER_TAG });
				productDetails[counter][9] = addTagToContent(getProcessedValue(product.getPrice()),
						new String[] { ExcelUtil.ALIGN_CENTER_TAG });
				if (!orderType.equalsIgnoreCase(PACKMAT_ORDER_TYPE)) {
					productDetails[counter + 1][8] = addTagToContent("Total Weight",
							new String[] { ExcelUtil.BOLD_TAG, ExcelUtil.ALIGN_CENTER_TAG });
					productDetails[counter + 1][9] = addTagToContent(getProcessedValue(deliveryList.getTotalWeight()),
							new String[] { ExcelUtil.ALIGN_CENTER_TAG });
					productDetails[counter + 2][8] = addTagToContent("Total Pre VAT",
							new String[] { ExcelUtil.BOLD_TAG, ExcelUtil.ALIGN_CENTER_TAG });
					productDetails[counter + 2][9] = addTagToContent(
							getProcessedValue(deliveryList.getTotalPricePreVAT()),
							new String[] { ExcelUtil.ALIGN_CENTER_TAG });
					productDetails[counter + 3][8] = addTagToContent("VAT",
							new String[] { ExcelUtil.BOLD_TAG, ExcelUtil.ALIGN_CENTER_TAG });
					productDetails[counter + 3][9] = addTagToContent(getProcessedValue(deliveryList.getTotalVAT()),
							new String[] { ExcelUtil.ALIGN_CENTER_TAG });
				}
				counter++;
			}
			applyRegularStyleToLastFourRows(counter, rows, productDetails);
		}
		return productDetails;
	}

	/**
	 * @param counter
	 * @param rows
	 * @param productDetails
	 */
	private void applyRegularStyleToLastFourRows(int counter, int rows, String[][] productDetails) {
		while (counter < (rows + 5)) {
			for (int col = 0; col < 10; col++) {
				if (StringUtils.isBlank(productDetails[counter][col])) {
					productDetails[counter][col] = ExcelUtil.REGULAR_STYLE_TAG;
				}
			}
			counter++;
		}
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
