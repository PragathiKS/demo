package com.tetrapak.publicweb.core.schedulers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletException;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
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

import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;
import com.day.cq.wcm.msm.api.RolloutManager;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.CreateLiveCopyService;
import com.tetrapak.publicweb.core.services.config.LBConfig;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.PageUtil;
import com.tetrapak.publicweb.core.utils.ResourceUtil;

import static com.tetrapak.publicweb.core.constants.PWConstants.PUBLICWEB_XF_PATH;

/**
 * The Class LionBridgeScheduledTask.
 */
@Designate(ocd = LBConfig.class)
@Component(immediate = true, service = LionBridgeScheduledTask.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class LionBridgeScheduledTask implements Runnable {

    /** The scheduler ID. */
    private int schedulerID;

    /** The scheduler. */
    @Reference
    private Scheduler scheduler;

    /** The resolverFactory. */
    @Reference
    private ResourceResolverFactory resolverFactory;

    /** The create live copy service. */
    @Reference
    private CreateLiveCopyService createLiveCopyService;

    /** The rollout manager. */
    @Reference
    private RolloutManager rolloutManager;

    /** The live rel manager. */
    @Reference
    private LiveRelationshipManager liveRelManager;

    @Reference
    private Replicator replicator;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LionBridgeScheduledTask.class);

    /** The map of LB resource to page mapping. */
    private Map<String,String> lbResourceToPageMapping;
    
    /** The map of sorted LB resource to page mapping. */
    private LinkedHashMap<String, String> sortedLbResourceToPageMapping ;
    
    /** The set of LB page processed. */
    private Set<String> pagesProcessed;

    private Set<String> xfsProcessed;
    
    List<String> deleteResourcesList;

    private Map<String,String> lbResourceToXFMapping;

    /**
     * Run.
     */
    @Override
    public void run() {
    	Comparator<String> sortComparatorByContentPathHierarchy = (String path1,String path2) -> {
            if(path1.split(PWConstants.SLASH).length < path2.split(PWConstants.SLASH).length)return -1;
            else if(path1.split(PWConstants.SLASH).length > path2.split(PWConstants.SLASH).length)return 1;
            else return 0;
        };
        lbResourceToPageMapping = new HashMap<>();
        lbResourceToXFMapping = new HashMap<>();
        LOGGER.debug("{{LionBridgeScheduledTask started}}");
        try (final ResourceResolver resolver = GlobalUtil.getResourceResolverFromSubService(resolverFactory)) {
            Resource lbTranslationRes = resolver.getResource(PWConstants.LB_TRANSLATED_PAGES_NODE);
            if (Objects.nonNull(lbTranslationRes) && lbTranslationRes.hasChildren()) {
                Iterator<Resource> lbChildResources = lbTranslationRes.getChildren().iterator();
                deleteResourcesList = new ArrayList<>();
                while (lbChildResources.hasNext()) {
                    Resource lbChildResource = lbChildResources.next();
                    LOGGER.info("LionBridgeScheduledTask starts processing on {}", lbChildResource.getName());
                    Long lbResourceTime = Long
                            .valueOf(lbChildResource.getName().replace("lbtranslatedpages-", StringUtils.EMPTY));
                    Long timeDiff = Calendar.getInstance().getTimeInMillis() - lbResourceTime;
                    LOGGER.info("LionBridgeScheduledTask time diff on resource {} is {}", lbChildResource.getName(),
                            timeDiff);
                    if (timeDiff >= 600000) {
                        LOGGER.info("LionBridgeScheduledTask createRolloutAndActivate started on {}",
                                lbChildResource.getName());
                        pagesToRolloutAndActivate(resolver, lbChildResource);
                    }

                }
                
                if (!lbResourceToPageMapping.isEmpty()) {
                	sortedLbResourceToPageMapping = lbResourceToPageMapping.entrySet().stream()
                    .sorted(Map.Entry.<String,String>comparingByValue(sortComparatorByContentPathHierarchy))
                    .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue,(e1,e2)->e1,LinkedHashMap::new));
                    createRolloutAndActivate(resolver, sortedLbResourceToPageMapping);
                }
                if (!lbResourceToXFMapping.isEmpty()) {
                    activateXFs(resolver, lbResourceToXFMapping);
                }
                if (!deleteResourcesList.isEmpty()) {
                    deleteResources(resolver, deleteResourcesList);
                }
            }
        } finally {
            LOGGER.debug("{{LionBridgeScheduledTask ended}}");
        }

    }

    private void activateXFs(ResourceResolver resolver, Map<String, String> lbResourceToXFMapping) {
        xfsProcessed = new HashSet<String>();
        lbResourceToXFMapping.forEach(
                (lbNode, translatedPagePath) -> {
                    try {
                        if(resolver.getResource(translatedPagePath) != null && !xfsProcessed.contains(translatedPagePath)) {
                            LOGGER.info("LionBridgeScheduledTask activate XF {}", translatedPagePath);
                            Session session = resolver.adaptTo(Session.class);
                            replicator.replicate(session, ReplicationActionType.ACTIVATE, translatedPagePath);
                            LOGGER.debug("{} ,Replicated", translatedPagePath);
                        }
                        deleteResourcesList.add(lbNode);
                        xfsProcessed.add(translatedPagePath);
                    }catch (UnsupportedOperationException | ReplicationException e) {
                        LOGGER.error("An error occurred while activating XF", e);
                    }
                }
        );
    }

    /**
     * pagesTorollout and activate.
     *
     * @param resolver            the resolver
     * @param lbChildResource            the lb child resource
     */
    public void pagesToRolloutAndActivate(final ResourceResolver resolver, final Resource lbChildResource) {
        if (lbChildResource.getValueMap().containsKey(PWConstants.LB_TRANSLATED_PROP)) {
            String translatedPage = lbChildResource.getValueMap().get(PWConstants.LB_TRANSLATED_PROP).toString();
            if(translatedPage.startsWith(PUBLICWEB_XF_PATH)){
                LOGGER.info("LionBridgeScheduledTask XFs to activate {}", translatedPage);
                lbResourceToXFMapping.put(lbChildResource.getPath(), translatedPage);
            }else{
                LOGGER.info("LionBridgeScheduledTask pagesToRolloutAndActivate on page {}", translatedPage);
                lbResourceToPageMapping.put(lbChildResource.getPath(), translatedPage);
            }

        }
    }

    /**
     * Createrollout and activate.
     *
     * @param resolver            the resolver
     * @param translatedPages the translated pages
     */
    public void createRolloutAndActivate(final ResourceResolver resolver, final Map<String,String> translatedPages) {
    	pagesProcessed = new HashSet<String>();
    	translatedPages.forEach(
    	          (lbNode, translatedPagePath) -> {
    				  try {
    		        	if(resolver.getResource(translatedPagePath) != null && !pagesProcessed.contains(translatedPagePath)) {
    			            String language = PageUtil.getLanguagePage(resolver.getResource(translatedPagePath)).getName();
    			            LOGGER.info("LionBridgeScheduledTask createRolloutAndActivate on page {}", translatedPagePath);
    			            createLiveCopyService.createLiveCopy(resolver, translatedPagePath, rolloutManager, liveRelManager, language,
    			                    false, true);
    		        	}
    		            deleteResourcesList.add(lbNode);
    		            pagesProcessed.add(translatedPagePath);
    		        }catch (RepositoryException | WCMException | IOException | UnsupportedOperationException | ServletException e) {
    		            LOGGER.error("An error occurred while creating live copy", e);
    		        }
    	          }
    	      );
    }

    /**
     * Update lionbridge resource.
     *
     * @param resolver            the resolver
     * @param deleteResourcesList the delete resources list
     */
    public void deleteResources(final ResourceResolver resolver, final List<String> deleteResourcesList) {
        for (String deleteResources : deleteResourcesList) {
            LOGGER.info("LionBridgeScheduledTask deleteResources started on {}", deleteResources);
            ResourceUtil.deleteResource(resolver, resolver.adaptTo(Session.class), deleteResources);

        }
    }

    /**
     * Activate.
     *
     * @param config
     *            the config
     */
    @Activate
    protected void activate(final LBConfig config) {
        schedulerID = PWConstants.LB_SCHEDULER_ID.hashCode();
        addScheduler(config);
    }

    /**
     * Modified.
     *
     * @param config
     *            the config
     */
    @Modified
    protected void modified(final LBConfig config) {
        removeScheduler();
        schedulerID = PWConstants.LB_SCHEDULER_ID.hashCode();
        addScheduler(config);
    }

    /**
     * Deactivate.
     *
     * @param config
     *            the config
     */
    @Deactivate
    protected void deactivate(final LBConfig config) {
        removeScheduler();
    }

    /**
     * Add a scheduler based on the scheduler ID.
     *
     * @param config
     *            the config
     */
    private void addScheduler(final LBConfig config) {
        if (!config.lbSchedulerDisable()) {
            final ScheduleOptions sopts = scheduler.EXPR(config.lbSchedulerExpression());
            sopts.name(String.valueOf(schedulerID));
            sopts.canRunConcurrently(false);
            scheduler.schedule(this, sopts);
            LOGGER.debug("LionBridgeScheduledTask added succesfully");
        } else {
            LOGGER.debug("LionBridgeScheduledTask is Disabled, no scheduler job created");
        }
    }

    /**
     * Remove a scheduler based on the scheduler ID.
     */
    private void removeScheduler() {
        LOGGER.debug("Removing LionBridgeScheduledTask Job '{}'", schedulerID);
        scheduler.unschedule(String.valueOf(schedulerID));
    }

}
