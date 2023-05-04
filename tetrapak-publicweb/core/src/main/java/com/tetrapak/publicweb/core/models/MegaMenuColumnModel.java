package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(
        adaptables = { Resource.class },
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MegaMenuColumnModel {

    @SlingObject
    private Resource resource;

    private List<Resource> items;

    @ValueMapValue
    private String bgColor;

    @PostConstruct
    protected void init(){
        items = new ArrayList<>();
        if(resource!=null) {
            Iterator<Resource> children = resource.listChildren();
            while (children.hasNext()) {
                items.add(children.next());
            }
        }
    }

    public String getBgColor() {
        return bgColor;
    }

    public List<Resource> getItems() {
        return items;
    }
}
