package com.tetrapak.supplierportal.e2e.tests.api.ui.engine;

import com.microsoft.playwright.Browser;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractMobilePlaywrightE2EConfig extends AbstractPlaywrightE2EConfig {
    @BeforeEach void setup() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(390, 844)
                .setIsMobile(true));
        page = context.newPage();
    }
}
