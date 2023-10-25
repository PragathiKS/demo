package com.tetrapak.publicweb.core.models.megamenucolumnitem;

import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static com.tetrapak.publicweb.core.constants.PWConstants.LANG_MASTERS;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = "publicweb/components/structure/megamenucolumnitems/description")
@Exporter(name = "jackson", extensions = "json")
public class DescriptionModel {

    @ValueMapValue
    private String text;

    @Inject
    SlingHttpServletRequest request;

    @ScriptVariable
    private Page currentPage;

    @PostConstruct
    protected void init(){
        if(request!=null && currentPage!=null){
            if(currentPage.getPath().startsWith("/content/experience-fragments")){
                String marketSuffix = request.getRequestPathInfo().getSuffix();
                if(StringUtils.isNotBlank(text) && StringUtils.isNotBlank(marketSuffix)){
                    String market = FilenameUtils.removeExtension(marketSuffix);
                    market = market.replace("/","");
                    text = text.replace(LANG_MASTERS,market);
                }
            }else{
                String marketPath = LinkUtils.getCountryPath(currentPage.getPath());
                if(StringUtils.isNotBlank(text) && text.contains(LANG_MASTERS)){
                    text = text.replace("/content/tetrapak/publicweb/lang-masters",marketPath);
                }
            }

        }

    }

    public String getText() {
        return text;
    }
}
