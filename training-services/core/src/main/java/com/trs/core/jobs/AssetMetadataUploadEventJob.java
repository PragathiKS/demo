package com.trs.core.jobs;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.ReplicationActionType;
import com.day.text.csv.Csv;
import com.trs.core.reports.StatefulReport;
import com.trs.core.services.AssetPageOpsService;
import com.trs.core.services.TaxonomyService;
import com.trs.core.services.TrsConfigurationService;
import com.trs.core.utils.TrsConstants;
import com.trs.core.utils.TrsUtils;

/**
 * author : Ankit Pathak
 * 
 * This job consumer performs the following tasks :
 * 
 * 1. Creation of page corresponding to video asset uploaded.
 * 
 * 2. Conversion of the Asset Xyleme tags provided in input metadata file to
 * corresponding AEM tags.
 * 
 * 3. Creation of author friendly logs for above operations.
 * 
 */
@Component(service = JobConsumer.class, immediate = true, property = {
        Constants.SERVICE_DESCRIPTION + "=Trs Asset Metadata upload event handler job",
        JobConsumer.PROPERTY_TOPICS + "=trs/page/creation/job" })
public class AssetMetadataUploadEventJob implements JobConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetMetadataUploadEventJob.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private AssetPageOpsService trsAssetPageOpsService;

    @Reference
    private TaxonomyService trsTaxonomyService;
    
    @Reference
    private TrsConfigurationService trsConfig;

    @Override
    public JobResult process(Job job) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        LOGGER.trace("TrS Page creation & activation job starts");

        // Creation of Author log for the Page creation operation
        StatefulReport pageCreationReport = trsAssetPageOpsService.createPageCreationReport();
        // Creation of Author log for the Tags conversion operation
        StatefulReport taxonomyServiceReport = trsTaxonomyService.createTaxonomyServiceReport();
        ResourceResolver resourceResolver = null;

        try {

            String inputFilePath = (String) job.getProperty("inputFilePath");
            resourceResolver = TrsUtils.getTrsResourceResolver(resolverFactory);

            Iterator<String[]> rowIterator = getRowIterator(resourceResolver,
                    inputFilePath + TrsConstants.FORWARD_SLASH + TrsConstants.JCR_CONTENT);
            while (rowIterator.hasNext()) {
                String[] metadataRow = rowIterator.next();
                // The following actions are applicable only for videos!
                if (resourceResolver.getResource(metadataRow[0]) != null
                        && TrsUtils.isVideoAsset(resourceResolver, metadataRow[0])) {
                    // Replicate Asset
                    trsAssetPageOpsService.replicationActionOnResource(resourceResolver, ReplicationActionType.ACTIVATE,
                            metadataRow[0]);
                    // Create corresponding page for the asset
                    trsAssetPageOpsService.createTrsPage(resourceResolver, metadataRow[0], pageCreationReport);
                    // Replicate the corresponding page for asset
                    trsAssetPageOpsService.replicationActionOnResource(resourceResolver, ReplicationActionType.ACTIVATE,
                            trsAssetPageOpsService.getPagePathForAsset(metadataRow[0]).get("pageResourcePath"));
                    // Convert the xyleme tags to AEM tags
                    trsTaxonomyService.convertXylemeTagsToAEMTags(resourceResolver, metadataRow[0], null,
                            taxonomyServiceReport);
                } else {
                    LOGGER.info("Ignoring row in Metadata sheet as it is not a video :" + metadataRow[0]);
                }
            }

            createLogFile(pageCreationReport, resourceResolver, trsConfig.getPageCreationLogFilePath(),
                    "Error while creating page creation author log file :");
            createLogFile(taxonomyServiceReport, resourceResolver, trsConfig.getTaxonomyTransformationLogFilePath(),
                    "Error while creating taxonomy transformation author log file :");

            LOGGER.trace("TrS Page creation & activation job ends");
            resourceResolver.close();
            return JobConsumer.JobResult.OK;

        } catch (LoginException | RepositoryException | IOException e) {
            LOGGER.error("Error while creating pages for the asset batch whose metadata has been uploaded : ", e);
            pageCreationReport.createExcelSheetRow(
                    new String[] { "System Error : Contact IT Team", ExceptionUtils.getRootCauseMessage(e) });
            try {
                TrsUtils.saveExcelSheetAsDamAsset(pageCreationReport, resourceResolver, trsConfig.getPageCreationLogFilePath());
            } catch (IOException e1) {
                LOGGER.error("Error while creating page creation author log file :", e1);
            }
            return JobResult.CANCEL;
        }  finally {
            resourceResolver.close();
            stopWatch.stop();
            LOGGER.info(
                    "Total time taken for Trs Page creation job :" + TrsUtils.millisToShortDHMS(stopWatch.getTime()));
        }
    }

    private Iterator<String[]> getRowIterator(ResourceResolver resourceResolver, String resourcePath)
            throws RepositoryException, IOException {
        Node jobFileForMetadataImport = resourceResolver.getResource(resourcePath).adaptTo(Node.class);
        InputStream in = TrsUtils.getInputStreamFromFile(jobFileForMetadataImport);
        final Csv csv = new Csv();
        Iterator<String[]> rowIterator = csv.read(in, StandardCharsets.UTF_8.toString());
        String[] row0 = rowIterator.next();
        // Handling the scenario when input metadata sheet is created by modifying the
        // exported metadata from AEM and it contains additional line on top
        if ("sep=".equals(row0[0])) {
            rowIterator.next();
        }
        return rowIterator;
    }

    private void createLogFile(StatefulReport report, ResourceResolver resourceResolver, String path, String message) {
        try {
            TrsUtils.saveExcelSheetAsDamAsset(report, resourceResolver, path);
        } catch (IOException e) {
            LOGGER.error(message, e);
        }
    }

}
