/*
 * 
 */
package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.beans.pxp.Category;
import com.tetrapak.publicweb.core.beans.pxp.FeatureOption;
import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;
import com.tetrapak.publicweb.core.beans.pxp.Openingclosure;
import com.tetrapak.publicweb.core.beans.pxp.Packagetype;
import com.tetrapak.publicweb.core.beans.pxp.Shape;
import com.tetrapak.publicweb.core.beans.pxp.Technology;
import com.tetrapak.publicweb.core.beans.pxp.TechnologyType;
import com.tetrapak.publicweb.core.beans.pxp.Video;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.utils.PageUtil;
import com.tetrapak.publicweb.core.utils.SearchMapHelper;

/**
 * The Class ProductModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductModel.class);

    /** The resource. */
    @Self
    protected Resource resource;

    /** The language resource. */
    private Resource languageResource;

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        Page currentPage = PageUtil.getCurrentPage(resource);
        if (currentPage != null) {
            Resource pageContentRes = currentPage.getContentResource();
            if (pageContentRes.getValueMap().containsKey(PWConstants.PRODUCT_ID)
                    && pageContentRes.getValueMap().containsKey(PWConstants.CQ_TEMPLATE)) {
                String template = pageContentRes.getValueMap().get(PWConstants.CQ_TEMPLATE).toString();
                String productId = pageContentRes.getValueMap().get(PWConstants.PRODUCT_ID).toString();
                String language = PageUtil.getLanguageCode(PageUtil.getLanguagePage(currentPage));
                String productLanguagePath = PWConstants.PXP_ROOT_PATH + PWConstants.SLASH + getTemplateType(template)
                        + PWConstants.SLASH + productId + PWConstants.SLASH + getLanguageCode(language);
                if (null != resource.getResourceResolver().getResource(productLanguagePath)) {
                    languageResource = resource.getResourceResolver().getResource(productLanguagePath);
                }
            }
        }
    }

    /**
     * Gets the template type.
     *
     * @param template
     *            the template
     * @return the template type
     */
    private String getTemplateType(String template) {
        String templateType = StringUtils.EMPTY;
        if (PWConstants.FILLING_MACHINE_TEMPLATE.equalsIgnoreCase(template)) {
            templateType = PWConstants.FILLING_MACHINE;
        }
        if (PWConstants.PROCESSING_EQUIP_TEMPLATE.equalsIgnoreCase(template)) {
            templateType = PWConstants.PROCESSING_EQUIPEMENT;
        }
        if (PWConstants.PACKAGE_TYPE_TEMPLATE.equalsIgnoreCase(template)) {
            templateType = PWConstants.PACKAGE_TYPE;
        }
        return templateType;
    }

    /**
     * Gets the language code.
     *
     * @param languageCode
     *            the language code
     * @return the language code
     */
    private String getLanguageCode(String languageCode) {
        String langCode = languageCode;
        switch (langCode) {
            case "pt":
                langCode = "pt-br";
                break;
            case "es":
                langCode = "es-xl";
                break;
            case "ja":
                langCode = "jp";
                break;
            case "zh":
                langCode = "cn";
                break;
            default:
                LOGGER.debug("Return the language code without manipulation, languageCode : {}", langCode);
        }
        return langCode;
    }

    /**
     * Gets the benifits.
     *
     * @return the benifits
     */
    public String getBenifits() {
        if (languageResource != null && languageResource.getValueMap().containsKey(PWConstants.BENEFITS)) {
            return languageResource.getValueMap().get(PWConstants.BENEFITS).toString();
        }
        return StringUtils.EMPTY;
    }

    /**
     * Gets the benefitsimage.
     *
     * @return the benefitsimage
     */
    public String getBenefitsimage() {
        if (languageResource != null && languageResource.getValueMap().containsKey(PWConstants.BENEFITS_IMAGE)) {
            return languageResource.getValueMap().get(PWConstants.BENEFITS_IMAGE).toString();
        }
        return StringUtils.EMPTY;
    }

    /**
     * Gets the header.
     *
     * @return the header
     */
    public String getHeader() {
        if (languageResource != null && languageResource.getValueMap().containsKey(PWConstants.HEADER)) {
            return languageResource.getValueMap().get(PWConstants.HEADER).toString();
        }
        return StringUtils.EMPTY;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        if (languageResource != null && languageResource.getValueMap().containsKey(PWConstants.NAME)) {
            return languageResource.getValueMap().get(PWConstants.NAME).toString();
        }
        return StringUtils.EMPTY;
    }

    /**
     * Gets the shapes.
     *
     * @return the shapes
     */
    public List<Shape> getShapes() {
        return getShapes(languageResource);
    }

    /**
     * Gets the shapes.
     *
     * @param rootResource
     *            the root resource
     * @return the shapes
     */
    private List<Shape> getShapes(Resource rootResource) {
        List<Shape> shapesList = new ArrayList<>();
        if (rootResource != null && rootResource.getChild("shapes") != null) {
            Iterator<Resource> shapesResource = rootResource.getChild("shapes").listChildren();
            while (shapesResource.hasNext()) {
                Shape shapeBean = new Shape();
                Resource shape = shapesResource.next();
                if (shape.getValueMap().containsKey(PWConstants.NAME)) {
                    shapeBean.setName(shape.getValueMap().get(PWConstants.NAME).toString());
                }
                if (shape.getValueMap().containsKey(PWConstants.THUMBNAIL)) {
                    shapeBean.setThumbnail(shape.getValueMap().get(PWConstants.THUMBNAIL).toString());
                }
                if (shape.getValueMap().containsKey(PWConstants.VOLUMES)) {
                    String[] volumes = (String[]) shape.getValueMap().get(PWConstants.VOLUMES);
                    shapeBean.setVolumes(Arrays.asList(volumes));
                }
                shapesList.add(shapeBean);
            }
        }
        return shapesList;
    }

    /**
     * Gets the features.
     *
     * @return the features
     */
    public List<FeatureOption> getFeatures() {
        List<FeatureOption> featuresList = new ArrayList<>();
        if (languageResource != null && languageResource.getChild(PWConstants.FEATURES) != null) {
            return getFeatuesOrOptionsList(featuresList, PWConstants.FEATURES);
        }
        return featuresList;
    }

    /**
     * Gets the options.
     *
     * @return the options
     */
    public List<FeatureOption> getOptions() {
        List<FeatureOption> optionsList = new ArrayList<>();
        if (languageResource != null && languageResource.getChild(PWConstants.OPTIONS) != null) {
            return getFeatuesOrOptionsList(optionsList, PWConstants.OPTIONS);
        }
        return optionsList;
    }

    /**
     * Gets the featues or options list.
     *
     * @param list
     *            the list
     * @param type
     *            the type
     * @return the featues or options list
     */
    private List<FeatureOption> getFeatuesOrOptionsList(List<FeatureOption> list, String type) {
        Iterator<Resource> childResources = languageResource.getChild(type).listChildren();
        while (childResources.hasNext()) {
            FeatureOption featuresBean = new FeatureOption();
            Resource childResource = childResources.next();
            if (childResource.getValueMap().containsKey(PWConstants.NAME)) {
                featuresBean.setName(childResource.getValueMap().get(PWConstants.NAME).toString());
            }
            if (childResource.getValueMap().containsKey(PWConstants.BODY)) {
                featuresBean.setBody(childResource.getValueMap().get(PWConstants.BODY).toString());
            }
            if (childResource.getValueMap().containsKey(PWConstants.HEADER)) {
                featuresBean.setHeader(childResource.getValueMap().get(PWConstants.HEADER).toString());
            }
            if (childResource.getValueMap().containsKey(PWConstants.IMAGE)) {
                featuresBean.setImage(childResource.getValueMap().get(PWConstants.IMAGE).toString());
            }
            if (childResource.getValueMap().containsKey(PWConstants.SRC)) {
                Video videoBean = new Video();
                videoBean.setSrc(childResource.getValueMap().get(PWConstants.SRC).toString());
                if (childResource.getValueMap().containsKey(PWConstants.POSTER)) {
                    videoBean.setPoster(childResource.getValueMap().get(PWConstants.POSTER).toString());
                }
                featuresBean.setVideo(videoBean);
            }
            list.add(featuresBean);
        }
        return list;
    }

    /**
     * Gets the opening clousers.
     *
     * @return the opening clousers
     */
    public List<Openingclosure> getOpeningClousers() {
        List<Openingclosure> openingClousersList = new ArrayList<>();
        if (languageResource != null && languageResource.getChild("openingclousers") != null) {
            Iterator<Resource> openingClouserResource = languageResource.getChild("openingclousers").listChildren();
            while (openingClouserResource.hasNext()) {
                Openingclosure openingClouserBean = new Openingclosure();
                Resource openingClouser = openingClouserResource.next();
                if (openingClouser.getValueMap().containsKey(PWConstants.NAME)) {
                    openingClouserBean.setName(openingClouser.getValueMap().get(PWConstants.NAME).toString());
                }
                if (openingClouser.getValueMap().containsKey(PWConstants.THUMBNAIL)) {
                    openingClouserBean.setThumbnail(openingClouser.getValueMap().get(PWConstants.THUMBNAIL).toString());
                }
                if (openingClouser.getValueMap().containsKey(PWConstants.ID)) {
                    openingClouserBean.setId(openingClouser.getValueMap().get(PWConstants.ID).toString());
                }
                if (openingClouser.getValueMap().containsKey("principle")) {
                    openingClouserBean.setPrinciple(openingClouser.getValueMap().get("principle").toString());
                }
                if (openingClouser.getValueMap().containsKey("type")) {
                    openingClouserBean.setType(openingClouser.getValueMap().get("type").toString());
                }
                if (openingClouser.getValueMap().containsKey(PWConstants.BENEFITS)) {
                    String[] benefits = (String[]) openingClouser.getValueMap().get(PWConstants.BENEFITS);
                    openingClouserBean.setBenefits(Arrays.asList(benefits));
                }
                openingClousersList.add(openingClouserBean);
            }
        }
        return openingClousersList;
    }

    /**
     * Gets the filling machine references.
     *
     * @return the filling machine references
     */
    public List<FillingMachine> getFillingMachineReferences() {
        List<FillingMachine> fillingMachineList = new ArrayList<>();
        if (languageResource != null && languageResource.getChild("fillingmachines") != null) {
            Iterator<Resource> fillingmachines = languageResource.getChild("fillingmachines").listChildren();
            while (fillingmachines.hasNext()) {
                Resource fillingMachine = fillingmachines.next();
                FillingMachine fillingMachineBean = new FillingMachine();
                if (fillingMachine.getValueMap().containsKey(PWConstants.HEADER)) {
                    fillingMachineBean.setHeader(fillingMachine.getValueMap().get(PWConstants.HEADER).toString());
                }
                if (fillingMachine.getValueMap().containsKey(PWConstants.ID)) {
                    fillingMachineBean.setId(fillingMachine.getValueMap().get(PWConstants.ID).toString());
                }
                if (fillingMachine.getValueMap().containsKey(PWConstants.NAME)) {
                    fillingMachineBean.setName(fillingMachine.getValueMap().get(PWConstants.NAME).toString());
                }
                if (fillingMachine.getValueMap().containsKey(PWConstants.THUMBNAIL)) {
                    fillingMachineBean.setThumbnail(fillingMachine.getValueMap().get(PWConstants.THUMBNAIL).toString());
                }
                fillingMachineList.add(fillingMachineBean);
            }
        }
        return fillingMachineList;
    }

    /**
     * Gets the package type references.
     *
     * @return the package type references
     */
    public List<Packagetype> getPackageTypeReferences() {
        List<Packagetype> packageTypeList = new ArrayList<>();
        if (languageResource != null && languageResource.getChild("packagetypes") != null) {
            Iterator<Resource> packageTypes = languageResource.getChild("packagetypes").listChildren();
            while (packageTypes.hasNext()) {
                Resource packageType = packageTypes.next();
                Packagetype packageTypeBean = new Packagetype();
                if (packageType.getValueMap().containsKey(PWConstants.ID)) {
                    packageTypeBean.setId(packageType.getValueMap().get(PWConstants.ID).toString());
                }
                if (packageType.getValueMap().containsKey(PWConstants.NAME)) {
                    packageTypeBean.setName(packageType.getValueMap().get(PWConstants.NAME).toString());
                }
                packageTypeBean.setShapes(getShapes(packageType));
                packageTypeList.add(packageTypeBean);
            }
        }
        return packageTypeList;
    }

    /**
     * Gets the technology type.
     *
     * @return the technology type
     */
    public TechnologyType getTechnologyType() {
        TechnologyType technologyType = null;
        if (languageResource != null && languageResource.getChild("technologytype") != null) {
            technologyType = new TechnologyType();
            Resource technologyTypeRes = languageResource.getChild("technologytype");
            technologyType.setName(technologyTypeRes.getValueMap().get(PWConstants.NAME).toString());
            technologyType.setId(technologyTypeRes.getValueMap().get(PWConstants.ID).toString());
        }
        return technologyType;
    }

    /**
     * Gets the categories.
     *
     * @return the categories
     */
    public List<Category> getCategories() {
        List<Category> categoriesList = new ArrayList<>();
        if (languageResource != null && languageResource.getChild("categories") != null) {
            Iterator<Resource> categories = languageResource.getChild("categories").listChildren();
            while (categories.hasNext()) {
                Resource category = categories.next();
                Category categoryBean = new Category();
                if (category.getValueMap().containsKey(PWConstants.ID)) {
                    categoryBean.setId(category.getValueMap().get(PWConstants.ID).toString());
                }
                if (category.getValueMap().containsKey(PWConstants.NAME)) {
                    categoryBean.setName(category.getValueMap().get(PWConstants.NAME).toString());
                }
                categoriesList.add(categoryBean);
            }
        }
        return categoriesList;
    }

    /**
     * Gets the technology.
     *
     * @return the technology
     */
    public Technology getTechnology() {
        Technology technology = null;
        if (languageResource != null && languageResource.getChild("technology") != null) {
            technology = new Technology();
            Resource technologyRes = languageResource.getChild("technology");
            technology.setName(technologyRes.getValueMap().get(PWConstants.NAME).toString());
            technology.setId(technologyRes.getValueMap().get(PWConstants.ID).toString());
        }
        return technology;
    }

}
