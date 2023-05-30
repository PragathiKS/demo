package com.tetrapak.publicweb.core.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.jcr.RangeIterator;
import javax.jcr.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.engine.SlingRequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.contentsync.handler.util.RequestResponseFactory;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.api.commands.WCMCommand;
import com.day.cq.wcm.msm.api.LiveRelationship;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;
import com.day.cq.wcm.msm.api.RolloutConfig;
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
     * @param rolloutConfigs 
     * @throws WCMException
     *             the WCM exception
     * @throws IOException 
     * @throws ServletException 
     */
    public static void rolloutLiveCopies(final Page blueprintPage, final RequestResponseFactory requestResponseFactory, 
    		final SlingRequestProcessor requestProcessor, final ResourceResolver resolver, final String livecopyPath)
            throws IOException, ServletException {
        LOGGER.debug("inside rolloutLiveCopies method");
        Map<String, Object> params = new HashMap<>();
        params.put(PWConstants.TYPE, PWConstants.PAGE);
        params.put(PWConstants.CMD, PWConstants.CMD_ROLLOUT);
        params.put(WCMCommand.PATH_PARAM, blueprintPage.getPath());
        params.put(PWConstants.CHARSET, PWConstants.UTF_8);
        params.put(PWConstants.RESET, false);
        params.put(PWConstants.MSM_TARGET_PATH, livecopyPath);
        HttpServletRequest req = requestResponseFactory.createRequest(PWConstants.POST, PWConstants.WCM_COMMAND_ENDPOINT, params);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HttpServletResponse response = requestResponseFactory.createResponse(out);
        requestProcessor.processRequest(req, response, resolver);
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
            case "ru":
                liveCopyBasePaths = config.getRussianLiveCopyBasePaths();
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
