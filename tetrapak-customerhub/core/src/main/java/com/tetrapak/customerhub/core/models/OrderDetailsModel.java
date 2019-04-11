package com.tetrapak.customerhub.core.models;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import com.google.gson.Gson;

/**
 * Model class for order search component
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

	private String i18nKeys;

	/**
	 * Populating the i18n keys to a JSON object string getting values from the dialog
	 */
	@PostConstruct
	protected void init() {
		Map<String, Object> i18KeyMap = new HashMap<String, Object>();
		i18KeyMap.put("backToOrderHistory", backToOrderHistory);
		i18KeyMap.put("orderNo", orderNo);
		i18KeyMap.put("customerNameLabel", customerNameLabel);
		i18KeyMap.put("purchaseOrderNumberLabel", purchaseOrderNumberLabel);
		i18KeyMap.put("orderDateLabel", orderDateLabel);
		i18KeyMap.put("customerNumberLabel", customerNumberLabel);
		i18KeyMap.put("webRefLabel", webRefLabel);
		i18KeyMap.put("custSupCentreLabel", custSupCentreLabel);
		i18KeyMap.put("createExcelLabel", createExcelLabel);
		i18KeyMap.put("createPdfLabel", createPdfLabel);
		i18KeyMap.put("deliveryNumberLabel", deliveryNumberLabel);
		i18KeyMap.put("shippingLabel", shippingLabel);
		i18KeyMap.put("trackOrderLabel", trackOrderLabel);
		i18KeyMap.put("deliveryAddrLabel", deliveryAddrLabel);
		i18KeyMap.put("invoiceAddrLabel", invoiceAddrLabel);
		i18KeyMap.put("totalWeightLabel", totalWeightLabel);
		i18KeyMap.put("totalPricePreVatLabel", totalPricePreVatLabel);
		i18KeyMap.put("totalVatLabel", totalVatLabel);
		i18KeyMap.put("deliveryOrder", deliveryOrder);
		i18KeyMap.put("deliveryNumber", deliveryNumber);
		i18KeyMap.put("productionPlace", productionPlace);
		i18KeyMap.put("requested", requested);
		i18KeyMap.put("etd", etd);
		i18KeyMap.put("kpkPopUpHeading", kpkPopUpHeading);
		i18KeyMap.put("kpkPopUpDescription", kpkPopUpDescription);
		i18KeyMap.put("noDataMsg", noDataMsg);
		i18KeyMap.put("errorMsg", errorMsg);
		i18nKeys = new Gson().toJson(i18KeyMap);
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

}
