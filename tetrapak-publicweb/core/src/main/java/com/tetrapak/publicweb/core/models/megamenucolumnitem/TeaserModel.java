package com.tetrapak.publicweb.core.models.megamenucolumnitem;

import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static com.tetrapak.publicweb.core.constants.PWConstants.LANG_MASTERS;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,resourceType = "publicweb/components/structure/megamenucolumnitems/teaser")
@Exporter(name = "jackson", extensions = "json")
public class TeaserModel {

    @SlingObject
    private SlingHttpServletRequest request;

    @SlingObject
    private Resource resource;

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

    @ScriptVariable
    private Page currentPage;

    @PostConstruct
    protected void init(){

        if(request!=null && currentPage!=null){
            imageLink = LinkUtils.getMarketLinkInXF(currentPage.getPath(),request,imageLink);
            imageLink =  LinkUtils.sanitizeLink(imageLink, request);
        }else{
            imageLink =  LinkUtils.sanitizeLink(imageLink, resource);
        }
    }

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
