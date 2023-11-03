package com.tetrapak.customerhub.core.models;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;

/**
 * Model class for reference component
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ReferenceModel {

    public static final String RESOURCE_TYPE = "customerhub/components/content/reference";

    @SlingObject
    private SlingHttpServletRequest request;

    @Inject
    private String contentPath;

    @OSGiService
    private UserPreferenceService userPreferenceService;

    /**
     * Process the path to fetch the locale specific path.
     *
     * @return processed path
     */
    public String getContentPath() {
        String path = contentPath;
        String locale = GlobalUtil.getSelectedLanguage(request, userPreferenceService);
        locale = StringUtils.isNotBlank(locale) ? locale : CustomerHubConstants.DEFAULT_LOCALE;
        if (null != path) {
            path = path.replace(CustomerHubConstants.PATH_SEPARATOR + CustomerHubConstants.DEFAULT_LOCALE,
                    CustomerHubConstants.PATH_SEPARATOR + locale);

            Resource checkResource = request.getResourceResolver().getResource(path);
            if (null == checkResource || ResourceUtil.isNonExistingResource(checkResource)) {
                path = StringUtils.EMPTY;
            }
        }

        return path;
    }
}