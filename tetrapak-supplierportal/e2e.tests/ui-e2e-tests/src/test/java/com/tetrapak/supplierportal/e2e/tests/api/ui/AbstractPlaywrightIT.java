package com.tetrapak.supplierportal.e2e.tests.api.ui;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class AbstractPlaywrightIT {
    private static Playwright playwright;
    private static Browser browser;

    @BeforeAll
    static void setUpClass() {
        playwright = Playwright.create();
        BrowserType browserType = playwright.chromium();
        browser = browserType.launch(
                new BrowserType.LaunchOptions().setHeadless(false));
    }

    @AfterAll
    static void tearDownClass() {
        playwright.close();
    }
}
