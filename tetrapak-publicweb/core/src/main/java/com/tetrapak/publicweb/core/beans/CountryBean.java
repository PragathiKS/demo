package com.tetrapak.publicweb.core.beans;

import java.util.List;

/**
 * The Class CountryBean.
 */
public class CountryBean {

    /** The longitude. */
    private Double longitude;

    /** The latitude. */
    private Double latitude;

    /** The offices. */
    private List<OfficeBean> offices;

    /**
     * Gets the longitude.
     *
     * @return the longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude.
     *
     * @param longitude
     *            the new longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the latitude.
     *
     * @return the latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude.
     *
     * @param latitude
     *            the new latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the offices.
     *
     * @return the offices
     */
    public List<OfficeBean> getOffices() {
        return offices;
    }

    /**
     * Sets the offices.
     *
     * @param offices
     *            the new offices
     */
    public void setOffices(List<OfficeBean> offices) {
        this.offices = offices;
    }

}
