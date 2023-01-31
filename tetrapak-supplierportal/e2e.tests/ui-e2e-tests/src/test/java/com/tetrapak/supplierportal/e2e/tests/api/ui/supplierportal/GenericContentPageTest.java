package com.tetrapak.supplierportal.e2e.tests.api.ui.supplierportal;

import com.tetrapak.supplierportal.e2e.tests.api.ui.engine.AbstractPlaywrightE2EConfig;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class GenericContentPageTest extends AbstractPlaywrightE2EConfig {
    @Test
    void genericContentPageTest() {
        GenericContentPage genericContentPage = new GenericContentPage(page).goTo();
        assertThat(genericContentPage.getLogo()).isVisible();
    }
}
