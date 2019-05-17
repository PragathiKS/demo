package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * AnalyticsGlobalTagsModel Implementation
 */
@Model(adaptables = {SlingHttpServletRequest.class,
        Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AnalyticsGlobalTagsModel {

    @Inject
    private Resource resource;

    @SlingObject
    private SlingHttpServletRequest request;

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyticsGlobalTagsModel.class.getName());
    private static final int subPageThreshold = 5;

    /**
     * Get Site Name.
     *
     * @return String customerhub
     */
    public String getSiteName() {
        return "customerhub";
    }

    /**
     * Get Page Type.
     *
     * @return page title in lower case as page type, if successful
     */
    public String getPageType() {
        return GlobalUtil.getPageTitle(resource).toLowerCase();
    }

    /**
     * Get Site Language.
     *
     * @return english
     */
    public String getSiteLanguage() {
        return "en";
    }

    /**
     * Get Site Country.
     *
     * @return gb
     */
    public String getSiteCountry() {
        return "gb";
    }

    /**
     * Get LogIn Status.
     *
     * @return logged-in status
     */
    public String getLogInStatus() {
        return "logged-in";
    }

    /**
     * Get Sales Force Id.
     *
     * @return 12ffsf343243345 value
     */
    public String getSalesForceId() {
        return "12ffsf343243345";
    }

    /**
     * Get User Language.
     *
     * @return english
     */
    public String getUserLanguage() {
        return "en";
    }

    /**
     * Get User Country Code.
     *
     * @return gb
     */
    public String getUserCountryCode() {
        return "gb";
    }

    /**
     * Get User Roles.
     *
     * @return userRoles, if successful
     */
    public List<String> getUserRoles() {
        ArrayList<String> userRoles = new ArrayList<>();
        if (null == resource) {
            return userRoles;
        }

        ResourceResolver resourceResolver = resource.getResourceResolver();
        UserManager userManager = resourceResolver.adaptTo(UserManager.class);
        Session session = resource.getResourceResolver().adaptTo(Session.class);
        if (null == session || null == userManager) {
            return userRoles;
        }

        Authorizable user;
        try {
            String userRole;
            user = userManager.getAuthorizable(session.getUserID());
            Iterator<Group> itr = user.memberOf();
            while (itr.hasNext()) {
                Group group = itr.next();
                userRole = group.getPrincipal().getName();
                userRoles.add(userRole);
            }
        } catch (RepositoryException e) {
            LOGGER.error("RepositoryException in AnalyticsGlobalTagsModel", e);
        }

        return userRoles;
    }

    /**
     * Get Error Code.
     *
     * @return errorCode, if successful
     */
    public Integer getErrorCode() {
        int status;
        try {
            status = (Integer) request.getAttribute("javax.servlet.error.status_code");
            return status;
        } catch (Exception e) {
            LOGGER.error("Exception in getting error code {}", e);
            return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
    }

    /**
     * Get Error Message.
     *
     * @return errorMessage, if successful
     */
    public String getErrorMessage() {
        String errorStatusMessage = (String) request.getAttribute("javax.servlet.error.message");
        String errorMessage = errorStatusMessage.substring(errorStatusMessage.indexOf(":") + 1);
        return errorMessage.trim().toLowerCase();
    }

    /**
     * This method returns the channel for a particular page
     *
     * @return String channel
     */
    public String getChannel() {
        final int DEPTH = 4;
        return GlobalUtil.getPageFromResource(resource, DEPTH).getName();
    }

    /**
     * This method returns is a page is sub second level page in site hirarchy
     *
     * @return true if sub page
     */
    public boolean isSubPage() {
        boolean isSubPage = false;
        if (GlobalUtil.getPageDepth(resource) > subPageThreshold) {
            isSubPage = true;
        }
        return isSubPage;
    }

}
