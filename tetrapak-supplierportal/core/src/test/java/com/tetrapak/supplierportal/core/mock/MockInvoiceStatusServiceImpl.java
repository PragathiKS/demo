package com.tetrapak.supplierportal.core.mock;

import java.util.List;
import java.util.Map;

import com.tetrapak.supplierportal.core.services.InvoiceStatusService;

public class MockInvoiceStatusServiceImpl implements InvoiceStatusService{

	@Override
	public Map<String, List<String>> invoiceStatusCodeMap() {
		return null;
	}

	@Override
	public int getFromToDateGapInMonthsVal() {
		return 3;
	}

}
