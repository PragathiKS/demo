package com.trs.core.scope;

import javax.servlet.http.HttpServletRequest;
import org.apache.jackrabbit.api.security.user.User;
import org.osgi.service.component.annotations.Component;
import com.adobe.granite.oauth.server.Scope;
import com.adobe.granite.oauth.server.ScopeWithPrivileges;

@Component(service = Scope.class)
public class DAMReadScope implements ScopeWithPrivileges {

    private static final String DAM_RESOURCE_URI = "/content/trs";
    private static final String DAM_RESOURCE_READ_SCOPE_NAME = "api_read";

    @Override
    public String getDescription(HttpServletRequest arg0) {
        return "Read DAM Assets";
    }

    @Override
    public String getEndpoint() {
        return null;
    }

    @Override
    public String getName() {
        return DAM_RESOURCE_READ_SCOPE_NAME;
    }

    @Override
    public String getResourcePath(User user) {
        return DAM_RESOURCE_URI;
    }

    @Override
    public String[] getPrivileges() {
        return new String[] { "jcr:read","jcr:readAccessControl" };
    }
}
