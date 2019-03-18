package com.tetrapak.customerhub.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AnalyticsGlobalTagsModel {
    
    @Self
    private Resource resource;
    
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    
    public String getSiteName() {
        return "customerhub";
    }
    
    public String getPageType() {
        PageManager pageManager = resource.getResourceResolver().adaptTo(PageManager.class);
        Page currentPage = pageManager.getContainingPage(resource.getPath());
        String pageType = currentPage.getTitle();
        return pageType;       
    }
    
    public String getSiteLanguage() {
        return "en";
    }
    
    public String getSiteCountry() {
        return "gb";
    }
    
    public String getLogInStatus() {
        return "logged-in";
    }
    
    public String getSalesForceId() {
        return "12ffsf343243345";
    }
    
    public String getUserLanguage() {
        return "en";
    }
    
    public String getUserCountryCode() {
        return "gb";
    }
    
    public ArrayList<String> getUserRoles() {
        ArrayList<String> userRoles = new ArrayList<String>();
        String userRole = null;
        ResourceResolver resourceResolver = resource.getResourceResolver();
        Session session = resource.getResourceResolver().adaptTo(Session.class);
        UserManager userManager = resourceResolver.adaptTo(UserManager.class);
        Authorizable user;
        try {
            user = (User) userManager.getAuthorizable(session.getUserID());
            Iterator<Group> itr = user.memberOf();
            while (itr.hasNext()) {
                Group group = (Group) itr.next();
                userRole = group.getPrincipal().getName();
                userRoles.add(userRole);
            }
        } catch (RepositoryException e) {
            LOG.error("RepositoryException in AnalyticsGlobalTagsModel", e);
        }
        
        return userRoles;
    }
}
