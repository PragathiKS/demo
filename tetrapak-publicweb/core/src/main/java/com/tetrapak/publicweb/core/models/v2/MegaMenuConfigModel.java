package com.tetrapak.publicweb.core.models.v2;

import com.day.cq.contentsync.handler.util.RequestResponseFactory;
import com.day.cq.wcm.api.WCMMode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tetrapak.publicweb.core.models.MegaMenuColumnModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.engine.SlingRequestProcessor;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * The Class MegaMenuSolutionModel.
 */
@Model(
        adaptables = { Resource.class },
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,resourceType = "publicweb/components/structure/megamenuconfigv2")
@Exporter(name = "jackson", extensions = "json")
public class MegaMenuConfigModel {

    @ValueMapValue
    private int numberOfColumns;

    private String modelJsonPath;

    @Self
    private Resource resource;

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
        return resource.getPath()+".model.json";
    }
}
