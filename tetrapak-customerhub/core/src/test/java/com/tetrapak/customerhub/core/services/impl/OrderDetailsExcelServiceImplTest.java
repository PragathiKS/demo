package com.tetrapak.customerhub.core.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.tetrapak.customerhub.core.beans.oderdetails.DeliveryAddress;
import com.tetrapak.customerhub.core.beans.oderdetails.DeliveryList;
import com.tetrapak.customerhub.core.beans.oderdetails.InvoiceAddress;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetails;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetailsData;
import com.tetrapak.customerhub.core.beans.oderdetails.Product;
import com.tetrapak.customerhub.core.models.OrderDetailsModel;

@RunWith(MockitoJUnitRunner.class)
public class OrderDetailsExcelServiceImplTest {

	OrderDetailsExcelServiceImpl excelService = new OrderDetailsExcelServiceImpl();

	@Mock
	private SlingHttpServletRequest servletRequest;

	@Mock
	private SlingHttpServletResponse response;

	@Mock
	private OrderDetailsModel orderDetailsModel;

	@Mock
	private ServletOutputStream servletOutputStream;

	@Before
	public void setUp() throws Exception {
		Mockito.when(orderDetailsModel.getOrderNo()).thenReturn("Order Number");
		Mockito.when(orderDetailsModel.getOrderStatus()).thenReturn("Order Status");
		Mockito.when(orderDetailsModel.getCustomerNameLabel()).thenReturn("CustomerNameLabel");
		Mockito.when(orderDetailsModel.getCustomerNumberLabel()).thenReturn("CustomerNumberLabel");
		Mockito.when(orderDetailsModel.getPurchaseOrderNumberLabel()).thenReturn("PurchaseOrderNumberLabel");
		Mockito.when(orderDetailsModel.getCustomerReferenceLabel()).thenReturn("CustomerReferenceLabel");
		Mockito.when(orderDetailsModel.getOrderDateLabel()).thenReturn("OrderDateLabel");
		Mockito.when(orderDetailsModel.getWebRefLabel()).thenReturn("WebRefLabel");
		Mockito.when(orderDetailsModel.getDeliveryNumber()).thenReturn("DeliveryNumber");
		Mockito.when(orderDetailsModel.getShippingLabel()).thenReturn("ShippingLabel");
		Mockito.when(orderDetailsModel.getTrackOrderLabel()).thenReturn("TrackOrderLabel");
		Mockito.when(orderDetailsModel.getDeliveryAddrLabel()).thenReturn("DeliveryAddrLabel");
		Mockito.when(orderDetailsModel.getInvoiceAddrLabel()).thenReturn("InvoiceAddrLabel");
		Mockito.when(orderDetailsModel.getOrderStatus()).thenReturn("OrderStatus");
		Mockito.when(orderDetailsModel.getTotalWeightLabel()).thenReturn("TotalWeightLabel");
	}

	@Test
	public void testGenerateOrderDetailsExcelOrderTypeBlank() {
		String orderType = "";
		OrderDetailsData orderDetailData = new OrderDetailsData();
		excelService.generateOrderDetailsExcel(servletRequest, response, orderType, orderDetailData, orderDetailsModel);
	}

	@Test
	public void testGenerateOrderDetailsExcelModelNull() {
		String orderType = "parts";
		OrderDetailsData orderDetailData = new OrderDetailsData();
		excelService.generateOrderDetailsExcel(servletRequest, response, orderType, orderDetailData, null);
	}

	@Test
	public void testGenerateOrderDetailsExcelApiDataNull() {
		String orderType = "parts";
		excelService.generateOrderDetailsExcel(servletRequest, response, orderType, null, orderDetailsModel);
	}

