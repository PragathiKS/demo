package com.tetrapak.publicweb.core.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "purposeOfContact", "country", "firstName", "lastName", "email", "message" })
public class ContactUs {

    @JsonProperty("purposeOfContact")
    private String purposeOfContact;
    @JsonProperty("country")
    private String country;
    @JsonProperty("purposeOfContactTitle")
    private String purposeOfContactTitle;
    @JsonProperty("countryTitle")
    private String countryTitle;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("message")
    private String message;
    @JsonProperty("domainURL")
    private String domainURL;

    @JsonProperty("purposeOfContact")
    public String getPurposeOfContact() {
        return purposeOfContact;
    }
    @JsonProperty("purposeOfContact")
    public void setPurposeOfContact(final String purposeOfContact) {
        this.purposeOfContact = purposeOfContact;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(final String country) {
        this.country = country;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("firstName")
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("lastName")
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(final String email) {
        this.email = email;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(final String message) {
        this.message = message;
    }

    @JsonProperty("purposeOfContactTitle")
    public String getPurposeOfContactTitle() {
        return purposeOfContactTitle;
    }

    @JsonProperty("purposeOfContactTitle")
    public void setPurposeOfContactTitle(final String purposeOfContactTitle) {
        this.purposeOfContactTitle = purposeOfContactTitle;
    }

    @JsonProperty("countryTitle")
    public String getCountryTitle() {
        return countryTitle;
    }

    @JsonProperty("countryTitle")
    public void setCountryTitle(final String countryTitle) {
        this.countryTitle = countryTitle;
    }

    /**
     * @return the domainURL
     */
    @JsonProperty("domainURL")
    public String getDomainURL() {
        return domainURL;
    }

    /**
     * @param domainURL
     *            the domainURL to set
     */
    @JsonProperty("domainURL")
    public void setDomainURL(final String domainURL) {
        this.domainURL = domainURL;
    }



}
