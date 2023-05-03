package com.tetralaval.services.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.tetralaval.beans.Dropdown;
import com.tetralaval.config.FormConfiguration;
import com.tetralaval.services.FormService;

@Component(immediate = true, service = FormService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = FormConfiguration.class)
public class FormServiceImpl implements FormService {

    public static final int LENGTH = 2;
    private static final Logger LOGGER = LoggerFactory.getLogger(FormServiceImpl.class);

    private String[] ignoreParameters;
    private String[] emailTemplates;
    private String[] actionTypes;
    private String contactUsFragmentsPath;

    @Override
    public String[] getIgnoreParameters() {
        return ignoreParameters;
    }

    @Override
    public String[] getEmailTemplates() {
        return emailTemplates;
    }

    @Override
    public String[] getActionTypes() {
        return actionTypes;
    }

    @Override
    public List<Dropdown> getEmailTemplatesAsDropdown() {
        return getConfig(emailTemplates);
    }

    @Override
    public List<Dropdown> getActionTypesAsDropdown() {
        return getConfig(actionTypes);
    }

    @Activate
    protected void activate(FormConfiguration config) {
        ignoreParameters = config.ignoreParameters();
        emailTemplates = config.emailTemplates();
        actionTypes = config.actionTypes();
        contactUsFragmentsPath = config.contactUsFragmentsPath();

    }

    private List<Dropdown> getConfig(String[] actionTypesConfig) {
        List<Dropdown> types = new ArrayList<>();
        try {
            for (String actionType : actionTypesConfig) {
                LOGGER.debug("Action Type: {}", actionType);
                String[] values = StringUtils.split(actionType, ":");
                if (values.length == LENGTH) {
                    types.add(new Dropdown(StringUtils.trim(values[0]), StringUtils.trim(values[1])));
                } else {
                    LOGGER.debug("No ':' available in string to split: {}", actionType);
                }

            }
        } catch (Exception e) {
            LOGGER.error("Error while reading Form Datasource Configuration", e);
        }
        return types;
    }

    @Override
    public List<Dropdown> getContactUsFragmentsAsDropdown(ResourceResolver resourceResolver) {
        List<Dropdown> contactUsFragments = new ArrayList<>();
        Resource contactUsRoot = resourceResolver.getResource(contactUsFragmentsPath);
        LOGGER.debug("contactUsRoot: {}", contactUsRoot);
        if (null != contactUsRoot && contactUsRoot.hasChildren()) {
            Iterator<Resource> contactUsIterator = contactUsRoot.listChildren();
            while (contactUsIterator.hasNext()) {
                Resource child = contactUsIterator.next();
                LOGGER.debug("Content fragment path: {}", child.getPath());
                ContentFragment contentFragment = child.adaptTo(ContentFragment.class);
                if (null != contentFragment) {
                    String label = contentFragment.getTitle();
                    String name = contentFragment.getName();
                    LOGGER.debug("Content fragment Name and Title: {} and {}", name, label);
                    contactUsFragments.add(new Dropdown(label, name));
                }
            }

        }
        return contactUsFragments;

    }

    @Override
    public String getContactUsFragmentsPath() {
        return contactUsFragmentsPath;
    }
}