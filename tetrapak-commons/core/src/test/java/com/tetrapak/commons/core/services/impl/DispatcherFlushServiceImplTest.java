package com.tetrapak.commons.core.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.day.cq.replication.Agent;
import com.day.cq.replication.AgentConfig;
import com.day.cq.replication.AgentManager;

/**
 * 
 * DispatcherFlushServiceImpl Test CLASS
 * 
 * @author swalamba
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class DispatcherFlushServiceImplTest {

	@InjectMocks
	DispatcherFlushServiceImpl dispatcherFlushService = new DispatcherFlushServiceImpl();

	@Mock
	private AgentManager agentManager;

	@Mock
	private Agent agent;

	@Mock
	private AgentConfig mockAgentConfig;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testFlushNoAgents() {
		Map<String, Agent> agentMaps = new HashMap<>();
		Mockito.when(agentManager.getAgents()).thenReturn(agentMaps);
		dispatcherFlushService.flush("/somepath");
	}

	@Test
	public void testFlush() {
		Map<String, Agent> agentMaps = new HashMap<>();
		Mockito.when(agent.isCacheInvalidator()).thenReturn(true);
		Mockito.when(agent.isEnabled()).thenReturn(true);
		Mockito.when(agent.getConfiguration()).thenReturn(mockAgentConfig);
		Mockito.when(mockAgentConfig.getAllTransportURIs()).thenReturn(null);
		agentMaps.put("dispatcherFlushAgent", agent);
		Mockito.when(agentManager.getAgents()).thenReturn(agentMaps);
		dispatcherFlushService.flush("/somepath");
		Mockito.verify(agentManager, Mockito.atLeastOnce()).getAgents();
	}

}
