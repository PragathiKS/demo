package com.tetrapak.customerhub.core.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import com.google.gson.Gson;

/**
 * Model class for Order Details component
 * 
 * @author swalamba
 *
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class OrderDetailsModel {

	@Inject
	private String backToOrderHistory;

	@Inject
	private String orderNo;

	@Inject
	private String customerNameLabel;

	@Inject
	private String purchaseOrderNumberLabel;

	@Inject
	private String orderDateLabel;

	@Inject
	private String customerNumberLabel;

	@Inject
	private String webRefLabel;

	@Inject
	private String custSupCentreLabel;

	@Inject
	private String createExcelLabel;

	@Inject
	private String createPdfLabel;

	@Inject
	private String deliveryNumberLabel;

	@Inject
	private String shippingLabel;

	@Inject
	private String trackOrderLabel;

	@Inject
	private String deliveryAddrLabel;

	@Inject
	private String invoiceAddrLabel;

	@Inject
	private String totalWeightLabel;

	@Inject
	private String totalPricePreVatLabel;

	@Inject
	private String totalVatLabel;

	@Inject
	private String deliveryOrder;

	@Inject
	private String deliveryNumber;

	@Inject
	private String productionPlace;

	@Inject
	private String requested;

	@Inject
	private String etd;

	@Inject
	private String kpkPopUpHeading;

	@Inject
	private String kpkPopUpDescription;

	@Inject
	private String noDataMsg;

	@Inject
	private String errorMsg;

	@Inject
	private String webReferenceLink;

	@Inject
	private String orderNoLink;

	@Inject
	private String packagingDeliveryTableCols;

	@Inject
	private String packagingProductsTableCols;

	@Inject
	private String partsDeliveryTableCols;

	private String i18nKeys;

	/**
	 * Populating the i18n keys to a JSON object string getting values from the
	 * dialog
	 */
	@PostConstruct
	protected void init() {
		Map<String, Object> i18KeyMap = new HashMap<String, Object>();
		i18KeyMap.put("backToOrderHistory", getBackToOrderHistory());
		i18KeyMap.put("orderNo", getOrderNo());
		i18KeyMap.put("customerNameLabel", getCustomerNameLabel());
		i18KeyMap.put("purchaseOrderNumberLabel", getPurchaseOrderNumberLabel());
		i18KeyMap.put("orderDateLabel", getOrderDateLabel());
		i18KeyMap.put("customerNumberLabel", getCustomerNumberLabel());
		i18KeyMap.put("webRefLabel", getWebRefLabel());
		i18KeyMap.put("custSupCentreLabel", getCustSupCentreLabel());
		i18KeyMap.put("createExcelLabel", getCreateExcelLabel());
		i18KeyMap.put("createPdfLabel", getCreatePdfLabel());
		i18KeyMap.put("deliveryNumberLabel", getDeliveryNumberLabel());
		i18KeyMap.put("shippingLabel", getShippingLabel());
		i18KeyMap.put("trackOrderLabel", getTrackOrderLabel());
		i18KeyMap.put("deliveryAddrLabel", getDeliveryAddrLabel());
		i18KeyMap.put("invoiceAddrLabel", getInvoiceAddrLabel());
		i18KeyMap.put("totalWeightLabel", getTotalWeightLabel());
		i18KeyMap.put("totalPricePreVatLabel", getTotalPricePreVatLabel());
		i18KeyMap.put("totalVatLabel", getTotalVatLabel());
		i18KeyMap.put("deliveryOrder", getDeliveryOrder());
		i18KeyMap.put("deliveryNumber", getDeliveryNumber());
		i18KeyMap.put("productionPlace", getProductionPlace());
		i18KeyMap.put("requested", getRequested());
		i18KeyMap.put("etd", getEtd());
		i18KeyMap.put("kpkPopUpHeading", getKpkPopUpHeading());
		i18KeyMap.put("kpkPopUpDescription", getKpkPopUpDescription());
		i18KeyMap.put("noDataMsg", getNoDataMsg());
		i18KeyMap.put("errorMsg", getErrorMsg());
		i18KeyMap.put("orderNoLink", getOrderNoLink());
		i18KeyMap.put("webReferenceLink", getWebReferenceLink());
		i18nKeys = new Gson().toJson(i18KeyMap);
		partsDeliveryTableCols = new Gson().toJson(getColumnNamesList(partsDeliveryTableCols));
		packagingProductsTableCols = new Gson().toJson(getColumnNamesList(packagingProductsTableCols));
		packagingDeliveryTableCols = new Gson().toJson(getColumnNamesList(packagingDeliveryTableCols));
	}

	/**
	 * 
	 * Get the list of the columns from comma separated list in sequence
	 * 
	 * @param columnList
	 * @return
	 */
	private List<String> getColumnNamesList(String columnList) {
		List<String> columnNameList = new ArrayList<>();
		if (!StringUtils.isBlank(columnList)) {
			String[] columnArray = columnList.split(",");
			columnArray = StringUtils.stripAll(columnArray);
			columnNameList = Arrays.asList(columnArray);
		}
		return columnNameList;
	}

	/**
	 * @return the i18nKeys
	 */
	public String getI18nKeys() {
		return i18nKeys;
	}

	/**
	 * @return the backToOrderHistory
	 */
	public String getBackToOrderHistory() {
		return backToOrderHistory;
	}

	/**
	 * @return the orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}

	/**
	 * @return the customerNameLabel
	 */
	public String getCustomerNameLabel() {
		return customerNameLabel;
	}

	/**
	 * @return the purchaseOrderNumberLabel
	 */
	public String getPurchaseOrderNumberLabel() {
		return purchaseOrderNumberLabel;
	}

	/**
	 * @return the orderDateLabel
	 */
	public String getOrderDateLabel() {
		return orderDateLabel;
	}

	/**
	 * @return the customerNumberLabel
	 */
	public String getCustomerNumberLabel() {
		return customerNumberLabel;
	}

	/**
	 * @return the webRefLabel
	 */
	public String getWebRefLabel() {
		return webRefLabel;
	}

	/**
	 * @return the custSupCentreLabel
	 */
	public String getCustSupCentreLabel() {
		return custSupCentreLabel;
	}

	/**
	 * @return the createExcelLabel
	 */
	public String getCreateExcelLabel() {
		return createExcelLabel;
	}

	/**
	 * @return the createPdfLabel
	 */
	public String getCreatePdfLabel() {
		return createPdfLabel;
	}

	/**
	 * @return the deliveryNumberLabel
	 */
	public String getDeliveryNumberLabel() {
		return deliveryNumberLabel;
	}

	/**
	 * @return the shippingLabel
	 */
	public String getShippingLabel() {
		return shippingLabel;
	}

	/**
	 * @return the trackOrderLabel
	 */
	public String getTrackOrderLabel() {
		return trackOrderLabel;
	}

	/**
	 * @return the deliveryAddrLabel
	 */
	public String getDeliveryAddrLabel() {
		return deliveryAddrLabel;
	}

	/**
	 * @return the invoiceAddrLabel
	 */
	public String getInvoiceAddrLabel() {
		return invoiceAddrLabel;
	}

	/**
	 * @return the totalWeightLabel
	 */
	public String getTotalWeightLabel() {
		return totalWeightLabel;
	}

	/**
	 * @return the totalPricePreVatLabel
	 */
	public String getTotalPricePreVatLabel() {
		return totalPricePreVatLabel;
	}

	/**
	 * @return the totalVatLabel
	 */
	public String getTotalVatLabel() {
		return totalVatLabel;
	}

	/**
	 * @return the deliveryOrder
	 */
	public String getDeliveryOrder() {
		return deliveryOrder;
	}

	/**
	 * @return the deliveryNumber
	 */
	public String getDeliveryNumber() {
		return deliveryNumber;
	}

	/**
	 * @return the productionPlace
	 */
	public String getProductionPlace() {
		return productionPlace;
	}

	/**
	 * @return the requested
	 */
	public String getRequested() {
		return requested;
	}

	/**
	 * @return the etd
	 */
	public String getEtd() {
		return etd;
	}

	/**
	 * @return the kpkPopUpHeading
	 */
	public String getKpkPopUpHeading() {
		return kpkPopUpHeading;
	}

	/**
	 * @return the kpkPopUpDescription
	 */
	public String getKpkPopUpDescription() {
		return kpkPopUpDescription;
	}

	/**
	 * @return the noDataMsg
	 */
	public String getNoDataMsg() {
		return noDataMsg;
	}

	/**
	 * @return the errorMsg
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * @return the customerReferenceLink
	 */
	public String getWebReferenceLink() {
		return webReferenceLink;
	}

	/**
	 * @return the orderNoLink
	 */
	public String getOrderNoLink() {
		return orderNoLink;
	}

	/**
	 * @return the packagingDeliveryTableCols
	 */
	public String getPackagingDeliveryTableCols() {
		return packagingDeliveryTableCols;
	}

	/**
	 * @return the packagingProductsTableCols
	 */
	public String getPackagingProductsTableCols() {
		return packagingProductsTableCols;
	}

	/**
	 * @return the partsDeliveryTableCols
	 */
	public String getPartsDeliveryTableCols() {
		return partsDeliveryTableCols;
	}
}
