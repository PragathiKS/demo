package com.tetralaval.services;

import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolver;

import com.tetralaval.beans.Dropdown;

public interface FormService {

    String[] getIgnoreParameters();

    String[] getEmailTemplates();

    String[] getActionTypes();

    String getContactUsFragmentsPath();

    List<Dropdown> getEmailTemplatesAsDropdown();

    List<Dropdown> getActionTypesAsDropdown();

    List<Dropdown> getContactUsFragmentsAsDropdown(ResourceResolver resourceResolver);
}
