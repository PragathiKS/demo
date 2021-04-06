package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.i18n.I18n;
import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.beans.pxp.Openingclosure;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.models.multifield.ManualModel;
import com.tetrapak.publicweb.core.utils.PageUtil;

/**
 * The Class PXPOpeningsModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PXPOpeningsModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PXPOpeningsModel.class);

    /** The request. */
    @Self
    private SlingHttpServletRequest request;

    /** The current page. */
    @ScriptVariable
    private Page currentPage;

    /** The resource. */
    @Self
    @Via("resource")
    private Resource resource;

    /** The heading. */
    @ValueMapValue(via = "resource")
    private String heading;

    /** The pw theme. */
    @ValueMapValue(via = "resource")
    @Default(values = "grayscale-white")
    private String pwTheme;

    /** The anchor id. */
    @ValueMapValue(via = "resource")
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue(via = "resource")
    private String anchorTitle;

    @ValueMapValue(via = "resource")
    private String pwImgBackground;

    /** The i18n. */
    private I18n i18n;

    /** The teaser list. */
    private final List<ManualModel> teaserList = new ArrayList<>();

    /** The Constant H5_START. */
    private static final String H5_START = "<h5>";

    /** The Constant H5_END. */
    private static final String H5_END = "</h5>";

    /** The Constant SPAN_START. */
    private static final String SPAN_START = "<span>";

    /** The Constant SPAN_END. */
    private static final String SPAN_END = "</span>";

    /** The Constant UL_START. */
    private static final String UL_START = "<ul>";

    /** The Constant UL_END. */
    private static final String UL_END = "</ul>";

    /** The Constant LI_START. */
    private static final String LI_START = "<li>";

    /** The Constant LI_END. */
    private static final String LI_END = "</li>";

    /** The Weburl. */
    private String webUrl;

    /** The package Name. */
    private String packageName;

    /**
     * The init method.
     */
    @PostConstruct
    protected void init() {
    	LOGGER.debug("Inside init of {}", this.getClass().getName());
        final ProductModel product = resource.adaptTo(ProductModel.class);
        if (Objects.nonNull(product)) {
        	setI18n();
            if(Objects.nonNull(product.getWebUrl())) {
                packageName = product.getName();
                webUrl = product.getWebUrl();
            }
            setTeaserList(product.getOpeningClousers());
        }
    }

    /**
     * Sets the I18n.
     */
    private void setI18n() {
        final Locale locale = PageUtil.getPageLocale(currentPage);
        final ResourceBundle bundle = request.getResourceBundle(locale);
        i18n = new I18n(bundle);
    }

    /**
     * Sets the teaser list.
     *
     * @param list the new teaser list
     */
    private void setTeaserList(final List<Openingclosure> list) {
        for (final Openingclosure opening : list) {
            final ManualModel model = new ManualModel();
            model.setTitle(opening.getName());
            model.setAlt(opening.getName());
            model.setFileReference(opening.getThumbnail());
            model.setDescription(getDescription(opening));
            teaserList.add(model);
        }
        LOGGER.debug("Teaser list updated successfully.");
    }

    /**
     * Gets the description.
     *
     * @param opening the opening
     * @return the description
     */
    private String getDescription(final Openingclosure opening) {
        final StringBuilder description = new StringBuilder();
        if (i18n != null) {
            description.append(H5_START + i18n.get(PWConstants.PXP_OPENINGS_TYPE) + H5_END);
            description.append(SPAN_START + opening.getType() + SPAN_END);
            description.append(H5_START + i18n.get(PWConstants.PXP_OPENINGS_PRINCIPLE) + H5_END);
            description.append(SPAN_START + opening.getPrinciple() + SPAN_END);
            description.append(H5_START + i18n.get(PWConstants.PXP_OPENINGS_BENEFITS) + H5_END);
            description.append(UL_START);
            for (final String benefit : opening.getBenefits()) {
                description.append(LI_START + benefit + LI_END);
            }
            description.append(UL_END);
        }
        return description.toString();
    }

    /**
     * Gets the heading.
     *
     * @return the heading
     */
    public String getHeading() {
        return heading;
    }

    /**
     * Gets the card style.
     *
     * @return the card style
     */
    public String getPwImgBackground() {
        return pwImgBackground;
    }

    /**
     * Gets the pw theme.
     *
     * @return the pw theme
     */
    public String getPwTheme() {
        return pwTheme;
    }

    /**
     * Gets the anchor id.
     *
     * @return the anchor id
     */
    public String getAnchorId() {
        return anchorId;
    }

    /**
     * Gets the anchor title.
     *
     * @return the anchor title
     */
    public String getAnchorTitle() {
        return anchorTitle;
    }

    /**
     * Gets the teaser list.
     *
     * @return the teaser list
     */
    public List<ManualModel> getTeaserList() {
        return teaserList;
    }

    /**
     * Gets the Weburl.
     *
     * @return the Weburl
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * Gets the packageName.
     *
     * @return the packageName
     */
    public String getPackageName() {
        return packageName;
    }
}
