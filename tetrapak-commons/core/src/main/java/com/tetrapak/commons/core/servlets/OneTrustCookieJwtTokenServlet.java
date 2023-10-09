package com.tetrapak.commons.core.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tetrapak.commons.core.constants.CommonsConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;

/**
 * The Class One Trust Cookie JWT Token servlet.
 */
@Component(
        service = Servlet.class,
        property = {Constants.SERVICE_DESCRIPTION + "=One Trust Cookie Jwt Token Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.selectors=" + "onetrustcookietoken", "sling.servlet.extensions=" + "json",
                "sling.servlet.resourceTypes=" + "customerhub/components/structure/page"})
@Designate(ocd = OneTrustCookieJwtTokenServlet.Config.class)
public class OneTrustCookieJwtTokenServlet extends SlingAllMethodsServlet {

    /**
     * The Interface Config.
     */
    @ObjectClassDefinition(
            name = "One Trust Cookie Generation private key path",
            description = "Path where one trust private key is stored")
    public static @interface Config {

        /**
         * No of results per hit.
         *
         * @return the int
         */
        @AttributeDefinition(name = "One Trust private key path", description = "One Trust private key path.")
        String oneTrustPrivatekeyPath() default "/mnt/crx/publish/TetraPakCookieMgmtPrivateKey.pem";
    }

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 953677548755980049L;

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OneTrustCookieJwtTokenServlet.class);

    /**
     * The path of one trust private key
     */
    private String oneTrustPrivateKeyPath;

    /**
     * Do get.
     *
     * @param request  the request
     * @param response the response
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        LOGGER.debug("Executing doGet method.");
        PrintWriter writer = response.getWriter();
        response.setContentType(CommonsConstants.APPLICATION_JSON);
        JsonObject jsonResponse = new JsonObject();
        try {
            if (Files.exists(Paths.get(this.oneTrustPrivateKeyPath))) {
                String uniqueUserId;
                if(request.getRequestPathInfo().toString().contains(CommonsConstants.CUSTOMER_HUB)) {
                	Session session = request.getResourceResolver().adaptTo(Session.class);
                	MessageDigest digest = MessageDigest.getInstance(CommonsConstants.SHA_256);
                	byte[] encodedhash = digest.digest(session.getUserID().toLowerCase().getBytes(StandardCharsets.UTF_8));
                	uniqueUserId = bytesToHex(encodedhash).toLowerCase();
                } else {
    	            if (StringUtils.isEmpty(Objects.requireNonNull(request.getRequestParameter(CommonsConstants.USER_ID)).toString()) ||
    	                     null == request.getRequestParameter(CommonsConstants.USER_ID) ||
    	                    Objects.requireNonNull(request.getRequestParameter(CommonsConstants.USER_ID)).toString().length() < 36 ) {
    	                uniqueUserId = String.valueOf(UUID.randomUUID());
    	            } else {
    	                uniqueUserId = Objects.requireNonNull(request.getRequestParameter(CommonsConstants.USER_ID)).toString();
    	            }
                }
                jsonResponse.addProperty("uid", uniqueUserId);

             } else {
                LOGGER.debug("Configured One Trust Private key Path {}  does not exists", this.oneTrustPrivateKeyPath);
                jsonResponse.addProperty("uid", StringUtils.EMPTY);
            }
        } catch (NoSuchAlgorithmException e) {
        	LOGGER.error("Unable to get instance of SHA256: {}", e.getMessage());
        	jsonResponse.addProperty("uid", StringUtils.EMPTY);
        }
        response.setStatus(HttpServletResponse.SC_OK);
        writer.print(new Gson().toJson(jsonResponse));
        writer.flush();
        writer.close();
    }

    /**
     * Activate.
     *
     * @param config the config
     */
    @Activate
    protected void activate(final Config config) {
        this.oneTrustPrivateKeyPath = config.oneTrustPrivatekeyPath();
    }
    
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
