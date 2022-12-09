package com.tetrapak.supplierportal.e2e.tests.api.ui.access;

import com.microsoft.playwright.Locator;
import com.tetrapak.supplierportal.e2e.tests.api.ui.engine.AbstractPlaywrightE2EConfig;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class RequestAccessFormTest extends AbstractPlaywrightE2EConfig {
    @Test
    void privacyPolicyLinkExistTest() {
        AccessFormPage accessFormPage = new AccessFormPage(page);
        accessFormPage.goToAndCloseCookieDialog();

        accessFormPage.getPage().locator("em:has-text(\"privacy policy\")").locator("a").click();

        assertThat(page.locator("div.breadcrumb span.tp_pw-breadcrumb__link.last"))
                .containsText("privacy policy ");
    }

    @Test
    void successfulSubmission() {
        AccessFormPage accessFormPage = new AccessFormPage(page);
        accessFormPage.goToAndCloseCookieDialog();

        typeValue(accessFormPage,
                accessFormPage.getEmailInput(),
                "shadi@test.com");

        typeValue(accessFormPage,
                accessFormPage.getFirstName(),
                "Shadi");

        typeValue(accessFormPage,
                accessFormPage.getLastName(),
                "My surname");

        typeValue(accessFormPage,
                accessFormPage.getCountry(),
                "Israel");

        typeValue(accessFormPage,
                accessFormPage.getCompany(),
                "Special Test Company");

        typeValue(accessFormPage,
                accessFormPage.getCompanyAddress(),
                "HaTnufa 4, Yokneam Illit");

        typeValue(accessFormPage,
                accessFormPage.getVendorNumber(),
                "55555");

        typeValue(accessFormPage,
                accessFormPage.getPhone(),
                "+972555555555");

        assertThat(accessFormPage.getUpdatesCheckbox()).not().isVisible();

        accessFormPage.getSubmitButton().click();
        accessFormPage.formIsSubmitted();
    }

    @Test
    void invalidData() {
        AccessFormPage accessFormPage = new AccessFormPage(page);

        submitEmptyForm(accessFormPage).isErrorCountValid(8);

        typeValueAndFormSubmit(accessFormPage,
                accessFormPage.getEmailInput(),
                "test@test.com")
                .isErrorCountValid(7);

        typeValueAndFormSubmit(accessFormPage,
                accessFormPage.getFirstName(),
                "John")
                .isErrorCountValid(6);

        typeValueAndFormSubmit(accessFormPage,
                accessFormPage.getLastName(),
                "Smith-Klein")
                .isErrorCountValid(5);

        typeValueAndFormSubmit(accessFormPage,
                accessFormPage.getCountry(),
                "Poland")
                .isErrorCountValid(4);

        typeValueAndFormSubmit(accessFormPage,
                accessFormPage.getCompany(),
                "Test Company")
                .isErrorCountValid(3);

        typeValueAndFormSubmit(accessFormPage,
                accessFormPage.getCompanyAddress(),
                "HaTnufa 4, Yokneam Illit")
                .isErrorCountValid(2);

        typeValueAndFormSubmit(accessFormPage,
                accessFormPage.getPhone(),
                "123 Text")
                .isErrorCountValid(2);

        accessFormPage.getPhone().fill("");

        typeValueAndFormSubmit(accessFormPage,
                accessFormPage.getPhone(),
                "+48 555555555")
                .isErrorCountValid(2);

        accessFormPage.getPhone().fill("");

        typeValueAndFormSubmit(accessFormPage,
                accessFormPage.getPhone(),
                "555555555")
                .isErrorCountValid(1);

        typeValue(accessFormPage,
                accessFormPage.getVendorNumber(),
                "55555");

        accessFormPage.getSubmitButton().click();
        accessFormPage.formIsSubmitted();
    }

    private AccessFormPage submitEmptyForm(AccessFormPage accessFormPage) {
        accessFormPage.goToAndCloseCookieDialog();
        accessFormPage.getSubmitButton().click();

        return accessFormPage;
    }

    private AccessFormPage typeValue(AccessFormPage accessFormPage, Locator locator, String valueToType) {
        locator.type(valueToType);
        return accessFormPage;
    }

    private AccessFormPage typeValueAndFormSubmit(AccessFormPage accessFormPage, Locator locator, String valueToType) {
        typeValue(accessFormPage, locator, valueToType);
        accessFormPage.getSubmitButton().click();

        return accessFormPage;
    }
}
