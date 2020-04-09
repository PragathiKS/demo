package com.tetrapak.publicweb.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.models.AggregatorModel;
import com.tetrapak.publicweb.core.models.TabsListModel;
import com.tetrapak.publicweb.core.models.multifield.TabBeanSemiAutoModel;
import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.services.SiteImproveScriptService;

/**
 * This is a global util class to access globally common utility methods
 *
 * @author Nitin Kumar
 */
public final class GlobalUtil {

    private GlobalUtil() {
        /*
            adding a private constructor to hide the implicit one
         */
    }
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TabsListModel.class);

    /**
     * Method to get service
     *
     * @param clazz class type
     * @return T
     */
    public static <T> T getService(final Class<T> clazz) {
        final BundleContext bundleContext = FrameworkUtil.getBundle(clazz).getBundleContext();
        ServiceReference serviceReference = bundleContext.getServiceReference(clazz.getName());
        if (null == serviceReference) {
            return null;
        }
        return (T) bundleContext.getService(serviceReference);
    }

    /**
     * @return site improve script
     */
    public static String getSiteImproveScript() {
        SiteImproveScriptService siteImproveScriptService = getService(SiteImproveScriptService.class);
        if (null == siteImproveScriptService) {
            return null;
        }
        return siteImproveScriptService.getSiteImproveScriptUrl();
    }

    /**
     * get scene 7 video url
     *
     * @param damVideoPath        video path
     * @param dynamicMediaService dynamic media service
     * @return video path from scene 7
     */
    public static String getVideoUrlFromScene7(String damVideoPath, DynamicMediaService dynamicMediaService) {
        final String FORWARD_SLASH = "/";
        damVideoPath = StringUtils.substringBeforeLast(damVideoPath, ".");
        damVideoPath = StringUtils.substringAfterLast(damVideoPath, FORWARD_SLASH);
        damVideoPath = dynamicMediaService.getVideoServiceUrl() + dynamicMediaService.getRootPath()
                + FORWARD_SLASH + damVideoPath;
        return damVideoPath;
    }
    
    /**
     * @param resource
     * @param tags
     * @param maxTabs
     * @return query results
     */
    public static SearchResult executeAggregatorQuery(Resource resource,String[] tags,int maxTabs) {
        LOGGER.info("Executing executeQuery method.");
        ResourceResolver resourceResolver = resource.getResourceResolver();
        Map<String, String> map = new HashMap<>();

        // adapt a ResourceResolver to a QueryBuilder
        QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
        Session session = resourceResolver.adaptTo(Session.class);

        // Adding query parameters
        map.put("path", PageUtil.getLanguagePage(resource).getPath());
        map.put("type", "cq:Page");

        // Parameter to look for tags on the page.
        if (tags != null && tags.length > 0) {
            map.put("1_group.p.and", "true");
            for (int i = 0; i < tags.length; i++) {
            map.put("1_group." + (i + 1) + "_group.property", "jcr:content/cq:tags");
            map.put("1_group." + (i + 1) + "_group.property.value", tags[i]);
            }
        }
        map.put("orderby", "jcr:content/cq:lastModified");
        map.put("orderby.sort", "desc");
        map.put("p.limit", String.valueOf(maxTabs));

        LOGGER.info("Here is the query PredicateGroup : {} ", PredicateGroup.create(map));
        Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);

        return query.getResult();
        }
    
    /**
     * @param resource
     * @param tags
     * @param maxTabs
     * @return aggregatorList
     */
    public static List<AggregatorModel> getAggregatorList(Resource resource,String[] tags,int maxTabs){
	List<AggregatorModel> aggregatorList = new ArrayList<AggregatorModel>();
	ResourceResolver resolver = resource.getResourceResolver();
	PageManager pageManager = resolver.adaptTo(PageManager.class);
	SearchResult searchResults = GlobalUtil.executeAggregatorQuery(resource,tags,maxTabs);
	for (Hit hit : searchResults.getHits()) {
	    try {
		AggregatorModel aggregator = PageUtil.getAggregator(pageManager.getPage(hit.getPath()));
		if(aggregator != null) {
		    aggregatorList.add(aggregator);
		}
	    } catch (RepositoryException e) {
		LOGGER.info("RepositoryException in getAggregatorList", e.getMessage(), e);
	    }
	}
	return aggregatorList;
    }
    

    /**
     * @param resource
     * @param pagePaths
     * @param maxTabs
     * @return aggregatorList
     */
    public static List<AggregatorModel> getAggregatorList(Resource resource, List<TabBeanSemiAutoModel> pagePaths, int maxTabs) {
	List<AggregatorModel> aggregatorList = new ArrayList<AggregatorModel>();
	ResourceResolver resolver = resource.getResourceResolver();
	PageManager pageManager = resolver.adaptTo(PageManager.class);
	for (TabBeanSemiAutoModel pagePath : pagePaths) {
	    AggregatorModel aggregator = PageUtil.getAggregator(pageManager.getPage(pagePath.getPageURL()));
	    if (aggregator != null) {
		aggregatorList.add(aggregator);
	    }
	}
	return aggregatorList;
    }
}
