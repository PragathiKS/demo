package com.tetrapak.publicweb.core.services.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
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
import com.tetrapak.publicweb.core.beans.CountryBean;
import com.tetrapak.publicweb.core.beans.DropdownOption;
import com.tetrapak.publicweb.core.beans.OfficeBean;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.FindMyOfficeService;

/**
 * The Class FindMyOfficeServiceImpl.
 */
@Component(service = FindMyOfficeService.class, immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = FindMyOfficeServiceImpl.FindMyOfficeServiceConfig.class)
public class FindMyOfficeServiceImpl implements FindMyOfficeService {

    /** The config. */
    private FindMyOfficeServiceConfig config;

    /**
     * The Interface FindMyOfficeServiceConfig.
     */
    @ObjectClassDefinition(name = "Find My Office Configuration", description = "Find My Office Service Configuration")
    @interface FindMyOfficeServiceConfig {

        /**
         * Gets the countries content fragment root path.
         *
         * @return the countries content fragment root path
         */
        @AttributeDefinition(
                name = "Countries Content Fragment Root Path",
                description = "countries Content Fragment Root Path")
        String getCountriesContentFragmentRootPath() default "/content/dam/tetrapak/findMyOffice/contentFragments/countries";

        /**
         * Gets the offices content fragment root path.
         *
         * @return the offices content fragment root path
         */
        @AttributeDefinition(
                name = "Offices Content Fragment Root Path",
                description = "Offices Content Fragment Root Path")
        String getOfficesContentFragmentRootPath() default "/content/dam/tetrapak/findMyOffice/contentFragments/offices";

        /**
         * Gets the google API key.
         *
         * @return the google API key
         */
        @AttributeDefinition(
                name = "Offices Content Fragment Root Path",
                description = "Offices Content Fragment Root Path")
        String getGoogleAPIKey() default "AIzaSyC1w2gKCuwiRCsgqBR9RnSbWNuFvI5lryQ";

