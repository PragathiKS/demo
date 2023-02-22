package com.tetrapak.supplierportal.e2e.tests.api.ui.access;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.tetrapak.supplierportal.e2e.tests.api.ui.components.CookieDialog;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AccessFormPage {
    private final Page page;
    private final Locator loginForm;
    private final Locator emailInput;
    private final Locator firstName;
    private final Locator lastName;
    private final Locator country;
    private final Locator company;
    private final Locator companyAddress;
    private final Locator vendorNumber;
    private final Locator phone;
    private final Locator updatesCheckbox;
    private final Locator submitButton;
    private final CookieDialog cookieDialog;

    public AccessFormPage(Page page) {
        this.page = page;
        cookieDialog = new CookieDialog(page);
        loginForm = page.locator("#pardot-form");
        emailInput = page.locator("#pardot-form p.form-field.email input");
        firstName = page.locator("#pardot-form p.form-field.first_name input");
        lastName = page.locator("#pardot-form p.form-field.last_name input");
        country = page.locator("#pardot-form p.form-field.country .select");
        company = page.locator("#pardot-form p.form-field.company input");
        companyAddress = page.locator("#pardot-form p.form-field.address_one input");
        vendorNumber = page.locator("#pardot-form p.form-field.Supplier_Portal_Vendor_Number input");

        phone = page.locator("#pardot-form p.form-field.phone input");
        updatesCheckbox = page.locator("#pardot-form p.form-field.Opt_in_Marketing_Communications.pd-checkbox");
        submitButton = page.locator("#pardot-form input[type=\"submit\"]");
    }

    public void formIsSubmitted() {
        assertThat(loginForm).containsText("Thank for applying to our Supplier Portal");
    }

    public void goToAndCloseCookieDialog() {
        page.navigate("https://go.tetrapak.com/supplier-portal/request-access");
        cookieDialog.closeCookieConsentDialog();
        assertThat(page.locator("#headline")).containsText("Supplier Portal");
    }

    public void isErrorCountValid(int expectedNumberOfErrors) {
        assertThat(loginForm.locator("p.error.no-label")).hasCount(expectedNumberOfErrors);
    }

    public Page getPage() {
        return page;
    }

    public Locator getLoginForm() {
        return loginForm;
    }

    public Locator getEmailInput() {
        return emailInput;
    }

    public Locator getFirstName() {
        return firstName;
    }

    public Locator getLastName() {
        return lastName;
    }

    public Locator getCountry() {
        return country;
    }

    public Locator getCompany() {
        return company;
    }

    public Locator getCompanyAddress() {
        return companyAddress;
    }

    public Locator getVendorNumber() {
        return vendorNumber;
    }

    public Locator getPhone() {
        return phone;
    }

    public Locator getUpdatesCheckbox() {
        return updatesCheckbox;
    }

    public Locator getSubmitButton() {
        return submitButton;
    }

    public CookieDialog getCookieDialog() {
        return cookieDialog;
    }
}
