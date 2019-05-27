package com.tetrapak.customerhub.core.listener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;

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

import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.DispatcherFlushService;
import com.tetrapak.customerhub.core.services.config.CuhuDispatcherFlushConfig;

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
	private static final String PAGE_CONTENT = "cq:PageContent";
	private static final String USER = "customerhubUser";

	@Activate
	protected void activate(ComponentContext context, CuhuDispatcherFlushConfig config) {
		dispatcherFlushConfig = config;
		if (dispatcherFlushConfig.enableCustomFlush().equalsIgnoreCase("true")) {
			try {
				session = repository.loginService(USER, null);
				observationManager = session.getWorkspace().getObservationManager();
				observationManager.addEventListener(this,
						Event.PROPERTY_ADDED | Event.PROPERTY_CHANGED | Event.PROPERTY_REMOVED,
						dispatcherFlushConfig.contentPath(), true, null, new String[] { PAGE_CONTENT }, true);

				LOGGER.debug("*************added JCR event listener");
			} catch (RepositoryException e) {
				LOGGER.error("RepositoryException while adding listener {}", e);
			}
		} else {
			LOGGER.debug("CustomFlush for Customer Hub is disabled in the configuration!");
		}
	}

	@Deactivate
	protected void deactivate() {
		try {
			if (observationManager != null) {
				observationManager.removeEventListener(this);
				LOGGER.debug("*************removed JCR event listener");
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
				String str = "/content/tetrapak/customerhub/en";
				String locale = StringUtils.EMPTY;
				if (eventPath.contains("content-components")) {
					str = "/content/tetrapak/customerhub/content-components/";
					locale = eventPath.substring("/content/tetrapak/customerhub/content-components/".length())
							.split(CustomerHubConstants.PATH_SEPARATOR)[0];
				}

				relativePath = eventPath.substring(str.length() + locale.length()).trim().split("/jcr:content")[0];
				List<String[]> countryLocaleList = getCountryLocaleList(dispatcherFlushConfig.getCountryLocaleList());
				if (!locale.trim().isEmpty() && checkIfLocaleConfigMatchesContentPathLocale(locale.toLowerCase().trim(),
						countryLocaleList)) {
					flushCountryLocaleCache(locale, relativePath, countryLocaleList);
				} else {
					flushCountryLocaleCache(StringUtils.EMPTY, relativePath, countryLocaleList);
				}

			} catch (RepositoryException e) {
				LOGGER.error("RepositoryException in DispatcherFlushEventListener {}", e);
			}
		}
	}

	/**
	 * Method to check If Locale Config Matches Content Path Locale
	 * 
	 * @param locale
	 * @param countryLocaleList
	 * @return
	 */
	private boolean checkIfLocaleConfigMatchesContentPathLocale(String locale, List<String[]> countryLocaleList) {
		Iterator<String[]> itr = countryLocaleList.iterator();
		Set<String> localeSet = new HashSet<>();
		while (itr.hasNext()) {
			String[] cntryLocale = itr.next();
			localeSet.add(cntryLocale[1].toLowerCase());
		}
		return localeSet.contains(locale);
	}

	/**
	 * @param isSpecificLocale
	 * @param relativePath
	 * @param countryLocaleList
	 */
	private void flushCountryLocaleCache(String specificLocale, String relativePath, List<String[]> countryLocaleList) {
		Iterator<String[]> itr = countryLocaleList.iterator();
		while (itr.hasNext()) {
			String[] countryLocale = itr.next();
			if (!specificLocale.isEmpty() && specificLocale.equalsIgnoreCase(countryLocale[1])) {
				String path = CustomerHubConstants.PATH_SEPARATOR + countryLocale[0]
						+ CustomerHubConstants.PATH_SEPARATOR + countryLocale[1] + relativePath;
				dispatcherFlush.flush(path);
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
				String[] countryLocale = list[i].split(CustomerHubConstants.HYPHEN_STRING);
				countryLocaleArray.add(countryLocale);
			}
		}
		return countryLocaleArray;
	}
}
