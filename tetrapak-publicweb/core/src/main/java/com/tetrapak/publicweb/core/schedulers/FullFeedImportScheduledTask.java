package com.tetrapak.publicweb.core.schedulers;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tetrapak.publicweb.core.beans.pxp.BearerToken;
import com.tetrapak.publicweb.core.beans.pxp.File;
import com.tetrapak.publicweb.core.beans.pxp.Files;
import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;
import com.tetrapak.publicweb.core.beans.pxp.Packagetype;
import com.tetrapak.publicweb.core.beans.pxp.ProcessingEquipement;
import com.tetrapak.publicweb.core.services.APIGEEService;
import com.tetrapak.publicweb.core.services.ProductService;

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
        boolean scheduler_disable() default false;
        
        @AttributeDefinition(name = "Refresh Bearer Token Time", description = "Refresh Bearer Token Time")
        int scheduler_refreshTokenTime() default 2700000;

    }
    
    private static final Logger LOGGER =  LoggerFactory.getLogger(FullFeedImportScheduledTask.class);

    @Reference
    APIGEEService apiGEEService;
    
    @Reference
    ProductService productService;

    private Timer timer;

    private BearerToken bearerToken;
    
    private Boolean isDisabled = false;
    
    private int refreshTokenTime;
    
    private static final String FULL_FEED_FILES_URI = "/equipment/pxpparameters/files/";

    @Override
    public void run() {
        if(isDisabled) {
            LOGGER.info("{{FullFeedImportScheduledTask is disabled}}");
            return;
        }
        timer = new Timer();
        setBearerToken();
        if (bearerToken != null && StringUtils.isNotBlank(bearerToken.getAccessToken())) {
            Files listOfFiles = apiGEEService.getListOfFiles("full", bearerToken.getAccessToken());
            if (listOfFiles.getFiles() != null && !listOfFiles.getFiles().isEmpty()) {
                for (File file : listOfFiles.getFiles()) {
                    if(file != null && StringUtils.isNotBlank(file.getName())) {
                        String fileType = productService.getFileType(file.getName());
                        String language = productService.getLanguage(file.getName());
                        switch (fileType) {
                            case "fillingmachines":
                                processFillingMachines(file.getName(),fileType,language);
                                break;
                            case "processingequipments":
                                processEquipments(file.getName(),fileType,language);
                                break;
                            case "packagetypes":
                                processPackageTypes(file.getName(),fileType,language);
                                break;
                            default:
                                LOGGER.info("Not a valid file type to process for url {}",file.getName());
                                break;
                        }
                    }
                }
            }
        }
        timer.cancel();
    }
    
    private void processFillingMachines(String fileURI,String fileType,String language) {
        List<FillingMachine> fillingMachinesList = apiGEEService.getFillingMachines(bearerToken.getAccessToken(), FULL_FEED_FILES_URI+fileURI);
        if(!fillingMachinesList.isEmpty()) {
            productService.createProductRootIfNotExists(fileType);
            for(FillingMachine fillingMachine:fillingMachinesList) {
                productService.createProductFillingMachine(fillingMachine,language);
            }
        }
        
    }
    
    private void processEquipments(String fileURI,String fileType,String language) {
        List<ProcessingEquipement> equipements = apiGEEService.getProcessingEquipements(bearerToken.getAccessToken(), FULL_FEED_FILES_URI+fileURI);
        if(!equipements.isEmpty()) {
            productService.createProductRootIfNotExists(fileType);
            for(ProcessingEquipement equipement:equipements) {
                productService.createProductProcessingEquipement(equipement,language);
            }
        }
    }
     
    private void processPackageTypes(String fileURI,String fileType,String language) {
        List<Packagetype> packageTypes = apiGEEService.getPackageTypes(bearerToken.getAccessToken(), FULL_FEED_FILES_URI+fileURI);
        if(!packageTypes.isEmpty()) {
            productService.createProductRootIfNotExists(fileType);
            for(Packagetype packageType:packageTypes) {
                productService.createProductPackageType(packageType,language);
            }
        }
    }
    
    private void setBearerToken() {
        bearerToken = apiGEEService.getBearerToken();
        TimerTask scheduleBearerTokenUpdate = new TimerTask() {
            public void run() {
                bearerToken = apiGEEService.getBearerToken();
            }
        };
        timer.scheduleAtFixedRate(scheduleBearerTokenUpdate,refreshTokenTime,refreshTokenTime);
    }
    
    @Activate
    protected void activate(final Config config) {
        isDisabled = config.scheduler_disable();
        refreshTokenTime = config.scheduler_refreshTokenTime();
    }

}
