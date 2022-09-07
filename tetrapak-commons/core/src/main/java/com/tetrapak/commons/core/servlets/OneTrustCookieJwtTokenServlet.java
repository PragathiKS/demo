package com.tetrapak.commons.core.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.rsa.RSASigner;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.fusionauth.jwt.Signer;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * The Class One Trust Cookie JWT Token servlet.
 */
@Component(
        service = Servlet.class,
        property = { Constants.SERVICE_DESCRIPTION + "=One Trust Cookie Jwt Token Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.selectors=" + "onetrustcookietoken", "sling.servlet.extensions=" + "json",
                "sling.servlet.resourceTypes=" + "publicweb/components/structure/pages/page" })
public class OneTrustCookieJwtTokenServlet extends SlingAllMethodsServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 953677548755980049L;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(OneTrustCookieJwtTokenServlet.class);

    /**
     * Do get.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        LOGGER.debug("Executing doGet method.");
        PrintWriter writer = response.getWriter();
        JsonObject jsonResponse = new JsonObject();
        Signer signer = RSASigner.newSHA256Signer(new String(Files.readAllBytes(Paths.get("D:\\TestPrivateKey.pem"))));
        final String uniqueUserId = String.valueOf(UUID.randomUUID());
        JWT jwt = new JWT().setUniqueId(String.valueOf(uniqueUserId));
        final String encodedJWT = JWT.getEncoder().encode(jwt, signer);
        LOGGER.error("encodedJWT {} ",encodedJWT);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        jsonResponse.addProperty("uid", uniqueUserId);
        jsonResponse.addProperty("jwt", encodedJWT);
        writer.print(new Gson().toJson(jsonResponse));
        writer.flush();
        writer.close();
    }
}
