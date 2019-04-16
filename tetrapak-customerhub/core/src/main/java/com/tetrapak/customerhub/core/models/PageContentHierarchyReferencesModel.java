package com.tetrapak.customerhub.core.models;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Model class for page content hierarchy reference
 */

/**
 * @author Tetrapak-customerhub
 *
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageContentHierarchyReferencesModel {

    @Self
    Resource resource;

    @ValueMapValue
    private String pageContentPath;

    @ValueMapValue
    private String includeSubPages;

    @SlingObject
    private ResourceResolver resourceResolver;


    List<String> componentsReference = new LinkedList<>();
    private String locale;
    private static final String PAGE_REFERENCE_RESOURCE_TYPE = "customerhub/components/content/pagereference";
    private static final String PAGE_REFERENCE_NODE = "pagereference";
    private static final String PATH_REFERENCE_NAME = "pageContentPath";
    private static final Logger LOGGER = LoggerFactory.getLogger(PageContentHierarchyReferencesModel.class);

    /**
     *
     */
    @PostConstruct
    protected void init() {
        if (Objects.nonNull(pageContentPath)) {
            locale = StringUtils.isNotBlank(locale) ? locale : "en";
            String pagePath = String.valueOf(pageContentPath);
            pagePath = pagePath.replace("/en", CustomerHubConstants.PATH_SEPARATOR + locale);
            String resGridPathWithoutJcrContent = CustomerHubConstants.PATH_SEPARATOR + CustomerHubConstants.ROOT_NODE + CustomerHubConstants.PATH_SEPARATOR +
                    CustomerHubConstants.RESPONSIVE_GRID_NODE;
            String resGridPath = pagePath.endsWith(JcrConstants.JCR_CONTENT) ? resGridPathWithoutJcrContent : CustomerHubConstants.PATH_SEPARATOR + JcrConstants.JCR_CONTENT + resGridPathWithoutJcrContent;
            pagePath = pagePath + resGridPath;
            pageReferenceComponents(pagePath);
            if (Objects.nonNull(includeSubPages) && includeSubPages.equalsIgnoreCase("true")) {
                pageContentPath = pageContentPath.replace(CustomerHubConstants.PATH_SEPARATOR + JcrConstants.JCR_CONTENT, CustomerHubConstants.EMPTY_STRING);
                clonePageContentHierachy(pageContentPath, locale);
            }
        }
    }

    private void clonePageContentHierachy(String pageContentHierachy, String locale) {
        Session session = null;
        try {
            session = resourceResolver.adaptTo(Session.class);
            PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
            if (Objects.nonNull(pageManager)) {
                Page currentPage = pageManager.getContainingPage(resource);
                Page srcParentPage = pageManager.getPage(pageContentHierachy);
                createPageContentHierachy(resourceResolver, pageManager, currentPage, srcParentPage, locale);
            }
            if (Objects.nonNull(session)) {
                session.save();
            }
        } catch (RepositoryException e) {
            LOGGER.error("RepositoryException in PageContentHierarchyReferencesModel", e);
        } catch (WCMException e) {
            LOGGER.error("WCMException in PageContentHierarchyReferencesModel", e);
        }
    }

    private void createPageContentHierachy(ResourceResolver resourceResolver, PageManager pageManager, Page currentPage,
                                           Page srcParentPage, String locale)
            throws RepositoryException, WCMException {
        String currentPagepath = currentPage.getPath();
        if (Objects.nonNull(currentPagepath)) {
            currentPagepath = currentPagepath.replace("/en", CustomerHubConstants.PATH_SEPARATOR + locale);
        }
        Page srcChildrenPage = null;
        Node sourceJcrContent = null;
        Iterator<Page> sourceChildrenPageIterator = srcParentPage.listChildren();
        while (sourceChildrenPageIterator.hasNext()) {
            srcChildrenPage = sourceChildrenPageIterator.next();
            sourceJcrContent = srcChildrenPage.getContentResource().adaptTo(Node.class);
            Page destChildrenPage = null;
            destChildrenPage = getOrCreatePage(resourceResolver, pageManager, currentPagepath, srcChildrenPage);
            Node childrenJcrContent = null;
            if (Objects.nonNull(destChildrenPage)) {
                childrenJcrContent = destChildrenPage.getContentResource().adaptTo(Node.class);
            }
            Node srcRoot = null;
            if (Objects.nonNull(sourceJcrContent)) {
                srcRoot = sourceJcrContent.getNode(CustomerHubConstants.ROOT_NODE);
            }
            Node destRootNode = null;
            Node srcResponsiveGrid = null;
            Node destResponsiveGrid = null;
            if (Objects.nonNull(srcRoot)) {
                if (Objects.nonNull(childrenJcrContent)) {
                    destRootNode = addNode(childrenJcrContent, srcRoot.getName());
                }
                if (Objects.nonNull(destRootNode)) {
                    destRootNode.setProperty(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY, srcRoot.getProperty(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY).getValue().getString());
                    srcResponsiveGrid = srcRoot.getNode(CustomerHubConstants.RESPONSIVE_GRID_NODE);
                    if (Objects.nonNull(srcResponsiveGrid)) {
                        destResponsiveGrid = addNode(destRootNode, srcResponsiveGrid.getName());
                    }
                }
            }
            Node destPageReference = null;
            if (Objects.nonNull(destResponsiveGrid)) {
                destResponsiveGrid.setProperty(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY, srcResponsiveGrid.getProperty(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY).getValue().getString());
                destPageReference = addNode(destResponsiveGrid, PAGE_REFERENCE_NODE);
            }
            if (Objects.nonNull(destPageReference)) {
                destPageReference.setProperty(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY, PAGE_REFERENCE_RESOURCE_TYPE);
                String srcChildrenPath = srcChildrenPage.getPath();
                srcChildrenPath = srcChildrenPath.replace("/en", CustomerHubConstants.PATH_SEPARATOR + locale);
                destPageReference.setProperty(PATH_REFERENCE_NAME, srcChildrenPath);
            }
            createPageContentHierachy(resourceResolver, pageManager, destChildrenPage, srcChildrenPage, locale);
        }
    }

    private Page getOrCreatePage(ResourceResolver resourceResolver, PageManager pageManager, String currentPagepath,
                                 Page srcChildrenPage) throws RepositoryException, WCMException {

        Page destChildrenPage = null;
        if (isPageExist(resourceResolver, currentPagepath + CustomerHubConstants.PATH_SEPARATOR + srcChildrenPage.getName())) {
            destChildrenPage = pageManager.getPage(currentPagepath + CustomerHubConstants.PATH_SEPARATOR + srcChildrenPage.getName());
            Node destJcrContent = destChildrenPage.getContentResource().adaptTo(Node.class);
            if (Objects.nonNull(destJcrContent)) {
                destJcrContent.setProperty(JcrConstants.JCR_TITLE, srcChildrenPage.getTitle());
            }
        } else {
            destChildrenPage = pageManager.create(currentPagepath, srcChildrenPage.getName(), srcChildrenPage.getProperties().get(CustomerHubConstants.CQ_TEMPLATE, String.class), srcChildrenPage.getTitle());
        }
        return destChildrenPage;
    }

    private boolean isPageExist(ResourceResolver resourceResolver, String pagePath) {
        boolean pageExit = false;
        Resource pageResource = resourceResolver.getResource(pagePath);
        if (Objects.nonNull(pageResource)) {
            pageExit = true;
        }
        return pageExit;
    }

    private void pageReferenceComponents(String path) {
        Resource componentResources = resourceResolver.getResource(path);
        if (Objects.nonNull(componentResources)) {
            Iterator<Resource> iterators = componentResources.listChildren();
            while (iterators.hasNext()) {
                componentsReference.add(iterators.next().getPath());
            }
        }
    }

    private Node addNode(Node node, String nodeName) throws RepositoryException {
        Node childNode = null;
        if (node.hasNode(nodeName)) {
            childNode = node.getNode(nodeName);
        } else {
            childNode = node.addNode(nodeName);
        }
        return childNode;
    }

    public List<String> getComponentsReference() {
        return componentsReference;
    }

    public String getPageContentPath() {
        return pageContentPath;
    }

    public String getIncludeSubPages() {
        return includeSubPages;
    }

}
