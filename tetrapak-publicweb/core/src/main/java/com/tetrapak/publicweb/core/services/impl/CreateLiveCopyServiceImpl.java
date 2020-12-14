package com.tetrapak.publicweb.core.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.contentsync.handler.util.RequestResponseFactory;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.api.commands.WCMCommand;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;
import com.day.cq.wcm.msm.api.MSMNameConstants;
import com.day.cq.wcm.msm.api.RolloutManager;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.CreateLiveCopyService;
import com.tetrapak.publicweb.core.services.config.CreateLiveCopyServiceConfig;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import com.tetrapak.publicweb.core.utils.CreateLiveCopyServiceUtil;

/**
 * The Class CreateLiveCopyServiceImpl.
 */
@Component(immediate = true, service = CreateLiveCopyService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = CreateLiveCopyServiceConfig.class)
public class CreateLiveCopyServiceImpl implements CreateLiveCopyService {

    /** The config. */
    private CreateLiveCopyServiceConfig config;

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
     * @param isDeep
     *            the is deep
     */
    @Override
    public void createLiveCopy(ResourceResolver resolver, String payload, RolloutManager rolloutManager,
            LiveRelationshipManager liveRelManager, String language, boolean isDeep) {
        try {
            LOGGER.info("payload : {}", payload);
            PageManager pageManager = resolver.adaptTo(PageManager.class);
            final Page blueprintPage = pageManager.getPage(payload);
            Resource res = resolver.getResource(payload);
            List<String> liveCopyList = CreateLiveCopyServiceUtil.getLiveCopies(liveRelManager, res);
            String rootPath = LinkUtils.getRootPath(payload);
            LOGGER.debug("rootPath : {}", rootPath);
            String page = payload.replace(rootPath, StringUtils.EMPTY);
            for (String path : CreateLiveCopyServiceUtil.getLiveCopyBasePaths(language, config)) {
                String pagePath = path + page;
                LOGGER.debug("pagepath : {}", pagePath);
                if (!liveCopyList.contains(pagePath)) {
                    createLiveCopies(resolver, payload, pagePath, res);
                }
            }
            CreateLiveCopyServiceUtil.rolloutLiveCopies(rolloutManager, blueprintPage, isDeep);
            liveCopyList = CreateLiveCopyServiceUtil.getLiveCopies(liveRelManager, res);
            CreateLiveCopyServiceUtil.replicatePaths(resolver, liveCopyList, replicator);
        } catch (ServletException | IOException | WCMException e) {
            LOGGER.error("An error occurred while creating live copy", e);
        }
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
