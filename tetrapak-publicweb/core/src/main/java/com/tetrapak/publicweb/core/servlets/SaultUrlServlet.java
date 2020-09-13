package com.tetrapak.publicweb.core.servlets;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.apache.sling.xss.XSSAPI;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The Class SaultUrlServlet.
 */
@Component(
        service = Servlet.class,
        property = { "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.paths=" + "/bin/publicweb/preview" })

public class SaultUrlServlet extends SlingAllMethodsServlet {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SaultUrlServlet.class);

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5454240563218095276L;

    /** The xss API. */
    @Reference
    transient XSSAPI xssAPI;

    /** The resolver factory. */
    @Reference
    transient ResourceResolverFactory resolverFactory;

    /**
     * Do post.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        JsonObject jsonResponse = new JsonObject();
        final String currentPagePath = xssAPI.getValidHref(request.getParameter("path"));
        final String relativePath = currentPagePath.concat(PWConstants.JCR);
        java.util.Date date = new java.util.Date();
        final String encodedSaultString = generateSault(currentPagePath.concat(date.toString()));
        final String saultPagePath = currentPagePath.concat(".html?preview=").concat(encodedSaultString);
        try (ResourceResolver resourceResolver = GlobalUtil.getResourceResolverFromSubService(resolverFactory)) {
            saveSaultToRepository(resourceResolver, encodedSaultString, relativePath);
            jsonResponse.addProperty("status", "success");
            jsonResponse.addProperty("saultPagePath", request.getRequestURL().append(saultPagePath).toString());
        } catch (RepositoryException e) {
            jsonResponse.addProperty("status", "fail");
            LOGGER.error("Error while writing the response object.", e.getMessage(), e);
        }
        writer.print(new Gson().toJson(jsonResponse));
    }

    /**
     * Method to update "preview-sault" value into repository.
     *
     * @param resolver
     *            the resolver
     * @param encodedSaultString
     *            the encoded sault string
     * @param relativePath
     *            the relative path
     * @throws RepositoryException
     *             the repository exception
     */
    private void saveSaultToRepository(final ResourceResolver resolver, final String encodedSaultString,
            final String relativePath) throws RepositoryException {
        if (null != resolver) {
            final Session session = resolver.adaptTo(Session.class);
            final Node node = session.getNode(relativePath);
            node.setProperty("previewSalt", encodedSaultString);
            session.save();
            session.logout();
        }
    }

    /**
     * Method to generate hash value using SHA-512 algorithm.
     *
     * @param path
     *            the path
     * @return the string
     */
    private static String generateSault(String path) {
        String saultValue = StringUtils.EMPTY;
        try {
            final MessageDigest messagedigest = MessageDigest.getInstance("SHA-512");
            final byte[] hashedValue = messagedigest.digest(path.getBytes(StandardCharsets.UTF_8));
            final StringBuilder stringbuilder = new StringBuilder();
            for (int i = 0; i < hashedValue.length; i++) {
                stringbuilder.append(String.format("%02x", hashedValue[i]));

            }
            saultValue = stringbuilder.toString();
        } catch (NoSuchAlgorithmException e) {

            LOGGER.error("Error while generating the hash value.", e.getMessage(), e);
        }
        return saultValue;

    }

}
