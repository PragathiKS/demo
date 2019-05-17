package com.tetrapak.customerhub.core.models;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
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
 *
 * @author Nitin Kumar
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageContentHierarchyReferencesModel {

    @Self
    private Resource resource;

    @ValueMapValue
    private String pageContentPath;

    @ValueMapValue
    private String includeSubPages;

    @SlingObject
    private ResourceResolver resourceResolver;


    private List<String> componentsReference = new LinkedList<>();
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
            GlobalUtil.setPageReferences(resourceResolver, componentsReference, locale, pageContentPath);
            if (Objects.nonNull(includeSubPages) && ("true").equalsIgnoreCase(includeSubPages)) {
                pageContentPath = pageContentPath.replace(CustomerHubConstants.PATH_SEPARATOR
                        + JcrConstants.JCR_CONTENT, CustomerHubConstants.EMPTY_STRING);
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
                createPageContentHierarchy(resourceResolver, pageManager, currentPage, srcParentPage, locale);
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

    private void createPageContentHierarchy(ResourceResolver resourceResolver, PageManager pageManager, Page currentPage,
                                            Page srcParentPage, String locale)
            throws RepositoryException, WCMException {
        String currentPagePath = currentPage.getPath();
        if (Objects.nonNull(currentPagePath)) {
            currentPagePath = currentPagePath.replace("/en", CustomerHubConstants.PATH_SEPARATOR + locale);
        }
        Page srcChildrenPage;
        Iterator<Page> sourceChildrenPageIterator = srcParentPage.listChildren();
        while (sourceChildrenPageIterator.hasNext()) {
            srcChildrenPage = sourceChildrenPageIterator.next();
            createHierarchy(resourceResolver, pageManager, locale, currentPagePath, srcChildrenPage);
        }
    }

    private void createHierarchy(ResourceResolver resourceResolver, PageManager pageManager, String locale,
                                 String currentpagepath, Page srcChildrenPage) throws RepositoryException, WCMException {
        Node sourceJcrContent;
        sourceJcrContent = srcChildrenPage.getContentResource().adaptTo(Node.class);
        Page destinationChildrenPage;
        destinationChildrenPage = getOrCreatePage(resourceResolver, pageManager, currentpagepath, srcChildrenPage);
        Node childrenJcrContent = null;
        if (Objects.nonNull(destinationChildrenPage)) {
            childrenJcrContent = destinationChildrenPage.getContentResource().adaptTo(Node.class);
        }
        Node srcRoot = null;
        if (Objects.nonNull(sourceJcrContent)) {
            srcRoot = sourceJcrContent.getNode(CustomerHubConstants.ROOT_NODE);
        }
        Node destinationRootNode = null;
        Node srcResponsiveGrid = null;
        Node destinationResponsiveGrid = null;
        if (Objects.nonNull(srcRoot)) {
            if (Objects.nonNull(childrenJcrContent)) {
                destinationRootNode = addNode(childrenJcrContent, srcRoot.getName());
            }
            if (Objects.nonNull(destinationRootNode)) {
                destinationRootNode.setProperty(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY,
                        srcRoot.getProperty(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY).getValue().getString());
                srcResponsiveGrid = srcRoot.getNode(CustomerHubConstants.RESPONSIVE_GRID_NODE);
                if (Objects.nonNull(srcResponsiveGrid)) {
                    destinationResponsiveGrid = addNode(destinationRootNode, srcResponsiveGrid.getName());
                }
            }
        }
        Node destinationPageReference = null;
        if (Objects.nonNull(destinationResponsiveGrid)) {
            destinationResponsiveGrid.setProperty(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY,
                    srcResponsiveGrid.getProperty(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY).getValue().getString());
            destinationPageReference = addNode(destinationResponsiveGrid, PAGE_REFERENCE_NODE);
        }
        if (Objects.nonNull(destinationPageReference)) {
            destinationPageReference.setProperty(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY, PAGE_REFERENCE_RESOURCE_TYPE);
            String srcChildrenPath = srcChildrenPage.getPath();
            srcChildrenPath = srcChildrenPath.replace("/en", CustomerHubConstants.PATH_SEPARATOR + locale);
            destinationPageReference.setProperty(PATH_REFERENCE_NAME, srcChildrenPath);
        }
        createPageContentHierarchy(resourceResolver, pageManager, destinationChildrenPage, srcChildrenPage, locale);
    }

    private Page getOrCreatePage(ResourceResolver resourceResolver, PageManager pageManager, String currentPagepath,
                                 Page srcChildrenPage) throws RepositoryException, WCMException {

        Page destinationChildrenPage = null;
        if (isPageExist(resourceResolver, currentPagepath + CustomerHubConstants.PATH_SEPARATOR + srcChildrenPage.getName())) {
            destinationChildrenPage = pageManager.getPage(
                    currentPagepath + CustomerHubConstants.PATH_SEPARATOR + srcChildrenPage.getName());
            Node destinationJcrContent = destinationChildrenPage.getContentResource().adaptTo(Node.class);
            if (Objects.nonNull(destinationJcrContent)) {
                destinationJcrContent.setProperty(JcrConstants.JCR_TITLE, srcChildrenPage.getTitle());
            }
        } else {
            destinationChildrenPage = pageManager.create(currentPagepath, srcChildrenPage.getName(),
                    srcChildrenPage.getProperties().get(CustomerHubConstants.CQ_TEMPLATE, String.class), srcChildrenPage.getTitle());
        }
        return destinationChildrenPage;
    }

    private boolean isPageExist(ResourceResolver resourceResolver, String pagePath) {
        boolean pageExit = false;
        Resource pageResource = resourceResolver.getResource(pagePath);
        if (Objects.nonNull(pageResource)) {
            pageExit = true;
        }
        return pageExit;
    }

    private Node addNode(Node node, String nodeName) throws RepositoryException {
        Node childNode;
        if (node.hasNode(nodeName)) {
            childNode = node.getNode(nodeName);
        } else {
            childNode = node.addNode(nodeName);
        }
        return childNode;
    }

    public List<String> getComponentsReference() {
        return new LinkedList<>(componentsReference);
    }

    public String getPageContentPath() {
        return pageContentPath;
    }

    public String getIncludeSubPages() {
        return includeSubPages;
    }

}
