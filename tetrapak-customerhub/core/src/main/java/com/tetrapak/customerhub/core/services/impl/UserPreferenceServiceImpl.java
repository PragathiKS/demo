package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Component(immediate = true, service = UserPreferenceService.class)
public class UserPreferenceServiceImpl implements UserPreferenceService {

    private static final String TETRAPAK_USER = "customerhubUser";
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private static final String ORDER_PREFERENCES = "orderPreferences";

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    public Set<String> getSavedPreferences(Resource resource) {
        Set<String> savedPreferences = new LinkedHashSet<>();
        final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, TETRAPAK_USER);
        ResourceResolver resourceResolver = GlobalUtil.getResourceResolverFromSubService(resolverFactory, paramMap);
        Session session = resource.getResourceResolver().adaptTo(Session.class);
        String userId = session.getUserID();
        UserManager userManager = resourceResolver.adaptTo(UserManager.class);
        try {
            Authorizable user = userManager.getAuthorizable(userId);

            String path = user.getPath();
            Resource userResource = resourceResolver.getResource(path);
            ValueMap map = userResource.getValueMap();
            if (map.containsKey(ORDER_PREFERENCES)) {
                String[] preferences = (String[]) map.get(ORDER_PREFERENCES);
                for (String pref : preferences) {
                    savedPreferences.add(pref);
                }
            }
        } catch (RepositoryException e) {
            LOG.error("Exception in UserPreferencesServlet", e);
        } finally {
            if(null != session && session.isLive()){
                session.logout();
            }
            if(null != resourceResolver && resourceResolver.isLive()){
                resourceResolver.close();
            }
        }
        return savedPreferences;
    }
}
