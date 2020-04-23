package com.tetrapak.publicweb.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;
import com.tetrapak.publicweb.core.beans.CountryBean;
import com.tetrapak.publicweb.core.beans.OfficeBean;
import com.tetrapak.publicweb.core.services.FindMyOfficeService;

/**
 * The Class FindMyOfficeServiceImpl.
 */
@Component(
        service = FindMyOfficeServiceImpl.class,
        immediate = true,
        configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = FindMyOfficeService.class)
public class FindMyOfficeServiceImpl {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FindMyOfficeServiceImpl.class);

    /** The data root path. */
    private static String DATA_ROOT_PATH = "/jcr:content/data/master";

    /** The country list. */
    private Map<String, CountryBean> countryOfficeList = new HashMap<>();

    /** The countries root path. */
    private String countriesRootPath;

    /** The offices root path. */
    private String officesRootPath;

    /** The google api key. */
    private String googleApiKey;

    /**
     * Activate.
     *
     * @param findMyOfficeService
     *            the find my office service
     */
    @Activate
    @Modified
    protected void activate(FindMyOfficeService findMyOfficeService) {
        countriesRootPath = findMyOfficeService.getCountriesContentFragmentRootPath();
        officesRootPath = findMyOfficeService.getOfficesContentFragmentRootPath();
        googleApiKey = findMyOfficeService.getGoogleAPIKey();

    }

    /**
     * Gets the find my office data.
     *
     * @param resourceResolver
     *            the resource resolver
     * @return the find my office data
     */
    public Map<String, CountryBean> getFindMyOfficeData(ResourceResolver resourceResolver) {
        LOGGER.debug("Inside getFindMyOfficeData method");
        Resource countriesRootRes = resourceResolver.getResource(countriesRootPath);
        if (Objects.nonNull(countriesRootRes)) {
            final Iterator<Resource> rootIterator = countriesRootRes.listChildren();
            while (rootIterator.hasNext()) {
                final CountryBean countryBean = new CountryBean();
                final Resource childResource = rootIterator.next();
                if (Objects.nonNull(childResource) && !childResource.getPath().contains(JcrConstants.JCR_CONTENT)) {
                    String countryTitle = StringUtils.EMPTY;
                    final String jcrPath = childResource.getPath() + "/" + JcrConstants.JCR_CONTENT;
                    Resource jcrResource = resourceResolver.getResource(jcrPath);
                    countryTitle = setCountryTitle(countryTitle, jcrResource);
                    final String countryName = childResource.getName();
                    final String dataPath = childResource.getPath() + DATA_ROOT_PATH;
                    Resource dataResource = resourceResolver.getResource(dataPath);
                    setCountryBean(resourceResolver, countryBean, countryName, dataResource);
                    countryOfficeList.put(countryTitle, countryBean);
                }

            }

        }

        return countryOfficeList;
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
    private void setCountryBean(ResourceResolver resourceResolver, final CountryBean countryBean,
            final String countryName, Resource dataResource) {
        if (Objects.nonNull(dataResource)) {
            final ValueMap vMap = dataResource.getValueMap();
            countryBean.setLongitude(vMap.get("longitude", Double.class));
            countryBean.setLatitude(vMap.get("latitude", Double.class));
            final String countryPath = officesRootPath + "/" + countryName;
            Resource countryResource = resourceResolver.getResource(countryPath);
            List<OfficeBean> officeBeanList = new ArrayList<>();
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
    private void setOfficeBean(List<OfficeBean> officeBeanList, final OfficeBean officeBean,
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
    private String setCountryTitle(String countryTitle, Resource jcrResource) {
        if (Objects.nonNull(jcrResource)) {
            countryTitle = jcrResource.getValueMap().get(JcrConstants.JCR_TITLE, StringUtils.EMPTY);
        }
        return countryTitle;
    }

    /**
     * Gets the google api key.
     *
     * @return the google api key
     */
    public String getGoogleApiKey() {
        return googleApiKey;
    }
}
