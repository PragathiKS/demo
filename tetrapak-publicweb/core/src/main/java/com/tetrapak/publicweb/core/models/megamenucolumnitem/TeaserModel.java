package com.tetrapak.publicweb.core.models.megamenucolumnitem;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,resourceType = "publicweb/components/structure/megamenucolumnitems/teaser")
@Exporter(name = "jackson", extensions = "json")
public class TeaserModel {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String imagePath;

    @ValueMapValue
    private String imageAltText;
    
    @ValueMapValue
    private String imageLink;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

	public String getImageAltText() {
        return imageAltText;
    }	

    public String getImageLink() {
		return imageLink;
	}
}
