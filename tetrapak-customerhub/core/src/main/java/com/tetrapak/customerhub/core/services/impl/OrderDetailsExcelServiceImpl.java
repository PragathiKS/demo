
package com.tetrapak.customerhub.core.services.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
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
import com.tetrapak.customerhub.core.utils.GlobalUtil;

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
	private static final String COLON_SPACE = ": ";
	private SlingHttpServletRequest request = null;

	/**
	 * to generate the excel
	 *
	 */
	@Override
	public boolean generateOrderDetailsExcel(SlingHttpServletRequest servletRequest, SlingHttpServletResponse response,
			String orderType, OrderDetailsData orderDetailData, OrderDetailsModel orderDetailsModel) {
		if (Objects.nonNull(orderDetailData) && Objects.nonNull(orderDetailsModel) && !StringUtils.isBlank(orderType)) {
			request = servletRequest;
			String[][] data = null;
			data = getOrderDetailsSection(orderDetailsModel, orderDetailData.getOrderDetails());
			LOGGER.debug("Order Details Section data added to the array..");
			if (Objects.nonNull(orderDetailData.getOrderSummary())) {
				data = ArrayUtils.addAll(data, getOrderSummary(orderDetailData.getOrderSummary(),
						orderDetailsModel.getPackagingProductsTableCols()));
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
			if (ExcelUtil.generateExcelReport(response, excelReportData)) {
				return true;
			}
		} else {
			LOGGER.error("Order Details Data is null so can not process the excel creation!");
		}
		return false;
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
		String[] halfBoldStyleTags = new String[] { ExcelUtil.HALF_BOLD_TAG };
		String[] regularStyleTag = new String[] { ExcelUtil.REGULAR_STYLE_TAG };
		orderDetailsSection[0][0] = addTagToContent(getI18nVal(orderDetailsModel.getOrderNo()) + COLON_SPACE
				+ getProcessedValue(orderDetails.getOrderNumber()), halfBoldStyleTags);
		orderDetailsSection[1][0] = addTagToContent(getI18nVal(orderDetailsModel.getOrderStatus()) + COLON_SPACE
				+ getProcessedValue(orderDetails.getStatus()), halfBoldStyleTags);
		orderDetailsSection[2][0] = addTagToContent(StringUtils.EMPTY, regularStyleTag);
		orderDetailsSection[3][0] = addTagToContent("Order Details",
				new String[] { ExcelUtil.BOLD_TAG, ExcelUtil.LIGHT_GREY_BG_TAG });
		orderDetailsSection[4][0] = addTagToContent(getI18nVal(orderDetailsModel.getCustomerNameLabel()) + COLON_SPACE
				+ getProcessedValue(orderDetails.getCustomerName()), halfBoldStyleTags);
		orderDetailsSection[5][0] = addTagToContent(getI18nVal(orderDetailsModel.getCustomerNumberLabel()) + COLON_SPACE
				+ getProcessedValue(orderDetails.getCustomerNumber()), halfBoldStyleTags);
		orderDetailsSection[6][0] = addTagToContent(getI18nVal(orderDetailsModel.getPurchaseOrderNumberLabel())
				+ COLON_SPACE + getProcessedValue(orderDetails.getPurchaseOrderNumber()), halfBoldStyleTags);
		orderDetailsSection[7][0] = addTagToContent(getI18nVal(orderDetailsModel.getCustomerReferenceLabel())
				+ COLON_SPACE + getProcessedValue(orderDetails.getCustomerReference()), halfBoldStyleTags);
		orderDetailsSection[8][0] = addTagToContent(getI18nVal(orderDetailsModel.getOrderDateLabel()) + COLON_SPACE
				+ getProcessedValue(orderDetails.getPlacedOn()), halfBoldStyleTags);
		orderDetailsSection[9][0] = addTagToContent(getI18nVal(orderDetailsModel.getWebRefLabel()) + COLON_SPACE
				+ getProcessedValue(orderDetails.getWebRefID()), halfBoldStyleTags);
		orderDetailsSection[10][0] = addTagToContent(StringUtils.EMPTY, regularStyleTag);

		return orderDetailsSection;
	}

	/**
	 * getI18nVal
	 * 
	 * @param value
	 * @return
	 */
	private String getI18nVal(String value) {
		if (null != request && !StringUtils.isBlank(value)) {
			value = GlobalUtil.getI18nValue(request, StringUtils.EMPTY, value);
		}
		return value;
	}

	/**
	 * Order Summary section in the excel Only in packaging material excel
	 * 
	 * @param columns
	 * 
	 * @param colsData
	 * 
	 * @return OrderSummary 2 d array
	 */
	private String[][] getOrderSummary(List<OrderSummary> orderSummary, String columns) {
		String[][] data = null;
		if (!StringUtils.isBlank(columns)) {
			String[] colList = columns.split(",");
			data = new String[5][colList.length + 1];
			String[] boldCenterStyleTags = new String[] { ExcelUtil.BOLD_TAG, ExcelUtil.ALIGN_CENTER_TAG };
			String[] regularStyleTag = new String[] { ExcelUtil.REGULAR_STYLE_TAG };
			data[0][0] = addTagToContent(StringUtils.EMPTY, regularStyleTag);

			for (int col = 1; col <= colList.length; col++) {
				data[0][col] = addTagToContent(colList[col - 1], boldCenterStyleTags);
			}

			int counter = 1;
			Iterator<OrderSummary> itr = orderSummary.iterator();
			while (itr.hasNext()) {
				OrderSummary summaryRow = itr.next();

				data[counter][1] = getProcessedValue(summaryRow.getProduct());
				data[counter][2] = getProcessedValue(summaryRow.getOrderQuantity());
				data[counter][3] = getProcessedValue(summaryRow.getDeliveredQuantity());
				counter++;
			}
			while (itr.hasNext()) {
				OrderSummary summaryRow = itr.next();
				data[counter][0] = addTagToContent(StringUtils.EMPTY, regularStyleTag);
				
				data[counter][1] = getProcessedValue(summaryRow.getProduct());
				data[counter][2] = getProcessedValue(summaryRow.getOrderQuantity());
				data[counter][3] = getProcessedValue(summaryRow.getDeliveredQuantity());
				counter++;
			}

			data[3][0] = addTagToContent("Only show above items in the deliverables : YES/NO (from api)",
					new String[] { ExcelUtil.HALF_BOLD_TAG });
			data[4][0] = addTagToContent(StringUtils.EMPTY, regularStyleTag);
		}
		return data;
	}

	private String getSummaryTableHeader(OrderSummary summaryRow, String columnName) {
		Map<String, String> map = new HashMap<>();
		map.put("productName", summaryRow.getProduct());
		map.put("orderQuantity", summaryRow.getOrderQuantity());
		map.put("deliveredQuantity", summaryRow.getDeliveredQuantity());
		if (map.containsKey(columnName)) {
			return map.get(columnName);
		}
		return "";
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
		String[][] deliveryDetailsSection1 = getDeliveryDetailsInitialSection(orderDetailsModel, orderType,
				deliveryList);
		String[][] productDetails = getProductDetails(orderType, orderDetailsModel, deliveryList);

		return ArrayUtils.addAll(deliveryDetailsSection1, productDetails);
	}

	/**
	 * DeliveryDetails data based on the orderType
	 * 
	 * @param orderDetailsModel
	 * 
	 * @param orderType
	 * @param deliveryList
	 * @return
	 */
	private String[][] getDeliveryDetailsInitialSection(OrderDetailsModel orderDetailsModel, String orderType,
			DeliveryList deliveryList) {
		String[][] deliveryDetailsSection;
		String[] halfBoldStyleTags = new String[] { ExcelUtil.HALF_BOLD_TAG };
		String[] regularStyleTag = new String[] { ExcelUtil.REGULAR_STYLE_TAG };
		String[] boldStyleTag = new String[] { ExcelUtil.BOLD_TAG };
		if (orderType.equalsIgnoreCase(PARTS_ORDER_TYPE)) {
			deliveryDetailsSection = new String[9][10];
			deliveryDetailsSection[0][0] = addTagToContent(
					getI18nVal(orderDetailsModel.getDeliveryNumber()) + COLON_SPACE
							+ getProcessedValue(deliveryList.getDeliveryOrder()),
					new String[] { ExcelUtil.BOLD_TAG, ExcelUtil.MERGE_ROW_TAG, ExcelUtil.LIGHT_GREY_BG_TAG });
			deliveryDetailsSection[1][0] = addTagToContent(getI18nVal(orderDetailsModel.getShippingLabel())
					+ COLON_SPACE + getProcessedValue(deliveryList.getCarrier()), halfBoldStyleTags);
			deliveryDetailsSection[2][0] = addTagToContent(getI18nVal(orderDetailsModel.getTrackOrderLabel())
					+ COLON_SPACE + getProcessedValue(deliveryList.getCarrierTrackingID()), halfBoldStyleTags);
			deliveryDetailsSection[3][0] = addTagToContent(getI18nVal(orderDetailsModel.getDeliveryAddrLabel()),
					boldStyleTag);
			deliveryDetailsSection[4][0] = getDeliveryAddress(deliveryList.getDeliveryAddress());
			deliveryDetailsSection[5][0] = addTagToContent(StringUtils.EMPTY, regularStyleTag);
			deliveryDetailsSection[6][0] = addTagToContent(getI18nVal(orderDetailsModel.getInvoiceAddrLabel()),
					boldStyleTag);
			deliveryDetailsSection[7][0] = getInvoiceAddress(deliveryList.getInvoiceAddress());
			deliveryDetailsSection[8][0] = addTagToContent(StringUtils.EMPTY, regularStyleTag);
		} else {
			deliveryDetailsSection = new String[11][10];
			deliveryDetailsSection[0][0] = addTagToContent(
					getI18nVal(orderDetailsModel.getDeliveryNumber()) + COLON_SPACE
							+ getProcessedValue(deliveryList.getDeliveryOrder()) + "-"
							+ getI18nVal(orderDetailsModel.getOrderStatus()),
					new String[] { ExcelUtil.BOLD_TAG, ExcelUtil.MERGE_ROW_TAG, ExcelUtil.LIGHT_GREY_BG_TAG });
			deliveryDetailsSection[1][0] = addTagToContent(getI18nVal(orderDetailsModel.getDeliveryOrder())
					+ COLON_SPACE + getProcessedValue(deliveryList.getDeliveryOrder()), halfBoldStyleTags);
			deliveryDetailsSection[2][0] = addTagToContent(getI18nVal(orderDetailsModel.getProductionPlace())
					+ COLON_SPACE + getProcessedValue(deliveryList.getProductPlace()), halfBoldStyleTags);
			deliveryDetailsSection[3][0] = addTagToContent(getI18nVal(orderDetailsModel.getRequested()) + COLON_SPACE
					+ getProcessedValue(deliveryList.getDeliveryOrder()), halfBoldStyleTags);
			deliveryDetailsSection[4][0] = addTagToContent(getI18nVal(orderDetailsModel.getEtd()) + COLON_SPACE
					+ getProcessedValue(deliveryList.getDeliveryOrder()), halfBoldStyleTags);
			deliveryDetailsSection[5][0] = addTagToContent(getI18nVal(orderDetailsModel.getDeliveryAddrLabel()),
					boldStyleTag);
			deliveryDetailsSection[6][0] = getDeliveryAddress(deliveryList.getDeliveryAddress());
			deliveryDetailsSection[7][0] = addTagToContent(StringUtils.EMPTY, regularStyleTag);
			deliveryDetailsSection[8][0] = addTagToContent(getI18nVal(orderDetailsModel.getInvoiceAddrLabel()),
					boldStyleTag);
			deliveryDetailsSection[9][0] = getInvoiceAddress(deliveryList.getInvoiceAddress());
			deliveryDetailsSection[10][0] = addTagToContent(StringUtils.EMPTY, regularStyleTag);
		}
		return deliveryDetailsSection;
	}

	/**
	 * @param invoiceAddress
	 * @return
	 */
	private String getInvoiceAddress(InvoiceAddress invoiceAddress) {
		return getProcessedValue(invoiceAddress.getName()) + " " + invoiceAddress.getName2()
				+ ExcelUtil.NEW_LINE_DETECTOR + getProcessedValue(invoiceAddress.getCity())
				+ ExcelUtil.NEW_LINE_DETECTOR + invoiceAddress.getState() + " , "
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
		StringBuilder processedData = new StringBuilder();
		if (null != rawData) {
			processedData.append(rawData);
			for (String tag : tags) {
				processedData.append(tag);
			}
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
			String[] darkGreyStyleTags = new String[] { ExcelUtil.DARK_GREY_BG_TAG };
			productDetails = new String[rows + 5][10];
			productDetails[0][0] = addTagToContent("#", darkGreyStyleTags);
			productDetails[0][1] = addTagToContent("Product", darkGreyStyleTags);
			productDetails[0][2] = addTagToContent("Product ID", darkGreyStyleTags);
			productDetails[0][3] = addTagToContent("Quantity", darkGreyStyleTags);
			productDetails[0][4] = addTagToContent("Weight", darkGreyStyleTags);
			productDetails[0][5] = addTagToContent("Sent", darkGreyStyleTags);
			productDetails[0][6] = addTagToContent("Open", darkGreyStyleTags);
			productDetails[0][7] = addTagToContent("ETA", darkGreyStyleTags);
			productDetails[0][8] = addTagToContent("Unit Price", darkGreyStyleTags);
			productDetails[0][9] = addTagToContent("Price", darkGreyStyleTags);

			Iterator<Product> productsIterator = products.iterator();
			int counter = 1;
			String[] alignCenterStyleTags = new String[] { ExcelUtil.ALIGN_CENTER_TAG };
			while (productsIterator.hasNext()) {
				Product product = productsIterator.next();
				productDetails[counter][0] = addTagToContent(Integer.toString(counter), alignCenterStyleTags);
				productDetails[counter][1] = addTagToContent(getProcessedValue(product.getProductName()),
						alignCenterStyleTags);
				productDetails[counter][2] = addTagToContent(getProcessedValue(product.getProductID()),
						alignCenterStyleTags);
				productDetails[counter][3] = addTagToContent(getProcessedValue(product.getOrderQuantity()),
						alignCenterStyleTags);
				productDetails[counter][4] = addTagToContent(getProcessedValue(product.getWeight()),
						alignCenterStyleTags);
				productDetails[counter][5] = addTagToContent(getProcessedValue(product.getDeliveredQuantity()),
						alignCenterStyleTags);
				productDetails[counter][6] = addTagToContent(getProcessedValue(product.getRemainingQuantity()),
						alignCenterStyleTags);
				productDetails[counter][7] = addTagToContent(getProcessedValue(product.getETA()), alignCenterStyleTags);
				productDetails[counter][8] = addTagToContent(getProcessedValue(product.getUnitPrice()),
						alignCenterStyleTags);
				productDetails[counter][9] = addTagToContent(getProcessedValue(product.getPrice()),
						alignCenterStyleTags);
				counter++;
			}
			if (!orderType.equalsIgnoreCase(PACKMAT_ORDER_TYPE)) {
				String[] boldCenterStyleTag = new String[] { ExcelUtil.BOLD_TAG, ExcelUtil.ALIGN_CENTER_TAG };
				productDetails[counter + 1][8] = addTagToContent(getI18nVal(orderDetailsModel.getTotalWeightLabel()),
						boldCenterStyleTag);
				productDetails[counter + 1][9] = addTagToContent(getProcessedValue(deliveryList.getTotalWeight()),
						alignCenterStyleTags);
				productDetails[counter + 2][8] = addTagToContent(
						getI18nVal(orderDetailsModel.getTotalPricePreVatLabel()), boldCenterStyleTag);
				productDetails[counter + 2][9] = addTagToContent(getProcessedValue(deliveryList.getTotalPricePreVAT()),
						alignCenterStyleTags);
				productDetails[counter + 3][8] = addTagToContent(getI18nVal(orderDetailsModel.getTotalVatLabel()),
						boldCenterStyleTag);
				productDetails[counter + 3][9] = addTagToContent(getProcessedValue(deliveryList.getTotalVAT()),
						alignCenterStyleTags);
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
