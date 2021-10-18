package com.tetrapak.publicweb.core.services.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;
import com.tetrapak.publicweb.core.beans.ContactUs;
import com.tetrapak.publicweb.core.beans.DropdownOption;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.CountryDetailService;

/**
 * The Class CountryDetailServiceImpl.
 */
@Component(service = CountryDetailService.class, immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = CountryDetailServiceImpl.CountryDetailServiceConfig.class)
public class CountryDetailServiceImpl implements CountryDetailService {

    /** The config. */
    private CountryDetailServiceConfig countryConfig;

    /**
     * The Interface CountryDetailServiceConfig.
     */
    @ObjectClassDefinition(
            name = "Public Web Country Detail Service Configuration",
            description = "Public Web Country Detail Service Configuration")
    @interface CountryDetailServiceConfig {

        /**
         * Gets the countries content fragment root path.
         *
         * @return the countries content fragment root path
         */
        @AttributeDefinition(
                name = "Countries Content Fragment Root Path",
                description = "countries Content Fragment Root Path")
        String getCountriesContentFragmentRootPath() default "/content/dam/tetrapak/publicweb/contentfragment/countries";
        
        @AttributeDefinition(
                name = "Pardot Countries Content Fragment Root Path",
                description = "Pardot countries Content Fragment Root Path")
        String getPardotCountriesCFRootPath() default "/content/dam/tetrapak/publicweb/contentfragment/pardot-countries";

    }

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CountryDetailServiceImpl.class);

    /** The data root path. */
    private static final String DATA_ROOT_PATH = "/jcr:content/data/master";

    /**
     * activate method.
     *
     * @param config
     *            site Improve Script URL configuration
     */
    @Activate
    public void activate(final CountryDetailServiceConfig countryConfig) {
        this.countryConfig = countryConfig;
    }

    /**
     * Sets the country title.
     *
     * @param countryTitle
     *            the country title
     * @param jcrResource
     *            the jcr resource
     * @return the string
     */
    private String setCountryTitle(final Resource jcrResource) {
        return jcrResource.getValueMap().get(JcrConstants.JCR_TITLE, StringUtils.EMPTY);
    }

    /**
     * Gets the country cf root path.
     *
     * @return the country cf root path
     */
    @Override
    public String getCountryCfRootPath() {
        return countryConfig.getCountriesContentFragmentRootPath();
    }

    @Override
    public String[] fetchContactEmailAddresses(final ContactUs contactUs, final ResourceResolver resourceResolver) {
        LOGGER.debug("Inside fetch contact email Addess - Start");
        String[] contactEmails = null;
        final String countryDataPath = getCountryCfRootPath() + PWConstants.SLASH + contactUs.getCountry()
                + DATA_ROOT_PATH;
        final Resource countryDataResource = resourceResolver.getResource(countryDataPath);
        if (Objects.nonNull(countryDataResource)) {
            contactEmails = (String[]) countryDataResource.getValueMap()
                    .get(contactUs.getPurposeOfContact() + "emails");
        }
        return contactEmails;
    }

    public List<DropdownOption> fetchCountryList(final ResourceResolver resourceResolver, String formType) {
        final List<DropdownOption> countryList = new ArrayList<>();
        final Resource countriesRootRes = resourceResolver.getResource(getCountryCfRootPath());
        if (Objects.nonNull(countriesRootRes)) {
            final Iterator<Resource> rootIterator = countriesRootRes.listChildren();
            while (rootIterator.hasNext()) {
                final Resource childResource = rootIterator.next();
                if (Objects.nonNull(childResource) && !childResource.getPath().contains(JcrConstants.JCR_CONTENT)) {
                    final Resource jcrResource = childResource.getChild(JcrConstants.JCR_CONTENT);
                    if (Objects.nonNull(jcrResource)) {
                        addCountry(countryList, childResource, jcrResource, formType);
                    }
                }
            }
        }
        countryList
                .sort((final DropdownOption op1, final DropdownOption op2) -> op1.getValue().compareTo(op2.getValue()));
        return countryList;
    }

    /**
     * @param countryList
     * @param childResource
     * @param jcrResource
     */
    private void addCountry(final List<DropdownOption> countryList, final Resource childResource,
            final Resource jcrResource, String formType) {
        final String title = setCountryTitle(jcrResource);
        if (!title.contains("Corporate")) {
            final DropdownOption country = new DropdownOption();
            if(Objects.nonNull(formType) && formType.equalsIgnoreCase(PWConstants.SOFT_CONVERSION)) {
                country.setKey(title);
                country.setValue(title);
            } else {
                country.setKey(childResource.getName());
                country.setValue(title);
            }            
            countryList.add(country);
        }
    }

	/**
	 * Fetch pardot country list.
	 *
	 * @param resourceResolver the resource resolver
	 * @return the list
	 */
	@Override
	public List<DropdownOption> fetchPardotCountryList(ResourceResolver resourceResolver, String formType) {
		final List<DropdownOption> countryList = new ArrayList<>();
        final Resource countriesRootRes = resourceResolver.getResource(getPardotCountryCfRootPath());
        if (Objects.nonNull(countriesRootRes)) {
            final Iterator<Resource> rootIterator = countriesRootRes.listChildren();
            while (rootIterator.hasNext()) {
                final Resource childResource = rootIterator.next();
                if (Objects.nonNull(childResource) && !childResource.getPath().contains(JcrConstants.JCR_CONTENT)) {
                    final Resource jcrResource = childResource.getChild(JcrConstants.JCR_CONTENT);
                    if (Objects.nonNull(jcrResource)) {
                        addCountry(countryList, childResource, jcrResource, formType);
                    }
                }
            }
        }
        countryList
                .sort((final DropdownOption op1, final DropdownOption op2) -> op1.getValue().compareTo(op2.getValue()));
        return countryList;
	}

	/**
	 * Gets the pardot country cf root path.
	 *
	 * @return the pardot country cf root path
	 */
	@Override
	public String getPardotCountryCfRootPath() {
		return countryConfig.getPardotCountriesCFRootPath();
	}
}
