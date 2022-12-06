package com.tetrapak.supplierportal.e2e.tests.api.ui.login;

import com.tetrapak.supplierportal.e2e.tests.api.ui.engine.AbstractPlaywrightE2EConfig;
import org.junit.jupiter.api.Test;

public class LoginFormTests extends AbstractPlaywrightE2EConfig {
    //todo add negative cases, login case, and mobile view test
    @Test
    void basicLoginFormTest() {
        new LoginFormPage(page).goToAndCheckBasicFields();
    }

    @Test
    void invalidCredentialsTypedInTest() {
        new LoginFormPage(page).goToAndCheckBasicFields();
        LoginFormPage loginFormPage = new LoginFormPage(page);
        loginFormPage.getUsername().type("invalid user");
        loginFormPage.getPassword().type("invalid password");
        loginFormPage.getSignInButton().click();
        loginFormPage.verifyPingErrorDisplayedWithText("didn't recognize the username or password");
    }
}
