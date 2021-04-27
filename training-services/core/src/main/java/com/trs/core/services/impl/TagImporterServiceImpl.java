package com.trs.core.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.AccessControlException;
import java.util.Iterator;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFormatException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.tagging.InvalidTagFormatException;
import com.day.cq.tagging.TagManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trs.core.services.TagImporterService;
import com.trs.core.services.TrsConfigurationService;
import com.trs.core.utils.TrsConstants;

@Component(immediate = true, service = TagImporterService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class TagImporterServiceImpl implements TagImporterService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TagImporterServiceImpl.class);
    public static final String ASSET_RENDITION_SUFFIX = "/jcr:content/renditions/original/jcr:content";
    public static final String TRS_RESOURCE_RESOLVER_SUBSERVICE = "trs-taxonomy-subservice";
    public static final String TAG_NAMESPACE_SEPARATOR = ":";
    public static final String TAG_ID_SEPARATOR = "/";
    public static final String JCR_DATA = "jcr:data";
    private static final String TAG_MAPPING_ERROR_MESSAGE= "Error while converting tag mapping json object to hashmap :";
    
    @Reference
    private TrsConfigurationService trsConfig;


    @Override
    public void iterateRows(ResourceResolver resourceResolver, Map<String, String> tagMappings,
            StringJoiner errorItems, final Iterator<Row> rows) throws PersistenceException {
        int rowCounter = 0;
        while (rows.hasNext()) {
            final Row row = rows.next();
            final Iterator<Cell> cells = row.cellIterator();
            
            
            iterateCells(resourceResolver, tagMappings,errorItems, rowCounter,
                    cells);
            rowCounter++;
        }
    }

    private void iterateCells(ResourceResolver resourceResolver,
            Map<String, String> tagMappings, StringJoiner errorItems, int rowCounter,
            final Iterator<Cell> cells) throws PersistenceException {
        
        final Pattern textPattern = Pattern.compile("\\{\\{(.+)}}$");
        StringBuilder tagName = new StringBuilder();
        int cellCounter = 0;
        
        while (cells.hasNext()) {

            final Cell cell = cells.next();

            String cellValue = StringUtils.trimToNull(cell.getStringCellValue());

            cellValue = StringUtils.stripToEmpty(cellValue);
            String title = textPattern.matcher(cellValue).replaceAll("");
            title = StringUtils.stripToEmpty(title);

            String name;

            final Matcher matcher = textPattern.matcher(cellValue);

            if (matcher.find() && matcher.groupCount() == 1) {
                name = matcher.group(1);
                name = StringUtils.stripToEmpty(name);
            } else {
                name = JcrUtil.createValidName(title);
            }

            tagName.append(JcrUtil.createValidName(title));
            if (!tagMappings.containsKey(name)) {
                tagMappings.put(name, tagName.toString());
            }

            createMappingNode(resourceResolver, tagName, name);

            TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
            if (tagManager.resolve(tagName.toString()) == null) {
                try {
                    tagManager.createTag(
                            tagName.toString()
                                    + (rowCounter == 0 ? TAG_NAMESPACE_SEPARATOR : StringUtils.EMPTY),
                            title, StringUtils.EMPTY);
                    resourceResolver.commit();
                    LOGGER.info("Tag created with title :" + title + " & derived node name : " + tagName
                            + " & name put in mapping : " + name);
                } catch (AccessControlException | InvalidTagFormatException e) {
                    errorItems.add(cellValue).add(System.lineSeparator());
                    LOGGER.error("Error while creating tag :", e);
                }
            } else {
                LOGGER.info("The following tag already exists : " + tagName.toString());
            }
            if (cellCounter == 0) {
                tagName.append(TAG_NAMESPACE_SEPARATOR);
            } else {
                tagName.append(TAG_ID_SEPARATOR);
            }
            cellCounter++;

        }
    }

    private void createMappingNode(ResourceResolver resourceResolver, StringBuilder tagName, String name) {
        Session session = resourceResolver.adaptTo(Session.class);
        try {
            LOGGER.info("Create mapping node for : "+name);
            Node mappingParentNode = JcrUtils.getOrCreateByPath(
                    trsConfig.getTrsXylemeMappingsPath(), "nt:unstructured", session);
            LOGGER.info("Parent node : "+mappingParentNode.getName());
            if (JcrUtils.getNodeIfExists(mappingParentNode, name) == null) {
                Node mappingNode = JcrUtils.getOrAddNode(mappingParentNode, name);
                LOGGER.info("Created tag node : "+mappingNode.getName());
                mappingNode.setProperty(TrsConstants.XYLEME_TAG_MAPPING_NODE_PROPERTY,
                        tagName.toString());
                session.save();
            }
        } catch (RepositoryException e1) {
            LOGGER.error("Error while creating Xyleme tag mapping node : ", e1);
        }
    }

    @Override
    public Map<String, String> readTagMappingFile(ResourceResolver resourceResolver, Map<String, String> tagMappings,ObjectMapper mapper)
            throws IOException {
        try {
            // reading the mapping file from dam
            if (resourceResolver.getResource(trsConfig.getXylemeAEMTagMappingFilePath() + ASSET_RENDITION_SUFFIX) != null) {
                Node node = resourceResolver.getResource(trsConfig.getXylemeAEMTagMappingFilePath() + ASSET_RENDITION_SUFFIX)
                        .adaptTo(Node.class);
                InputStream in;

                in = node.getProperty(JCR_DATA).getBinary().getStream();
                String jsonAsString = IOUtils.toString(in, StandardCharsets.UTF_8);
                in.close();
                tagMappings = mapper.readValue(jsonAsString, Map.class);
            }
        } catch (ValueFormatException e) {
            LOGGER.error(TAG_MAPPING_ERROR_MESSAGE, e);
        } catch (PathNotFoundException e) {
            LOGGER.error(TAG_MAPPING_ERROR_MESSAGE, e);
        } catch (RepositoryException e) {
            LOGGER.error(TAG_MAPPING_ERROR_MESSAGE, e);
        }
        return tagMappings;
    }
}
