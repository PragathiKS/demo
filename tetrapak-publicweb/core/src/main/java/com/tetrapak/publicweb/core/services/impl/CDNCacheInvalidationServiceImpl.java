package com.tetrapak.publicweb.core.services.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;

import com.day.cq.replication.ReplicationResult;
import com.day.cq.replication.ReplicationTransaction;
import com.day.cq.replication.TransportContext;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tetrapak.publicweb.core.services.CDNCacheInvalidationService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * Impl class for CDNCacheInvalidationService
 *
 */
@Component(immediate = true, service = CDNCacheInvalidationService.class)
public class CDNCacheInvalidationServiceImpl implements CDNCacheInvalidationService {
    
	/**
	 * files parameter name
	 */
	private static final String SI_PARAM_URLS = "urls";

	private static final String SI_PARAM_DIRS = "dirs";
	/**
	 * Protocol for replication agent transport URI that triggers this transport
	 * handler.
	 */
	private static final String SI_PROTOCOL = "serviceinsight://";

	/**
	 * Send test request to service insight via a GET request.
	 *
	 * service insight will respond with a 200 HTTP status code if the request was
	 * successfully submitted. The response will have information about the queue
	 * length, but we're simply interested in the fact that the request was
	 * authenticated.
	 *
	 * @param ctx Transport Context
	 * @param tx  Replication Transaction
	 * @return ReplicationResult OK if 200 response from ServiceInsight
	 */
    @Override
	public ReplicationResult doTest(final TransportContext ctx, final ReplicationTransaction tx) {
		String uri = ctx.getConfig().getTransportURI().replace(SI_PROTOCOL, "");
		String domain = uri.substring(0, uri.indexOf("/"));
		String fullHttpUrl = "https://" + domain + ctx.getConfig().getProperties().get("testUri").toString();
		final HttpGet request = new HttpGet(fullHttpUrl);
		tx.getLog().info("------ Triggering TEST RUN ------");
		tx.getLog().info("URL :: " + fullHttpUrl);
		return getResult(sendRequest(request, ctx, tx), tx);
	}

	
	/**
	 * Send purge request to ServiceInsight via a POST request
	 *
	 * ServiceInsight will respond with a 201 HTTP status code if the purge request
	 * was successfully submitted.
	 *
	 * @param ctx - Transport Context
	 * @param tx  - Replication Transaction
	 * @return ReplicationResult - OK if 201 response from ServiceInsight
	 */
    @Override
	public ReplicationResult doActivate(final TransportContext ctx, final ReplicationTransaction tx,ResourceResolverFactory resolverFactory) {
		tx.getLog().info("--------- Triggering CDN Flush -------------------");
		final String cfEndPoint = ctx.getConfig().getTransportURI().replace(SI_PROTOCOL, "https://");
		final HttpPost request = new HttpPost(cfEndPoint);
		createPostBody(request, tx, resolverFactory);
		return getResult(sendRequest(request, ctx, tx), tx);
	}
	
	

	/**
	 * Get Replication Result
	 *
	 * @param response - HttpResponse object
	 * @param tx       - ReplicationTransaction object
	 * @return ReplicationResult - result
	 */
	private ReplicationResult getResult(final HttpResponse response, final ReplicationTransaction tx) {
		if (response != null) {
			final int statusCode = response.getStatusLine().getStatusCode();
			tx.getLog().info(statusCode + ":::" + response.toString());
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String str = null;
				try {
					str = EntityUtils.toString(entity);
				} catch (ParseException e) {

				} catch (IOException e) {

				}
				tx.getLog().info(str);
			}
			tx.getLog().info("---------------------------------------");
			if (statusCode == HttpStatus.SC_OK) {
				return ReplicationResult.OK;
			}
		}
		return new ReplicationResult(false, 0, "Replication failed");
	}

	/**
	 * Build preemptive basic authentication headers and send request.
	 *
	 * @param <T>     Type
	 * @param request - The request to send to ServiceInsight
	 * @param ctx     - The TransportContext containing the username and password
	 * @param tx      - Replication Transaction
	 * @return HttpResponse - The HTTP response from ServiceInsight
	 */
	private <T extends HttpRequestBase> HttpResponse sendRequest(final T request, final TransportContext ctx,
			final ReplicationTransaction tx) {
		HttpResponse response = null;
		try {
			request.setHeader("Accept", "application/json");
			request.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
			addAuthHeader(request, ctx);
			tx.getLog().info("------ Triggering request ------");
			HttpClient client = HttpClientBuilder.create().build();
			response = client.execute(request);
		} catch (Exception e) {
			tx.getLog().error("Could not send replication request- " + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	private static void addAuthHeader(AbstractHttpMessage request, final TransportContext ctx) throws Exception {
		Date date = new Date();
		String dateString = getDate(date);
		String authoriztion = encode(dateString, ctx.getConfig().getTransportUser(),
				ctx.getConfig().getTransportPassword());
		request.setHeader("Date", dateString);
		request.setHeader("Authorization", "Basic " + authoriztion);
	}

	private static String getDate(Date date) {
		SimpleDateFormat rfc822DateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		rfc822DateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
		String dateString = rfc822DateFormat.format(date);
		return dateString;
	}

	private static String encode(String dateString, String username, String apikey) throws Exception {
		String signature = signAndBase64Encode(dateString.getBytes(), apikey);
		String userAndPwd = username + ":" + signature;
		return new String(Base64.encodeBase64(userAndPwd.getBytes("UTF-8")));
	}

	private static String signAndBase64Encode(byte[] data, String apikey) throws Exception {
		Mac mac = Mac.getInstance("HmacSHA256");// 如果使用sha256则参数是：HmacSHA256
		mac.init(new SecretKeySpec(apikey.getBytes(), "HmacSHA256"));// 如果使用sha256则参数是：HmacSHA256
		byte[] signature = mac.doFinal(data);
		return new String(Base64.encodeBase64(signature));
	}

	/**
	 * Build the ServiceInsight purge request body based on the replication agent
	 * settings and append it to the POST request.
	 *
	 * @param request The HTTP POST request to append the request body
	 * @param tx      ReplicationTransaction
	 */
	private void createPostBody(final HttpPost request, final ReplicationTransaction tx,ResourceResolverFactory resolverFactory) {
		JsonObject json = new JsonObject();
		JsonArray purgeURLs = new JsonArray();
		JsonArray purgeDirs = new JsonArray();

		for (String path : tx.getAction().getPaths()) {
			if (StringUtils.isNotBlank(path)
					&& (path.startsWith("/content/tetrapak/publicweb/") || path.startsWith("/content/dam/"))) {
				final String contentPath = LinkUtils.sanitizeLink(path,
						GlobalUtil.getResourceResolverFromSubService(resolverFactory));
				purgeURLs.add(contentPath);
				purgeDirs.add(contentPath.substring(0,contentPath.length()-1));
			}
		}
		if (purgeURLs.size() > 0) {
			json.add(SI_PARAM_URLS, purgeURLs);
			json.add(SI_PARAM_DIRS, purgeDirs);
			final StringEntity entity = new StringEntity(json.toString(), CharEncoding.ISO_8859_1);
			tx.getLog().info("Clearing cache for paths param: " + json);
			request.setEntity(entity);
		}
	}
	
}
