package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SoftConversionFormModel {

	private static final Logger log = LoggerFactory.getLogger(SoftConversionFormModel.class);

	@Inject
	private String titleI18n;

	@Inject
	private String description;

	@Inject
	private String headline;

	@Inject
	private String buttonLabel;

	@Inject
	private String[] radioButtonGroups;
	
	@Inject
	private String documentPath;

	@Inject
	private String firstNameLabel;

	@Inject
	private String lastNameLabel;

	@Inject
	private String emailAddressLabel;

	@Inject
	private String companyLabel;
	
	@Inject
	private String positionLabel;

	@Inject
	private String previousButtonLabel;

	@Inject
	private String nextButtonLabel;
	
	@Inject
	private String submitButtonLabel;

	public String getTitleI18n() {
		return titleI18n;
	}

	public String getDescription() {
		return description;
	}

	public String getHeadline() {
		return headline;
	}

	public String getButtonLabel() {
		return buttonLabel;
	}

	public List<String> getRadioButtonGroups() {
		return getRadioButtonGroups(radioButtonGroups);
	}

	public String getFirstNameLabel() {
		return firstNameLabel!=null ? firstNameLabel : "First Name";
	}

	public String getLastNameLabel() {
		return lastNameLabel!=null ? lastNameLabel : "Last Name";
	}

	public String getEmailAddressLabel() {
		return emailAddressLabel!=null ? emailAddressLabel : "Email Address";
	}

	public String getCompanyLabel() {
		return companyLabel!=null ? companyLabel : "Company";
	}
	
	public String getPositionLabel() {
		return positionLabel!=null ? positionLabel : "Position";
	}

	public String getPreviousButtonLabel() {
		return previousButtonLabel;
	}

	public String getNextButtonLabel() {
		return nextButtonLabel;
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public String getSubmitButtonLabel() {
		return submitButtonLabel;
	}

	/**
	 * Method to get the tab link text from the multifield property saved in CRX for
	 * each of the radio button groups.
	 *
	 * @param tabLinks String[]
	 * @return List<String>
	 */
	public static List<String> getRadioButtonGroups(String[] radioButtonGroups) {
		@SuppressWarnings("deprecation")
		List<String> radioButtons = new ArrayList<String>();
		JSONObject jObj;
		try {
			if (radioButtonGroups == null) {
				log.error("Radio Button Groups value is NULL");
			} else {
				for (int i = 0; i < radioButtonGroups.length; i++) {
					jObj = new JSONObject(radioButtonGroups[i]);

					if (jObj.has("radiobuttonTitle")) {
						radioButtons.add(jObj.getString("radiobuttonTitle"));
					}
				}
			}
		} catch (Exception e) {
			log.error("Exception while Multifield data {}", e.getMessage(), e);
		}
		return radioButtons;
	}

}
