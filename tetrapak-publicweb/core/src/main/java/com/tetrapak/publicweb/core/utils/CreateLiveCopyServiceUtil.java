package com.tetrapak.publicweb.core.utils;

import java.util.List;
import java.util.Objects;

import javax.jcr.RangeIterator;
import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.msm.api.LiveRelationship;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;
import com.day.cq.wcm.msm.api.RolloutConfig;
import com.day.cq.wcm.msm.api.RolloutManager;
import com.day.cq.wcm.msm.api.RolloutManager.RolloutParams;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.config.CreateLiveCopyServiceConfig;

/**
 * The Class CreateLiveCopyServiceUtil.
 */
public final class CreateLiveCopyServiceUtil {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateLiveCopyServiceUtil.class);

    /**
     * Instantiates a new resource util.
     */
    private CreateLiveCopyServiceUtil() {
        /*
         * adding a private constructor to hide the implicit one
         */
    }

    /**
     * Rollout live copies.
     *
     * @param rolloutManager
     *            the rollout manager
     * @param blueprintPage
     *            the blueprint page
     * @param isDeep
     *            the is deep
     * @throws WCMException
     *             the WCM exception
     */
    public static void rolloutLiveCopies(RolloutManager rolloutManager, final Page blueprintPage, boolean isDeep)
            throws WCMException {
        LOGGER.debug("inside rolloutLiveCopies method");
        final RolloutParams rolloutParams = new RolloutParams();
        LOGGER.debug("isDeep : {}", isDeep);
        rolloutParams.isDeep = isDeep;
        rolloutParams.master = blueprintPage;
        rolloutParams.reset = false;
        rolloutParams.trigger = RolloutManager.Trigger.ROLLOUT;
        rolloutManager.rollout(rolloutParams);
    }
    
    /**
     * Gets the live copies.
     *
     * @param liveRelManager
     *            the live rel manager
     * @param res
     *            the res
     * @return the live copies
     * @throws WCMException
     *             the WCM exception
     */
    public static RolloutConfig[] getRollOutConfigs(LiveRelationshipManager liveRelManager, Resource res) throws WCMException {
        LOGGER.info("res path : {}", res.getPath());
        RangeIterator rangeIterator = liveRelManager.getLiveRelationships(res.getParent(), PWConstants.CONTENT_ROOT_PATH, null);
        RolloutConfig[] rolloutConfigs = null;
        while (Objects.nonNull(rangeIterator) && rangeIterator.hasNext()) {
            LiveRelationship liveCopy = (LiveRelationship) rangeIterator.next();
            rolloutConfigs = (RolloutConfig[])liveCopy.getRolloutConfigs().toArray();
            break;
        }
        return rolloutConfigs;
    }


    /**
     * Gets the live copy base paths.
     *
     * @param language
     *            the language
     * @param config
     *            the config
     * @return the live copy base paths
     */
    public static String[] getLiveCopyBasePaths(String language, CreateLiveCopyServiceConfig config) {
        String[] liveCopyBasePaths = null;
        switch (language) {
            case "en":
                liveCopyBasePaths = config.getEnglishLiveCopyBasePaths();
                break;
            case "fr":
                liveCopyBasePaths = config.getFrenchLiveCopyBasePaths();
                break;
            case "de":
                liveCopyBasePaths = config.getGermanLiveCopyBasePaths();
                break;
            case "es":
                liveCopyBasePaths = config.getSpanishLiveCopyBasePaths();
                break;
            case "it":
                liveCopyBasePaths = config.getItalianLiveCopyBasePaths();
                break;
            case "ja":
                liveCopyBasePaths = config.getJapaneseLiveCopyBasePaths();
                break;
            case "pt":
                liveCopyBasePaths = config.getPortugeseLiveCopyBasePaths();
                break;
            case "sv_se":
                liveCopyBasePaths = config.getSwedishLiveCopyBasePaths();
                break;
            case "tr":
                liveCopyBasePaths = config.getTurkishLiveCopyBasePaths();
                break;
            case "zh":
                liveCopyBasePaths = config.getChineseLiveCopyBasePaths();
                break;
            default:
                liveCopyBasePaths = config.getEnglishLiveCopyBasePaths();
        }
        return liveCopyBasePaths;

    }

    /**
     * Replicate paths.
     *
     * @param resolver
     *            the resolver
     * @param liveCopies
     *            the live copies
     * @param replicator
     *            the replicator
     */
    public static void replicatePaths(ResourceResolver resolver, List<String> liveCopies, Replicator replicator) {
        for (String liveCopyPath : liveCopies) {
            try {
                if (Objects.nonNull(replicator)) {
                    Session session = resolver.adaptTo(Session.class);
                    replicator.replicate(session, ReplicationActionType.ACTIVATE, liveCopyPath);
                    LOGGER.debug("{} ,Replicated", liveCopyPath);
                }
            } catch (ReplicationException e) {
                LOGGER.error("Error occured while rollout :: {}", e.getMessage(), e);
            }
        }

    }
}
