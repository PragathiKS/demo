package com.tetrapak.publicweb.core.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.jcr.RangeIterator;
import javax.jcr.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.engine.SlingRequestProcessor;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.contentsync.handler.util.RequestResponseFactory;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.api.commands.WCMCommand;
import com.day.cq.wcm.msm.api.LiveRelationship;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;
import com.day.cq.wcm.msm.api.MSMNameConstants;
import com.day.cq.wcm.msm.api.RolloutManager;
import com.day.cq.wcm.msm.api.RolloutManager.RolloutParams;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.CreateLiveCopyService;
import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * The Class CreateLiveCopyServiceImpl.
 */
@Component(immediate = true, service = CreateLiveCopyService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = CreateLiveCopyServiceImpl.CreateLiveCopyServiceConfig.class)
public class CreateLiveCopyServiceImpl implements CreateLiveCopyService {

    /** The config. */
    private CreateLiveCopyServiceConfig config;

    /**
     * The Interface CreateLiveCopyServiceConfig.
     */
    @ObjectClassDefinition(
            name = "Public Web Create Live Copy Config",
            description = "Public Web Create Live Copy Config")
    @interface CreateLiveCopyServiceConfig {

        /**
         * Gets the english live copy base paths.
         *
         * @return the english live copy base paths
         */
        @AttributeDefinition(name = "English Live Copy Base Paths", description = "English Live Copy Base Paths")
        String[] getEnglishLiveCopyBasePaths();

        /**
         * Gets the french live copy base paths.
         *
         * @return the french live copy base paths
         */
        @AttributeDefinition(name = "French Live Copy Base Paths", description = "French Live Copy Base Paths")
        String[] getFrenchLiveCopyBasePaths();

        /**
         * Gets the chinese live copy base paths.
         *
         * @return the chinese live copy base paths
         */
        @AttributeDefinition(name = "Chinese Live Copy Base Paths", description = "Chinese Live Copy Base Paths")
        String[] getChineseLiveCopyBasePaths();

        /**
         * Gets the german live copy base paths.
         *
         * @return the german live copy base paths
         */
        @AttributeDefinition(name = "German Live Copy Base Paths", description = "German Live Copy Base Paths")
        String[] getGermanLiveCopyBasePaths();

        /**
         * Gets the italian live copy base paths.
         *
         * @return the italian live copy base paths
         */
        @AttributeDefinition(name = "Italian Live Copy Base Paths", description = "Italian Live Copy Base Paths")
        String[] getItalianLiveCopyBasePaths();

        /**
         * Gets the japanese live copy base paths.
         *
         * @return the japanese live copy base paths
         */
        @AttributeDefinition(name = "Japanese Live Copy Base Paths", description = "Japanese Live Copy Base Paths")
        String[] getJapaneseLiveCopyBasePaths();

        /**
         * Gets the portugese live copy base paths.
         *
         * @return the portugese live copy base paths
         */
        @AttributeDefinition(name = "Portugese Live Copy Base Paths", description = "Portugese Live Copy Base Paths")
        String[] getPortugeseLiveCopyBasePaths();

        /**
         * Gets the russian live copy base paths.
         *
         * @return the russian live copy base paths
         */
        @AttributeDefinition(name = "Russian Live Copy Base Paths", description = "Russian Live Copy Base Paths")
        String[] getRussianLiveCopyBasePaths();

        /**
         * Gets the spanish live copy base paths.
         *
         * @return the spanish live copy base paths
         */
        @AttributeDefinition(name = "Spanish Live Copy Base Paths", description = "Spanish Live Copy Base Paths")
        String[] getSpanishLiveCopyBasePaths();

        /**
         * Gets the sweedish live copy base paths.
         *
         * @return the sweedish live copy base paths
         */
        @AttributeDefinition(name = "Swedish Live Copy Base Paths", description = "Swedish Live Copy Base Paths")
        String[] getSwedishLiveCopyBasePaths();

        /**
         * Gets the turkish live copy base paths.
         *
         * @return the turkish live copy base paths
         */
        @AttributeDefinition(name = "Turkish Live Copy Base Paths", description = "Turkish Live Copy Base Paths")
        String[] getTurkishLiveCopyBasePaths();

