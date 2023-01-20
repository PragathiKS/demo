package com.tetrapak.supplierportal.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.tetrapak.supplierportal.core.multifield.FAQModel;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AccordionModel {

	/** The request. */
	@Self
	private Resource resource;

	/** Heading. */
	@Inject
	private String heading;

	@Inject
	private List<FAQModel> questionList;
	
	/**
	 * Gets the heading.
	 *
	 * @return the heading
	 */
	public String getHeading() {
		return heading;
	}
	

	/**
	 * Gets the Question List.
	 *
	 * @return lists
	 */
	public List<FAQModel> getQuestionList() {
		final List<FAQModel> lists = new ArrayList<>();
		if (Objects.nonNull(questionList)) {
			lists.addAll(questionList);
		}
		return lists;

	}
}
