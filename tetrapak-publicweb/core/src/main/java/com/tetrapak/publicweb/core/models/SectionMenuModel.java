package com.tetrapak.publicweb.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.beans.PseudoCategoriesSectionBean;
import com.tetrapak.publicweb.core.beans.SectionMenuBean;
import com.tetrapak.publicweb.core.beans.SubSectionBean;
import com.tetrapak.publicweb.core.beans.SubSectionMenuBean;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.PseudoCategoryService;
import com.tetrapak.publicweb.core.utils.LinkUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * The Class SectionMenuModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SectionMenuModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SectionMenuModel.class);

    /** The current page. */
    @ScriptVariable
    private Page currentPage;

    /** The page manager. */
    @ScriptVariable
    private PageManager pageManager;

    /** The resource resolver. */
    @SlingObject
    private ResourceResolver resourceResolver;

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The pseudo category service. */
    @Inject
    private PseudoCategoryService pseudoCategoryService;

    /** The section menu. */
    private final List<SectionMenuBean> sectionMenu = new ArrayList<>();

    /** The solution page. */
    private String solutionPagePath;

    /** The Constant EN_PAGE. */
    private static final String EN_PAGE = "/content/tetrapak/public-web/lang-masters/en";

    /**
     * The init method.
     */
    @PostConstruct
    protected void init() {
        LOGGER.debug("inside init method");
        // Populate Solutions page path
        this.solutionPagePath = StringUtils.substringBeforeLast(fetchSolutionPagePath(), ".html");
        // Fetch absolute parent page
        final Page page = fetchAbsoluteParent();
        // Populate section menu
        populateSectionMenu(page);
    }

    /**
     * Fetch solution page path.
     *
     * @return the string
     */
    private String fetchSolutionPagePath() {
        String solutionPagePath = StringUtils.EMPTY;
        final String rootPath = LinkUtils.getRootPath(request.getPathInfo());
        final String path = rootPath + "/jcr:content/root/responsivegrid/headerconfiguration";
        final Resource headerConfigurationResource = request.getResourceResolver().getResource(path);
        if (Objects.nonNull(headerConfigurationResource)) {
            final HeaderConfigurationModel configurationModel = headerConfigurationResource
                    .adaptTo(HeaderConfigurationModel.class);
            if (Objects.nonNull(configurationModel)) {
                solutionPagePath = configurationModel.getSolutionPage();
            }
        }
        return solutionPagePath;
    }

    /**
     * Fetch absolute parent.
     *
     * @return the page
     */
    private Page fetchAbsoluteParent() {
        Page page;
        if (currentPage.getPath().contains(solutionPagePath)) {
            page = currentPage.getAbsoluteParent(6);
        } else {
            page = currentPage.getAbsoluteParent(5);
        }
        return page;
    }

    /**
     * Populate section menu.
     *
     * @param page
     *            the page
     */
    private void populateSectionMenu(final Page page) {
        final Iterator<Page> pages = page.listChildren();
        while (pages.hasNext()) {
            final Page nextPage = pages.next();
            final SectionMenuBean sectionMenuBean = new SectionMenuBean();
            sectionMenuBean.setLinkText(nextPage.getTitle());
            sectionMenuBean.setLinkPath(LinkUtils.sanitizeLink(nextPage.getPath()));
            sectionMenuBean.setExternal(false);
            sectionMenuBean.setSubSectionMenu(populateSubSectionMenu(nextPage));
            sectionMenu.add(sectionMenuBean);
        }
    }

    /**
     * Populate sub section menu.
     *
     * @param page
     *            the page
     * @return the sub section menu bean
     */
    private SubSectionMenuBean populateSubSectionMenu(final Page page) {
        final Map<String, List<String>> pseudoCategoryMap = new LinkedHashMap<>();
        final List<SubSectionBean> subSections = new ArrayList<>();

        final Iterator<Page> pages = page.listChildren();
        while (pages.hasNext()) {
            final Page nextPage = pages.next();
            populatePseudoCategoryMap(pseudoCategoryMap, nextPage);
            subSections.add(populateSubSection(nextPage));
        }

        final SubSectionMenuBean subSectionMenuBean = new SubSectionMenuBean();
        if (page.getPath().contains(solutionPagePath)) {
            subSectionMenuBean.setPseudoCategoriesSection(populatePseudoSection(pseudoCategoryMap));
        } else {
            subSectionMenuBean.setSubSections(subSections);
            subSectionMenuBean.setSubSectionCount(subSections.size());
        }
        return subSectionMenuBean;
    }

    /**
     * Populate pseudo category map.
     *
     * @param pseudoCategoryMap
     *            the pseudo category map
     * @param nextPage
     *            the next page
     */
    private void populatePseudoCategoryMap(final Map<String, List<String>> pseudoCategoryMap, final Page nextPage) {
        final String pseudoCategory = getPseudoCategory(nextPage);
        List<String> pageList = new ArrayList<>();
        if (StringUtils.isNotBlank(pseudoCategory)) {
            if (!pseudoCategoryMap.containsKey(pseudoCategory)) {
                pageList.add(nextPage.getPath());
            } else {
                pageList = pseudoCategoryMap.get(pseudoCategory);
                pageList.add(nextPage.getPath());
            }
            pseudoCategoryMap.put(pseudoCategory, pageList);
        }
    }

    /**
     * Gets the pseudo category.
     *
     * @param nextPage
     *            the next page
     * @return the pseudo category
     */
    private String getPseudoCategory(final Page nextPage) {
        // final Resource resource = resourceResolver.getResource(nextPage.getPath() + PWConstants.SLASH + JcrConstants.JCR_CONTENT);
        final Resource resource = nextPage.getContentResource();
        final ValueMap properties = resource.getValueMap();
        final String pseudoCategory = properties.get("pseudoCategory", StringUtils.EMPTY);
        return pseudoCategory;
    }

    /**
     * Populate pseudo section.
     *
     * @param pseudoCategoryMap
     *            the pseudo category map
     * @return the list
     */
    private List<PseudoCategoriesSectionBean> populatePseudoSection(final Map<String, List<String>> pseudoCategoryMap) {
        final List<PseudoCategoriesSectionBean> pseudoSection = new ArrayList<>();

        for (final Entry<String, List<String>> entrySet : pseudoCategoryMap.entrySet()) {
            final PseudoCategoriesSectionBean pseudoCategoriesSectionBean = new PseudoCategoriesSectionBean();
            pseudoCategoriesSectionBean.setTitle(entrySet.getKey());

            final List<SubSectionBean> subSectionList = getSubSectionList(entrySet.getValue());
            pseudoCategoriesSectionBean.setSubsections(subSectionList);
            pseudoCategoriesSectionBean.setSubSectionCount(subSectionList.size());
            pseudoSection.add(pseudoCategoriesSectionBean);
        }
        return pseudoSection;
    }

    /**
     * Gets the sub section bean.
     *
     * @param pathList
     *            the path list
     * @return the sub section bean
     */
    private List<SubSectionBean> getSubSectionList(final List<String> pathList) {
        final List<SubSectionBean> subSections = new ArrayList<>();
        for (final String path : pathList) {
            final Page page = pageManager.getPage(path);
            subSections.add(populateSubSection(page));
        }
        return subSections;
    }

    /**
     * Populate sub section.
     *
     * @param page
     *            the page
     * @return the sub section bean
     */
    private SubSectionBean populateSubSection(final Page page) {
        final SubSectionBean subSectionBean = new SubSectionBean();
        subSectionBean.setLinkText(page.getTitle());
        subSectionBean.setLinkPath(LinkUtils.sanitizeLink(page.getPath()));
        subSectionBean.setExternal(false);
        return subSectionBean;
    }

    /**
     * Gets the page hierarchy.
     *
     * @return the page hierarchy
     */
    public String getPageHierarchy() {
        final StringBuilder builder = new StringBuilder();
        final String path = currentPage.getPath();
        if (path.contains(EN_PAGE)) {
            final String activeHierarchy = StringUtils.substringAfter(path, EN_PAGE + PWConstants.SLASH);
            final String[] activeHierarchyArray = activeHierarchy.split(PWConstants.SLASH);
            for (int i = 0; i < activeHierarchyArray.length; i++) {
                builder.append("l" + (i + 1) + "=");
                builder.append(activeHierarchyArray[i]);
                if (i < activeHierarchyArray.length - 1) {
                    builder.append("&");
                }
            }
        }
        return builder.toString();
    }

    /**
     * Gets the section menu list.
     *
     * @return the section menu list
     */
    public List<SectionMenuBean> getSectionMenu() {
        return sectionMenu;
    }

    /**
     * Gets the pseudo categories.
     *
     * @return the pseudo categories
     */
    public Map<String, String> getPseudoCategories() {
        return pseudoCategoryService.fetchPseudoCategories(resourceResolver);
    }
}
