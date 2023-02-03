package com.tetrapak.commons.core.servlets;

import com.adobe.granite.ui.components.Config;
import com.adobe.granite.ui.components.rendercondition.RenderCondition;
import com.adobe.granite.ui.components.rendercondition.SimpleRenderCondition;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import static com.tetrapak.commons.core.constants.CommonsConstants.ADMIN_USER;
import static com.tetrapak.commons.core.constants.CommonsConstants.USER_GROUP_READ_SERVICE;

/**
 * This is a render condition to show or hide a granite field in any dialog
 * based on the allowedGroups property configured in the granite:rendercondition node
 * of the field. For admin user, this check is bypassed.
 */
@Component(service = Servlet.class, property = {
        Constants.SERVICE_DESCRIPTION + "= Allow Groups RenderConditions Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.resourceTypes=" + "commons/renderconditions/allowgroups" })
public class AllowGroupRenderConditionServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(AllowGroupRenderConditionServlet.class);

    @Reference
    private transient ResourceResolverFactory resolverFactory;

    private static final String ALLOWED_GROUPS = "allowedGroups";

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) {

        boolean render = false;
        final Map<String, Object> authInfo = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE,
                USER_GROUP_READ_SERVICE);
        try(ResourceResolver adminResolver = resolverFactory.getServiceResourceResolver(authInfo)){
            Config config = new Config(request.getResource());
            String currentUserId = request.getResourceResolver().getUserID();
            String[] allowedGroups = config.get(ALLOWED_GROUPS, String[].class);
            if(currentUserId.equals(ADMIN_USER)){
                render = true;
            }
            if(!render && allowedGroups != null && allowedGroups.length > 0){
                UserManager userManager = adminResolver.adaptTo(UserManager.class);
                for (String group : allowedGroups) {
                    if(!render && userManager!= null &&  userManager.getAuthorizable(group) != null && userManager.getAuthorizable(group).isGroup() && currentUserId!=null){
                        Authorizable currentUser = userManager.getAuthorizable(currentUserId);
                        if(currentUser!=null){
                            Iterator<Group> groups = currentUser.memberOf();
                            while (groups.hasNext()){
                                Group userGroup = groups.next();
                                if(userGroup.getID().equals(group)){
                                    render = true;
                                    break;
                                }
                            }
                        }

                    }
                }
            }

        } catch (LoginException e) {
            logger.error("Error while fetching resolver object from system user", e);
        } catch (RepositoryException e) {
            logger.error("Error while accessing repository", e);
        }
        request.setAttribute(RenderCondition.class.getName(), new SimpleRenderCondition(render));

    }
}
