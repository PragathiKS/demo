package com.tetrapak.publicweb.core.models.v2;

import com.tetrapak.publicweb.core.beans.AnchorBean;
import com.tetrapak.publicweb.core.models.LinkModel;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AnchorModel extends com.tetrapak.publicweb.core.models.AnchorModel {

    @Self
    @Via (type= ResourceSuperType.class)
    private com.tetrapak.publicweb.core.models.AnchorModel anchorModel;

    @Self
    private SlingHttpServletRequest request;
    
    @ValueMapValue
    private String ctaLinkText;

    @ValueMapValue
    private String ctaLinkUrl;

    public List<AnchorBean> getAnchorDetailList() {

        return anchorModel.getAnchorDetailList();
    }
    
    public String getCtaLinkText() {
        return ctaLinkText;
    }

    public String getCtaLinkUrl() {
        return LinkUtils.sanitizeLink(ctaLinkUrl,request);
    }
}
