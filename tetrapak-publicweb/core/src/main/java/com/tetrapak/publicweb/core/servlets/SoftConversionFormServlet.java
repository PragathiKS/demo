package com.tetrapak.publicweb.core.servlets;

import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * This is the servlet that is triggered to get the sub categories from a given
 * tag path.
 *
 * @author abhbhatn
 */
@Component(service = Servlet.class, property = {
        Constants.SERVICE_DESCRIPTION + "=Tetra Pak - Public Web Soft Conversion Form service",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths=" + "/bin/tetrapak/pw-softconversion"})
public class SoftConversionFormServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(SoftConversionFormServlet.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    private ResourceResolver resourceResolver;

    private String UGC_CONTENT_PATH = "/content/usergenerated";

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        log.info("Inside doGet method.");
        try {
            // get resource resolver, tagManager objects.
            resourceResolver = request.getResourceResolver();
            Session session = resourceResolver.adaptTo(Session.class);

            String group = request.getParameter("group");
            String firstName = request.getParameter("first-name");
            String lastName = request.getParameter("last-name");
            String emailAddress = request.getParameter("email-address");
            String company = request.getParameter("company");
            String position = request.getParameter("position");

            if (session != null) {
                Node rootNode = session.getNode(UGC_CONTENT_PATH);
                Node pwNode = JcrUtils.getOrAddNode(rootNode, "terapak-publicweb");
                Node softConvNode = JcrUtils.getOrAddNode(pwNode, "soft-conversion");
                Node itemNode = JcrUtils.getOrAddNode(softConvNode, emailAddress);
                itemNode.setProperty("group", group);
                itemNode.setProperty("firstName", firstName);
                itemNode.setProperty("lastName", lastName);
                itemNode.setProperty("emailAddress", emailAddress);
                itemNode.setProperty("company", company);
                itemNode.setProperty("position", position);

                session.save();
                log.info("Saved user data at path : {}", itemNode.getPath());

                setCookie(response);

            }

            // set the response type
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (PathNotFoundException e1) {
            log.error("Error finding the path. {}", e1);
        } catch (RepositoryException e2) {
            log.error("Error in repository operation. {}", e2);
        }

    }

    private void setCookie(SlingHttpServletResponse response) {
        Cookie cookie = new Cookie("softConvUserExists", "true");
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 7);
        response.addCookie(cookie);
        log.info("Cookie added.");

    }
}
