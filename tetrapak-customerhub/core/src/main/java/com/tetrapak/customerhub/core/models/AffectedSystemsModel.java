package com.tetrapak.customerhub.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AffectedSystemsModel {

    /** The label */
    @Inject
    private String label;

    @Inject
    List<ProductsInvolvedModel> productsInvolved;

    public String getLabel() {
        return label;
    }

    public List<ProductsInvolvedModel> getProductsInvolved() {
        return productsInvolved;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setProductsInvolved(List<ProductsInvolvedModel> productsInvolved) {
        this.productsInvolved = productsInvolved;
    }
}
