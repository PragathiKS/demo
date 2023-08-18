package com.tetrapak.publicweb.core.models.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tetrapak.publicweb.core.models.MegaMenuColumnModel;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

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
}
