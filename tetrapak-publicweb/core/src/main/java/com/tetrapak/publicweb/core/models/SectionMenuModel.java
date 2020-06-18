package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.beans.ExternalTemplateBean;
import com.tetrapak.publicweb.core.beans.PseudoCategoriesSectionBean;
import com.tetrapak.publicweb.core.beans.PseudoCategoryCFBean;
import com.tetrapak.publicweb.core.beans.SectionMenuBean;
import com.tetrapak.publicweb.core.beans.SubSectionBean;
import com.tetrapak.publicweb.core.beans.SubSectionMenuBean;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.PseudoCategoryService;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import com.tetrapak.publicweb.core.utils.NavigationUtil;

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

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The pseudo category service. */
    @Inject
    private PseudoCategoryService pseudoCategoryService;

    /** The solution page title. */
    private String solutionPageTitle;

    /** The section home page title. */
    private String sectionHomePageTitle;

    /** The section home page path. */
    private String sectionHomePagePath;

    /** The section menu. */
    private final List<SectionMenuBean> sectionMenu = new ArrayList<>();

    /** The page title map. */
    private final Map<String, String> pageTitleMap = new HashMap<>();

    /**
     * The init method.
     */
    @PostConstruct
    protected void init() {
        LOGGER.debug("inside init method");
        // Populate Solutions page path
        final String solutionPage = NavigationUtil.fetchSolutionPagePath(request);
        // Substring path before extension
        final String solutionPagePath = NavigationUtil.getSolutionPageWithoutExtension(solutionPage);
        // Set solution page title
        this.solutionPageTitle = NavigationUtil.getSolutionPageTitle(request, solutionPage);
        // Fetch absolute parent page
        final Page page = fetchAbsoluteParent(solutionPagePath);
        // Set section menu home page title
        setSectionHomePageTitle(page);
        // Set section menu home page url
        setSectionHomePagePath(page);
        // Set page hierarchy
        setPageHierarchy(currentPage);
        // Populate section menu
        populateSectionMenu(page, solutionPagePath, pseudoCategoryService, request.getResourceResolver());
    }

    /**
     * Fetch absolute parent.
     *
     * @param solutionPagePath the solution page path
     * @return the page
     */
    private Page fetchAbsoluteParent(final String solutionPagePath) {
        Page page;
        if (currentPage.getPath().contains(solutionPagePath)) {
            page = currentPage.getAbsoluteParent(PWConstants.SOLUTIONS_SECTION_MENU_PAGE_LEVEL);
        } else {
            page = currentPage.getAbsoluteParent(PWConstants.OTHERS_SECTION_MENU_PAGE_LEVEL);
        }
        return page;
    }

    /**
     * Populate section menu. This is called from HeaderModel as well, so public.
     *
     * @param page
     *            the page
     * @param path
     *            the path
     * @param pseudoCategoryService
     * @param resourceResolver
     */
    public void populateSectionMenu(final Page page, final String path,
            final PseudoCategoryService pseudoCategoryService, final ResourceResolver resourceResolver) {
        final Iterator<Page> pages = page.listChildren();
        while (pages.hasNext()) {
            final Page nextPage = pages.next();
            if (!nextPage.isHideInNav()) {
                final SectionMenuBean sectionMenuBean = new SectionMenuBean();
                sectionMenuBean.setLinkText(nextPage.getTitle());
                // Set external page url
                final ExternalTemplateBean externalTemplate = checkExternalTemplate(nextPage);
                if (externalTemplate.isExternal()) {
                    sectionMenuBean.setExternal(true);
                    sectionMenuBean.setLinkPath(externalTemplate.getExternalUrl());
                } else if (!nextPage.getContentResource().getValueMap().containsKey("disableClickInNavigation")) {
                    sectionMenuBean.setExternal(false);
                    sectionMenuBean.setLinkPath(LinkUtils.sanitizeLink(nextPage.getPath()));
                }
                sectionMenuBean.setSubSectionMenu(
                        populateSubSectionMenu(nextPage, path, pseudoCategoryService, resourceResolver));
                sectionMenu.add(sectionMenuBean);
            }
        }
    }

    /**
     * Check external template.
     *
     * @param page the page
     * @return the external template
     */
    private ExternalTemplateBean checkExternalTemplate(final Page page) {
        final ExternalTemplateBean externalTemplate = new ExternalTemplateBean();
        final ValueMap properties = page.getProperties();
        if (properties.containsKey(PWConstants.CQ_TEMPLATE)) {
            final String template = properties.get(PWConstants.CQ_TEMPLATE, StringUtils.EMPTY);
            if (PWConstants.EXTERNAL_REDIRECT_TEMPLATE.equalsIgnoreCase(template)) {
                externalTemplate.setExternal(true);
                externalTemplate.setExternalUrl(properties.get(PWConstants.CQ_REDIRECT_TARGET, StringUtils.EMPTY));
            }
        }
        return externalTemplate;
    }

    /**
     * Populate sub section menu.
     *
     * @param page
     *            the page
     * @param path
     *            the path
     * @param pseudoCategoryService
     * @param resourceResolver
     * @return the sub section menu bean
     */
    private SubSectionMenuBean populateSubSectionMenu(final Page page, final String path,
            final PseudoCategoryService pseudoCategoryService, final ResourceResolver resourceResolver) {
        final Map<String, List<String>> pseudoCategoryMap = new LinkedHashMap<>();
        final List<SubSectionBean> subSections = new ArrayList<>();

        sortMap(pseudoCategoryMap, pseudoCategoryService, resourceResolver);

        final Iterator<Page> pages = page.listChildren();
        while (pages.hasNext()) {
            final Page nextPage = pages.next();
            if (!nextPage.isHideInNav()) {
                populatePseudoCategoryMap(pseudoCategoryMap, nextPage);
                subSections.add(populateSubSection(nextPage));
            }
        }

        final SubSectionMenuBean subSectionMenuBean = new SubSectionMenuBean();
        if (page.getPath().contains(path)) {
            if (isPseudoCategoryMapEmpty(pseudoCategoryMap)) {
                subSectionMenuBean.setSubSections(subSections);
                subSectionMenuBean.setSubSectionCount(subSections.size());
            } else {
                final List<PseudoCategoriesSectionBean> pseudoSection = populatePseudoSection(pseudoCategoryMap);
                subSectionMenuBean.setPseudoCategoriesSection(pseudoSection);
            }
        } else {
            subSectionMenuBean.setSubSections(subSections);
            subSectionMenuBean.setSubSectionCount(subSections.size());
        }
        return subSectionMenuBean;
    }

    /**
     * Checks if pseudo category map is empty.
     *
     * @param pseudoCategoryMap the pseudo category map
     * @return true, if is pseudo category map empty
     */
    private boolean isPseudoCategoryMapEmpty(final Map<String, List<String>> pseudoCategoryMap) {
        boolean isEmpty = false;
        for (final Entry<String, List<String>> entrySet : pseudoCategoryMap.entrySet()) {
            if (entrySet.getValue().isEmpty()) {
                isEmpty = true;
            } else {
                isEmpty = false;
                break;
            }
        }
        return isEmpty;
    }

    /**
     * Sort map.
     *
     * @param pseudoCategoryMap the pseudo category map
     * @param pseudoCategoryService the pseudo category service
     * @param resourceResolver the resource resolver
     */
    private void sortMap(final Map<String, List<String>> pseudoCategoryMap,
            final PseudoCategoryService pseudoCategoryService, final ResourceResolver resourceResolver) {
        final List<PseudoCategoryCFBean> pseudoCategories = pseudoCategoryService.fetchPseudoCategories(resourceResolver);
        pseudoCategories.sort((final PseudoCategoryCFBean cf1,
                final PseudoCategoryCFBean cf2) -> cf1.getPseudoCategoryOrder() - cf2.getPseudoCategoryOrder());
        for (final PseudoCategoryCFBean bean : pseudoCategories) {
            pseudoCategoryMap.put(bean.getPseudoCategoryKey(), bean.getPageList());
        }
    }

    /**
     * Populate pseudo category map.
     *
     * @param pseudoCategoryMap the pseudo category map
     * @param nextPage the next page
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
     * @param nextPage the next page
     * @return the pseudo category
     */
    private String getPseudoCategory(final Page nextPage) {
        final ValueMap properties = nextPage.getProperties();
        return properties.get("pseudoCategory", StringUtils.EMPTY);
    }

    /**
     * Populate pseudo section.
     *
     * @param pseudoCategoryMap the pseudo category map
     * @return the list
     */
    private List<PseudoCategoriesSectionBean> populatePseudoSection(final Map<String, List<String>> pseudoCategoryMap) {
        final List<PseudoCategoriesSectionBean> pseudoSection = new ArrayList<>();

        for (final Entry<String, List<String>> entrySet : pseudoCategoryMap.entrySet()) {
            if (CollectionUtils.isNotEmpty(entrySet.getValue())) {
                final PseudoCategoriesSectionBean pseudoCategoriesSectionBean = new PseudoCategoriesSectionBean();
                pseudoCategoriesSectionBean.setTitle(entrySet.getKey());

                final List<SubSectionBean> subSectionList = getSubSectionList(entrySet.getValue());
                pseudoCategoriesSectionBean.setSubSections(subSectionList);
                pseudoCategoriesSectionBean.setSubSectionCount(subSectionList.size());
                pseudoSection.add(pseudoCategoriesSectionBean);
            }
        }
        return pseudoSection;
    }

    /**
     * Gets the sub section bean.
     *
     * @param pathList the path list
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
     * @param page the page
     * @return the sub section bean
     */
    private SubSectionBean populateSubSection(final Page page) {
        final SubSectionBean subSectionBean = new SubSectionBean();
        subSectionBean.setLinkText(page.getTitle());

        final ExternalTemplateBean externalTemplate = checkExternalTemplate(page);
        if (externalTemplate.isExternal()) {
            subSectionBean.setExternal(true);
            subSectionBean.setLinkPath(externalTemplate.getExternalUrl());
        } else {
            subSectionBean.setExternal(false);
            subSectionBean.setLinkPath(LinkUtils.sanitizeLink(page.getPath()));
        }

        return subSectionBean;
    }

    /**
     * Gets the page hierarchy.
     *
     * @return the page hierarchy
     */
    private void setPageHierarchy(final Page page) {
        final String path = page.getPath();
        final String languagePagePath = LinkUtils.getRootPath(request.getPathInfo());
        if (path.contains(languagePagePath)) {
            final String activeHierarchy = StringUtils.substringAfter(path, languagePagePath + PWConstants.SLASH);
            final String[] activeHierarchyArray = activeHierarchy.split(PWConstants.SLASH);
            StringBuilder pagePath = new StringBuilder(languagePagePath);
            for (int i = 0; i < activeHierarchyArray.length; i++) {
                pagePath = pagePath.append(PWConstants.SLASH + activeHierarchyArray[i]);
                final Page nextPage = pageManager.getPage(pagePath.toString());
                pageTitleMap.put("l" + (i + 1), nextPage.getTitle());
            }
        }
    }

    /**
     * Gets the page hierarchy.
     *
     * @return the page hierarchy
     */
    public Map<String, String> getPageHierarchy() {
        return pageTitleMap;
    }

    /**
     * Sets the section home page title.
     *
     * @param page the new section home page title
     */
    public void setSectionHomePageTitle(final Page page) {
        final ValueMap properties = page.getProperties();
        sectionHomePageTitle = properties.get("sectionHomePageTitle", StringUtils.EMPTY);
    }

    /**
     * Gets the section home page title.
     *
     * @return the section home page title
     */
    public String getSectionHomePageTitle() {
        return sectionHomePageTitle;
    }

    /**
     * Sets the section home page path.
     *
     * @param page the new section home page path
     */
    public void setSectionHomePagePath(final Page page) {
        sectionHomePagePath = LinkUtils.sanitizeLink(page.getPath());
    }

    /**
     * Gets the section home page path.
     *
     * @return the section home page path
     */
    public String getSectionHomePagePath() {
        return sectionHomePagePath;
    }

    /**
     * Gets the section menu list.
     *
     * @return the section menu list
     */
    public List<SectionMenuBean> getSectionMenu() {
        return new ArrayList<>(sectionMenu);
    }

    /**
     * Gets the solution page title.
     *
     * @return the solution page title
     */
    public String getSolutionPageTitle() {
        return solutionPageTitle;
    }
}
