package com.tetrapak.supplierportal.core.services;

import java.util.List;
import java.util.Map;

public interface InvoiceStatusService {

	Map<String, List<String>> invoiceStatusCodeMap();
	
    int getFromToDateGapInMonthsVal();

}
