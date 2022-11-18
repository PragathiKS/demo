package com.tetrapak.supplierportal.e2e.tests.api.ui.access;

import com.tetrapak.supplierportal.e2e.tests.api.ui.engine.AbstractPlaywrightE2EConfig;
import org.junit.jupiter.api.Test;

class RequestAccessFormTest extends AbstractPlaywrightE2EConfig {
    @Test
    void myFirstTest() {
            AccessFormPage accessFormPage = new AccessFormPage(page);
            accessFormPage.goToAndCloseCookieDialog();
    }
}
