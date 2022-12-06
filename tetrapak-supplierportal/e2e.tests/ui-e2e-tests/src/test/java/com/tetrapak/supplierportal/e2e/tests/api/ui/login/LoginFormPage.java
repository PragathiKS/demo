package com.tetrapak.supplierportal.e2e.tests.api.ui.login;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginFormPage {
    private final Page page;
    private final Locator pingHeader;
    private final Locator username;
    private final Locator password;
    private final Locator rememberMe;
    private final Locator signInButton;
    private final Locator cfeContainerRight;

    private final Locator pingMessages;

    public LoginFormPage(Page page) {
        this.page = page;
        pingHeader = page.locator("div.ping-header");
        username = page.locator("#username");
        password = page.locator("#password");
        rememberMe = page.locator(".remember-username");
        signInButton = page.locator("#signInButton");
        cfeContainerRight = page.locator(".cfe-content-container-right");
        pingMessages = page.locator(".ping-messages");
    }

    public void goToAndCheckBasicFields() {
        page.navigate("https://sso.tetrapak.com/idp/startSSO.ping?PartnerSpId=MyTetraPak");
        assertThat(pingHeader).containsText("Login to My Tetra Pak");
        assertThat(username).isVisible();
        assertThat(password).isVisible();
        assertThat(rememberMe).isVisible();
        assertThat(signInButton).isVisible();
        assertThat(cfeContainerRight).containsText("Change Password");
        assertThat(cfeContainerRight).containsText("Forgot your password? Account locked out");
        assertThat(cfeContainerRight).containsText("Contact Tetra Pak");
        assertThat(cfeContainerRight).containsText("TetraPak.com");
        assertThat(cfeContainerRight).containsText("Sign up for e-Business Service Products");
        assertThat(cfeContainerRight).containsText("By logging in you agree to the Site Privacy Policy and Disclaimers");
    }

    public void verifyPingErrorDisplayedWithText(String errorTextContains) {
        assertThat(pingMessages.locator(".ping-error")).containsText(errorTextContains);
    }

    public Page getPage() {
        return page;
    }

    public Locator getPingHeader() {
        return pingHeader;
    }

    public Locator getUsername() {
        return username;
    }

    public Locator getPassword() {
        return password;
    }

    public Locator getRememberMe() {
        return rememberMe;
    }

    public Locator getSignInButton() {
        return signInButton;
    }

    public Locator getCfeContainerRight() {
        return cfeContainerRight;
    }
}
