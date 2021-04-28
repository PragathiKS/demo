package com.trs.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Session;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.dam.api.Asset;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.trs.core.exceptions.PageNotCreatedException;
import com.trs.core.reports.StatefulReport;
import com.trs.core.services.AssetPageOpsService;
import com.trs.core.services.TrsConfigurationService;
import com.trs.core.utils.TrsConstants;

@Component(immediate = true, service = AssetPageOpsService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class AssetPageOpsServiceImpl implements AssetPageOpsService {

    private static final Logger LOG = LoggerFactory.getLogger(AssetPageOpsServiceImpl.class);

    private static final String AUTHOR_LOG_COLUMN_ONE_HEADING = "Asset Path";
    private static final String AUTHOR_LOG_COLUMN_TWO_HEADING = "Status";
    private static final String AUTHOR_LOG_COLUMN_THREE_HEADING = "Additional Detail";
    private static final String AUTHOR_LOG_COLUMN_FOUR_HEADING = "Page Path (if created)";
    private static final String STATUS = "status";

    @Reference
    private TrsConfigurationService trsConfig;

    @Reference
    private Replicator replicator;

    @Override
    public void createTrsPage(ResourceResolver resolver, String path, StatefulReport pageCreationReport){
        Map<String, String> responseMap = null;
        try {
            responseMap = createTrsPage(resolver, path);
            pageCreationReport.createExcelSheetRow(new String[] { path, responseMap.get(STATUS),
                    responseMap.get("detail"), responseMap.get("pagePath") });

        } catch (PageNotCreatedException e) {
            LOG.error("Error while creating page for the asset : {}", path, e);
            pageCreationReport
                    .createExcelSheetRow(new String[] { path, "Error", ExceptionUtils.getRootCauseMessage(e) });
        } 
    }

    @Override
    public Map<String, String> createTrsPage(ResourceResolver resolver, String path) throws PageNotCreatedException {

        Map<String, String> responseMap = new HashMap<>();
        Asset asset = resolver.getResource(path).adaptTo(Asset.class);

        Map<String, String> pagePaths = getPagePathForAsset(path);
        String pageParentPath = pagePaths.get("pageParentPath");
        String assetNameWithoutExtension = pagePaths.get("assetNameWithoutExtension");
        String pageResourcePath = pagePaths.get("pageResourcePath");
        responseMap.put("pagePath", pageResourcePath);

        // Create parent pages of the Video Asset HTML page, if not existing
        String[] parentPagePathNodes = pageParentPath.split("\\/");
        createParentPages(parentPagePathNodes, resolver);

        Page page = null;

        try {
            if (resolver.getResource(pageResourcePath) != null) {

                page = resolver.getResource(pageResourcePath).adaptTo(Page.class);
                LOG.info("Page already exists for the asset {} at the path {}", path, pageResourcePath);

            } else {

                PageManager pageManager = resolver.adaptTo(PageManager.class);
                page = pageManager.create(pageParentPath, assetNameWithoutExtension,
                        trsConfig.getTrsVideoPageTemplatePath(),
                        StringUtils.isNotBlank(asset.getMetadataValue(TrsConstants.ASSET_TITLE_PROPERTY))
                                ? asset.getMetadataValue(TrsConstants.ASSET_TITLE_PROPERTY)
                                : assetNameWithoutExtension);
                LOG.info("Created Page for the asset {} at the path {}", path, pageResourcePath);

            }

            validateAndSetPageProperties(page, asset, responseMap);

            // Setting asset replication flags as false
            ModifiableValueMap assetProperties = resolver.getResource(path + TrsConstants.ASSET_METADATA_RELATIVE_PATH)
                    .adaptTo(ModifiableValueMap.class);
            assetProperties.put("dam:pagePublishUrl", trsConfig.getTrsDNS() + pageResourcePath + ".html");
            assetProperties.put("dam:pageAuthorUrl", "/editor.html" + pageResourcePath + ".html");
            resolver.commit();
        } catch (WCMException e) {
            throw new PageNotCreatedException("Unable to create page", e);
        } catch (PersistenceException e) {
            throw new PageNotCreatedException("Unable to create page", e);
        }

        return responseMap;
    }

    @Override
    public Map<String, String> getPagePathForAsset(String path) {
        Map<String, String> pagePaths = new HashMap<>();

        String mod = path.replace(TrsConstants.FORWARD_SLASH + TrsConstants.DAM, StringUtils.EMPTY)
                .replace(TrsConstants.FORWARD_SLASH + trsConfig.getTrsAssetsRootFolderName(), TrsConstants.FORWARD_SLASH
                        + trsConfig.getTrsSitesRootFolderName())
                .replace("/videos", StringUtils.EMPTY);

        String pageParentPath = FilenameUtils.getFullPath(mod);
        String assetNameWithoutExtension = JcrUtil
                .createValidName(FilenameUtils.getBaseName(path).replace(StringUtils.SPACE, TrsConstants.HYPHEN));
        String pageResourcePath = pageParentPath + assetNameWithoutExtension;
        pagePaths.put("pageParentPath", pageParentPath);
        pagePaths.put("assetNameWithoutExtension", assetNameWithoutExtension);
        pagePaths.put("pageResourcePath", pageResourcePath);

        return pagePaths;
    }

    public void createParentPages(String[] parentPagePathNodes, ResourceResolver resolver) {

        StringBuilder progressiveParentPath = new StringBuilder();
        for (int i = 0; i < parentPagePathNodes.length; i++) {
            if (resolver.getResource(
                    progressiveParentPath.toString() + TrsConstants.FORWARD_SLASH + parentPagePathNodes[i]) == null) {
                PageManager pageManager = resolver.adaptTo(PageManager.class);
                try {
                    pageManager.create(progressiveParentPath.toString(), parentPagePathNodes[i],
                            trsConfig.getTrsEmptyPageTemplatePath(), parentPagePathNodes[i]);
                } catch (WCMException e) {
                    LOG.error("Error in creating parent page at the path {}",
                            progressiveParentPath.toString() + TrsConstants.FORWARD_SLASH + parentPagePathNodes[i], e);
                }
            }
            if (StringUtils.isNotBlank(parentPagePathNodes[i])) {
                progressiveParentPath.append(TrsConstants.FORWARD_SLASH).append(parentPagePathNodes[i]);
            }
        }
    }

    public void validateAndSetPageProperties(Page page, Asset asset, Map<String, String> responseMap) {

        boolean isMissingProperty = false;
        ModifiableValueMap pageProperties = page.adaptTo(ModifiableValueMap.class);
        Resource videoComponentResource = page.getContentResource("root/responsivegrid/video");
        ModifiableValueMap videoProperties = videoComponentResource.adaptTo(ModifiableValueMap.class);
        videoProperties.put(TrsConstants.SOURCE_ASSET_PATH, asset.getPath());
        // Check if the video (& consequently its corresponding page) is missing
        // business important properties
        List<String> missingProps = new ArrayList<>();
        if (StringUtils.isBlank(asset.getMetadataValue(TrsConstants.ASSET_TITLE_PROPERTY))) {
            responseMap.put(STATUS, "Warning");
            missingProps.add("Missing Title");
            isMissingProperty = true;
        }else {
            pageProperties.put("jcr:title",asset.getMetadataValue(TrsConstants.ASSET_TITLE_PROPERTY));
        }
        if (StringUtils.isBlank(asset.getMetadataValue(TrsConstants.ASSET_DESCRIPTION_PROPERTY))) {
            responseMap.put(STATUS, "Warning");
            missingProps.add("Missing Description");
            isMissingProperty = true;
        }else {
            pageProperties.put("jcr:description",asset.getMetadataValue(TrsConstants.ASSET_DESCRIPTION_PROPERTY));
        }
        if (StringUtils.isNotBlank(asset.getMetadataValue(TrsConstants.ASSET_SCENE7_FILENAME_PROPERTY))) {
            videoProperties.put(TrsConstants.PAGE_SCENE7_FILENAME_PROPERTY,
                    asset.getMetadataValue(TrsConstants.ASSET_SCENE7_FILENAME_PROPERTY));
        } else {
            responseMap.put(STATUS, "Error");
            missingProps.add("Video not uploaded to Dynamic Media!");
            isMissingProperty = true;
        }

        if (isMissingProperty) {

            responseMap.put("detail", String.join(TrsConstants.COMMA, missingProps));
        } else {
            responseMap.put(STATUS, "Success");

        }

    }

    @Override
    public void replicationActionOnResource(ResourceResolver resolver, ReplicationActionType action,
            String resourcePath) {
        Session session = resolver.adaptTo(Session.class);
        try {
            replicator.replicate(session, action, resourcePath);
            LOG.info("Performed action: {} on the resource: {}", action.toString(), resourcePath);
        } catch (ReplicationException e) {
            LOG.error("Error while performing the action: S{} on the resource: {}", action.toString(), resourcePath, e);
        }
    }

    @Override
    public StatefulReport createPageCreationReport() {

        return new StatefulReport.Builder().reportWorkbook().reportWorkBooksheet().build()
                .createExcelSheetRow(new String[] { AUTHOR_LOG_COLUMN_ONE_HEADING, AUTHOR_LOG_COLUMN_TWO_HEADING,
                        AUTHOR_LOG_COLUMN_THREE_HEADING, AUTHOR_LOG_COLUMN_FOUR_HEADING });
    }

}
