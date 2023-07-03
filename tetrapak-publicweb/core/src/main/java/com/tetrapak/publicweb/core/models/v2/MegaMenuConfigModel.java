package com.tetrapak.publicweb.core.models.v2;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The Class MegaMenuSolutionModel.
 */
@Model(
        adaptables = { Resource.class },
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MegaMenuConfigModel {

    @ValueMapValue
    private int numberOfColumns;

    public int getNumberOfColumns() {
        return numberOfColumns;
    }
}
