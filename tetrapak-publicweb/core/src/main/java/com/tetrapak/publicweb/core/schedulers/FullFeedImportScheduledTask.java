package com.tetrapak.publicweb.core.schedulers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
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
import com.tetrapak.publicweb.core.services.config.PXPConfig;
import com.tetrapak.publicweb.core.utils.GlobalUtil;

/**
 * @author Sandip Kumar
 * 
 *         Full Feed Scheduler for products import.
 *
 */
@Designate(ocd = PXPConfig.class)
@Component(immediate = true, service = FullFeedImportScheduledTask.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class FullFeedImportScheduledTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(FullFeedImportScheduledTask.class);

    /** The apiGEE service. */
    @Reference
    private APIGEEService apiGEEService;

    /** The product service. */
    @Reference
    private ProductService productService;

    /** The replicator. */
    @Reference
    private Replicator replicator;

    /** The resolverFactory. */
    @Reference
    private ResourceResolverFactory resolverFactory;

    /** The scheduler. */
    @Reference
    private Scheduler scheduler;

    /** The resolver. */
    private ResourceResolver resolver;

    /** The session. */
    private Session session;

    /** The timer. */
    private Timer timer;

    /** The bearer token. */
    private BearerToken bearerToken;

    /** The refresh token time. */
    private int refreshTokenTime;

    /** The dam root path. */
    private String damRootPath;

    /** The video Types. */
    private String videoTypes;

    /** The scheduler ID. */
    private int schedulerID;

    private Set<String> pathsToReplicate;

    /**
     * start scheduler
     */
    @Override
    public void run() {
        LOGGER.info("{{Public Web Full Feed Product Import Scheduled Task Started}}");
        setResourceResolver();
        pathsToReplicate = new HashSet<>();
        if (resolver == null) {
            LOGGER.debug("Tetrapak System User Session is null");
            return;
        }
        this.session = resolver.adaptTo(Session.class);
        timer = new Timer();
        try {
            setBearerToken();
            processFiles();
            replicateAllProducts();
        } finally {
            timer.cancel();
            timer.purge();
            if (resolver.isLive()) {
                resolver.close();
            }
            LOGGER.info("{{Public Web Full Feed Product Import Scheduled Task Ended}}");
        }
    }

    /**
     * process files
     */
    private void processFiles() {
        if (bearerToken != null && StringUtils.isNotBlank(bearerToken.getAccessToken())) {
            Files listOfFiles = apiGEEService.getListOfFiles("full", bearerToken.getAccessToken());
            if (listOfFiles.getFiles() != null && !listOfFiles.getFiles().isEmpty()) {
                for (File file : listOfFiles.getFiles()) {
                    processFile(file);
                }
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
                PWConstants.FULL_FEED_FILES_URI + fileURI);
        if (!fillingMachines.isEmpty()) {
            pathsToReplicate.addAll(productService.createProductFillingMachine(resolver, session, fileType,
                    fillingMachines, language, damRootPath, videoTypes));
        }

    }

    /**
     * @param fileURI
     * @param fileType
     * @param language
     */
    private void processEquipments(String fileURI, String fileType, String language) {
        List<ProcessingEquipement> equipements = apiGEEService.getProcessingEquipements(bearerToken.getAccessToken(),
                PWConstants.FULL_FEED_FILES_URI + fileURI);
        if (!equipements.isEmpty()) {
            pathsToReplicate.addAll(productService.createProductProcessingEquipement(resolver, session, fileType,
                    equipements, language, damRootPath, videoTypes));
        }
    }

    /**
     * @param fileURI
     * @param fileType
     * @param language
     */
    private void processPackageTypes(String fileURI, String fileType, String language) {
        List<Packagetype> packageTypes = apiGEEService.getPackageTypes(bearerToken.getAccessToken(),
                PWConstants.FULL_FEED_FILES_URI + fileURI);
        if (!packageTypes.isEmpty()) {
            pathsToReplicate.addAll(productService.createProductPackageType(resolver, session, fileType, packageTypes,
                    language, damRootPath, videoTypes));
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
                if (bearerToken != null) {
                    LOGGER.debug("Bearer Token Refreshed. New token is {}", bearerToken.getAccessToken());
                }
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
            replicateAllChildResource(
                    resolver.getResource(PWConstants.ROOT_PATH + PWConstants.SLASH + PWConstants.PXP));
        } catch (ReplicationException e) {
            LOGGER.error("Replication Exception in activating PXP products", e.getMessage(), e);
        }
    }

    private void replicateAllChildResource(Resource rootRes) throws ReplicationException {
        if (rootRes == null || !rootRes.hasChildren()) {
            return;
        }
        Iterator<Resource> itr = rootRes.listChildren();
        while (itr.hasNext()) {
            Resource res = itr.next();
            replicator.replicate(session, ReplicationActionType.ACTIVATE, res.getPath());
            replicateAllChildResource(res);
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
    protected void activate(PXPConfig config) {
        schedulerID = PWConstants.FULL_FEED_SCHEDULER_ID.hashCode();
        refreshTokenTime = config.schedulerRefreshTokenTime();
        videoTypes = config.videoTypes();
        damRootPath = config.damRootPath();
        addScheduler(config);
    }

    /**
     * @param config
     */
    @Modified
    protected void modified(PXPConfig config) {
        removeScheduler();
        schedulerID = PWConstants.FULL_FEED_SCHEDULER_ID.hashCode();
        refreshTokenTime = config.schedulerRefreshTokenTime();
        videoTypes = config.videoTypes();
        damRootPath = config.damRootPath();
        addScheduler(config);
    }

    /**
     * @param config
     */
    @Deactivate
    protected void deactivate(PXPConfig config) {
        removeScheduler();
    }

    /**
     * Add a scheduler based on the scheduler ID
     */
    private void addScheduler(PXPConfig config) {
        if (!config.fullFeedSchedulerDisable()) {
            ScheduleOptions sopts = scheduler.EXPR(config.fullFeedSchedulerExpression());
            sopts.name(String.valueOf(schedulerID));
            sopts.canRunConcurrently(false);
            scheduler.schedule(this, sopts);
            LOGGER.debug("FullFeedImportScheduledTask added succesfully");
        } else {
            LOGGER.debug("FullFeedImportScheduledTask is Disabled, no scheduler job created");
        }
    }

    /**
     * Remove a scheduler based on the scheduler ID
     */
    private void removeScheduler() {
        LOGGER.debug("Removing FullFeedImportScheduledTask Job '{}'", schedulerID);
        scheduler.unschedule(String.valueOf(schedulerID));
    }

}
