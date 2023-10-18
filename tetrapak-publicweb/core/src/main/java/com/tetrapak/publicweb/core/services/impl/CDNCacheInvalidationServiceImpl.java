package com.tetrapak.publicweb.core.services.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.SimpleTimeZone;
import java.util.concurrent.TimeUnit;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Impl class for CDNCacheInvalidationService.
 */
@Component(
        immediate = true,
        service = CDNCacheInvalidationService.class,
        configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = CDNCacheInvalidationServiceConfig.class)
public class CDNCacheInvalidationServiceImpl implements CDNCacheInvalidationService {

    /** LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CDNCacheInvalidationServiceImpl.class);

    /** The config. */
    private CDNCacheInvalidationServiceConfig config;

    /** files parameter name. */
    private static final String SI_PARAM_DIRS = "dirs";

    /** The Constant SI_PARAM_DIR_ACTION. */
    private static final String SI_PARAM_DIR_ACTION = "dirAction";

    /** The Constant SI_ACTION_EXPIRE. */
    private static final String SI_ACTION_EXPIRE = "expire";

    /** The Constant BASIC_AUTH_HEADER_PREFIX. */
    private static final String BASIC_AUTH_HEADER_PREFIX = "Basic ";

    /** The Constant MAC_ALGORITHM. */
    private static final String MAC_ALGORITHM = "HmacSHA256";

    /**
     * Protocol for replication agent transport URI that triggers this transport handler.
     */
    private static final String SI_PROTOCOL = "cdn-serviceinsight://";

    /** path of directory to be purged in cdn *. */
    private String directoryToBePurged = StringUtils.EMPTY;

    /** Static variable to host cdn request directory path along with their timestamp *. */
    private static Map<String, String> cdnRequestTimeStampRecord = new HashMap<>();

    /**
     * activate method.
     *
     * @param config
     *            Pardot Service configuration
     */
    @Activate
    public void activate(final CDNCacheInvalidationServiceConfig config) {

        this.config = config;
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
        final String cfEndPoint = ctx.getConfig().getTransportURI().replace(SI_PROTOCOL, PWConstants.HTTPS_PROTOCOL);
        final HttpPost request = new HttpPost(cfEndPoint);
        if (createPostBody(request, tx)) {
            tx.getLog().info("--------- Triggering CDN Cache Flush ------------------");
            return getResult(sendRequest(request, ctx, tx, directoryToBePurged), tx);
        }
        return ReplicationResult.OK;
    }

