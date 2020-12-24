package com.tetrapak.publicweb.core.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;
import com.day.cq.wcm.msm.api.RolloutManager;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.CreateLiveCopyService;
import com.tetrapak.publicweb.core.services.config.CreateLiveCopyServiceConfig;
import com.tetrapak.publicweb.core.utils.CreateLiveCopyServiceUtil;
import com.tetrapak.publicweb.core.utils.LinkUtils;

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

    List<String> pathToReplicate = new ArrayList<>();

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
            if (config.enableConfig()) {
                LOGGER.info("payload : {}", payload);
                PageManager pageManager = resolver.adaptTo(PageManager.class);
                final Page blueprintPage = pageManager.getPage(payload);
                Resource res = resolver.getResource(payload);
                String rootPath = LinkUtils.getRootPath(payload);
                LOGGER.debug("rootPath : {}", rootPath);
                String page = payload.replace(rootPath, StringUtils.EMPTY);
                checkAndCreateLiveCopies(resolver, payload, language, res, page, isDeep, liveRelManager);
                CreateLiveCopyServiceUtil.rolloutLiveCopies(rolloutManager, blueprintPage, isDeep);
                CreateLiveCopyServiceUtil.replicatePaths(resolver, pathToReplicate, replicator);
            }
        } catch (ServletException | IOException | WCMException e) {
            LOGGER.error("An error occurred while creating live copy", e);
        }
    }

    /**
     * Check and create live copies.
     *
     * @param resolver
     *            the resolver
     * @param payload
     *            the payload
     * @param language
     *            the language
     * @param res
     *            the res
     * @param liveCopyList
     *            the live copy list
     * @param page
     *            the page
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws WCMException
     */
    private void checkAndCreateLiveCopies(ResourceResolver resolver, String payload, String language, Resource res,
            String page, Boolean isDeep, LiveRelationshipManager liveRelManager)
            throws ServletException, IOException, WCMException {
        PageManager pageManager = resolver.adaptTo(PageManager.class);
        final Page blueprintPage = pageManager.getPage(payload);
        for (String path : CreateLiveCopyServiceUtil.getLiveCopyBasePaths(language, config)) {
            String pagePath = path + page;
            LOGGER.debug("pagepath : {}", pagePath);
            if (resolver.getResource(pagePath) == null) {
                pageManager.copy(blueprintPage, pagePath, null, !isDeep, false, true);
                Page targetPage = pageManager.getPage(pagePath);
                if (blueprintPage.getDepth() == PWConstants.CHAPTER_LEVEL) {
                    liveRelManager.establishRelationship(blueprintPage, targetPage, true, true,
                            CreateLiveCopyServiceUtil.getRollOutConfigs(liveRelManager, res));
                }
            }
            pathToReplicate.add(pagePath);
        }
    }
}