        /**
         * Gets the google API key.
         *
         * @return the google API key
         */
        @AttributeDefinition(
                name = "Purpose of contact Content Fragment Root Path",
                description = "Purpose of contact  Content Fragment Root Path")
        String geturposeOfcontactRootPath() default "/content/dam/tetrapak/findMyOffice/contentFragments/purposeofcontact";

    }

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FindMyOfficeServiceImpl.class);

    /** The data root path. */
    private static String DATA_ROOT_PATH = "/jcr:content/data/master";

    /** The country list. */
    private final Map<String, CountryBean> countryOfficeList = new LinkedHashMap<>();

    /** The corporate office list. */
    private final Map<String, CountryBean> corporateOfficeList = new TreeMap<>();

    /**
     * activate method.
     *
     * @param config
     *            site Improve Script URL configuration
     */
    @Activate
    public void activate(final FindMyOfficeServiceConfig config) {
        this.config = config;
    }

    /**
     * Gets the find my office data.
     *
     * @param resourceResolver
     *            the resource resolver
     * @return the find my office data
     */
    @Override
    public Map<String, CountryBean> getFindMyOfficeData(final ResourceResolver resourceResolver) {
        LOGGER.debug("Inside getFindMyOfficeData method");
        final Resource countriesRootRes = resourceResolver.getResource(getCountryCfRootPath());
        if (Objects.nonNull(countriesRootRes)) {
            final Iterator<Resource> rootIterator = countriesRootRes.listChildren();
            while (rootIterator.hasNext()) {
                final CountryBean countryBean = new CountryBean();
                final Resource childResource = rootIterator.next();
                if (Objects.nonNull(childResource) && !childResource.getPath().contains(JcrConstants.JCR_CONTENT)) {
                    final Resource jcrResource = childResource.getChild(JcrConstants.JCR_CONTENT);
                    String countryTitle = StringUtils.EMPTY;
                    if (Objects.nonNull(jcrResource)) {
                        countryTitle = setCountryTitle(jcrResource);
                    }
                    final String countryName = childResource.getName();
                    final String dataPath = childResource.getPath() + DATA_ROOT_PATH;
                    final Resource dataResource = resourceResolver.getResource(dataPath);
                    setCountryBean(resourceResolver, countryBean, countryName, dataResource);
                    setLists(countryBean, countryTitle);
                }

            }

        }

        return countryOfficeList;
    }

    /**
     * Sets the lists.
     *
     * @param countryBean
     *            the country bean
     * @param countryTitle
     *            the country title
     */
    private void setLists(final CountryBean countryBean, final String countryTitle) {
        if (countryTitle.contains("Corporate")) {
            corporateOfficeList.put(countryTitle, countryBean);
        } else {
            countryOfficeList.put(countryTitle, countryBean);
        }
    }

    /**
     * Sets the country bean.
     *
     * @param resourceResolver
     *            the resource resolver
     * @param countryBean
     *            the country bean
     * @param countryName
     *            the country name
     * @param dataResource
     *            the data resource
     */
    private void setCountryBean(final ResourceResolver resourceResolver, final CountryBean countryBean,
            final String countryName, final Resource dataResource) {
        if (Objects.nonNull(dataResource)) {
            final ValueMap vMap = dataResource.getValueMap();
            countryBean.setLongitude(vMap.get("longitude", Double.class));
            countryBean.setLatitude(vMap.get("latitude", Double.class));
            final String countryPath = getOfficeCfRootPath() + "/" + countryName;
            final Resource countryResource = resourceResolver.getResource(countryPath);
            final List<OfficeBean> officeBeanList = new ArrayList<>();
            if (Objects.nonNull(countryResource)) {
                final Iterator<Resource> officeIterator = countryResource.listChildren();
                while (officeIterator.hasNext()) {
                    final OfficeBean officeBean = new OfficeBean();
                    final Resource officeResource = officeIterator.next();
                    final String officeDataPath = officeResource.getPath() + DATA_ROOT_PATH;
                    final Resource officeDataResource = resourceResolver.getResource(officeDataPath);
                    setOfficeBean(officeBeanList, officeBean, officeDataResource);
                }

            }
            countryBean.setOffices(officeBeanList);
        }
    }

    /**
     * Sets the office bean.
     *
     * @param officeBeanList
     *            the office bean list
     * @param officeBean
     *            the office bean
     * @param officeDataResource
     *            the office data resource
     */
    private void setOfficeBean(final List<OfficeBean> officeBeanList, final OfficeBean officeBean,
            final Resource officeDataResource) {
        if (Objects.nonNull(officeDataResource)) {
            final ValueMap officeMap = officeDataResource.getValueMap();
            officeBean.setName(officeMap.get("name", StringUtils.EMPTY));
            officeBean.setAddress(officeMap.get("address", StringUtils.EMPTY));
            officeBean.setPhoneNumber(officeMap.get("phoneNumber", StringUtils.EMPTY));
            officeBean.setFax(officeMap.get("fax", StringUtils.EMPTY));
            officeBean.setLatitude(officeMap.get("latitude", Double.class));
            officeBean.setLongitude(officeMap.get("longitude", Double.class));
            officeBean.setLocalSiteUrl(officeMap.get("localSiteUrl", StringUtils.EMPTY));
            officeBeanList.add(officeBean);
        }
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
     * Gets the google api key.
     *
     * @return the google api key
     */
    @Override
    public String getGoogleApiKey() {
        return config.getGoogleAPIKey();
    }

    /**
     * Gets the country cf root path.
     *
     * @return the country cf root path
     */
    @Override
    public String getCountryCfRootPath() {
        return config.getCountriesContentFragmentRootPath();
    }

    /**
     * Gets the office cf root path.
     *
     * @return the office cf root path
     */
    @Override
    public String getOfficeCfRootPath() {
        return config.getOfficesContentFragmentRootPath();
    }

    /**
     * Gets the corporate office list.
     *
     * @return the corporate office list
     */
    @Override
    public Map<String, CountryBean> getCorporateOfficeList() {
        return corporateOfficeList;
    }

    @Override
    public String[] fetchContactEmailAddresses(final ContactUs contactUs, final ResourceResolver resourceResolver) {
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

    @Override
    public List<DropdownOption> fetchCountryList(final ResourceResolver resourceResolver) {
        final List<DropdownOption> countryList = new ArrayList<>();
        final Resource countriesRootRes = resourceResolver.getResource(getCountryCfRootPath());
        if (Objects.nonNull(countriesRootRes)) {
            final Iterator<Resource> rootIterator = countriesRootRes.listChildren();
            while (rootIterator.hasNext()) {
                final Resource childResource = rootIterator.next();
                if (Objects.nonNull(childResource) && !childResource.getPath().contains(JcrConstants.JCR_CONTENT)) {
                    final Resource jcrResource = childResource.getChild(JcrConstants.JCR_CONTENT);
                    if (Objects.nonNull(jcrResource)) {
                        final DropdownOption country = new DropdownOption();
                        country.setKey(childResource.getName());
                        country.setValue(setCountryTitle(jcrResource));
                        countryList.add(country);
                    }
                }
            }
        }
        countryList
                .sort((final DropdownOption op1, final DropdownOption op2) -> op1.getValue().compareTo(op2.getValue()));
        return countryList;
    }

}
