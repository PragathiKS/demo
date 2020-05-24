package com.tetrapak.publicweb.core.services;

import java.util.List;

import org.apache.sling.api.resource.ResourceResolver;

import com.tetrapak.publicweb.core.beans.ContactUs;
import com.tetrapak.publicweb.core.beans.DropdownOption;

public interface CountryDetailService {

    List<DropdownOption> fetchCountryList(ResourceResolver resourceResolver);

    String[] fetchContactEmailAddresses(ContactUs contactUs, ResourceResolver resourceResolver);

    String getCountryCfRootPath();

}
