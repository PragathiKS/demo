package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.beans.AnchorBean;
import com.tetrapak.publicweb.core.constants.PWConstants;

/**
 * The Class AnchorMenuModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AnchorMenuModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AnchorMenuModel.class);

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The current page. */
    @Inject
    private Page currentPage;

    /** The anchor detail list. */
    private List<AnchorBean> anchorDetailList = new ArrayList<>();

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
    	LOGGER.debug("Inside init of {}", this.getClass().getName());
        final String rootPath = currentPage.getPath() + "/jcr:content/root/responsivegrid";
        Resource rootResource = request.getResourceResolver().getResource(rootPath);
        if (Objects.nonNull(rootResource)) {
            final Iterator<Resource> rootIterator = rootResource.listChildren();
            while (rootIterator.hasNext()) {
                final AnchorBean bean = new AnchorBean();
                final Resource childResource = rootIterator.next();
                if (Objects.nonNull(childResource)) {
					Resource resposiveGridChild = childResource.getChild("cq:responsive/default");
					String hideValue = Objects.nonNull(resposiveGridChild)? resposiveGridChild.getValueMap().get("behavior",String.class): "";
					if(!hideValue.equalsIgnoreCase("hide")) {
						final ValueMap vMap = childResource.getValueMap();
						final String anchorId = vMap.get("anchorId", StringUtils.EMPTY);
						final String anchorTitle = vMap.get("anchorTitle", StringUtils.EMPTY);
						setAnchorBean(bean, anchorId, anchorTitle, childResource.getResourceType());
					}

                }
            }
        }
    }

    /**
     * Sets the anchor bean.
     *
     * @param bean
     *            the bean
     * @param anchorId
     *            the anchor id
     * @param anchorTitle
     *            the anchor title
     */
    private void setAnchorBean(final AnchorBean bean, final String anchorId, final String anchorTitle, final String childResPath) {
    	if (StringUtils.isNotBlank(anchorId) && StringUtils.isNotBlank(anchorTitle)) {
    		boolean setBean = false;
    		Resource resource = request.getResource();
            final ProductModel product = resource.adaptTo(ProductModel.class);
        	String pxpItem = childResPath.substring(childResPath.lastIndexOf(PWConstants.SLASH));
        	if (pxpItem.contains("pxp") && Objects.nonNull(product)) {
        		setBean = checkSetBean(setBean, product, pxpItem.replace("/pxp", StringUtils.EMPTY));	
        	} else {
        		setBean = true;
        	}
        	if (setBean) {
        		bean.setAnchorId(anchorId);
                bean.setAnchorTitle(anchorTitle);
                anchorDetailList.add(bean);
        	}
        }
    }

	/**
	 * Check set bean.
	 *
	 * @param setBean the set bean
	 * @param product the product
	 * @param pxpItem the pxp item
	 * @return true, if successful
	 */
	private boolean checkSetBean(boolean setBean, final ProductModel product, String pxpItem) {
		switch(pxpItem) {
			case PWConstants.FEATURES:
				if (!product.getFeatures().isEmpty()) {
					setBean = true;
				}
				break;
			case PWConstants.OPTIONS:
				if (!product.getOptions().isEmpty()) {
					setBean = true;
				}
				break;
			case PWConstants.PACKAGE_TYPE:
				if (!product.getPackageTypeReferences().isEmpty()) {
					setBean = true;
				}
				break;
			case PWConstants.FILLING_MACHINE:
				if (!product.getFillingMachineReferences().isEmpty()) {
					setBean = true;
				}
				break;
			case "openings":
				if (!product.getOpeningClousers().isEmpty()) {
					setBean = true;
				}
				break;
			case "shapesandvolumes":
				if (!product.getShapes().isEmpty()) {
					setBean = true;
				}
				break;
			default: setBean = false; 
				break;
		}
		return setBean;
	}

    /**
     * Gets the anchor detail list.
     *
     * @return the anchor detail list
     */
    public List<AnchorBean> getAnchorDetailList() {
        return new ArrayList<AnchorBean>(anchorDetailList);
    }
}