    /**
     * Get Replication Result.
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
                    LOGGER.error("Error while parsing response for path :: {}", tx.getAction().getPath(), e);
                }
                tx.getLog().info("Response body :: " + str);
            }
            if (statusCode == HttpStatus.SC_OK) {
                return ReplicationResult.OK;
            }
        }
        LOGGER.error("Error received from cdn and response is null for path:: {}", tx.getAction().getPath());
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
     * @param directoryToBePurged
     *            the directory to be purged
     * @return HttpResponse - The HTTP response from ServiceInsight
     */
    private <T extends HttpRequestBase> HttpResponse sendRequest(final T request, final TransportContext ctx,
            final ReplicationTransaction tx, final String directoryToBePurged) {
        HttpResponse response = null;
        try {
            request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
            request.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
            addAuthHeader(request, ctx);
            final HttpClient client = HttpClientBuilder.create().build();
            response = client.execute(request);
        } catch (final Exception e) {
            tx.getLog().error("Could not send replication request {}", e.getMessage());
            LOGGER.error("Could not send replication request {}", e.getMessage());

        }
        if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            cdnRequestTimeStampRecord.put(directoryToBePurged, getDate(new Date()));
        }
        return response;
    }

    /**
     * Forms and Adds authorization header to the request headers.
     *
     * @param request
     *            the request
     * @param ctx
     *            the ctx
     * @throws InvalidKeyException
     *             the invalid key exception
     * @throws NoSuchAlgorithmException
     *             the no such algorithm exception
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
     * Method provides date in a format specified by service insight API.
     *
     * @param date
     *            the date
     * @return the date
     */
    private static String getDate(final Date date) {
        final SimpleDateFormat rfc822DateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        rfc822DateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return rfc822DateFormat.format(date);
    }

    /**
     * Provides encoded authorization header key.
     *
     * @param dateString
     *            the date string
     * @param username
     *            the username
     * @param apikey
     *            the apikey
     * @return the string
     * @throws InvalidKeyException
     *             the invalid key exception
     * @throws NoSuchAlgorithmException
     *             the no such algorithm exception
     */
    private static String encode(final String dateString, final String username, final String apikey)
            throws InvalidKeyException, NoSuchAlgorithmException {
        final String signature = signAndBase64Encode(dateString.getBytes(), apikey);
        final String userAndPwd = username + ":" + signature;
        return new String(Base64.encodeBase64(userAndPwd.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Provides signature encoded in base64.
     *
     * @param data
     *            the data
     * @param apikey
     *            the apikey
     * @return the string
     * @throws NoSuchAlgorithmException
     *             the no such algorithm exception
     * @throws InvalidKeyException
     *             the invalid key exception
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
     * @return true, if successful
     */
    private boolean createPostBody(final HttpPost request, final ReplicationTransaction tx) {
        tx.getLog().debug("Inside create request JSON");
        JsonObject json = null;
        final JsonArray purgeDirs = new JsonArray();
        for (final String path : tx.getAction().getPaths()) {
            findDirectoryForPurge(purgeDirs, path);
        }
        /**
         * Below condition checks if cache purge request for same directory is made within 5 minutes, then don't send
         * request to cdn
         */
        if (cdnRequestTimeStampRecord.containsKey(directoryToBePurged)
                && findDateDifferenceInMinutes(cdnRequestTimeStampRecord.get(directoryToBePurged),
                        getDate(new Date())) < 6) {
            return false;
        }

        if (purgeDirs.size() > 0) {
            json = new JsonObject();
            json.add(SI_PARAM_DIRS, purgeDirs);
            json.addProperty(SI_PARAM_DIR_ACTION, SI_ACTION_EXPIRE);
            final StringEntity entity = new StringEntity(json.toString(), CharEncoding.ISO_8859_1);
            request.setEntity(entity);
            tx.getLog().info("Clearing cache for paths param: " + json);
            return true;
        }
        return false;
    }

    /**
     * Find directory for Purge.
     *
     * @param purgeDirs
     *            the purge dirs
     * @param path
     *            the path
     */
    private void findDirectoryForPurge(final JsonArray purgeDirs, final String path) {
        if (StringUtils.isNotBlank(path) && (path.startsWith(config.contentPathForCDNCacheInvalidation())
                || path.startsWith(config.damPathForCDNCacheInvalidation()))) {
            final String contentPath = config.domainForCDN() + findUrlMapping(
                    LinkUtils.getRootPath(path).replace(config.contentPathForCDNCacheInvalidation(), ""));
            purgeDirs.add(contentPath);
            directoryToBePurged = contentPath;
        } else if (StringUtils.isNotBlank(path) && (path.startsWith(PWConstants.ONLINE_HELP_CONTENT_PATH))
                || path.startsWith(PWConstants.ONLINE_HELP_DAM_PATH)) {
            purgeOnlineHelpCache(purgeDirs);
        } else if (StringUtils.isNotBlank(path)
                && (path.startsWith(PWConstants.DS_CONTENT_PATH) || path.startsWith(PWConstants.DS_CONTENT_DAM_PATH))) {
            purgeDesignSystemCache(purgeDirs);
        } else if (StringUtils.isNotBlank(path) && (path.startsWith(PWConstants.TETRA_LAVAL_CONTENT_PATH)
                || path.startsWith(PWConstants.TETRA_LAVAL_CONTENT_DAM_PATH))) {
            purgeTetraLavalCache(purgeDirs);
        } else if(StringUtils.isNotBlank(path) && (path.startsWith(config.xfPathForCDNCacheInvalidation()))){
            purgeXFCache(purgeDirs);
        }
    }

    private void purgeXFCache(final JsonArray purgeDirs) {
        final String contentPath = config.domainForCDN() + PWConstants.SLASH;
        LOGGER.debug("XF content path {}", contentPath);
        purgeDirs.add(contentPath);
        directoryToBePurged = contentPath;
    }

    /**
     * Purge design system cache.
     *
     * @param purgeDirs
     *            the purge dirs
     */
    private void purgeDesignSystemCache(final JsonArray purgeDirs) {
        final String contentPath = config.dsDomainForCDN() + PWConstants.SLASH;
        LOGGER.debug("DS content path {}", contentPath);
        purgeDirs.add(contentPath);
        directoryToBePurged = contentPath;
    }

    /**
     * Purge online help cache.
     *
     * @param purgeDirs
     *            the purge dirs
     */
    private void purgeOnlineHelpCache(final JsonArray purgeDirs) {
        final String contentPath = config.onlineHelpDomainForCDN() + PWConstants.SLASH;
        LOGGER.debug("Online Help content path {}", contentPath);
        purgeDirs.add(contentPath);
        directoryToBePurged = contentPath;
    }

    /**
     * Purge tetra laval cache.
     *
     * @param purgeDirs
     *            the purge dirs
     */
    private void purgeTetraLavalCache(final JsonArray purgeDirs) {
        final String contentPath = config.tetraLavalDomainForCDN() + PWConstants.SLASH;
        LOGGER.debug("Tetra Laval content path {}", contentPath);
        purgeDirs.add(contentPath);
        directoryToBePurged = contentPath;
    }

    /**
     * Find url mapping.
     *
     * @param path
     *            the path
     * @return the string
     */
    private String findUrlMapping(final String path) {
        return Arrays.stream(config.urlMapping()).filter(e -> e.contains(path)).findAny().orElse(StringUtils.EMPTY)
                .split("=")[0] + PWConstants.SLASH;
    }

    /**
     * method to find difference between two dates in minutes.
     *
     * @param startDate
     *            the start date
     * @param endDate
     *            the end date
     * @return the long
     */
    private long findDateDifferenceInMinutes(final String startDate, final String endDate) {
        final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        long differenceInTime = 0;
        try {
            differenceInTime = sdf.parse(endDate).getTime() - sdf.parse(startDate).getTime();
            differenceInTime = TimeUnit.MILLISECONDS.toMinutes(differenceInTime) % 60;
        } catch (java.text.ParseException e) {
            LOGGER.error("Error occurred in findDateDifferenceInMinutes {}", e.getMessage());
        }
        return differenceInTime;
    }
}
