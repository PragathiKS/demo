package com.tetrapak.supplierportal.e2e.tests.api.ui.engine;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractPlaywrightE2EConfig {
    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext context;
    protected Page page;


    //add mobile and desktop tests
    @BeforeAll static void setUpClass() {
        playwright = Playwright.create();
        BrowserType browserType = playwright.webkit();
        browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @BeforeEach void setup() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1366, 768)
                .setIsMobile(false)
        );
        page = context.newPage();
    }

    @AfterEach void tearDown() {
        page.context().close();
    }

    @AfterAll static void tearDownClass() {
        playwright.close();
    }
}
