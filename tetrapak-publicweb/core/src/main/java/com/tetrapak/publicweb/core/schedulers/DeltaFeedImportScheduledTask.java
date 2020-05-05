package com.tetrapak.publicweb.core.schedulers;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import javax.jcr.Session;
import org.apache.commons.lang3.StringUtils;
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
import com.day.cq.replication.Replicator;
import com.tetrapak.publicweb.core.beans.pxp.BearerToken;
import com.tetrapak.publicweb.core.beans.pxp.DeltaFillingMachine;
import com.tetrapak.publicweb.core.beans.pxp.DeltaPackageType;
import com.tetrapak.publicweb.core.beans.pxp.DeltaProcessingEquipement;
import com.tetrapak.publicweb.core.beans.pxp.File;
import com.tetrapak.publicweb.core.beans.pxp.Files;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.APIGEEService;
import com.tetrapak.publicweb.core.services.ProductService;
import com.tetrapak.publicweb.core.services.config.DeltaPXPConfig;
import com.tetrapak.publicweb.core.utils.DeltaFeedUtil;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.ProductUtil;
import com.tetrapak.publicweb.core.utils.ResourceUtil;

/**
 * The Class DeltaFeedImportScheduledTask.
 *
 * @author Sandip Kumar
 * 
 *         Delta Feed Scheduler for products import.
 */
@Designate(ocd = DeltaPXPConfig.class)
@Component(
        immediate = true,
        service = DeltaFeedImportScheduledTask.class,
        configurationPolicy = ConfigurationPolicy.REQUIRE)
