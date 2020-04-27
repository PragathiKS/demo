package com.tetrapak.publicweb.core.schedulers;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.tetrapak.publicweb.core.beans.pxp.BearerToken;
import com.tetrapak.publicweb.core.beans.pxp.File;
import com.tetrapak.publicweb.core.beans.pxp.Files;
import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;
import com.tetrapak.publicweb.core.beans.pxp.Packagetype;
import com.tetrapak.publicweb.core.beans.pxp.ProcessingEquipement;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.APIGEEService;
import com.tetrapak.publicweb.core.services.ProductService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;

/**
 * @author Sandip Kumars
 * 
 *         Full Feed Scheduler for products import.
 *
 */
@Designate(ocd = FullFeedImportScheduledTask.Config.class)
@Component(service = Runnable.class)
public class FullFeedImportScheduledTask implements Runnable {

    @ObjectClassDefinition(
            name = "Full FeedProduct Import Scheduled Job",
            description = "Full FeedProduct Import Scheduled Job")
    public static @interface Config {

        @AttributeDefinition(name = "Cron-job expression")
        String scheduler_expression() default "0 0 0 ? * SUN *";

        @AttributeDefinition(name = "Disable Scheduled Task", description = "Disable Scheduled Task")
        boolean schedulerDisable() default false;

        @AttributeDefinition(name = "Refresh Bearer Token Time", description = "Refresh Bearer Token Time")
        int schedulerRefreshTokenTime() default 2700000;

    }

    private static final Logger LOGGER = LoggerFactory.getLogger(FullFeedImportScheduledTask.class);

    /** The apiGEE service. */
    @Reference
    APIGEEService apiGEEService;

    /** The product service. */
    @Reference
    ProductService productService;

    /** The replicator. */
    @Reference
    Replicator replicator;

    /** The resolverFactory. */
    @Reference
    private ResourceResolverFactory resolverFactory;

    /** The resolver. */
    private ResourceResolver resolver;

    /** The session. */
    private Session session;

    /** The timer. */
    private Timer timer;

    /** The bearer token. */
    private BearerToken bearerToken;

    /** The isDisabled. */
    private Boolean isDisabled = false;

    /** The refresh token time. */
    private int refreshTokenTime;

    /** The resolver. */
    private static final String FULL_FEED_FILES_URI = "/equipment/pxpparameters/files/";

    /**
     * start scheduler
     */
    @Override
    public void run() {
        if (Boolean.TRUE.equals(isDisabled)) {
            LOGGER.info("{{FullFeedImportScheduledTask is disabled}}");
            return;
        }
        setResourceResolver();
        if (resolver == null) {
            LOGGER.info("Tetrapak System User Session is null");
            return;
        }
        timer = new Timer();
        try {
            setBearerToken();
            if (bearerToken != null && StringUtils.isNotBlank(bearerToken.getAccessToken())) {
                Files listOfFiles = apiGEEService.getListOfFiles("full", bearerToken.getAccessToken());
                if (listOfFiles.getFiles() != null && !listOfFiles.getFiles().isEmpty()) {
                    for (File file : listOfFiles.getFiles()) {
                        processFile(file);
                    }
                }
            }
            replicateAllProducts();
        } finally {

            timer.cancel();
            if (resolver.isLive()) {
                resolver.close();
            }
            if (session != null && session.isLive()) {
                session.logout();
            }
        }

    }

    /**
     * @param file
     */
    private void processFile(File file) {
        if (file != null && StringUtils.isNotBlank(file.getName())) {
            String fileType = productService.getFileType(file.getName());
            String language = productService.getLanguage(file.getName());
            switch (fileType) {
                case "fillingmachines":
                    processFillingMachines(file.getName(), fileType, language);
                    break;
                case "processingequipments":
                    processEquipments(file.getName(), fileType, language);
                    break;
                case "packagetypes":
                    processPackageTypes(file.getName(), fileType, language);
                    break;
                default:
                    LOGGER.info("Not a valid file type to process for url {}", file.getName());
                    break;
            }
        }
    }

    /**
     * @param fileURI
     * @param fileType
     * @param language
     */
    private void processFillingMachines(String fileURI, String fileType, String language) {
        List<FillingMachine> fillingMachines = apiGEEService.getFillingMachines(bearerToken.getAccessToken(),
                FULL_FEED_FILES_URI + fileURI);
        if (!fillingMachines.isEmpty()) {
            productService.createProductFillingMachine(resolver, session, fileType, fillingMachines, language);
        }

    }

    /**
     * @param fileURI
     * @param fileType
     * @param language
     */
    private void processEquipments(String fileURI, String fileType, String language) {
        List<ProcessingEquipement> equipements = apiGEEService.getProcessingEquipements(bearerToken.getAccessToken(),
                FULL_FEED_FILES_URI + fileURI);
        if (!equipements.isEmpty()) {
            productService.createProductProcessingEquipement(resolver, session, fileType, equipements, language);
        }
    }

    /**
     * @param fileURI
     * @param fileType
     * @param language
     */
    private void processPackageTypes(String fileURI, String fileType, String language) {
        List<Packagetype> packageTypes = apiGEEService.getPackageTypes(bearerToken.getAccessToken(),
                FULL_FEED_FILES_URI + fileURI);
        if (!packageTypes.isEmpty()) {
            productService.createProductPackageType(resolver, session, fileType, packageTypes, language);
        }
    }

    /**
     * set bearer token
     */
    private void setBearerToken() {
        bearerToken = apiGEEService.getBearerToken();
        TimerTask scheduleBearerTokenUpdate = new TimerTask() {
            public void run() {
                bearerToken = apiGEEService.getBearerToken();
            }
        };
        timer.scheduleAtFixedRate(scheduleBearerTokenUpdate, refreshTokenTime, refreshTokenTime);
    }

    /**
     * replicate products.
     */
    private void replicateAllProducts() {
        try {
            replicator.replicate(session, ReplicationActionType.ACTIVATE,
                    PWConstants.ROOT_PATH + PWConstants.SLASH + PWConstants.PXP);
        } catch (ReplicationException e) {
            LOGGER.error("Replication Exception in activating PXP products");
        }
    }

    /**
     * Sets the resource resolver.
     */
    private void setResourceResolver() {
        this.resolver = GlobalUtil.getResourceResolverFromSubService(resolverFactory);
    }

    /**
     * @param config
     */
    @Activate
    protected void activate(final Config config) {
        isDisabled = config.schedulerDisable();
        refreshTokenTime = config.schedulerRefreshTokenTime();
    }

}
