package com.tetrapak.supplierportal.core.bean;

import java.util.List;

public class PaymentDetailResponse {
	
	private Meta meta;
	
	private List<PaymentDetails> data;

	public List<PaymentDetails> getData() {
		return data;
	}

	public void setData(List<PaymentDetails> data) {
		this.data = data;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}
	
}
