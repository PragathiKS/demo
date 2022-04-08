package com.tetrapak.customerhub.core.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.post.JSONResponse;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        "sling.servlet.paths=" + "/bin/customerhub/plant-master-groups"
})
public class PlantMasterGroupsServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 533258111248160710L;

    private static final String STATIC_GROUPS_DEFINITION_JSON = "aipTrainingsAndLicenseGroups.json";

    private static final Logger LOGGER = LoggerFactory.getLogger(PlantMasterGroupsServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        LOGGER.debug("HTTP GET request from PlantMasterGroupsServlet");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(JSONResponse.RESPONSE_CONTENT_TYPE);
        JsonObject resultJson = new JsonObject();
        resultJson.add("groups", new JsonArray());

        List<String> userGroups = GlobalUtil.getCustomerGroups(request);

        Optional.ofNullable(getJsonGroups())
                .map(jsonGroups -> jsonGroups.getAsJsonArray("groups"))
                .orElse(new JsonArray())
                .forEach(el -> {
                    String groupName = el.getAsJsonObject().get("groupName").getAsString();
                    if (userGroups.contains(groupName)) {
                        resultJson.get("groups").getAsJsonArray().add(el);
                    }
                });
        response.getWriter().write(resultJson.toString());
    }

    private JsonObject getJsonGroups() throws IOException {
        JsonObject jsonGroups = null;
        JsonParser parser = new JsonParser();
        InputStream io = null;
        try {
            io = this.getClass().getClassLoader().getResourceAsStream(STATIC_GROUPS_DEFINITION_JSON);
            Object obj = parser.parse(new InputStreamReader(io, "UTF-8"));
            jsonGroups = (JsonObject) obj;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (io != null) {
                io.close();
            }
        }
        return jsonGroups;
    }
}
