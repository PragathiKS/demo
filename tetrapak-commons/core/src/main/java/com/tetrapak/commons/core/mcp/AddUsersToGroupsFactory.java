package com.tetrapak.commons.core.mcp;

import com.adobe.acs.commons.mcp.ProcessDefinitionFactory;
import org.osgi.service.component.annotations.Component;

/**
 * Factory class for the MCP process which
 * maps users to groups based on the input
 */
@Component(service = ProcessDefinitionFactory.class)
public class AddUsersToGroupsFactory extends ProcessDefinitionFactory<AddUsersToGroups> {

    @Override
    public String getName() {
        return "Add Users to Groups";
    }

    @Override
    public AddUsersToGroups createProcessDefinitionInstance() {
        return new AddUsersToGroups();
    }
}
