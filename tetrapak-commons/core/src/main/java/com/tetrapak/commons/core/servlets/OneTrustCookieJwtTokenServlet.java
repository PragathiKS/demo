package com.tetrapak.commons.core.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tetrapak.commons.core.constants.CommonsConstants;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.rsa.RSASigner;
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
import io.fusionauth.jwt.Signer;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
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
                "sling.servlet.resourceTypes=" + "publicweb/components/structure/pages/page",
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
        if (Files.exists(Paths.get(this.oneTrustPrivateKeyPath))) {
            Signer signer = RSASigner.newSHA256Signer(new String(Files.readAllBytes(Paths.get(this.oneTrustPrivateKeyPath))));
            String uniqueUserId;
            if (StringUtils.isEmpty(Objects.requireNonNull(request.getRequestParameter(CommonsConstants.USER_ID)).toString()) ||
                     null == request.getRequestParameter(CommonsConstants.USER_ID) ||
                    Objects.requireNonNull(request.getRequestParameter(CommonsConstants.USER_ID)).toString().length() < 36 ) {
                uniqueUserId = String.valueOf(UUID.randomUUID());
            } else {
                uniqueUserId = Objects.requireNonNull(request.getRequestParameter(CommonsConstants.USER_ID)).toString();
            }
            JWT jwt = new JWT().setSubject(String.valueOf(uniqueUserId));
            final String encodedJWT = JWT.getEncoder().encode(jwt, signer);
            LOGGER.debug("encodedJWT {} ", encodedJWT);
            jsonResponse.addProperty("uid", uniqueUserId);
            jsonResponse.addProperty("jwt", encodedJWT);
        } else {
            LOGGER.debug("Configured One Trust Private key Path {}  does not exists", this.oneTrustPrivateKeyPath);
            jsonResponse.addProperty("uid", StringUtils.EMPTY);
            jsonResponse.addProperty("jwt", StringUtils.EMPTY);
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
        LOGGER.debug("OneTrust Cookie JWT token Servlet activated");
        this.oneTrustPrivateKeyPath = config.oneTrustPrivatekeyPath();
    }
}
