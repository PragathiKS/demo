package com.tetrapak.commons.core.redirect;

import com.adobe.acs.commons.redirects.LocationHeaderAdjuster;
import com.tetrapak.commons.core.constants.CommonsConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;

@Component(
        immediate = true,
        service = LocationHeaderAdjuster.class,
        name = "Tetra- Pak ACS commons redirect Location Header Adjuster")
public class RedirectLocationHeaderAdjuster implements LocationHeaderAdjuster {
    @Override
    public String adjust(SlingHttpServletRequest request, String location) {
        if(location.startsWith(CommonsConstants.AEM_CONTENT_PATH) && location.endsWith(CommonsConstants.HTML)){
            return request.getResourceResolver().map(location.replace(CommonsConstants.HTML,""));
        }
        return request.getResourceResolver().map(location);
    }
}