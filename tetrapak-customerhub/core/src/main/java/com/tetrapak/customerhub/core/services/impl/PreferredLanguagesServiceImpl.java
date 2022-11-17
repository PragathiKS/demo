package com.tetrapak.customerhub.core.services.impl;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.PreferredLanguagesService;
import com.tetrapak.customerhub.core.services.config.PreferredLanguagesConfiguration;

/**
 * Implementaion of the Preferred Languages Service class
 * 
 * @author semathura
 *
 */

@Component(service = PreferredLanguagesService.class, immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = PreferredLanguagesConfiguration.class)
public class PreferredLanguagesServiceImpl implements PreferredLanguagesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreferredLanguagesServiceImpl.class);

    private PreferredLanguagesConfiguration config;

    @Activate
    public void activate(final PreferredLanguagesConfiguration config) {
	this.config = config;
    }

    public Map<String,String> getPreferredLanguages(final ResourceResolver resourceResolver) {
	LOGGER.debug("Inside getPreferredLanguages Method");
	Map<String,String> listOfLanguages = new HashMap<>();
	final Resource preferredLangRootRes = resourceResolver.getResource(config.path());
	if (Objects.nonNull(preferredLangRootRes)) {
		final Iterator<Resource> rootIterator = preferredLangRootRes.listChildren();
		while (rootIterator.hasNext()) {
			final Resource childResource = rootIterator.next();
			if (Objects.nonNull(childResource) && !childResource.getPath().contains(JcrConstants.JCR_CONTENT)) {
				final String dataPath = childResource.getPath() + CustomerHubConstants.DATA_ROOT_PATH;
				Resource dataResource = resourceResolver.getResource(dataPath);
				if (Objects.nonNull(dataResource)) {
		            final ValueMap vMap = dataResource.getValueMap();
		            listOfLanguages.put(vMap.get(CustomerHubConstants.LANG_CODE).toString(), vMap.get(CustomerHubConstants.LANG_DESC).toString());
				}
		    }
	    }
    }
	listOfLanguages = listOfLanguages.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue(String.CASE_INSENSITIVE_ORDER))
            .collect(Collectors.toMap(Map.Entry::getKey,
                    Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	return listOfLanguages;
    }
}
