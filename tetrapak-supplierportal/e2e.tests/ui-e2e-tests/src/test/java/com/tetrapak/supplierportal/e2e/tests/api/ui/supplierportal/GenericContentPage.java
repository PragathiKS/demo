package com.tetrapak.supplierportal.e2e.tests.api.ui.supplierportal;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class GenericContentPage {
    public static final String TETRAPAK_COM_FOOTER_LINK_TEXT = "Tetrapak.com";
    public static final String COOKIE_POLICY_FOOTER_LINK_TEXT = "Cookie Policy";
    public static final String TERMS_CONDITIONS_FOOTER_LINK_TEXT = "Terms & Conditions";
    public static final String CHANGE_LANGUAGE_FOOTER_LINK_TEXT = "Change language";

    protected final Page page;
    protected final Locator header;
    protected final Locator logo;
    protected final Locator mobileLogo;
    protected final Locator menuButton;
    protected final Locator footer;

    public GenericContentPage(Page page) {
        this.page = page;
        header = page.locator("header.tp-header");
        mobileLogo = page.locator(".tp-header__logo.d-md-none img");
        logo = page.locator(".tp-header__logo.d-md-flex img");
        footer = page.locator("footer.tp-pw-footer");
        menuButton = page.locator("span.tp-header__burger-menu");
    }

    public GenericContentPage goTo() {
        page.navigate("https://supplier-qa.tetrapak.com/dashboard.html");
        verifyPageContent();
        verifyFooter();
        return this;
    }

    protected void verifyPageContent() {
        assertThat(header).isVisible();
        assertThat(footer).isVisible();
        assertThat(menuButton).isHidden();
    }

    public Locator getHeader() {
        return header;
    }

    public Locator getLogo() {
        return logo;
    }

    public Locator getMenuButton() {
        return menuButton;
    }

    public Locator getFooter() {
        return footer;
    }

    public Locator getMobileLogo() {
        return mobileLogo;
    }

    public Page getPage() {
        return page;
    }

    protected void verifyFooter() {
        assertThat(footer.locator("a:has-Text(\"" + TETRAPAK_COM_FOOTER_LINK_TEXT + "\")")).isVisible();
        assertThat(footer.locator("a:has-Text(\"" + COOKIE_POLICY_FOOTER_LINK_TEXT + "\")")).isVisible();
        assertThat(footer.locator("a:has-Text(\"" + TERMS_CONDITIONS_FOOTER_LINK_TEXT + "\")")).isVisible();
        assertThat(footer.locator("a:has-Text(\"" + CHANGE_LANGUAGE_FOOTER_LINK_TEXT + "\")")).isVisible();
    }
}