	@Test
	public void testGenerateOrderDetailsExcel() throws IOException {
		String orderType = "parts";
		OrderDetailsData orderDetailData = new OrderDetailsData();
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setCustomerName("customerName");
		orderDetails.setCustomerNumber(12345);
		orderDetails.setCustomerReference(1234);
		orderDetails.setOrderNumber("orderNumber");
		orderDetails.setPlacedOn("placedOn");
		orderDetails.setPurchaseOrderNumber(123);
		orderDetails.setStatus("status");
		orderDetails.setWebRefID(123);
		orderDetailData.setOrderDetails(orderDetails);
		List<DeliveryList> deliveryList = new ArrayList<>();
		DeliveryList delivery1 = new DeliveryList();
		delivery1.setCarrier("carrier");
		delivery1.setCarrierTrackingID("carrierTrackingID");
		DeliveryAddress deliveryAddress = new DeliveryAddress();
		deliveryAddress.setCity("city");
		deliveryAddress.setCountry("country");
		deliveryAddress.setPostalcode("postalcode");
		deliveryAddress.setName("name");
		deliveryAddress.setName2("name2");
		deliveryAddress.setState("state");
		delivery1.setDeliveryAddress(deliveryAddress);
		delivery1.setDeliveryNumber("deliveryNumber");
		delivery1.setDeliveryOrder("deliveryOrder");
		delivery1.setDeliveryStatus("deliveryStatus");
		delivery1.setETD("eTD");
		InvoiceAddress invoiceAddress = new InvoiceAddress();
		invoiceAddress.setCity("city");
		delivery1.setInvoiceAddress(invoiceAddress);
		delivery1.setOrderNumber("orderNumber");
		delivery1.setProductPlace("productPlace");
		List<Product> products = new ArrayList<>();
		delivery1.setProducts(products);
		deliveryList.add(delivery1);
		orderDetailData.setDeliveryList(deliveryList);
		
		Mockito.when(response.getOutputStream()).thenReturn(servletOutputStream);
		Mockito.doNothing().when(servletOutputStream).write(Mockito.any(), Mockito.anyInt(), Mockito.anyInt());
		excelService.generateOrderDetailsExcel(servletRequest, response, orderType, orderDetailData, orderDetailsModel);
	}
	

	@Test
	public void testGenerateOrderDetailsExcelPackageMat() throws IOException {
		String orderType = "packmat";
		OrderDetailsData orderDetailData = new OrderDetailsData();
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setCustomerName("customerName");
		orderDetails.setCustomerNumber(12345);
		orderDetails.setCustomerReference(1234);
		orderDetails.setOrderNumber("orderNumber");
		orderDetails.setPlacedOn("placedOn");
		orderDetails.setPurchaseOrderNumber(123);
		orderDetails.setStatus("status");
		orderDetails.setWebRefID(123);
		orderDetailData.setOrderDetails(orderDetails);
		List<DeliveryList> deliveryList = new ArrayList<>();
		DeliveryList delivery1 = new DeliveryList();
		delivery1.setCarrier("carrier");
		delivery1.setCarrierTrackingID("carrierTrackingID");
		DeliveryAddress deliveryAddress = new DeliveryAddress();
		deliveryAddress.setCity("city");
		deliveryAddress.setCountry("country");
		deliveryAddress.setPostalcode("postalcode");
		deliveryAddress.setName("name");
		deliveryAddress.setName2("name2");
		deliveryAddress.setState("state");
		delivery1.setDeliveryAddress(deliveryAddress);
		delivery1.setDeliveryNumber("deliveryNumber");
		delivery1.setDeliveryOrder("deliveryOrder");
		delivery1.setDeliveryStatus("deliveryStatus");
		delivery1.setETD("eTD");
		InvoiceAddress invoiceAddress = new InvoiceAddress();
		invoiceAddress.setCity("city");
		delivery1.setInvoiceAddress(invoiceAddress);
		delivery1.setOrderNumber("orderNumber");
		delivery1.setProductPlace("productPlace");
		List<Product> products = new ArrayList<>();
		delivery1.setProducts(products);
		deliveryList.add(delivery1);
		orderDetailData.setDeliveryList(deliveryList);
		
		Mockito.when(response.getOutputStream()).thenReturn(servletOutputStream);
		Mockito.doNothing().when(servletOutputStream).write(Mockito.any(), Mockito.anyInt(), Mockito.anyInt());
		excelService.generateOrderDetailsExcel(servletRequest, response, orderType, orderDetailData, orderDetailsModel);
	}

}
