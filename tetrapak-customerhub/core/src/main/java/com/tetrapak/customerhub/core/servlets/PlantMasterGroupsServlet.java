package com.tetrapak.customerhub.core.servlets;

import com.adobe.fd.fp.util.RepositoryUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.post.JSONResponse;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

/**
 * Plant Master Groups Servlet
 *
 * @author Szymon Krzysztyniak
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Plant Master Groups Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.resourceTypes=" + "customerhub/components/content/plantmasterlicenses",
        "sling.servlet.resourceTypes=" + "customerhub/components/content/plantmastertrainings",
        "sling.servlet.selectors=" + "plantmaster", "sling.servlet.extensions=" + "json"
})
public class PlantMasterGroupsServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 533258111248160710L;
    private static final String GROUPS_EL = "groups";
    private static final String SITE_LICENSE_EL = "siteLicenseGroup";
    private static final String SITE_LICENSE_GROUP = "cuhu_tppm_role_customerkey";
    private static final String DISPATCHER_HEADER = "Dispatcher";
    private static final String NO_CACHE = "no-cache";
    private static final String STATIC_GROUPS_DEFINITION_JSON = "/content/dam/customerhub/aip/aipTrainingsAndLicenseGroupsStatic.json";

    private static final Logger LOGGER = LoggerFactory.getLogger(PlantMasterGroupsServlet.class);

    /**
     * The resolver factory.
     */
    @Reference
    private transient ResourceResolverFactory resolverFactory;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        LOGGER.debug("HTTP GET request from PlantMasterGroupsServlet");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(JSONResponse.RESPONSE_CONTENT_TYPE);
        response.setHeader(DISPATCHER_HEADER, NO_CACHE);
        JsonObject resultJson = new JsonObject();
        resultJson.add(GROUPS_EL, new JsonArray());

        List<String> userGroups = GlobalUtil.getCustomerGroups(request);

        if (userGroups.contains(SITE_LICENSE_GROUP)) {
            resultJson.addProperty(SITE_LICENSE_EL, true);
        }

        Optional.ofNullable(getJsonGroups(request.getResourceResolver()))
                .map(jsonGroups -> jsonGroups.getAsJsonArray(GROUPS_EL)).orElse(new JsonArray())
                .forEach((JsonElement el) -> {
                    String groupName = el.getAsJsonObject().get("groupName").getAsString();
                    if (userGroups.contains(groupName)) {
                        resultJson.get(GROUPS_EL).getAsJsonArray().add(el);
                    }
                });

        response.getWriter().write(resultJson.toString());
    }

    private JsonObject getJsonGroups(ResourceResolver resolver) {
        final JsonParser parser = new JsonParser();
        return Optional.ofNullable(resolver.getResource(STATIC_GROUPS_DEFINITION_JSON))
                .map(r -> r.adaptTo(Node.class))
                .map(node -> {
                    try (InputStream io = node.getNode(RepositoryUtils.JCR_CONTENT_NODE_NAME).getProperty("jcr:data")
                            .getBinary().getStream()) {
                        Object obj = parser.parse(new InputStreamReader(io, "UTF-8"));
                        return (JsonObject) obj;
                    } catch (IOException | RepositoryException e) {
                        LOGGER.error("Cannot read file with mapping PING Group <-> License/Training ID", e);
                        return new JsonObject();
                    }
                }).orElseGet(() -> {
                    LOGGER.error("Cannot read file with mapping PING Group <-> License/Training ID");
                    return new JsonObject();
                });
    }
}