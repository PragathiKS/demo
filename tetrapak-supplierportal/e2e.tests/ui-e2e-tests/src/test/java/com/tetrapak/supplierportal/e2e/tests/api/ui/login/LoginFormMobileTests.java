package com.tetrapak.supplierportal.e2e.tests.api.ui.login;

import com.tetrapak.supplierportal.e2e.tests.api.ui.engine.AbstractMobilePlaywrightE2EConfig;
import com.tetrapak.supplierportal.e2e.tests.api.ui.engine.AbstractPlaywrightE2EConfig;
import org.junit.jupiter.api.Test;

public class LoginFormMobileTests extends AbstractMobilePlaywrightE2EConfig {
    //todo add login case
    @Test
    void basicLoginFormTest() {
        new LoginFormPage(page).goToAndCheckBasicMobileFields();
    }

    @Test
    void invalidCredentialsTypedInTest() {
        new LoginFormPage(page).goToAndCheckBasicMobileFields();
        LoginFormPage loginFormPage = new LoginFormPage(page);
        loginFormPage.getUsername().type("invalid user");
        loginFormPage.getPassword().type("invalid password");
        loginFormPage.getSignInButton().click();
        loginFormPage.verifyPingErrorDisplayedWithText("didn't recognize the username or password");
    }
}
