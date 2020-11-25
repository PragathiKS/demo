package com.tetrapak.publicweb.core.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class ContactUs.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "purposeOfContact", "country", "firstName", "lastName", "email", "message" })
public class ContactUs {

    /** The purpose of contact. */
    @JsonProperty("purposeOfContact")
    private String purposeOfContact;

    /** The country. */
    @JsonProperty("country")
    private String country;

    /** The purpose of contact title. */
    @JsonProperty("purposeOfContactTitle")
    private String purposeOfContactTitle;

    /** The country title. */
    @JsonProperty("countryTitle")
    private String countryTitle;

    /** The first name. */
    @JsonProperty("firstName")
    private String firstName;

    /** The last name. */
    @JsonProperty("lastName")
    private String lastName;

    /** The email. */
    @JsonProperty("email")
    private String email;

    /** The message. */
    @JsonProperty("message")
    private String message;

    /** The domain URL. */
    @JsonProperty("domainURL")
    private String domainURL;

    /**
     * Gets the purpose of contact.
     *
     * @return the purpose of contact
     */
    @JsonProperty("purposeOfContact")
    public String getPurposeOfContact() {
        return purposeOfContact;
    }

    /**
     * Sets the purpose of contact.
     *
     * @param purposeOfContact
     *            the new purpose of contact
     */
    @JsonProperty("purposeOfContact")
    public void setPurposeOfContact(final String purposeOfContact) {
        this.purposeOfContact = purposeOfContact;
    }

    /**
     * Gets the country.
     *
     * @return the country
     */
    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country.
     *
     * @param country
     *            the new country
     */
    @JsonProperty("country")
    public void setCountry(final String country) {
        this.country = country;
    }

    /**
     * Gets the first name.
     *
     * @return the first name
     */
    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName
     *            the new first name
     */
    @JsonProperty("firstName")
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name.
     *
     * @return the last name
     */
    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     *
     * @param lastName
     *            the new last name
     */
    @JsonProperty("lastName")
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the email.
     *
     * @return the email
     */
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param email
     *            the new email
     */
    @JsonProperty("email")
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     *
     * @param message
     *            the new message
     */
    @JsonProperty("message")
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * Gets the purpose of contact title.
     *
     * @return the purpose of contact title
     */
    @JsonProperty("purposeOfContactTitle")
    public String getPurposeOfContactTitle() {
        return purposeOfContactTitle;
    }

    /**
     * Sets the purpose of contact title.
     *
     * @param purposeOfContactTitle
     *            the new purpose of contact title
     */
    @JsonProperty("purposeOfContactTitle")
    public void setPurposeOfContactTitle(final String purposeOfContactTitle) {
        this.purposeOfContactTitle = purposeOfContactTitle;
    }

    /**
     * Gets the country title.
     *
     * @return the country title
     */
    @JsonProperty("countryTitle")
    public String getCountryTitle() {
        return countryTitle;
    }

    /**
     * Sets the country title.
     *
     * @param countryTitle
     *            the new country title
     */
    @JsonProperty("countryTitle")
    public void setCountryTitle(final String countryTitle) {
        this.countryTitle = countryTitle;
    }

    /**
     * Gets the domain URL.
     *
     * @return the domainURL
     */
    @JsonProperty("domainURL")
    public String getDomainURL() {
        return domainURL;
    }

    /**
     * Sets the domain URL.
     *
     * @param domainURL
     *            the domainURL to set
     */
    @JsonProperty("domainURL")
    public void setDomainURL(final String domainURL) {
        this.domainURL = domainURL;
    }

}
