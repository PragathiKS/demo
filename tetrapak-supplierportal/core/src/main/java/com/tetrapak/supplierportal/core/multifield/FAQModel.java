package com.tetrapak.supplierportal.core.multifield;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FAQModel {
	
	/** The resource. */
	@Self
	private Resource request;
	
	/** The questionNo. */
	@ValueMapValue
	private String quesNo;

	/** The questionDetail. */
	@ValueMapValue
	private String quesDetail;

	/**
	 * Gets the question No.
	 *
	 * @return the question No.
	 */
	public String getQuestionNo() {
		return quesNo;
	}

	/**
	 * Gets the question Detail.
	 *
	 * @return the question Detail
	 */
	public String getQuestionDetail() {
		return quesDetail;
	}


}
