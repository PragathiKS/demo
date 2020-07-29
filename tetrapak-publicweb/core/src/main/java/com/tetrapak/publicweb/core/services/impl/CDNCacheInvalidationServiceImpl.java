package com.tetrapak.publicweb.core.services.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.SimpleTimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

import com.day.cq.replication.ReplicationResult;
import com.day.cq.replication.ReplicationTransaction;
import com.day.cq.replication.TransportContext;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.CDNCacheInvalidationService;
import com.tetrapak.publicweb.core.services.config.CDNCacheInvalidationServiceConfig;
import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * Impl class for CDNCacheInvalidationService
 *
 */
@Component(
        immediate = true,
        service = CDNCacheInvalidationService.class,
        configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = CDNCacheInvalidationServiceConfig.class)
public class CDNCacheInvalidationServiceImpl implements CDNCacheInvalidationService {

    private CDNCacheInvalidationServiceConfig config;

    /**
     * files parameter name
     */
    private static final String SI_PARAM_DIRS = "dirs";
    private static final String SI_PARAM_DIR_ACTION = "dirAction";
    private static final String SI_ACTION_EXPIRE = "expire";
    private static final String SI_PROP_TESTURI = "testUri";
    private static final String BASIC_AUTH_HEADER_PREFIX = "Basic ";
    private static final String MAC_ALGORITHM = "HmacSHA256";

    /**
     * Protocol for replication agent transport URI that triggers this transport handler.
     */
    private static final String SI_PROTOCOL = "cdn-serviceinsight://";

    /**
     * activate method
     *
     * @param config
     *            Pardot Service configuration
     */
    @Activate
    public void activate(final CDNCacheInvalidationServiceConfig config) {

        this.config = config;
    }

    /**
     * Send test request to service insight via a GET request.
     *
     * service insight will respond with a 200 HTTP status code if the request was successfully submitted. The response
     * will have information about the queue length, but we're simply interested in the fact that the request was
     * authenticated.
     *
     * @param ctx
     *            Transport Context
     * @param tx
     *            Replication Transaction
     * @return ReplicationResult OK if 200 response from ServiceInsight
     */
    @Override
    public ReplicationResult doTest(final TransportContext ctx, final ReplicationTransaction tx) {
        final String uri = ctx.getConfig().getTransportURI().replace(SI_PROTOCOL, StringUtils.EMPTY);
        final String domain = uri.substring(0, uri.indexOf(PWConstants.SLASH));
        final String fullHttpUrl = PWConstants.HTTPS_PROTOCOL + domain
                + ctx.getConfig().getProperties().get(SI_PROP_TESTURI).toString();
        final HttpGet request = new HttpGet(fullHttpUrl);
        tx.getLog().info("------ Triggering TEST RUN ------");
        tx.getLog().info("URL :: " + fullHttpUrl);
        return getResult(sendRequest(request, ctx, tx), tx);
    }

    /**
     * Send purge request to ServiceInsight via a POST request
     *
     * ServiceInsight will respond with a 201 HTTP status code if the purge request was successfully submitted.
     *
     * @param ctx
     *            - Transport Context
     * @param tx
     *            - Replication Transaction
     * @return ReplicationResult - OK if 201 response from ServiceInsight
     */
    @Override
    public ReplicationResult doActivate(final TransportContext ctx, final ReplicationTransaction tx) {
        tx.getLog().info("--------- Triggering CDN Cache Flush ------------------");
        final String cfEndPoint = ctx.getConfig().getTransportURI().replace(SI_PROTOCOL, PWConstants.HTTPS_PROTOCOL);
        final HttpPost request = new HttpPost(cfEndPoint);
        createPostBody(request, tx);
        return getResult(sendRequest(request, ctx, tx), tx);
    }

    /**
     * Get Replication Result
     *
     * @param response
     *            - HttpResponse object
     * @param tx
     *            - ReplicationTransaction object
     * @return ReplicationResult - result
     */
    private ReplicationResult getResult(final HttpResponse response, final ReplicationTransaction tx) {
        if (Objects.nonNull(response)) {
            final int statusCode = response.getStatusLine().getStatusCode();
            tx.getLog().debug(statusCode + "::" + response.toString());
            final HttpEntity entity = response.getEntity();
            if (Objects.nonNull(entity)) {
                String str = null;
                try {
                    str = EntityUtils.toString(entity);
                } catch (ParseException | IOException e) {
                    tx.getLog().error("Error while parsing response :: " + e);
                }
                tx.getLog().debug("Response body :: " + str);
            }
            if (statusCode == HttpStatus.SC_OK) {
                return ReplicationResult.OK;
            }
        }
        return new ReplicationResult(false, 0, "Replication failed");
    }

