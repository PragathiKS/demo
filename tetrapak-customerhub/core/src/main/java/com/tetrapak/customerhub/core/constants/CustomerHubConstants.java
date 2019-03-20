package com.tetrapak.customerhub.core.constants;

/**
 * The CustomerHubConstants class
 */
public class CustomerHubConstants {

    private CustomerHubConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String GLOBAL_PAGE_PATH = "/content/tetrapak/customerhub/global";

    public static final String GLOBAL_CONFIGURATION_RESOURCE_TYPE = "customerhub/components/structure/globalconfiguration";

    public static final String CQ_REDIRECT_PROPERTY = "cq:redirectTarget";

    public static final String HTML_EXTENSION = ".html";

    public static final String RESPONSE_STATUS_FAILURE = "failure";

    public static final String RESPONSE_STATUS_SUCCESS = "success";
}
