package com.tetrapak.customerhub.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * This is a model class for Rich Text Editor component.
 *
 * @author Nitin Kumar
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RichTextModel {

    /** The text. */
    @ValueMapValue
    private String text;

    /**
     * Gets the text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }
}
