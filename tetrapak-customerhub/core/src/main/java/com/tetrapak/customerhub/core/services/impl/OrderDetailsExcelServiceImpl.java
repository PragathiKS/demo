
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
	private static final String ORDER_DETAIL_I18_PREFIX = "cuhu.orderDetail.";
	private static final String S_NO = "serialNo";
	private static final String DOUBLE_COMMA = ",,";
	private static final String SINGLE_COMMA = ",";

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
			data = new String[orderSummary.size() + 3][colList.length + 1];
			String[] boldCenterStyleTags = new String[] { ExcelUtil.BOLD_TAG, ExcelUtil.ALIGN_CENTER_TAG };
			String[] regularStyleTag = new String[] { ExcelUtil.REGULAR_STYLE_TAG };
			data[0][0] = addTagToContent(StringUtils.EMPTY, regularStyleTag);

			for (int col = 1; col <= colList.length; col++) {
				data[0][col] = addTagToContent(getI18nVal(ORDER_DETAIL_I18_PREFIX + "orderSummary." + colList[col - 1]),
						boldCenterStyleTags);
			}

			int counter = 1;
			Iterator<OrderSummary> itr = orderSummary.iterator();
			while (itr.hasNext()) {
				OrderSummary summaryRow = itr.next();
				for (int i = 0; i < colList.length; i++) {
					data[counter][i + 1] = getProcessedValue(getSummaryTableHeader(summaryRow, colList[i]));
				}
				counter++;
			}
			data[counter][0] = addTagToContent("Only show above items in the deliverables : YES/NO (from api)",
					new String[] { ExcelUtil.HALF_BOLD_TAG });
			data[counter + 1][0] = addTagToContent(StringUtils.EMPTY, regularStyleTag);
		}
		return data;
	}

	/**
	 * Get column data from the summary table data
	 * 
	 * @param summaryRow
	 * @param columnName
	 * @return
	 */
	private String getSummaryTableHeader(OrderSummary summaryRow, String columnName) {
		Map<String, String> map = new HashMap<>();
		map.put("product", summaryRow.getProduct());
		map.put("orderQuantity", summaryRow.getOrderQuantity());
		map.put("deliveredQuantity", summaryRow.getDeliveredQuantity());
		if (map.containsKey(columnName)) {
			return map.get(columnName);
		}
		return StringUtils.EMPTY;
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
							+ getProcessedValue(deliveryList.getDeliveryStatus()),
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
			String[] columnArray = getColumnData(orderType, orderDetailsModel);

			if (Objects.nonNull((columnArray))) {
				int cols = columnArray.length + 1;

				productDetails = new String[rows + 5][cols];
				productDetails[0][0] = addTagToContent("#", darkGreyStyleTags);
				for (int col = 1; col < cols; col++) {

					productDetails[0][col] = addTagToContent(
							getI18nVal(setPrefixBasedOnOrderType(orderType) + columnArray[col - 1]), darkGreyStyleTags);
				}

				Iterator<Product> productsIterator = products.iterator();
				int counter = 1;
				String[] alignCenterStyleTags = new String[] { ExcelUtil.ALIGN_CENTER_TAG };
				while (productsIterator.hasNext()) {
					Product product = productsIterator.next();
					productDetails[counter][0] = addTagToContent(Integer.toString(counter), alignCenterStyleTags);
					setProdDetails(productDetails, product, counter, columnArray, alignCenterStyleTags);
					counter++;
				}
				if (!orderType.equalsIgnoreCase(PACKMAT_ORDER_TYPE)) {
					String[] boldCenterStyleTag = new String[] { ExcelUtil.BOLD_TAG, ExcelUtil.ALIGN_CENTER_TAG };
					productDetails[counter][cols - 2] = addTagToContent(
							getI18nVal(orderDetailsModel.getTotalWeightLabel()), boldCenterStyleTag);
					productDetails[counter][cols - 1] = addTagToContent(
							getProcessedValue(deliveryList.getTotalWeight()), alignCenterStyleTags);
					productDetails[counter + 1][cols - 2] = addTagToContent(
							getI18nVal(orderDetailsModel.getTotalPricePreVatLabel()), boldCenterStyleTag);
					productDetails[counter + 1][cols - 1] = addTagToContent(
							getProcessedValue(deliveryList.getTotalPricePreVAT()), alignCenterStyleTags);
					productDetails[counter + 2][cols - 2] = addTagToContent(
							getI18nVal(orderDetailsModel.getTotalVatLabel()), boldCenterStyleTag);
					productDetails[counter + 2][cols - 1] = addTagToContent(
							getProcessedValue(deliveryList.getTotalVAT()), alignCenterStyleTags);
				}
				applyRegularStyleToLastFourRows(counter, rows, cols, productDetails);
			}
		}
		return productDetails;
	}

	/**
	 * @param orderType
	 * @return
	 */
	private String setPrefixBasedOnOrderType(String orderType) {
		String prefix = ORDER_DETAIL_I18_PREFIX;
		if (orderType.equalsIgnoreCase(PACKMAT_ORDER_TYPE)) {
			prefix += "deliveryList.products.";
		}
		return prefix;
	}

	/**
	 * @param productDetails
	 * @param product
	 * @param row
	 * @param columnArray
	 * @param alignCenterStyleTags
	 */
	private void setProdDetails(String[][] productDetails, Product product, int row, String[] columnArray,
			String[] alignCenterStyleTags) {
		for (int i = 0; i < columnArray.length; i++) {
			productDetails[row][i + 1] = addTagToContent(
					getProcessedValue(getDeliveryTableRowData(product, columnArray[i])), alignCenterStyleTags);
		}
	}

	/**
	 * 
	 * Get the authored column data as a string array based on the ordertype
	 * 
	 * @param orderType         parts or packMat
	 * @param orderDetailsModel to get the authored column names comma separated
	 *                          list
	 * @return delivery table column data
	 */
	private String[] getColumnData(String orderType, OrderDetailsModel orderDetailsModel) {
		String[] columnArray = null;
		if (orderType.equalsIgnoreCase(PARTS_ORDER_TYPE)) {
			String partsDeliveryColumnString = orderDetailsModel.getPartsDeliveryTableCols();
			if (!StringUtils.isBlank(partsDeliveryColumnString)) {
				partsDeliveryColumnString = handleSNoInString(partsDeliveryColumnString);
				columnArray = partsDeliveryColumnString.split(",");
			}
		} else {
			String packMatDeliveryColStr = orderDetailsModel.getPackagingDeliveryTableCols();
			if (!StringUtils.isBlank(packMatDeliveryColStr)) {
				columnArray = packMatDeliveryColStr.split(",");
			}
		}
		return columnArray;
	}

	/**
	 * @param partsDeliveryColumnString without serialnumber
	 */
	private String handleSNoInString(String data) {
		if (data.contains(S_NO)) {
			data = data.replace(S_NO, StringUtils.EMPTY);
			data = data.trim();
			if (data.contains(DOUBLE_COMMA)) {
				data = data.replace(DOUBLE_COMMA, SINGLE_COMMA);
			} else if (data.startsWith(SINGLE_COMMA)) {
				data = data.replaceFirst(SINGLE_COMMA, StringUtils.EMPTY);
			} else if (data.endsWith(SINGLE_COMMA)) {
				data = data.substring(0, data.length() - 1);
			}
		}
		return data;
	}

	/**
	 * Get column data from the summary table data
	 * 
	 * @param summaryRow
	 * @param columnName
	 * @return
	 */
	private String getDeliveryTableRowData(Product product, String columnName) {
		Map<String, String> map = new HashMap<>();
		map.put("productName", product.getProductName());
		map.put("productID", product.getProductID());
		map.put("weight", product.getWeight());
		map.put("deliveredQuantity", product.getDeliveredQuantity());
		map.put("remainingQuantity", product.getRemainingQuantity());
		map.put("ETA", product.getETA());
		map.put("unitPrice", product.getUnitPrice());
		map.put("price", product.getPrice());
		map.put("materialCode", product.getMaterialCode());
		map.put("SKU", product.getMaterialCode());
		map.put("orderNumber", product.getOrderNumber().toString());
		if (map.containsKey(columnName)) {
			return map.get(columnName);
		}
		return StringUtils.EMPTY;
	}

	/**
	 * @param counter
	 * @param rows
	 * @param cols
	 * @param productDetails
	 */
	private void applyRegularStyleToLastFourRows(int counter, int rows, int cols, String[][] productDetails) {
		while (counter < (rows + 5)) {
			for (int col = 0; col < cols; col++) {
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
