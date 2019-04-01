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
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;

/**
 * Model class for page content hierarchy reference 
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageContentHierarchyReferencesModel {

	@Self
	Resource resource;
	
    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    private String pageContentPath;
    
    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    private String includeSubPages;
    
    List<String> componentsReference = new LinkedList<String>();
    private String locale;
    
	@PostConstruct
    protected void init() {
        if(Objects.nonNull(pageContentPath)) {
        	ResourceResolver resourceResolver = resource.getResourceResolver();
        	locale = StringUtils.isNotBlank(locale) ? locale : "en";
            pageContentPath = pageContentPath.replace("/en", "/" + locale);
            String pageContentHierachy = new String(pageContentPath);
        	if(!pageContentPath.endsWith("/jcr:content")) {
        	pageContentPath= pageContentPath+"/jcr:content/root/responsivegrid";
        	pageReferenceComponents(pageContentPath);
        	}
        	else if(pageContentPath.endsWith("/jcr:content")){
        		pageReferenceComponents(pageContentPath+"/root/responsivegrid");
        	}
        	if(Objects.nonNull(includeSubPages) && includeSubPages.equalsIgnoreCase("true"))
        	clonePageContentHierachy(resourceResolver, pageContentHierachy, locale);
        }
    }

	private void clonePageContentHierachy(ResourceResolver resourceResolver, String pageContentHierachy, String locale) {
	try {
		Session session = resourceResolver.adaptTo(Session.class);
		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
		Page currentPage = pageManager.getContainingPage(resource);
		Page srcParentPage = pageManager.getPage(pageContentHierachy);
		createPageContentHierachy(resourceResolver, pageManager, currentPage, srcParentPage, locale);
		session.save();
	} catch (PathNotFoundException e) {
	} catch (ItemExistsException e) {
	} catch (VersionException e) {
	} catch (ConstraintViolationException e) {
	} catch (LockException e) {
	} catch (ValueFormatException e) {
	} catch (WCMException e) {
	} catch (RepositoryException e) {
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
					destChildrenPage = pageManager.create(currentPagepath, srcChildrenPage.getName(),srcChildrenPage.getTemplate().getPath(), srcChildrenPage.getTitle());
				}
				
				Node childrenJcrContent = destChildrenPage.getContentResource().adaptTo(Node.class);
				Node srcRoot = sourceJcrContent.getNode("root");
				Node destRootNode = null;
				if(childrenJcrContent.hasNode(srcRoot.getName())) {
					destRootNode  = childrenJcrContent.getNode(srcRoot.getName());
				}else {
					destRootNode  = childrenJcrContent.addNode(srcRoot.getName());
				}
				
				destRootNode.setProperty("sling:resourceType", srcRoot.getProperty("sling:resourceType").getValue().getString());
				Node srcResponsiveGrid = srcRoot.getNode("responsivegrid");
				Node destResponsiveGrid = null;
				
				if(destRootNode.hasNode(srcResponsiveGrid.getName())) {
					destResponsiveGrid = destRootNode.getNode(srcResponsiveGrid.getName());
				}
				else {
					destResponsiveGrid = destRootNode.addNode(srcResponsiveGrid.getName());
				}
				
				destResponsiveGrid.setProperty("sling:resourceType", srcResponsiveGrid.getProperty("sling:resourceType").getValue().getString());
				Node destPageReference = null;
				if(destResponsiveGrid.hasNode("pagereference")) {
					destPageReference = destResponsiveGrid.getNode("pagereference");
				}else {
					destPageReference = destResponsiveGrid.addNode("pagereference");
				}
				
				destPageReference.setProperty("sling:resourceType", "customerhub/components/content/pagereference");
				String srcChildrenPath = srcChildrenPage.getPath();
				srcChildrenPath = srcChildrenPath.replace("/en", "/" + locale);
				destPageReference.setProperty("pageContentPath", srcChildrenPath);
				createPageContentHierachy(resourceResolver, pageManager, destChildrenPage, srcChildrenPage, locale);
		}
	}
	
	private boolean isPageExist(ResourceResolver resourceResolver, String pagePath) {
		Resource resource = resourceResolver.getResource(pagePath);
		if(null!= resource) {
			return true;
		}
		return false;
	}
    private void pageReferenceComponents(String path) {
    	
    	Resource componentResources =	resource.getResourceResolver().getResource(path);
    	if(null != componentResources) {
    		Iterator<Resource> iterators = componentResources.listChildren();
        	while(iterators.hasNext()) {
        		String componentRefPath = iterators.next().getPath();
        		if(filterContentDrivenComponent(componentRefPath))
        		componentsReference.add(componentRefPath);
        		}
    		}
    	}
    
    private boolean filterContentDrivenComponent(String path) {
    	
		boolean isContentDrivenComponent = false;
		if(Objects.nonNull(path) && (path.contains("introscreen") || path.contains("recommendedforyoucar") || path.contains("getstarted"))) {
			isContentDrivenComponent = true;
		}
		return isContentDrivenComponent;
	}

	public List<String> getComponentsReference() {
		
		return componentsReference;
	}

	public String getPageContentPath() {
		
		return pageContentPath;
	}

}
