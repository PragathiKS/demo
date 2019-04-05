package com.tetrapak.customerhub.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * AnalyticsGlobalTagsModel Implementation
 * @param Resource, the parameter of the class
 */
@Model(adaptables = {SlingHttpServletRequest.class,Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AnalyticsGlobalTagsModel {
    
    
    @Self
    private Resource resource;
    
    @SlingObject
    private SlingHttpServletRequest request;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyticsGlobalTagsModel.class.getName());
    
    /**
     * Get Site Name.
     * @return customerhub
     */
    public String getSiteName() {
        return "customerhub";
    }
    
    /**
     * Get Page Type.
     * @return pageType, if successful
     */
    public String getPageType() {
        if(null == resource){
            return "";
        }
        PageManager pageManager = resource.getResourceResolver().adaptTo(PageManager.class);
        if(null == pageManager){
            return "";
        }
        Page currentPage = pageManager.getContainingPage(resource.getPath());
        String pageType = currentPage.getTitle();
        return pageType;       
    }
    
    /**
     * Get Site Language.
     * @return english 
     */
    public String getSiteLanguage() {
        return "en";
    }
    
    /**
     * Get Site Country.
     * @return gb 
     */
    public String getSiteCountry() {
        return "gb";
    }
    
    /**
     * Get LogIn Status.
     * @return logged-in status
     */
    public String getLogInStatus() {
        return "logged-in";
    }
    
    /**
     * Get Sales Force Id.
     * @return 12ffsf343243345 value
     */
    public String getSalesForceId() {
        return "12ffsf343243345";
    }
    
    /**
     * Get User Language.
     * @return english
     */
    public String getUserLanguage() {
        return "en";
    }
    
    /**
     * Get User Country Code.
     * @return gb
     */
    public String getUserCountryCode() {
        return "gb";
    }
    
    /**
     * Get User Roles.
     * @return userRoles, if successful
     */
    public List<String> getUserRoles() {
        ArrayList<String> userRoles = new ArrayList<>();
        if(null == resource){
            return userRoles;
        }

        String userRole;
        ResourceResolver resourceResolver = resource.getResourceResolver();
        Session session = resource.getResourceResolver().adaptTo(Session.class);
        if(null == session){
            return userRoles;
        }
        UserManager userManager = resourceResolver.adaptTo(UserManager.class);
        if(null == userManager){
            return userRoles;
        }
        Authorizable user;
        try {
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
     * @return errorCode, if successful
     */   
    public Integer getErrorCode(){
        Integer errorCode = (Integer)request.getAttribute("javax.servlet.error.status_code") ;
        return errorCode;        
    }
    
    /**
     * Get Error Message.
     * @return errorMessage, if successful
     */
    public String getErrorMessage(){
        String errorStatusMessage = (String)request.getAttribute("javax.servlet.error.message") ;
        String  errorMessage = errorStatusMessage.substring(errorStatusMessage.indexOf(":")+1);
        return errorMessage.trim();        
    }
    










}
