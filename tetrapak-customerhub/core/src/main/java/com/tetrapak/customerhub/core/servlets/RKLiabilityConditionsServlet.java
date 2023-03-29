package com.tetrapak.customerhub.core.servlets;

import com.adobe.acs.commons.genericlists.GenericList;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.rebuildingkits.PDFLink;
import com.tetrapak.customerhub.core.beans.rebuildingkits.RKLiabilityConditionsPDF;
import com.tetrapak.customerhub.core.services.RKLiabilityConditionsService;
import com.tetrapak.customerhub.core.services.config.KeylinesConfiguration;
import com.tetrapak.customerhub.core.services.config.RKLiabilityConditionsConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import javax.servlet.Servlet;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.tetrapak.customerhub.core.servlets.RKLiabilityConditionsServlet.JSON_EXTENSION;
import static com.tetrapak.customerhub.core.servlets.RKLiabilityConditionsServlet.PDF_LINKS_SELECTOR;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Get RK Liability Conditions PDF Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.resourceTypes=" + "customerhub/components/content/rkliabilityconditions",
        "sling.servlet.selectors=" + PDF_LINKS_SELECTOR, "sling.servlet.extensions=" + JSON_EXTENSION
})
public class RKLiabilityConditionsServlet extends SlingAllMethodsServlet {

    public static final String PDF_LINKS_SELECTOR = "getpdflinks";

    public static final String JSON_EXTENSION = "json";

    public static final String PREFERRED_LANGUAGE_PARAM = "preferredLanguage";

    public static final String ERROR_KEY = "error";

    public static final String ERROR_MESSAGE = "Internal Server error";

    @Reference
    private RKLiabilityConditionsService rkLiabilityConditionsService;

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {
        String preferredLanguage = request.getParameter(PREFERRED_LANGUAGE_PARAM);
        RKLiabilityConditionsPDF rkLiabilityConditionsPDF = rkLiabilityConditionsService.getPDFLinksJSON(request.getResourceResolver(),preferredLanguage);

        if(rkLiabilityConditionsPDF.getEnglishPDF()==null && rkLiabilityConditionsPDF.getPreferredLanguagePDF()==null){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(ERROR_KEY,ERROR_MESSAGE);
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().print(jsonObject.toString());
        }else{
            String pdfLinksJSON = new Gson().toJson(rkLiabilityConditionsPDF,RKLiabilityConditionsPDF.class);
            response.setStatus(HttpStatus.SC_OK);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().print(pdfLinksJSON);
        }


    }


}
