package com.tetrapak.publicweb.core.models.v2;

import com.day.cq.contentsync.handler.util.RequestResponseFactory;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.WCMMode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tetrapak.publicweb.core.models.MegaMenuColumnModel;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.engine.SlingRequestProcessor;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static com.tetrapak.publicweb.core.constants.PWConstants.*;

/**
 * The Class MegaMenuSolutionModel.
 */
@Model(
        adaptables = {SlingHttpServletRequest.class, Resource.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,resourceType = "publicweb/components/structure/megamenuconfigv2")
@Exporter(name = "jackson", extensions = "json")
public class MegaMenuConfigModel {

    @ValueMapValue
    private int numberOfColumns;

    private String modelJsonPath;

    @Inject
    private Resource resource;

    @Inject
    private SlingHttpServletRequest request;

    @ScriptVariable
    private Page currentPage;

    @ChildResource
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MegaMenuColumnModel col1;

    @ChildResource
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MegaMenuColumnModel col2;

    @ChildResource
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MegaMenuColumnModel col3;

    @ChildResource
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MegaMenuColumnModel col4;

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public MegaMenuColumnModel getCol1() {
        return col1;
    }

    public MegaMenuColumnModel getCol2() {
        return col2;
    }

    public MegaMenuColumnModel getCol3() {
        return col3;
    }

    public MegaMenuColumnModel getCol4() {
        return col4;
    }

    public String getModelJsonPath() {
        String market = StringUtils.EMPTY;
        if(currentPage.getPath().startsWith(PUBLICWEB_XF_PATH)){
            String marketSuffix = request.getRequestPathInfo().getSuffix();
            if(StringUtils.isNotBlank(marketSuffix)){
                market = FilenameUtils.removeExtension(marketSuffix);
                market = market.replace(SLASH,StringUtils.EMPTY);
            }
        }else{
            String marketPath = LinkUtils.getCountryPath(currentPage.getPath());
            market = StringUtils.substringAfterLast(marketPath,SLASH);
        }

        return request.getResource().getPath()+DOT+MOBILE_MEGAMENU_SELECTOR+DOT+market+JSON_EXTENSION;
    }

}
