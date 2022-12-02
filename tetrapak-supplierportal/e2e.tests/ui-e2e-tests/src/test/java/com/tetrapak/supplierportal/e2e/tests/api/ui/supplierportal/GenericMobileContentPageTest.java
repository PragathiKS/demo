package com.tetrapak.supplierportal.e2e.tests.api.ui.supplierportal;

import com.tetrapak.supplierportal.e2e.tests.api.ui.engine.AbstractMobilePlaywrightE2EConfig;
import org.junit.jupiter.api.Test;

public class GenericMobileContentPageTest extends AbstractMobilePlaywrightE2EConfig {
    @Test
    void genericContentPageTest() {
        new GenericMobileContentPage(page).goTo();
    }
}
