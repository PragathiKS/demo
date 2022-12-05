package com.tetrapak.supplierportal.e2e.tests.api.ui.supplierportal;

import com.tetrapak.supplierportal.e2e.tests.api.ui.engine.AbstractPlaywrightE2EConfig;
import org.junit.jupiter.api.Test;

public class GenericContentPageTest extends AbstractPlaywrightE2EConfig {
    @Test
    void genericContentPageTest() {
        new GenericContentPage(page).goTo();
    }
}
