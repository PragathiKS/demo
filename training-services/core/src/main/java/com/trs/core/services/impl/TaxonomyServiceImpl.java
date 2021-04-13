package com.trs.core.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.io.IOUtils;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.Replicator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trs.core.exceptions.TaxonomyOperationException;
import com.trs.core.reports.StatefulReport;
import com.trs.core.services.TaxonomyService;
import com.trs.core.services.TrsConfigurationService;
import com.trs.core.servlets.TagImporter;
import com.trs.core.utils.TrsConstants;

@Component(immediate = true, service = TaxonomyService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class TaxonomyServiceImpl implements TaxonomyService {

    private static final Logger LOG = LoggerFactory.getLogger(TaxonomyServiceImpl.class);

    public static final String FAILURE_MESSAGE_PREFIX = "Encountered problems. Check the logs ";
    public static final String XYLEME_TAG_MISSING = "Xyleme Tag missing on asset";

    public static final String ASSET_DOES_NOT_EXIST = "Asset does not exist on AEM";
    public static final String XYLEME_TAG_MAPPING_MISSING = "Xyleme tag missing in mapping";

    private static final String AUTHOR_LOG_COLUMN_ONE_HEADING = "Asset Path";
    private static final String AUTHOR_LOG_COLUMN_TWO_HEADING = "Status";
    private static final String AUTHOR_LOG_COLUMN_THREE_HEADING = "Additional Detail";

    @Reference
    private TrsConfigurationService trsConfig;

    @Reference
    private Replicator replicator;

    @Override
    public void convertXylemeTagsToAEMTags(ResourceResolver resourceResolver, String assetPath,
            Map<String, String> tagMappings, StatefulReport taxonomyReport) {
        List<String[]> reportRows = convertXylemeTagsToAEMTags(resourceResolver, assetPath, tagMappings);
        for (String[] reportRow : reportRows) {
            taxonomyReport.createExcelSheetRow(reportRow);
        }
    }

    @Override
    public List<String[]> convertXylemeTagsToAEMTags(ResourceResolver resourceResolver, String assetPath,
            Map<String, String> tagMappings) {

        Resource res = resourceResolver.getResource(assetPath);
        List<String[]> reportRows = new ArrayList<>();

        if (res != null) {
            String[] xyleneTags = res.getChild(TrsConstants.METADATA_NODE_RELATIVE_PATH).getValueMap()
                    .get(trsConfig.getXylemeTagsPropertyName(), String[].class);
            if (xyleneTags != null && xyleneTags.length > 0) {
                String[] xyleneTagsMappedToCQTags = new String[xyleneTags.length];
                for (int i = 0; i < xyleneTags.length; i++) {

                    getCQTag(resourceResolver, assetPath, tagMappings, reportRows, xyleneTags, xyleneTagsMappedToCQTags,
                            i);
                }
                LOG.info("For asset at the path " + res.getPath() + "Xyleme tags = \"" + Arrays.toString(xyleneTags)
                        + "\" converted to cq: tags = \"" + Arrays.toString(xyleneTagsMappedToCQTags) + "\"");
                try {
                    res.getChild(TrsConstants.METADATA_NODE_RELATIVE_PATH).adaptTo(Node.class)
                            .setProperty(TrsConstants.CQ_TAGS_PROPERTY, xyleneTagsMappedToCQTags);
                    resourceResolver.commit();
                } catch (RepositoryException e) {
                    LOG.error("Error while setting cq:tags property for the asset " + res.getPath() + " : ", e);
                } catch (PersistenceException e) {
                    LOG.error("Error while setting cq:tags property for the asset " + res.getPath() + " : ", e);
                }
            } else {
                reportRows.add(new String[] { assetPath, XYLEME_TAG_MISSING });
                LOG.info("The following asset does not have Xyleme tag specified : " + assetPath);
            }
        } else {
            reportRows.add(new String[] { assetPath, ASSET_DOES_NOT_EXIST });
            LOG.info("The following asset does not exist : " + assetPath);
        }
        return reportRows;
    }

    private void getCQTag(ResourceResolver resourceResolver, String assetPath, Map<String, String> tagMappings,
            List<String[]> reportRows, String[] xyleneTags, String[] xyleneTagsMappedToCQTags, int i) {
        if (tagMappings != null && tagMappings.get(xyleneTags[i]) != null) {
            xyleneTagsMappedToCQTags[i] = tagMappings.get(xyleneTags[i]);
        } else if (resourceResolver.getResource(
                trsConfig.getTrsXylemeMappingsPath() + TrsConstants.FORWARD_SLASH + xyleneTags[i]) != null) {
            xyleneTagsMappedToCQTags[i] = resourceResolver
                    .getResource(trsConfig.getTrsXylemeMappingsPath() + TrsConstants.FORWARD_SLASH + xyleneTags[i])
                    .getValueMap().get(TrsConstants.XYLEME_TAG_MAPPING_NODE_PROPERTY, String.class);
        } else {
            reportRows.add(new String[] { assetPath, XYLEME_TAG_MAPPING_MISSING, xyleneTags[i] });
        }
    }

    @Override
    public Map<String, String> getXylemeToAEMTagMapping(ResourceResolver resourceResolver)
            throws TaxonomyOperationException {

        Map<String, String> tagMappings = null;
        ObjectMapper mapper = new ObjectMapper();

        Node node = resourceResolver
                .getResource(trsConfig.getXylemeAEMTagMappingFilePath() + TrsConstants.ASSET_RENDITION_SUFFIX)
                .adaptTo(Node.class);

        InputStream in = null;

        try {
            in = node.getProperty(TagImporter.JCR_DATA).getBinary().getStream();
            String jsonAsString = IOUtils.toString(in, StandardCharsets.UTF_8);
            in.close();
            tagMappings = mapper.readValue(jsonAsString, Map.class);
        } catch (IOException | RepositoryException e) {
            throw new TaxonomyOperationException("Unable to fetch Xyleme to AEM tag mapping", e);
        } 
        
        return tagMappings;
    }

    @Override
    public StatefulReport createTaxonomyServiceReport() {

        return new StatefulReport.Builder().reportWorkbook().reportWorkBooksheet().build()
                .createExcelSheetRow(new String[] { AUTHOR_LOG_COLUMN_ONE_HEADING, AUTHOR_LOG_COLUMN_TWO_HEADING,
                        AUTHOR_LOG_COLUMN_THREE_HEADING });
    }

}
