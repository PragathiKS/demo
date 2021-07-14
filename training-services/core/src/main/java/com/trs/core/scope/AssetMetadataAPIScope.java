package com.trs.core.scope;

import javax.servlet.http.HttpServletRequest;
import org.apache.jackrabbit.api.security.user.User;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.granite.oauth.server.Scope;
import com.adobe.granite.oauth.server.ScopeWithPrivileges;
import com.trs.core.services.TrsConfigurationService;

@Component(service = Scope.class)
public class AssetMetadataAPIScope implements ScopeWithPrivileges {

    private static final String TRS_METADATA_API_SCOPE_NAME = "api_read";
    
    @Reference
    private TrsConfigurationService trsConfig;

    @Override
    public String getDescription(HttpServletRequest arg0) {
        return "Expose TRS Assets Metadata";
    }

    @Override
    public String getEndpoint() {
        return null;
    }

    @Override
    public String getName() {
        return TRS_METADATA_API_SCOPE_NAME;
    }

    @Override
    public String getResourcePath(User user) {
        return trsConfig.getAssetMetadataAPIScopePath();
    }

    @Override
    public String[] getPrivileges() {
        return new String[] { "jcr:read","jcr:readAccessControl" };
    }
}