        /**
         * Gets the rollout configs.
         *
         * @return the rollout configs
         */
        @AttributeDefinition(name = "Rollout Configs", description = "Rollout Configs")
        String[] getRolloutConfigs();

    }

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateLiveCopyServiceImpl.class);

    /** The replicator. */
    @Reference
    private Replicator replicator;

    /** The Constant CMD_LIVE_COPY. */
    private static final String CMD_LIVE_COPY = "createLiveCopy";

    /** The Constant CMD. */
    private static final String CMD = "cmd";

    /** The Constant WCM_COMMAND_ENDPOINT. */
    private static final String WCM_COMMAND_ENDPOINT = "/bin/wcmcommand";

    /** The Constant CHARSET. */
    private static final String CHARSET = "_charset_";

    /** The request response factory. */
    @Reference
    private RequestResponseFactory requestResponseFactory;

    /** The request processor. */
    @Reference
    private SlingRequestProcessor requestProcessor;

    /**
     * activate method.
     *
     * @param config
     *            site Improve Script URL configuration
     */
    @Activate
    public void activate(final CreateLiveCopyServiceConfig config) {
        this.config = config;
    }

    /**
     * This method accepts the required parameters to create a live copy and calls the WCM Command Servlet to create
     * live copy.
     *
     * @param resolver
     *            the resolver
     * @param payload
     *            the payload
     * @param rolloutManager
     *            the rollout manager
     * @param liveRelManager
     *            the live rel manager
     * @param language
     *            the language
     */
    @Override
    public void createLiveCopy(ResourceResolver resolver, String payload, RolloutManager rolloutManager,
            LiveRelationshipManager liveRelManager, String language) {
        try {
            PageManager pageManager = resolver.adaptTo(PageManager.class);
            final Page blueprintPage = pageManager.getPage(payload);
            Resource res = resolver.getResource(payload);
            List<String> liveCopyList = getLiveCopies(liveRelManager, res);
            String rootPath = LinkUtils.getRootPath(payload);
            LOGGER.debug("rootPath : {}", rootPath);
            String page = payload.replace(rootPath, StringUtils.EMPTY);
            for (String path : getLiveCopyBasePaths(language)) {
                String pagePath = path + page;
                LOGGER.debug("pagepath : {}", pagePath);
                if (!liveCopyList.contains(pagePath)) {
                    createLiveCopies(resolver, payload, pagePath, res);
                }
            }
            rolloutLiveCopies(rolloutManager, blueprintPage);
            liveCopyList = getLiveCopies(liveRelManager, res);
            replicatePaths(resolver, liveCopyList);
        } catch (ServletException | IOException | WCMException e) {
            LOGGER.error("An error occurred while creating live copy", e);
        }
    }

    /**
     * Gets the live copy base paths.
     *
     * @param language
     *            the language
     * @return the live copy base paths
     */
    private String[] getLiveCopyBasePaths(String language) {
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
     */
    private void replicatePaths(ResourceResolver resolver, List<String> liveCopies) {
        for (String liveCopyPath : liveCopies) {
            try {
                if (Objects.nonNull(replicator)) {
                    replicator.replicate(resolver.adaptTo(Session.class), ReplicationActionType.ACTIVATE, liveCopyPath);
                    LOGGER.debug("{} ,Replicated", liveCopyPath);
                }
            } catch (ReplicationException e) {
                LOGGER.error("Error occured while rollout :: {}", e.getMessage(), e);
            }
        }

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
    private List<String> getLiveCopies(LiveRelationshipManager liveRelManager, Resource res) throws WCMException {
        RangeIterator rangeIterator = liveRelManager.getLiveRelationships(res, "", null);
        List<String> liveCopyList = new ArrayList<>();
        while (Objects.nonNull(rangeIterator) && rangeIterator.hasNext()) {
            LiveRelationship liveCopy = (LiveRelationship) rangeIterator.next();
            String liveCopyPath = liveCopy.getLiveCopy().getPath();
            LOGGER.debug("LiveCopy path: {}", liveCopyPath);
            liveCopyList.add(liveCopyPath);
        }
        return liveCopyList;
    }

    /**
     * Rollout live copies.
     *
     * @param rolloutManager
     *            the rollout manager
     * @param blueprintPage
     *            the blueprint page
     * @throws WCMException
     *             the WCM exception
     */
    private void rolloutLiveCopies(RolloutManager rolloutManager, final Page blueprintPage) throws WCMException {
        LOGGER.debug("inside rolloutLiveCopies method");
        final RolloutParams rolloutParams = new RolloutParams();
        rolloutParams.isDeep = false;
        rolloutParams.master = blueprintPage;
        rolloutParams.reset = false;
        rolloutParams.trigger = RolloutManager.Trigger.ROLLOUT;
        rolloutManager.rollout(rolloutParams);
    }

    /**
     * Creates the live copies.
     *
     * @param resolver
     *            the resolver
     * @param payload
     *            the payload
     * @param pagePath
     *            the page path
     * @param res
     *            the res
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void createLiveCopies(ResourceResolver resolver, String payload, String pagePath, Resource res)
            throws ServletException, IOException {
        final String rootPath = StringUtils.substringBeforeLast(pagePath, PWConstants.SLASH);
        ValueMap vMap = res.getValueMap();
        Map<String, Object> params = new HashMap<>();
        LOGGER.debug("payload path : {}", payload);
        LOGGER.debug("Root Path : {}", rootPath);
        params.put(CHARSET, StandardCharsets.UTF_8);
        params.put(CMD, CMD_LIVE_COPY);
        params.put(WCMCommand.SRC_PATH_PARAM, payload);
        params.put(WCMCommand.DEST_PATH_PARAM, rootPath);
        params.put(WCMCommand.PAGE_TITLE_PARAM, vMap.get(JcrConstants.JCR_TITLE, StringUtils.EMPTY));
        params.put(WCMCommand.PAGE_LABEL_PARAM, vMap.get(JcrConstants.JCR_NAME, StringUtils.EMPTY));
        params.put(MSMNameConstants.PN_ROLLOUT_CONFIGS, config.getRolloutConfigs());
        HttpServletRequest req = requestResponseFactory.createRequest("POST", WCM_COMMAND_ENDPOINT, params);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HttpServletResponse response = requestResponseFactory.createResponse(out);
        requestProcessor.processRequest(req, response, resolver);
    }
}
