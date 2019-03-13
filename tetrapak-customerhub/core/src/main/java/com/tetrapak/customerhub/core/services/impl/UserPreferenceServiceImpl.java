package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.services.UserPreferenceService;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.LinkedHashSet;
import java.util.Set;

@Component(immediate = true, service = UserPreferenceService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class UserPreferenceServiceImpl implements UserPreferenceService {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private static final String ORDER_PREFERENCES = "orderPreferences";

    @Override
    public Set<String> getSavedPreferences(Resource resource) {
        Set<String> savedPreferences = new LinkedHashSet<>();
        ResourceResolver resourceResolver = resource.getResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);
        String userId = session.getUserID();
        UserManager userManager = resourceResolver.adaptTo(UserManager.class);
        try {
            Authorizable user = userManager.getAuthorizable(userId);

            String path = user.getPath();
            Resource userResource = resourceResolver.getResource(path);
            ValueMap map = userResource.getValueMap();
            if (map.containsKey(ORDER_PREFERENCES)) {
                String preferences = (String) map.get(ORDER_PREFERENCES);

                String[] preferencesList = preferences.split(",");
                for (String pref : preferencesList) {
                    savedPreferences.add(pref);
                }
            }
        } catch (RepositoryException e) {
            LOG.error("Exception in UserPreferencesServlet", e);
        }
        return savedPreferences;
    }
}