public class DeltaFeedImportScheduledTask implements Runnable {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeltaFeedImportScheduledTask.class);

    /** The apiGEE service. */
    @Reference
    private APIGEEService apiGEEService;

    /** The replicator. */
    @Reference
    private Replicator replicator;

    /** The product service. */
    @Reference
    private ProductService productService;

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

    /** The scheduler ID. */
    private int schedulerID;

    /** The paths to activate. */
    private Set<String> pathsToActivate;

    /** The paths to deactivate. */
    private Set<String> deletedProducts;

    /**
     * start scheduler.
     */
    @Override
    public void run() {
        LOGGER.info("{{Public Web Delta Feed Product Import Scheduled Task Started}}");
        setResourceResolver();
        if (resolver == null) {
            LOGGER.debug("Tetrapak System User Session is null");
            return;
        }
        pathsToActivate = new HashSet<>();
        deletedProducts = new HashSet<>();
        this.session = resolver.adaptTo(Session.class);
        timer = new Timer();
        try {
            setBearerToken();
            processFiles();
            DeltaFeedUtil.activateUpdatedProducts(resolver, replicator, session, pathsToActivate);
            DeltaFeedUtil.deactivatePDPs(resolver, replicator, session, deletedProducts);
        } finally {
            timer.cancel();
            timer.purge();
            if (resolver.isLive()) {
                resolver.close();
            }
            LOGGER.info("{{Public Web Delta Feed Product Import Scheduled Task Ended}}");
        }
    }

    /**
     * process files.
     */
    private void processFiles() {
        if (bearerToken != null && StringUtils.isNotBlank(bearerToken.getAccessToken())) {
            Files listOfFiles = apiGEEService.getListOfFiles(PWConstants.DELTA_FEED, bearerToken.getAccessToken());
            if (listOfFiles.getFiles() != null && !listOfFiles.getFiles().isEmpty()) {
                for (File file : listOfFiles.getFiles()) {
                    processFile(file);
                }
            }
        }
    }

    /**
     * Process file.
     *
     * @param file
     *            the file
     */
    private void processFile(File file) {
        if (file != null && StringUtils.isNotBlank(file.getName())) {
            String fileType = ProductUtil.getFileType(file.getName());
            String language = ProductUtil.getLanguage(file.getName());
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
     * Process filling machines.
     *
     * @param fileURI
     *            the file URI
     * @param fileType
     *            the file type
     * @param language
     *            the language
     */
    private void processFillingMachines(String fileURI, String fileType, String language) {
        DeltaFillingMachine deltaFillingMachines = apiGEEService.getDeltaFillingMachines(bearerToken.getAccessToken(),
                PWConstants.FEED_FILES_URI + fileURI);
        if (deltaFillingMachines != null && !deltaFillingMachines.getFillingMachine().isEmpty()) {
            pathsToActivate.addAll(productService.createOrUpdateProductFillingMachine(resolver, session, fileType,
                    deltaFillingMachines.getFillingMachine(), language));
        }
        if (!deltaFillingMachines.getDeleted().isEmpty()) {
            deletedProducts.addAll(deltaFillingMachines.getDeleted());
            for (String deletedProduct : deltaFillingMachines.getDeleted()) {
                ResourceUtil.deactivatePath(replicator, session,
                        PWConstants.PXP_ROOT_PATH + PWConstants.SLASH + PWConstants.FILLING_MACHINE + deletedProduct);
                ResourceUtil.deleteResource(resolver, session,
                        PWConstants.PXP_ROOT_PATH + PWConstants.SLASH + PWConstants.FILLING_MACHINE + deletedProduct);
            }

        }
    }

    /**
     * Process equipments.
     *
     * @param fileURI
     *            the file URI
     * @param fileType
     *            the file type
     * @param language
     *            the language
     */
    private void processEquipments(String fileURI, String fileType, String language) {
        DeltaProcessingEquipement deltaEquipements = apiGEEService
                .getDeltaProcessingEquipements(bearerToken.getAccessToken(), PWConstants.FEED_FILES_URI + fileURI);
        if (deltaEquipements != null && !deltaEquipements.getProcessingEquipement().isEmpty()) {
            pathsToActivate.addAll(productService.createOrUpdateProductProcessingEquipement(resolver, session, fileType,
                    deltaEquipements.getProcessingEquipement(), language));
        }
        if (!deltaEquipements.getDeleted().isEmpty()) {
            deletedProducts.addAll(deltaEquipements.getDeleted());
            for (String deletedProduct : deltaEquipements.getDeleted()) {
                ResourceUtil.deactivatePath(replicator, session,
                        PWConstants.PXP_ROOT_PATH + PWConstants.SLASH + PWConstants.FILLING_MACHINE + deletedProduct);
                ResourceUtil.deleteResource(resolver, session,
                        PWConstants.PXP_ROOT_PATH + PWConstants.SLASH + PWConstants.FILLING_MACHINE + deletedProduct);
            }
        }
    }

    /**
     * Process package types.
     *
     * @param fileURI
     *            the file URI
     * @param fileType
     *            the file type
     * @param language
     *            the language
     */
    private void processPackageTypes(String fileURI, String fileType, String language) {
        DeltaPackageType deltaPackageTypes = apiGEEService.getDeltaPackageTypes(bearerToken.getAccessToken(),
                PWConstants.FEED_FILES_URI + fileURI);
        if (deltaPackageTypes != null && !deltaPackageTypes.getPackagetype().isEmpty()) {
            pathsToActivate.addAll(productService.createOrUpdateProductPackageType(resolver, session, fileType,
                    deltaPackageTypes.getPackagetype(), language));
        }
        if (!deltaPackageTypes.getDeleted().isEmpty()) {
            deletedProducts.addAll(deltaPackageTypes.getDeleted());
            for (String deletedProduct : deltaPackageTypes.getDeleted()) {
                ResourceUtil.deactivatePath(replicator, session,
                        PWConstants.PXP_ROOT_PATH + PWConstants.SLASH + PWConstants.PACKAGE_TYPE + deletedProduct);
                ResourceUtil.deleteResource(resolver, session,
                        PWConstants.PXP_ROOT_PATH + PWConstants.SLASH + PWConstants.FILLING_MACHINE + deletedProduct);
            }
        }
    }

    /**
     * set bearer token.
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
     * Sets the resource resolver.
     */
    private void setResourceResolver() {
        this.resolver = GlobalUtil.getResourceResolverFromSubService(resolverFactory);
    }

    /**
     * Activate.
     *
     * @param config
     *            the config
     */
    @Activate
    protected void activate(DeltaPXPConfig config) {
        schedulerID = PWConstants.DELTA_FEED_SCHEDULER_ID.hashCode();
        refreshTokenTime = config.schedulerRefreshTokenTime();
        addScheduler(config);
    }

    /**
     * Modified.
     *
     * @param config
     *            the config
     */
    @Modified
    protected void modified(DeltaPXPConfig config) {
        removeScheduler();
        schedulerID = PWConstants.DELTA_FEED_SCHEDULER_ID.hashCode();
        refreshTokenTime = config.schedulerRefreshTokenTime();
        addScheduler(config);
    }

    /**
     * Deactivate.
     *
     * @param config
     *            the config
     */
    @Deactivate
    protected void deactivate(DeltaPXPConfig config) {
        removeScheduler();
    }

    /**
     * Add a scheduler based on the scheduler ID.
     *
     * @param config
     *            the config
     */
    private void addScheduler(DeltaPXPConfig config) {
        if (!config.deltaFeedSchedulerDisable()) {
            ScheduleOptions sopts = scheduler.EXPR(config.deltaFeedSchedulerExpression());
            sopts.name(String.valueOf(schedulerID));
            sopts.canRunConcurrently(false);
            scheduler.schedule(this, sopts);
            LOGGER.debug("DeltaFeedImportScheduledTask added succesfully");
        } else {
            LOGGER.debug("DeltaFeedImportScheduledTask is Disabled, no scheduler job created");
        }
    }

    /**
     * Remove a scheduler based on the scheduler ID.
     */
    private void removeScheduler() {
        LOGGER.debug("Removing DeltaFeedImportScheduledTask Job '{}'", schedulerID);
        scheduler.unschedule(String.valueOf(schedulerID));
    }

}
