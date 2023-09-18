package com.tetrapak.supplierportal.core.services;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;

public interface PaymentInvoiceDownloadService {
	
	public HttpResponse preparePdf(String authTokenStr, String documentRef) throws IOException,URISyntaxException;

}
