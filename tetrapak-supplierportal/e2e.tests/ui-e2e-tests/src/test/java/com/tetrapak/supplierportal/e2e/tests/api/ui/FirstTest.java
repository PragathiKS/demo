package com.tetrapak.supplierportal.e2e.tests.api.ui;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.Test;

class FirstTest extends AbstractPlaywrightIT {
    @Test
    void myFirstTest() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.webkit().launch();
            Page page = browser.newPage();
            page.navigate("https://author-tetrapak-dev65.adobecqms.net/");
        }
    }
}