    /**
     * Build preemptive basic authentication headers and send request.
     *
     * @param <T>
     *            Type
     * @param request
     *            - The request to send to ServiceInsight
     * @param ctx
     *            - The TransportContext containing the username and password
     * @param tx
     *            - Replication Transaction
     * @return HttpResponse - The HTTP response from ServiceInsight
     */
    private <T extends HttpRequestBase> HttpResponse sendRequest(final T request, final TransportContext ctx,
            final ReplicationTransaction tx) {
        HttpResponse response = null;
        try {
            request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
            request.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
            addAuthHeader(request, ctx);
            final HttpClient client = HttpClientBuilder.create().build();
            response = client.execute(request);
        } catch (final Exception e) {
            tx.getLog().error("Could not send replication request- " + e.getMessage());
        }
        return response;
    }

    /**
     * Forms and Adds authorization header to the request headers
     *
     * @param request
     * @param ctx
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    private static void addAuthHeader(final AbstractHttpMessage request, final TransportContext ctx)
            throws InvalidKeyException, NoSuchAlgorithmException {
        final Date date = new Date();
        final String dateString = getDate(date);
        final String authoriztion = encode(dateString, ctx.getConfig().getTransportUser(),
                ctx.getConfig().getTransportPassword());
        request.setHeader(HttpHeaders.DATE, dateString);
        request.setHeader(HttpHeaders.AUTHORIZATION, BASIC_AUTH_HEADER_PREFIX + authoriztion);
    }

    /**
     * Method provides date in a format specified by service insight API
     *
     * @param date
     * @return
     */
    private static String getDate(final Date date) {
        final SimpleDateFormat rfc822DateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        rfc822DateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return rfc822DateFormat.format(date);
    }

    /**
     * Provides encoded authorization header key
     *
     * @param dateString
     * @param username
     * @param apikey
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    private static String encode(final String dateString, final String username, final String apikey)
            throws InvalidKeyException, NoSuchAlgorithmException {
        final String signature = signAndBase64Encode(dateString.getBytes(), apikey);
        final String userAndPwd = username + ":" + signature;
        return new String(Base64.encodeBase64(userAndPwd.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Provides signature encoded in base64
     *
     * @param data
     * @param apikey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    private static String signAndBase64Encode(final byte[] data, final String apikey)
            throws NoSuchAlgorithmException, InvalidKeyException {
        final Mac mac = Mac.getInstance(MAC_ALGORITHM);
        mac.init(new SecretKeySpec(apikey.getBytes(), MAC_ALGORITHM));
        final byte[] signature = mac.doFinal(data);
        return new String(Base64.encodeBase64(signature));
    }

    /**
     * Build the ServiceInsight purge request body based on the replication agent settings and append it to the POST
     * request.
     *
     * @param request
     *            The HTTP POST request to append the request body
     * @param tx
     *            ReplicationTransaction
     * @param resolverFactory
     */
    private void createPostBody(final HttpPost request, final ReplicationTransaction tx) {
        tx.getLog().debug("Inside create request JSON");
        final JsonObject json = new JsonObject();
        final JsonArray purgeDirs = new JsonArray();
        for (final String path : tx.getAction().getPaths()) {
            if (StringUtils.isNotBlank(path) && (path.startsWith(config.contentPathForCDNCacheInvalidation())
                    || path.startsWith(config.damPathForCDNCacheInvalidation()))) {
                final String contentPath = config.domainForCDN()
                        + LinkUtils.getRootPath(path).replace(config.contentPathForCDNCacheInvalidation(), "")
                        + PWConstants.SLASH;
                purgeDirs.add(contentPath);
            }
        }

        if (purgeDirs.size() > 0) {
            json.add(SI_PARAM_DIRS, purgeDirs);
            json.addProperty(SI_PARAM_DIR_ACTION, SI_ACTION_EXPIRE);
            final StringEntity entity = new StringEntity(json.toString(), CharEncoding.ISO_8859_1);
            request.setEntity(entity);
        }
        tx.getLog().info("Clearing cache for paths param: " + json);
    }

}
