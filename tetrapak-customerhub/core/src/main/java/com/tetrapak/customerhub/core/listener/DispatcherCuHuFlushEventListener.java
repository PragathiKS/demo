package com.tetrapak.customerhub.core.listener;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.customerhub.core.services.DispatcherFlushService;
import com.tetrapak.customerhub.core.services.config.CuhuDispatcherFlushConfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;

/**
 * Event Listener | adds an event listener as per the configured path and clear
 * cache for the configured path
 * 
 * @author Swati Lamba
 */
@Component(immediate = true, service = EventListener.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = CuhuDispatcherFlushConfig.class)
public class DispatcherCuHuFlushEventListener implements EventListener {

	private CuhuDispatcherFlushConfig dispatcherFlushConfig;
	private static final Logger LOGGER = LoggerFactory.getLogger(EventListener.class);

	@Reference
	private DispatcherFlushService dispatcherFlush;

	@Reference
	private SlingRepository repository;

	private Session session;
	private ObservationManager observationManager;

	@Activate
	protected void activate(ComponentContext context, CuhuDispatcherFlushConfig config) {
		dispatcherFlushConfig = config;
		try {
			session = repository.loginService("customerhubUser", null);
			observationManager = session.getWorkspace().getObservationManager();
			observationManager.addEventListener(this,
					Event.PROPERTY_ADDED | Event.PROPERTY_CHANGED | Event.PROPERTY_REMOVED,
					dispatcherFlushConfig.contentPath(), true, null, new String[] { "cq:PageContent" }, true);

			LOGGER.info("*************added JCR event listener");
		} catch (RepositoryException e) {
			LOGGER.error("RepositoryException while adding listener {}", e);
		}
	}

	@Deactivate
	protected void deactivate() {
		try {
			if (observationManager != null) {
				observationManager.removeEventListener(this);
				LOGGER.info("*************removed JCR event listener");
			}
		} catch (RepositoryException re) {
			LOGGER.error("*************error removing the JCR event listener ", re);
		} finally {
			if (session != null) {
				session.logout();
				session = null;
			}
		}
	}

	@Override
	public void onEvent(EventIterator events) {
		while (events.hasNext()) {
			Event event = events.nextEvent();
			try {
				LOGGER.info("Event triggered at path: {}", event.getPath());
				String eventPath = event.getPath();
				String relativePath = StringUtils.EMPTY;
				Pattern pattern = Pattern.compile("/content/tetrapak/customerhub/en");

				if (eventPath.contains("content-components")) {
					pattern = Pattern.compile("/content/tetrapak/customerhub/content-components");
				}

				Matcher matcher = pattern.matcher(eventPath);
				dispatcherFlush.flush(event.getPath());

				if (matcher.find()) {
					relativePath = eventPath.substring(matcher.end() + "/en".length()).trim().split("/jcr:content1")[0];
				}

				List<String[]> countryLocaleList = getCountryLocaleList(dispatcherFlushConfig.getCountryLocaleList());
				Iterator<String[]> itr = countryLocaleList.iterator();
				while (itr.hasNext()) {
					String[] countryLocale = itr.next();
					String path = "/" + countryLocale[0] + "/" + countryLocale[1] + relativePath;
					dispatcherFlush.flush(path);
				}

			} catch (RepositoryException e) {
				LOGGER.error("RepositoryException in DispatcherFlushEventListener {}", e);
			}
		}
	}

	/**
	 * @param countryLocaleList comma and hifen separated list of country and locale
	 * @return organised list having the coutry and locale in a string array data
	 *         structure
	 */
	private List<String[]> getCountryLocaleList(String countryLocaleList) {
		List<String[]> countryLocaleArray = new ArrayList<>();
		if (StringUtils.isNotBlank(countryLocaleList)) {
			String[] list = countryLocaleList.trim().split(",");
			for (int i = 0; i < list.length; i++) {
				String[] countryLocale = list[i].split("-");
				countryLocaleArray.add(countryLocale);
			}
		}
		return countryLocaleArray;
	}
}
