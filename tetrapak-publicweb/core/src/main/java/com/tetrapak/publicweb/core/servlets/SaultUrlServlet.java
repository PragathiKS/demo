package com.tetrapak.publicweb.core.servlets;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tetrapak.publicweb.core.constants.PWConstants;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The Class SaultUrlServlet.
 */
@Component(service = Servlet.class, property = { "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths=" + "/bin/previewServlet" })

public class SaultUrlServlet extends SlingSafeMethodsServlet {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SaultUrlServlet.class);

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5454240563218059527L;

    /**
     * Do get.
     *
     * @param request the request
     * @param response    the response
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {

        final ResourceResolver resolver = request.getResourceResolver();
        final String currentPagePath = request.getParameter("request-type").replace(PWConstants.HTML, "");
        final String relativePath = currentPagePath.concat(PWConstants.JCR);
        java.util.Date date = new java.util.Date();
        final String encodedSaultString = generateSault(currentPagePath.concat(date.toString()));
        final String saultPagePath = currentPagePath.concat("?wcmmode=disabled&preview=").concat(encodedSaultString);
        try {
            saveSaultToRepository(resolver, encodedSaultString, relativePath);
            response.setContentType("text/plain;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            ServletOutputStream sout = response.getOutputStream();
            sout.print(saultPagePath);
        } catch (IOException | RepositoryException e) {
            LOGGER.error("Error while writing the response object.", e);
        }

    }

    /**
     * Method to update "preview-sault" value into repository
     */
    private void saveSaultToRepository(final ResourceResolver resolver, final String encodedSaultString,
            final String relativePath) throws RepositoryException {
        if (null != resolver) {
            final Session session = resolver.adaptTo(Session.class);
            final Node node = session.getNode(relativePath);
            node.setProperty("preview-sault", encodedSaultString);
            session.save();
            session.logout();
        }
    }

    /**
     * Method to generate hash value using SHA-512 algorithm
     */
    private static String generateSault(String path) {
        String saultValue = StringUtils.EMPTY;
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("SHA-512");
            byte[] hashedValue = messagedigest.digest(path.getBytes());
            final StringBuilder stringbuilder = new StringBuilder();
            for (int i = 0; i < hashedValue.length; i++) {
                stringbuilder.append(String.format("%02x", hashedValue[i]));

            }
            saultValue = stringbuilder.toString();
        } catch (NoSuchAlgorithmException e) {

            LOGGER.error("NoSuchAlgorithmException", e);
        }
        return saultValue;

    }

}
