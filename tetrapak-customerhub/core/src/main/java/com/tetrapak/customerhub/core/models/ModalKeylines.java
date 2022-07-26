package com.tetrapak.customerhub.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.google.gson.annotations.Expose;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ModalKeylines {

    private static final String DEFAULT_TITLE = "cuhu.packDesign.keylines.modalTitle";
    private static final String DEFAULT_DESCRIPTION = "cuhu.packDesign.keylines.modalDescription";
    private static final String DEFAULT_VOLUMES = "cuhu.packDesign.keylines.selectVolumes";
    private static final String DEFAULT_OPENINGS = "cuhu.packDesign.keylines.selectOpenings";
    private static final String DEFAULT_DOWNLOAD = "cuhu.packDesign.keylines.downloadKeyline";
    private static final String DEFAULT_DOWNLOAD_ERROR = "cuhu.packDesign.keylines.errorMessage";

    @ValueMapValue
    @Expose(serialize = true)
    @Default(values = DEFAULT_TITLE)
    private String modalTitle;

    @ValueMapValue
    @Default(values = DEFAULT_DESCRIPTION)
    @Expose(serialize = true)
    private String modalDescription;

    @ValueMapValue
    @Default(values = DEFAULT_VOLUMES)
    @Expose(serialize = true)
    private String selectVolumes;

    @ValueMapValue
    @Default(values = DEFAULT_OPENINGS)
    @Expose(serialize = true)
    private String selectOpenings;

    @ValueMapValue
    @Default(values = DEFAULT_DOWNLOAD)
    @Expose(serialize = true)
    private String downloadKeyline;
    
    @ValueMapValue
    @Default(values = DEFAULT_DOWNLOAD_ERROR)
    @Expose(serialize = true)
    private String downloadKeylineError;

    public String getModalTitle() {
	return (modalTitle != null ? modalTitle : DEFAULT_TITLE);
    }

    public String getModalDescription() {
	return (modalDescription != null ? modalDescription : DEFAULT_DESCRIPTION);
    }

    public String getSelectVolumes() {
	return (selectVolumes != null ? selectVolumes : DEFAULT_VOLUMES);
    }

    public String getSelectOpenings() {
	return (selectOpenings != null ? selectOpenings : DEFAULT_OPENINGS);
    }

    public String getDownloadKeyline() {
	return (downloadKeyline != null ? downloadKeyline : DEFAULT_DOWNLOAD);
    }
    
    public String getDownloadKeylineError() {
	return (downloadKeylineError != null ? downloadKeylineError : DEFAULT_DOWNLOAD_ERROR);
    }

}
