package com.tetrapak.customerhub.core.models;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;

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
    
    
    List<String> componentsReference = new LinkedList<String>();
    private String locale;
    private final static String PAGE_REFERENCE_RESOURCE_TYPE = "customerhub/components/content/pagereference";
    private final static String SLING_RESOURCE_TYPE = "sling:resourceType";
    private final static String TEMPLATE = "cq:template";
    private static final Logger LOGGER = LoggerFactory.getLogger(PageContentHierarchyReferencesModel.class.getName());
    /**
     * 
     */
	@PostConstruct
    protected void init() {
        if(Objects.nonNull(pageContentPath)) {
        	locale = StringUtils.isNotBlank(locale) ? locale : "en";
        	String pagePath = new String(pageContentPath);
        	pagePath = pagePath.replace("/en", "/" + locale);
        	if(!pagePath.endsWith("/jcr:content")) {
        		pagePath = pagePath+"/jcr:content/root/responsivegrid";
        		pageReferenceComponents(pagePath);
        	}
        	else if(pagePath.endsWith("/jcr:content")){
        		pageReferenceComponents(pagePath+"/root/responsivegrid");
        	}
        	if(Objects.nonNull(includeSubPages) && includeSubPages.equalsIgnoreCase("true")) {
        		 pageContentPath = pageContentPath.replace("/jcr:content", "");
        		 clonePageContentHierachy(pageContentPath, locale);
        	}
        	
        }
    }

	private void clonePageContentHierachy(String pageContentHierachy, String locale) {
		Session session = null;
	try {
		session = resourceResolver.adaptTo(Session.class);
		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
		Page currentPage = pageManager.getContainingPage(resource);
		Page srcParentPage = pageManager.getPage(pageContentHierachy);
		createPageContentHierachy(resourceResolver, pageManager, currentPage, srcParentPage, locale);
		session.save();
	} catch (PathNotFoundException e) {
		LOGGER.error("PathNotFoundException in PageContentHierarchyReferencesModel", e);
	} catch (ItemExistsException e) {
		LOGGER.error("ItemExistsException in PageContentHierarchyReferencesModel", e);
	} catch (VersionException e) {
		LOGGER.error("VersionException in PageContentHierarchyReferencesModel", e);
	} catch (ConstraintViolationException e) {
		LOGGER.error("ConstraintViolationException in PageContentHierarchyReferencesModel", e);
	} catch (LockException e) {
		LOGGER.error("LockException in PageContentHierarchyReferencesModel", e);
	} catch (ValueFormatException e) {
		LOGGER.error("ValueFormatException in PageContentHierarchyReferencesModel", e);
	} catch (WCMException e) {
		LOGGER.error("WCMException in PageContentHierarchyReferencesModel", e);
	} catch (RepositoryException e) {
		LOGGER.error("RepositoryException in PageContentHierarchyReferencesModel", e);
	}
 }

	private void createPageContentHierachy(ResourceResolver resourceResolver, PageManager pageManager, Page currentPage,
			Page srcParentPage, String locale)
			throws ValueFormatException, VersionException, LockException, ConstraintViolationException,
			RepositoryException, WCMException, PathNotFoundException, ItemExistsException {
		Iterator<Page> sourceChildrenPageIterator = srcParentPage.listChildren();
		while(sourceChildrenPageIterator.hasNext()) {
			Page srcChildrenPage = sourceChildrenPageIterator.next();
			Node sourceJcrContent = srcChildrenPage.getContentResource().adaptTo(Node.class);
				Page destChildrenPage = null;
				String currentPagepath = currentPage.getPath();
				currentPagepath = currentPagepath.replace("/en", "/" + locale);
				
				if(isPageExist(resourceResolver,currentPagepath+"/"+srcChildrenPage.getName())) {
					destChildrenPage = pageManager.getPage(currentPagepath+"/"+srcChildrenPage.getName());
					Node destJcrContent = destChildrenPage.getContentResource().adaptTo(Node.class);
					destJcrContent.setProperty(JcrConstants.JCR_TITLE, srcChildrenPage.getTitle());
				}
				else {
					destChildrenPage = pageManager.create(currentPagepath, srcChildrenPage.getName(), srcChildrenPage.getProperties().get(TEMPLATE, String.class), srcChildrenPage.getTitle());
				}
				
				Node childrenJcrContent = destChildrenPage.getContentResource().adaptTo(Node.class);
				Node srcRoot = sourceJcrContent.getNode("root");
				Node destRootNode = null;
				if(childrenJcrContent.hasNode(srcRoot.getName())) {
					destRootNode  = childrenJcrContent.getNode(srcRoot.getName());
				}else {
					destRootNode  = childrenJcrContent.addNode(srcRoot.getName());
				}
				
				destRootNode.setProperty(SLING_RESOURCE_TYPE, srcRoot.getProperty(SLING_RESOURCE_TYPE).getValue().getString());
				Node srcResponsiveGrid = srcRoot.getNode("responsivegrid");
				Node destResponsiveGrid = null;
				
				if(destRootNode.hasNode(srcResponsiveGrid.getName())) {
					destResponsiveGrid = destRootNode.getNode(srcResponsiveGrid.getName());
				}
				else {
					destResponsiveGrid = destRootNode.addNode(srcResponsiveGrid.getName());
				}
				
				destResponsiveGrid.setProperty(SLING_RESOURCE_TYPE, srcResponsiveGrid.getProperty(SLING_RESOURCE_TYPE).getValue().getString());
				Node destPageReference = null;
				if(destResponsiveGrid.hasNode("pagereference")) {
					destPageReference = destResponsiveGrid.getNode("pagereference");
				}else {
					destPageReference = destResponsiveGrid.addNode("pagereference");
				}
				
				destPageReference.setProperty(SLING_RESOURCE_TYPE, PAGE_REFERENCE_RESOURCE_TYPE);
				String srcChildrenPath = srcChildrenPage.getPath();
				srcChildrenPath = srcChildrenPath.replace("/en", "/" + locale);
				destPageReference.setProperty("pageContentPath", srcChildrenPath);
				createPageContentHierachy(resourceResolver, pageManager, destChildrenPage, srcChildrenPage, locale);
		}
	}
	
	private boolean isPageExist(ResourceResolver resourceResolver, String pagePath) {
		Resource resource = resourceResolver.getResource(pagePath);
		if(Objects.nonNull(resource)) {
			return true;
		}
		return false;
	}
	
    private void pageReferenceComponents(String path) {
    	Resource componentResources =	resourceResolver.getResource(path);
    	if(Objects.nonNull(componentResources)) {
    		Iterator<Resource> iterators = componentResources.listChildren();
        	while(iterators.hasNext()) {
        		componentsReference.add(iterators.next().getPath());
        		}
    		}
    	}
    
	public List<String> getComponentsReference() {
		return componentsReference;
	}

	public String getPageContentPath() {
		return pageContentPath;
	}

}
