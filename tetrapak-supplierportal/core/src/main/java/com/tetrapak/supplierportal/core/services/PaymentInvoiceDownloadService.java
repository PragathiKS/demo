package com.tetrapak.supplierportal.core.services;

import java.io.IOException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import com.google.gson.JsonObject;
import com.tetrapak.supplierportal.core.bean.PaymentDetailResponse;
import com.tetrapak.supplierportal.core.models.PaymentDetailsModel;

public interface PaymentInvoiceDownloadService {
	
	JsonObject retrievePaymentDetails(String authTokenStr, String documentRef) throws IOException;
	
	boolean preparePdf(PaymentDetailResponse paymentDetails, SlingHttpServletRequest request,SlingHttpServletResponse response,PaymentDetailsModel paymentDetailsModel);

}
