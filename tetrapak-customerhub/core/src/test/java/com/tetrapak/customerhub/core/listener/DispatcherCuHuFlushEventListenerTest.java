/**
 * 
 */
package com.tetrapak.customerhub.core.listener;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.ObservationManager;

import org.apache.sling.jcr.api.SlingRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.service.component.ComponentContext;

import com.tetrapak.commons.core.services.DispatcherFlushService;
import com.tetrapak.customerhub.core.services.config.CuhuDispatcherFlushConfig;

/**
 * 
 * DispatcherCuHuFlushEventListenerTest
 * 
 * @author swalamba
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class DispatcherCuHuFlushEventListenerTest {

	@InjectMocks
	DispatcherCuHuFlushEventListener listener = new DispatcherCuHuFlushEventListener();

	@Mock
	private ComponentContext context;

	@Mock
	private CuhuDispatcherFlushConfig config;

	@Mock
	private SlingRepository repository;

	@Mock
	private Session session;

	@Mock
	private Workspace mockWorkSpace;

	@Mock
	private ObservationManager obsMgr;

	@Mock
	private EventIterator events;

	@Mock
	private Event mockEvent;

	@Mock
	private DispatcherFlushService dispatcherFlush;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		Mockito.when(config.contentPath()).thenReturn("/content/tetrapak/customerhub");
		Mockito.when(repository.loginService("customerhubUser", null)).thenReturn(session);
		Mockito.when(session.getWorkspace()).thenReturn(mockWorkSpace);
		Mockito.doNothing().when(obsMgr).addEventListener(listener,
				Event.PROPERTY_ADDED | Event.PROPERTY_CHANGED | Event.PROPERTY_REMOVED, "/content/tetrapak/customerhub",
				true, null, new String[] { "cq:PageContent" }, true);
		Mockito.when(mockWorkSpace.getObservationManager()).thenReturn(obsMgr);

	}

	/**
	 * Test method for
	 * {@link com.tetrapak.customerhub.core.listener.DispatcherCuHuFlushEventListener#onEvent(javax.jcr.observation.EventIterator)}.
	 * 
	 * @throws RepositoryException
	 */
	@Test
	public void testOnEventContentComponentPath() throws RepositoryException {
		Mockito.when(config.enableCustomFlush()).thenReturn("true");
		listener.activate(context, config);
		Mockito.when(config.getCountryLocaleList()).thenReturn("us-en,fr-sp");
		Mockito.when(events.hasNext()).thenReturn(true, false);
		Mockito.when(events.nextEvent()).thenReturn(mockEvent);
		Mockito.when(mockEvent.getPath()).thenReturn(
				"/content/tetrapak/customerhub/content-components/en/dashboard/jcr:content/root/responsivegrid/orderingcard");
		Mockito.doNothing().when(dispatcherFlush).flush(Mockito.anyString());
		listener.onEvent(events);
		Mockito.verify(events, Mockito.atLeastOnce()).nextEvent();
		listener.deactivate();
	}

	/**
	 * Test method for
	 * {@link com.tetrapak.customerhub.core.listener.DispatcherCuHuFlushEventListener#onEvent(javax.jcr.observation.EventIterator)}.
	 * 
	 * @throws RepositoryException
	 */
	@Test
	public void testOnEvent() throws RepositoryException {
		Mockito.when(config.enableCustomFlush()).thenReturn("true");
		listener.activate(context, config);
		Mockito.when(config.getCountryLocaleList()).thenReturn("us-en,fr-sp");
		Mockito.when(events.hasNext()).thenReturn(true, false);
		Mockito.when(events.nextEvent()).thenReturn(mockEvent);
		Mockito.when(mockEvent.getPath())
				.thenReturn("/content/tetrapak/customerhub/global/en/dashboard/jcr:content/root/responsivegrid/orderingcard");
		Mockito.doNothing().when(dispatcherFlush).flush(Mockito.anyString());
		listener.onEvent(events);
		Mockito.verify(events, Mockito.atLeastOnce()).nextEvent();
		listener.deactivate();
	}

	/**
	 * Test method for
	 * {@link com.tetrapak.customerhub.core.listener.DispatcherCuHuFlushEventListener#onEvent(javax.jcr.observation.EventIterator)}.
	 * 
	 * @throws RepositoryException
	 */
	@Test
	public void testOnEventDisabledInConfig() throws RepositoryException {
		Mockito.when(config.enableCustomFlush()).thenReturn("false");
		listener.activate(context, config);
		Mockito.when(events.hasNext()).thenReturn(false);
		listener.onEvent(events);
		Mockito.verify(events, Mockito.never()).nextEvent();
		listener.deactivate();
	}

}
