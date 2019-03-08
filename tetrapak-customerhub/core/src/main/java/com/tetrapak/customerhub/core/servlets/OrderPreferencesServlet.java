/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.tetrapak.customerhub.core.servlets;

import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import com.day.cq.commons.jcr.JcrConstants;
import com.tetrapak.customerhub.core.models.OrderingCardModel;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;


/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(service=Servlet.class,
           property={
                   Constants.SERVICE_DESCRIPTION + "=Saved Prefernces Servlet",
                   "sling.servlet.paths="+ "/bin/userServlet",
                   "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                   "sling.servlet.resourceTypes="+ "customerhub/components/structure/page",
                   "sling.servlet.extensions=" + "txt"
           })
public  class OrderPreferencesServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static final String ORDER_PREFERENCES = "orderPreferences";

    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        
        ResourceResolver resourceResolver = req.getResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);
         String userId = session.getUserID();
         UserManager userManager = resourceResolver.adaptTo(UserManager.class);
         try {
           Authorizable user = userManager.getAuthorizable(userId);
          // Value [] preferences = (Value[]) user.getProperty(ORDER_PREFERENCES);
           String prefFromRequest = req.getParameter("savedPreferences");
           String [] preferList = prefFromRequest.split(",");
                        
           String path = user.getPath();
           Resource userResource = resourceResolver.getResource(path);
           ModifiableValueMap properties = userResource.adaptTo(ModifiableValueMap.class);
           properties.put(ORDER_PREFERENCES,preferList);
            resourceResolver.commit();   
            
           if (preferList != null) {            
            OrderingCardModel orderingCardModel = req.adaptTo(OrderingCardModel.class);           
            orderingCardModel.setSavedPreferences(preferList);
            resp.getWriter().write(" Output from saved preferences is :" +  orderingCardModel.getSavedPreferences());
           }
        } catch (RepositoryException e) {
        }
        
    }
}
