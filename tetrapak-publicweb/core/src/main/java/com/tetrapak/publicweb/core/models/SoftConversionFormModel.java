package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SoftConversionFormModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoftConversionFormModel.class);

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

    @Inject
    private String[] radioButtonGroups;

    public String getFirstNameLabel() {
        if (firstNameLabel != null) {
            return firstNameLabel;
        }
        return "First Name";
    }

    public String getLastNameLabel() {
        if (lastNameLabel != null) {
            return lastNameLabel;
        }
        return "Last Name";
    }

    public String getEmailAddressLabel() {
        if (emailAddressLabel != null) {
            return emailAddressLabel;
        }
        return "Email Address";
    }

    public String getCompanyLabel() {
        if (companyLabel != null) {
            return companyLabel;
        }
        return "Company";
    }

    public String getPositionLabel() {
        if (positionLabel != null) {
            return positionLabel;
        }
        return "Position";
    }

    public String getPreviousButtonLabel() {
        return previousButtonLabel;
    }

    public String getNextButtonLabel() {
        return nextButtonLabel;
    }

    public String getSubmitButtonLabel() {
        return submitButtonLabel;
    }

    public List<String> getRadioButtonGroups() {
        return getRadioButtonGroups(radioButtonGroups);
    }

    /**
     * Method to get the tab link text from the multifield property saved in CRX for
     * each of the radio button groups.
     *
     * @param radioButtonGroups radio button group
     * @return List<String> list of string
     */
    public static List<String> getRadioButtonGroups(String[] radioButtonGroups) {
        List<String> radioButtons = new ArrayList<>();
        JSONObject jObj;
        try {
            if (radioButtonGroups == null) {
                LOGGER.error("Radio Button Groups value is NULL");
            } else {
                for (int i = 0; i < radioButtonGroups.length; i++) {
                    jObj = new JSONObject(radioButtonGroups[i]);

                    if (jObj.has("radiobuttonTitle")) {
                        radioButtons.add(jObj.getString("radiobuttonTitle"));
                    }
                }
            }
        } catch (JSONException e) {
            LOGGER.error("Exception while Multifield data {}", e.getMessage(), e);
        }
        return radioButtons;
    }

}
