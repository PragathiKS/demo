package com.tetrapak.publicweb.core.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.engine.SlingRequestProcessor;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.contentsync.handler.util.RequestResponseFactory;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;
import com.day.cq.wcm.msm.api.RolloutManager;
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
    
    /** The Constant CHAPTER_LEVEL. */
    public static final int CHAPTER_LEVEL = 6;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateLiveCopyServiceImpl.class);

    /** The replicator. */
    @Reference
    private Replicator replicator;
    
    /** Service to create HTTP Servlet requests and responses */
    @Reference
    private RequestResponseFactory requestResponseFactory;

    /** Service to process requests through Sling */
    @Reference
    private SlingRequestProcessor requestProcessor;

    /** The path to replicate. */
    List<String> pathToReplicate;

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
     * @throws RepositoryException 
     * @throws WCMException 
     * @throws ServletException 
     * @throws IOException 
     */
    @Override
    public void createLiveCopy(ResourceResolver resolver, String payload, RolloutManager rolloutManager,
            LiveRelationshipManager liveRelManager, String language, boolean isDeep, boolean flowComingFromLBScheduler) throws WCMException, RepositoryException, IOException, ServletException {
            if (config.enableConfig()) {
                pathToReplicate = new ArrayList<>();
                LOGGER.info("payload : {}", payload);
                String rootPath = LinkUtils.getRootPath(payload);
                LOGGER.debug("rootPath : {}", rootPath);
                String page = payload.replace(rootPath, StringUtils.EMPTY);
                checkAndCreateLiveCopies(resolver, payload, language, page, isDeep, flowComingFromLBScheduler);
                CreateLiveCopyServiceUtil.replicatePaths(resolver, pathToReplicate, replicator);
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
     * @param page
     *            the page
     * @param isDeep
     *            the is deep
     * @param liveRelManager
     *            the live rel manager
     * @return the boolean
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws WCMException
     *             the WCM exception
     * @throws RepositoryException
     * @throws LockException
     * @throws ConstraintViolationException
     * @throws VersionException
     * @throws NoSuchNodeTypeException
     */
    private Boolean checkAndCreateLiveCopies(ResourceResolver resolver, String payload, String language,
            String page, Boolean isDeep, boolean flowComingFromLBScheduler)
            throws WCMException, RepositoryException, IOException, ServletException {
        Boolean isLiveCopyExists = true;
        PageManager pageManager = resolver.adaptTo(PageManager.class);
        final Page blueprintPage = pageManager.getPage(payload);
        for (String path : CreateLiveCopyServiceUtil.getLiveCopyBasePaths(language, config)) {
            String pagePath = path + page;
            String parentPagePath = pagePath.substring(0, pagePath.lastIndexOf("/"));
            LOGGER.debug("pagepath : {}", pagePath);
            if(resolver.getResource(pagePath) == null && resolver.getResource(parentPagePath) == null && flowComingFromLBScheduler) {
            	throw new WCMException("Unable to perform rollout operation for "+pagePath+" as parent page not present.");
            }
            
            if(resolver.getResource(pagePath) == null && resolver.getResource(parentPagePath) == null && !flowComingFromLBScheduler) {
            	LOGGER.info("Unable to perform rollout operation for {} as parent page not present.", pagePath);        
            }

            if (resolver.getResource(pagePath) == null && resolver.getResource(parentPagePath) != null) {
                isLiveCopyExists = false;
                pageManager.copy(blueprintPage, pagePath, getNextPage(blueprintPage), !isDeep, false, true);
                if (resolver.getResource(pagePath + "/jcr:content") != null) {
                    Resource contentRes = resolver.getResource(pagePath + "/jcr:content");
                    Node contentNode = contentRes.adaptTo(Node.class);
                    LOGGER.debug("depth : {}",blueprintPage.getDepth());
                    contentNode.addMixin("cq:LiveRelationship");
                    setRelationShip(contentRes);
                    if (blueprintPage.getDepth() == CHAPTER_LEVEL) {                        
                        contentNode.addMixin("cq:LiveSync");
                        contentNode.addNode("cq:LiveSyncConfig", "cq:LiveCopy");
                        Node syncNode = contentNode.getNode("cq:LiveSyncConfig");
                        syncNode.setProperty("cq:isDeep", true);
                        syncNode.setProperty("cq:master", blueprintPage.getPath());
                        syncNode.setProperty("cq:rolloutConfigs",
                                new String[] { "/apps/msm/wcm/rolloutconfigs/default" });
                    }
                    if(Objects.nonNull(resolver.getResource(blueprintPage.getPath()+"/jcr:content"))) {
                     ModifiableValueMap map = resolver.getResource(blueprintPage.getPath()+"/jcr:content").adaptTo(ModifiableValueMap.class);
                     map.put("cq:lastModified",Calendar.getInstance());
                    }
                    resolver.commit();
                }
            }
            CreateLiveCopyServiceUtil.rolloutLiveCopies(blueprintPage, requestResponseFactory, requestProcessor, resolver, pagePath);
            pathToReplicate.add(pagePath);
        }
        return isLiveCopyExists;
    }
    
    public void setRelationShip(Resource rootRes) throws RepositoryException {
        if (rootRes == null || !rootRes.hasChildren()) {
            return;
        }
        Iterator<Resource> itr = rootRes.listChildren();
        while (itr.hasNext()) {
            Resource res = itr.next();
            Node contentNode = res.adaptTo(Node.class);
            contentNode.addMixin("cq:LiveRelationship");
            setRelationShip(res);
        }
    }

    /**
     * Gets the next page.
     *
     * @param blueprintPage
     *            the blueprint page
     * @return the next page
     */
    private String getNextPage(Page blueprintPage) {
        Iterator<Page> pages = blueprintPage.getParent().listChildren();
        int count = 0;
        String nextPageName = StringUtils.EMPTY;
        while (pages.hasNext()) {
            Page page = pages.next();
            if (count == 1) {
                nextPageName = page.getName();
                break;
            }

            if (page.getName().equals(blueprintPage.getName())) {
                count++;
            }
        }
        return nextPageName;
    }
}