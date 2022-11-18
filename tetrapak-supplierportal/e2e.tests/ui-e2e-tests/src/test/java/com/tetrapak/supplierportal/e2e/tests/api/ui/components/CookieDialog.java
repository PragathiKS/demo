package com.tetrapak.supplierportal.e2e.tests.api.ui.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CookieDialog {
    private final Page page;
    private final Locator cookiesDescription;
    private final Locator cookieConsentButton;


    public CookieDialog(Page page) {
        this.page = page;
        cookiesDescription = page.locator("div[aria-label=\"cookieconsent\"]");
        cookieConsentButton = page.locator("div[aria-label=\"cookieconsent\"] .cc-compliance a");
    }

    public void closeCookieConsentDialog() {
        assertThat(cookiesDescription).isVisible();
        assertThat(cookiesDescription).containsText("We are using cookies. By continuing");
        assertThat(cookieConsentButton).isVisible();

        cookieConsentButton.click();

        assertThat(page.locator("div[role=\"dialog\"]")).isHidden();
        assertThat(cookiesDescription).isHidden();
        assertThat(cookieConsentButton).isHidden();
    }
}
