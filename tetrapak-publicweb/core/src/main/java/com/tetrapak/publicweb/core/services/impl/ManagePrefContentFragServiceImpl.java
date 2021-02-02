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
import com.tetrapak.publicweb.core.beans.DropdownOption;
import com.tetrapak.publicweb.core.services.ManagePrefContentFragService;

/**
 * @author ojaswarn
 * The Class ManagePrefCountryServiceImpl.
 */
@Component(service = ManagePrefContentFragService.class, immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = ManagePrefContentFragServiceImpl.ManagePrefContentFragServiceConfig.class)
public class ManagePrefContentFragServiceImpl implements ManagePrefContentFragService {

    /** The country config. */
    private ManagePrefContentFragServiceConfig managePrefCfConfig;

    /**
     * The Interface ManagePrefCountryServiceConfig.
     */
    @ObjectClassDefinition(
            name = "Public Web Manage Preference Country and Language Detail Service Configuration",
            description = "Public Web Manage Preference Country and Language Detail Service Configuration")
    @interface ManagePrefContentFragServiceConfig {

        /**
         * Gets the country content fragment root path.
         *
         * @return the country content fragment root path
         */
        @AttributeDefinition(
                name = "Manage Preference Countries Content Fragment Root Path",
                description = "Manage Preference countries Content Fragment Root Path")
        String getCountryContentFragmentRootPath() default "/content/dam/tetrapak/publicweb/contentfragment/manage-preference-countries";
        
        /**
         * Gets the language content fragment root path.
         *
         * @return the language content fragment root path
         */
        @AttributeDefinition(
                name = "Manage Preference Language Content Fragment Root Path",
                description = "Manage Preference language Content Fragment Root Path")
        String getLanguageContentFragmentRootPath() default "/content/dam/tetrapak/publicweb/contentfragment/manage-preference-languages";

    }

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ManagePrefContentFragServiceImpl.class);

    /**
     * Activate.
     *
     * @param countryConfig the country config
     */
    @Activate
    public void activate(final ManagePrefContentFragServiceConfig countryConfig) {
        this.managePrefCfConfig = countryConfig;
    }

    /**
     * Sets the country title.
     *
     * @param jcrResource the jcr resource
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
        return managePrefCfConfig.getCountryContentFragmentRootPath();
    }
    
    /**
     * Gets the language cf root path.
     *
     * @return the language cf root path
     */
    @Override
	public String getLanguageCfRootPath() {
    	return managePrefCfConfig.getLanguageContentFragmentRootPath();
	}

    /**
     * Fetch country list.
     *
     * @param resourceResolver the resource resolver
     * @return the list
     */
    @Override
    public List<DropdownOption> fetchCountryList(final ResourceResolver resourceResolver) {
    	LOGGER.debug("Inside fetchCountryList of ManagePrefCountryServiceImpl");
    	return getCfDataList(resourceResolver, getCountryCfRootPath());
    }
    
    /**
     * Fetch language list.
     *
     * @param resourceResolver the resource resolver
     * @return the list
     */
    @Override
	public List<DropdownOption> fetchLanguageList(ResourceResolver resourceResolver) {
    	LOGGER.debug("Inside fetchLanguageList of ManagePrefCountryServiceImpl");
		return getCfDataList(resourceResolver, getLanguageCfRootPath());
	}
    
    /**
     * Gets the cf data list.
     *
     * @param resourceResolver the resource resolver
     * @param cfRootPath the cf root path
     * @return the cf data list
     */
    private List<DropdownOption> getCfDataList (ResourceResolver resourceResolver, String cfRootPath) {
        final List<DropdownOption> dataList = new ArrayList<>();
        final Resource cfRootResource = resourceResolver.getResource(cfRootPath);
        if (Objects.nonNull(cfRootResource)) {
            final Iterator<Resource> rootIterator = cfRootResource.listChildren();
            while (rootIterator.hasNext()) {
                final Resource childResource = rootIterator.next();
                if (Objects.nonNull(childResource) && !childResource.getPath().contains(JcrConstants.JCR_CONTENT)) {
                    final Resource jcrResource = childResource.getChild(JcrConstants.JCR_CONTENT);
                    if (Objects.nonNull(jcrResource)) {
                        addEntryToList(dataList, childResource, jcrResource);
                    }
                }
            }
        }
        dataList.sort((final DropdownOption op1, final DropdownOption op2) -> op1.getValue().compareTo(op2.getValue()));
        return dataList;
    }

    /**
     * Adds the entry to list.
     *
     * @param dataList the data list
     * @param childResource the child resource
     * @param jcrResource the jcr resource
     */
    private void addEntryToList(final List<DropdownOption> dataList, final Resource childResource,
        final Resource jcrResource) {
        final DropdownOption optionList = new DropdownOption();
        optionList.setKey(childResource.getName());
        optionList.setValue(setCountryTitle(jcrResource));
        dataList.add(optionList);
    }
}
