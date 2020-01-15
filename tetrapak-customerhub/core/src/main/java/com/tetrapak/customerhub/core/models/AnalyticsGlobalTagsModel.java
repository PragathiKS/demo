package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.tetrapak.customerhub.core.utils.HttpUtil.encodeStr;

/**
 * AnalyticsGlobalTagsModel Implementation
 */
@Model(adaptables = {Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AnalyticsGlobalTagsModel {

    @Inject
    private Resource resource;

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyticsGlobalTagsModel.class.getName());
    private static final int SUB_PAGE_THRESHOLD = 6;

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
     * Get Site Country.
     *
     * @return StringUtils.EMPTY
     */
    public String getSiteCountry() {
        return StringUtils.EMPTY;
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

        return StringUtils.EMPTY;
    }

    /**
     * Get User Id
     *
     * @return user ID
     */
    public String getVisitorId() {
        if (null == resource) {
            return null;
        }
        ResourceResolver resourceResolver = resource.getResourceResolver();
        UserManager userManager = resourceResolver.adaptTo(UserManager.class);
        Session session = resource.getResourceResolver().adaptTo(Session.class);
        if (null == session || null == userManager) {
            return null;
        }
        String userId;
        userId = session.getUserID();
        return encodeStr(userId);
    }

    /**
     * Get User Country Code.
     *
     * @return gb
     */
    public String getUserCountryCode() {
        return StringUtils.EMPTY;
    }

    public String getUserType(){
        String bpNumber = getBpNumber();
        if(bpNumber.isEmpty()){
            return StringUtils.EMPTY;
        }
        else if(bpNumber.startsWith("EE")){
            return "internal";
        }else{
            return "external";
        }
    }

    public String getBpNumber() {
        String bpnumber = StringUtils.EMPTY;
        if (null == resource) {
            return bpnumber;
        }
        ResourceResolver resourceResolver = resource.getResourceResolver();
        UserManager userManager = resourceResolver.adaptTo(UserManager.class);
        Session session = resourceResolver.adaptTo(Session.class);
        if (null == session || null == userManager) {
            return bpnumber;
        }
        Authorizable user;
        try {
            user = userManager.getAuthorizable(session.getUserID());
            Resource userResource = resourceResolver.getResource(user.getPath());
            if (userResource == null) {
                return bpnumber;
            }
            ValueMap vMap = userResource.getValueMap();
            if (vMap.containsKey("bpnumber")) {
                bpnumber = (String) vMap.get("bpnumber");
            }
        } catch (RepositoryException e) {
            LOGGER.error("RepositoryException in getting BP number in AnalyticsGlobalTagsModel", e);
        }
        return bpnumber;
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
        Session session = resourceResolver.adaptTo(Session.class);
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
            LOGGER.error("RepositoryException in getting user roles in AnalyticsGlobalTagsModel", e);
        }

        return userRoles;
    }

    /**
     * Get Error Code.
     *
     * @return errorCode, if successful
     */
    public Integer getErrorCode() {
        if (resource.getPath().contains("500")) {
            return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        } else if (resource.getPath().contains("404")) {
            return HttpServletResponse.SC_NOT_FOUND;
        } else {
            return null;
        }
    }

    /**
     * Get Error Message.
     *
     * @return errorMessage, if successful
     */
    public String getErrorMessage() {
        if (resource.getPath().contains("500")) {
            return "internal server error";
        } else if (resource.getPath().contains("404")) {
            return "resource not found";
        } else {
            return null;
        }
    }

    /**
     * This method returns the channel for a particular page
     *
     * @return String channel
     */
    public String getChannel() {
        final int DEPTH = 4;
        String channel = StringUtils.substringAfter(StringUtils.substringBefore(resource.getPath(), "/jcr:content"),
                GlobalUtil.getPageFromResource(resource, DEPTH).getPath() + CustomerHubConstants.PATH_SEPARATOR);
        return channel.replaceAll(CustomerHubConstants.PATH_SEPARATOR, ":").toLowerCase();
    }

    /**
     * This method returns is a page is sub second level page in site hierarchy
     *
     * @return true if sub page
     */
    public boolean isSubPage() {
        boolean isSubPage = false;
        if (GlobalUtil.getPageDepth(resource) > SUB_PAGE_THRESHOLD) {
            isSubPage = true;
        }
        return isSubPage;
    }

}
