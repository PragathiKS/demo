package com.tetrapak.publicweb.core.models.megamenucolumnitem;

import com.fasterxml.jackson.annotation.JsonRawValue;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = "publicweb/components/structure/megamenucolumnitems/description")
@Exporter(name = "jackson", extensions = "json")
public class DescriptionModel {

    @ValueMapValue
    private String text;

    public String getText() {
        return text;
    }
}
