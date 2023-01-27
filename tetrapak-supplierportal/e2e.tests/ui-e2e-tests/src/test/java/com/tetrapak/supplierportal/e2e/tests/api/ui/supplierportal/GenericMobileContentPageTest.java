package com.tetrapak.supplierportal.e2e.tests.api.ui.supplierportal;

import com.tetrapak.supplierportal.e2e.tests.api.ui.engine.AbstractMobilePlaywrightE2EConfig;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class GenericMobileContentPageTest extends AbstractMobilePlaywrightE2EConfig {
    @Test
    void genericContentPageTest() {
        GenericContentPage genericMobileContentPage = new GenericMobileContentPage(page).goTo();
        assertThat(genericMobileContentPage.getMobileLogo()).isVisible();
    }
}
