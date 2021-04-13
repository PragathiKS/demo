package com.trs.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.AssetManager;
import com.day.cq.dam.commons.util.DamUtil;
import com.trs.core.reports.StatefulReport;
import com.trs.core.servlets.TagImporter;

public final class TrsUtils {

    private static final Logger LOG = LoggerFactory.getLogger(TrsUtils.class);
    
    private TrsUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static InputStream getInputStreamFromFile(Node node)
            throws RepositoryException {

        return node.getProperty(TagImporter.JCR_DATA).getBinary().getStream();
    }

    public static Integer createExcelSheetRow(String[] columnValues, Sheet sheet, Integer authorLogRowCounter) {
        int columnCounter = 0;
        Row row = sheet.createRow(authorLogRowCounter);

        for (String columnValue : columnValues) {
            Cell column = row.createCell(columnCounter);
            column.setCellValue(columnValue);
            columnCounter++;
        }
        authorLogRowCounter++;
        return authorLogRowCounter;
    }

    /**
     * 
     * This method creates service resource resolver for TrS operations
     * 
     * @param resolverFactory
     * @return Resource Resolver
     * @throws LoginException
     */
    public static ResourceResolver getTrsResourceResolver(ResourceResolverFactory resolverFactory)
            throws LoginException {
        Map<String, Object> resolverMappingParam = new HashMap<>();
        resolverMappingParam.put(ResourceResolverFactory.SUBSERVICE, TagImporter.TRS_RESOURCE_RESOLVER_SUBSERVICE);
        ResourceResolver resourceResolver = null;
        resourceResolver = resolverFactory.getServiceResourceResolver(resolverMappingParam);
        return resourceResolver;
    }

    public static void saveExcelSheetAsDamAsset(XSSFWorkbook workbook, ResourceResolver resourceResolver,
            String damPath) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        byte[] barray = bos.toByteArray();
        InputStream is = new ByteArrayInputStream(barray);
        AssetManager assetManager = resourceResolver.adaptTo(AssetManager.class);
        assetManager.createAsset(damPath, is, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                true);
        resourceResolver.commit();
    }

    public static void saveExcelSheetAsDamAsset(StatefulReport report, ResourceResolver resourceResolver,
            String damPath) throws IOException {
        saveExcelSheetAsDamAsset(report.getReportWorkbook(), resourceResolver, damPath);
    }

    public static boolean isVideoAsset(ResourceResolver resourceResolver, String assetPath) {
        boolean isVideoAsset = false;

        if (null != resourceResolver.getResource(assetPath)) {
            Asset asset = resourceResolver.getResource(assetPath).adaptTo(Asset.class);
            isVideoAsset = DamUtil.isVideo(asset);
        } else {
            LOG.info("Resource does not exist at the path :" + assetPath);
        }

        return isVideoAsset;
    }

    public static String millisToShortDHMS(long duration) {
        String res = "";
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration)
                - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
        long millis = TimeUnit.MILLISECONDS.toMillis(duration)
                - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(duration));

        if (days == 0) {
            res = String.format("%02d:%02d:%02d.%04d", hours, minutes, seconds, millis);
        }else {
            res = String.format("%dd %02d:%02d:%02d.%04d", days, hours, minutes, seconds, millis);
        }
        return res;
    }

}
