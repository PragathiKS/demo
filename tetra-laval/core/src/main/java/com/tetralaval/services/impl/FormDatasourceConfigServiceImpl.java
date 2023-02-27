package com.tetralaval.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetralaval.beans.Dropdown;
import com.tetralaval.config.FormDatasourceConfiguration;
import com.tetralaval.services.FormDatasourceConfigService;

@Component(immediate = true, service = FormDatasourceConfigService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = FormDatasourceConfiguration.class)
public class FormDatasourceConfigServiceImpl implements FormDatasourceConfigService {

    /** LOGGER constant */
    private static final Logger LOGGER = LoggerFactory.getLogger(FormDatasourceConfigServiceImpl.class);

    private FormDatasourceConfiguration config;

    @Activate
    public void activate(FormDatasourceConfiguration config) {
	this.config = config;
    }

    @Override
    public List<Dropdown> getActionTypes() {
	String[] actionTypesConfig = config.actionTypes();
	return getConfig(actionTypesConfig);
    }

    private List<Dropdown> getConfig(String[] actionTypesConfig) {
	List<Dropdown> actionTypes = new ArrayList<>();
	try {

	    for (String actionType : actionTypesConfig) {
		String[] values = StringUtils.split(actionType, ":");
		if (values.length == 2) {
		    actionTypes.add(new Dropdown(StringUtils.trim(values[0]), StringUtils.trim(values[1])));
		}

	    }
	} catch (Exception e) {
	    LOGGER.error("Error while reading Form Datasource Configuration", e);
	}
	return actionTypes;
    }

    @Override
    public List<Dropdown> getEmailTemplates() {
	String[] emailTemplatesConfig = config.emailTemplates();
	return getConfig(emailTemplatesConfig);
    }
}
