package com.tetrapak.supplierportal.e2e.tests.api.ui.supplierportal;

import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class GenericMobileContentPage extends GenericContentPage {
    public GenericMobileContentPage(Page page) {
        super(page);
    }

    protected void verifyPageContent() {
        assertThat(header).isVisible();
        assertThat(logo).isVisible();
        assertThat(footer).isVisible();
        assertThat(menuButton).isVisible();
    }
}
