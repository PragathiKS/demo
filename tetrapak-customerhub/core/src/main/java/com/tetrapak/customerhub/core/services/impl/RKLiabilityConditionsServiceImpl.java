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
import com.tetrapak.customerhub.core.services.RKLiabilityConditionsService;
import com.tetrapak.customerhub.core.services.config.KeylinesConfiguration;
import com.tetrapak.customerhub.core.services.config.RKLiabilityConditionsConfig;
import com.tetrapak.customerhub.core.servlets.RKLiabilityConditionsServlet;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component(immediate = true, service = RKLiabilityConditionsService.class)
@Designate(ocd = RKLiabilityConditionsConfig.class)
public class RKLiabilityConditionsServiceImpl implements RKLiabilityConditionsService {

    private RKLiabilityConditionsConfig config;

    @Activate
    public void activate(final RKLiabilityConditionsConfig config) {
        this.config = config;
    }

    public RKLiabilityConditionsPDF getPDFLinksJSON(ResourceResolver resourceResolver, String preferredLanguage){
        Gson gson = new Gson();
        RKLiabilityConditionsPDF rkLiabilityConditionsPDF = new RKLiabilityConditionsPDF();
        if(StringUtils.isNotBlank(preferredLanguage)) {
            if (StringUtils.isNotBlank(config.pdfFolderMappingGLPath())) {
                PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
                if (pageManager != null) {
                    Page pdfFolderMappingGLPage = pageManager.getPage(config.pdfFolderMappingGLPath());
                    if (pdfFolderMappingGLPage != null) {
                        GenericList pdfGL = pdfFolderMappingGLPage.adaptTo(GenericList.class);
                        if (pdfGL != null) {
                            List<GenericList.Item> itemList = pdfGL.getItems();
                            for (GenericList.Item item : itemList) {
                                if (item.getTitle().equals(preferredLanguage) && !preferredLanguage.equals("en")) {
                                    PDFLink pdfLink = getPDFLinkFromAsset(item.getValue(), resourceResolver);
                                    rkLiabilityConditionsPDF.setPreferredLanguagePDF(pdfLink);
                                }
                                if (item.getTitle().equals("en")) {
                                    PDFLink pdfLink = getPDFLinkFromAsset(item.getValue(), resourceResolver);
                                    rkLiabilityConditionsPDF.setEnglishPDF(pdfLink);
                                }
                            }
                        }

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
                    pdfLink.setTitle("Liability Conditions");
                }else{
                    pdfLink.setTitle(assetTitle);
                }
                return pdfLink;
            }

        }
        return new PDFLink();
    }
}
