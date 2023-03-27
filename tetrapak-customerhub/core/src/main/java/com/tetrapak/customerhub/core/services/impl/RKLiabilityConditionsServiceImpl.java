package com.tetrapak.customerhub.core.services.impl;

import com.adobe.acs.commons.genericlists.GenericList;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.rebuildingkits.PDFLink;
import com.tetrapak.customerhub.core.beans.rebuildingkits.RKLiabilityConditionsPDF;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.RKLiabilityConditionsService;
import com.tetrapak.customerhub.core.services.config.KeylinesConfiguration;
import com.tetrapak.customerhub.core.services.config.RKLiabilityConditionsConfig;
import com.tetrapak.customerhub.core.servlets.RKLiabilityConditionsServlet;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tetrapak.customerhub.core.constants.CustomerHubConstants.ENGLISH_LANGUAGE;
import static com.tetrapak.customerhub.core.constants.CustomerHubConstants.RK_LIABILITY_CONDITIONS_DEFAULT_TITLE;

@Component(immediate = true, service = RKLiabilityConditionsService.class)
@Designate(ocd = RKLiabilityConditionsConfig.class)
public class RKLiabilityConditionsServiceImpl implements RKLiabilityConditionsService {

    private RKLiabilityConditionsConfig config;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    private static final Logger LOGGER = LoggerFactory.getLogger(RKLiabilityConditionsServiceImpl.class);

    @Activate
    public void activate(final RKLiabilityConditionsConfig config) {
        this.config = config;
    }

    public RKLiabilityConditionsPDF getPDFLinksJSON(ResourceResolver resourceResolver, String preferredLanguage){
        Gson gson = new Gson();
        RKLiabilityConditionsPDF rkLiabilityConditionsPDF = new RKLiabilityConditionsPDF();
        if(StringUtils.isNotBlank(preferredLanguage)) {
            if (StringUtils.isNotBlank(config.pdfFolderMappingGLPath())) {
                final Map<String, Object> paramMap = new HashMap<>();
                paramMap.put(ResourceResolverFactory.SUBSERVICE, CustomerHubConstants.READ_GL_USER);
                ResourceResolver serviceResourceResolver = null;
                try {
                    serviceResourceResolver = resourceResolverFactory.getServiceResourceResolver(paramMap);
                    PageManager pageManager = serviceResourceResolver.adaptTo(PageManager.class);
                    if (pageManager != null) {
                        Page pdfFolderMappingGLPage = pageManager.getPage(config.pdfFolderMappingGLPath());
                        if (pdfFolderMappingGLPage != null) {
                            GenericList pdfGL = pdfFolderMappingGLPage.adaptTo(GenericList.class);
                            if (pdfGL != null) {
                                List<GenericList.Item> itemList = pdfGL.getItems();
                                for (GenericList.Item item : itemList) {
                                    if (item.getTitle().equals(preferredLanguage) && !preferredLanguage.equals(ENGLISH_LANGUAGE)) {
                                        PDFLink pdfLink = getPDFLinkFromAsset(item.getValue(), resourceResolver);
                                        rkLiabilityConditionsPDF.setPreferredLanguagePDF(pdfLink);
                                    }
                                    if (item.getTitle().equals(ENGLISH_LANGUAGE)) {
                                        PDFLink pdfLink = getPDFLinkFromAsset(item.getValue(), resourceResolver);
                                        rkLiabilityConditionsPDF.setEnglishPDF(pdfLink);
                                    }
                                }
                            }

                        }

                    }
                } catch (LoginException e) {
                    LOGGER.error("An error occurred while getting service resolver",e);
                    return new RKLiabilityConditionsPDF();
                } finally {
                    if(serviceResourceResolver!=null && serviceResourceResolver.isLive()){
                        serviceResourceResolver.close();
                    }
                }


            }

        }
        return rkLiabilityConditionsPDF;
    }

    private PDFLink getPDFLinkFromAsset(String assetPath, ResourceResolver resourceResolver) {
        Resource pdfAssetResource = resourceResolver.getResource(assetPath);
        if(pdfAssetResource!=null){
            Asset pdfAsset = pdfAssetResource.adaptTo(Asset.class);
            if(pdfAsset!=null){
                String assetTitle = pdfAsset.getMetadataValue(DamConstants.DC_TITLE);
                PDFLink pdfLink = new PDFLink();
                pdfLink.setLink(pdfAsset.getPath());
                if(StringUtils.isBlank(assetTitle)){
                    pdfLink.setTitle(RK_LIABILITY_CONDITIONS_DEFAULT_TITLE);
                }else{
                    pdfLink.setTitle(assetTitle);
                }
                return pdfLink;
            }

        }
        return new PDFLink();
    }
}
