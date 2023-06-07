package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
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
import com.tetrapak.publicweb.core.beans.SectionMenuBean;
import com.tetrapak.publicweb.core.beans.SubSectionBean;
import com.tetrapak.publicweb.core.beans.SubSectionMenuBean;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.models.multifield.PseudoCategoryModel;
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

    /** The Constant MOBILE_OVERVIEW_LABEL. */
    private static final String MOBILE_OVERVIEW_LABEL = "mobileOverviewLabel";

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
        setSectionHomePagePath(page, request);
        // Set page hierarchy
        setPageHierarchy(currentPage);
        // Populate section menu
        populateSectionMenu(NavigationUtil.getMegaMenuConfigurationModel(request, request.getPathInfo()), page,
                solutionPagePath, request);
    }

    /**
     * Fetch absolute parent.
     *
     * @param solutionPagePath
     *            the solution page path
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
     * @param megaMenuConfigurationModel
     *            the mega menu configuration model
     * @param page
     *            the page
     * @param path
     *            the path
     * @param resourceResolver
     *            the resource resolver
     */
    public void populateSectionMenu(final MegaMenuConfigurationModel megaMenuConfigurationModel, final Page page,
            final String path, final SlingHttpServletRequest request) {
        final Iterator<Page> pages = page.listChildren();
        while (pages.hasNext()) {
            final Page nextPage = pages.next();
            final SectionMenuBean sectionMenuBean = new SectionMenuBean();
            if (nextPage.getContentResource() != null  && !nextPage.isHideInNav()) {               
                sectionMenuBean.setLinkText(NavigationUtil.getNavigationTitle(nextPage));
                if(request.getPathInfo().equalsIgnoreCase(nextPage.getPath()+PWConstants.HTML) 
                        || request.getPathInfo().equalsIgnoreCase(nextPage.getPath())
                        || request.getPathInfo().equalsIgnoreCase(nextPage.getPath()+PWConstants.SLASH)) {
                    sectionMenuBean.setHighlighted(true);
                }
                // Set external page url
                final ExternalTemplateBean externalTemplate = checkExternalTemplate(nextPage);
                processExternalTemplateData(request, nextPage, sectionMenuBean, externalTemplate);
                sectionMenuBean.setSubSectionMenu(
                        populateSubSectionMenu(megaMenuConfigurationModel, nextPage, path, request));
                sectionMenu.add(sectionMenuBean);
            }
        }
    }

    /**
     * Process external template data.
     *
     * @param request
     *            the request
     * @param nextPage
     *            the next page
     * @param sectionMenuBean
     *            the section menu bean
     * @param externalTemplate
     *            the external template
     */
    private void processExternalTemplateData(final SlingHttpServletRequest request, final Page nextPage,
            final SectionMenuBean sectionMenuBean, final ExternalTemplateBean externalTemplate) {
        if (externalTemplate.isExternal()) {
            sectionMenuBean.setExternal(true);
            sectionMenuBean.setLinkPath(externalTemplate.getExternalUrl());
        } else if (!nextPage.getContentResource().getValueMap().containsKey("disableClickInNavigation")) {
            sectionMenuBean.setExternal(false);
            sectionMenuBean.setLinkPath(LinkUtils.sanitizeLink(nextPage.getPath(), request));
            final ValueMap valueMap = nextPage.getProperties();
            if (Objects.nonNull(valueMap)
                    && StringUtils.isNotBlank(valueMap.get(MOBILE_OVERVIEW_LABEL, StringUtils.EMPTY))) {
                sectionMenuBean.setMobileOverviewLabel(valueMap.get(MOBILE_OVERVIEW_LABEL, StringUtils.EMPTY));
            }
        }
    }
    

    /**
     * Check external template.
     *
     * @param page
     *            the page
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
     * @param megaMenuConfigurationModel
     *            the mega menu configuration model
     * @param page
     *            the page
     * @param path
     *            the path
     * @param resourceResolver
     *            the resource resolver
     * @return the sub section menu bean
     */
    private SubSectionMenuBean populateSubSectionMenu(final MegaMenuConfigurationModel megaMenuConfigurationModel,
            final Page page, final String path, final SlingHttpServletRequest request) {
        final Map<String, List<String>> pseudoCategoryMap = new LinkedHashMap<>();
        final List<SubSectionBean> subSections = new ArrayList<>();
        final List<PseudoCategoryModel> pseudoCategories = megaMenuConfigurationModel.getPseudoCategoryList();
        final Map<String, String> pseudoConfigMap = new HashMap<>();
        if (pseudoCategories != null && !pseudoCategories.isEmpty()) {
            for (final PseudoCategoryModel pseudoCategory : pseudoCategories) {
                pseudoConfigMap.put(pseudoCategory.getPseudoCategoryKey(), pseudoCategory.getPseudoCategoryValue());
                pseudoCategoryMap.put(pseudoCategory.getPseudoCategoryKey(), new ArrayList<String>());
            }
        }

        final Iterator<Page> pages = page.listChildren();
        while (pages.hasNext()) {
            final Page nextPage = pages.next();
            if (!nextPage.isHideInNav()) {
                populatePseudoCategoryMap(pseudoCategoryMap, nextPage);
                subSections.add(populateSubSection(nextPage, request));
            }
        }

        final SubSectionMenuBean subSectionMenuBean = new SubSectionMenuBean();
        if (page.getPath().contains(path)) {
            if (isPseudoCategoryMapEmpty(pseudoCategoryMap)) {
                subSectionMenuBean.setSubSections(subSections);
                subSectionMenuBean.setSubSectionCount(subSections.size());
                populateLimit(subSections.size(), subSectionMenuBean);
            } else {
                final List<PseudoCategoriesSectionBean> pseudoSection = populatePseudoSection(pseudoConfigMap,
                        pseudoCategoryMap, request);
                subSectionMenuBean.setPseudoCategoriesSection(pseudoSection);
            }
        } else {
            subSectionMenuBean.setSubSections(subSections);
            subSectionMenuBean.setSubSectionCount(subSections.size());
            populateLimit(subSections.size(), subSectionMenuBean);
        }
        return subSectionMenuBean;
    }

    /**
     * Populate limit.
     *
     * @param count
     *            the count
     * @param subSectionMenuBean
     *            the sub section menu bean
     */
    private void populateLimit(final int count, final SubSectionMenuBean subSectionMenuBean) {
        FlyoutLimit flyoutLimit;
        if (count <= 24) {
            flyoutLimit = limitTill24(count);
        } else {
            flyoutLimit = limitAfter24(count);
        }
        subSectionMenuBean.setCol1Limit(flyoutLimit.getCol1Limit());
        subSectionMenuBean.setCol2Limit(flyoutLimit.getCol2Limit());
        subSectionMenuBean.setCol3Limit(flyoutLimit.getCol3Limit());
        subSectionMenuBean.setCol4Limit(flyoutLimit.getCol4Limit());
    }

    /**
     * Limit after 24.
     *
     * @param count
     *            the count
     * @return the flyout limit
     */
    private FlyoutLimit limitAfter24(final int count) {
        int col1Limit = 0;
        int col2Limit = 0;
        int col3Limit = 0;
        int col4Limit = 0;
        final int columnCount = count / 4;
        switch (count % 4) {
            case 3:
                // 27, 31, 35..
                col1Limit = columnCount + 1;
                col2Limit = col1Limit + columnCount + 1;
                col3Limit = col2Limit + columnCount + 1;
                col4Limit = count;
                break;
            case 2:
                // 26, 30, 34..
                col1Limit = columnCount + 1;
                col2Limit = col1Limit + columnCount + 1;
                col3Limit = col2Limit + columnCount;
                col4Limit = count;
                break;
            case 1:
                // 25, 29, 33..
                col1Limit = columnCount + 1;
                col2Limit = col1Limit + columnCount;
                col3Limit = col2Limit + columnCount;
                col4Limit = count;
                break;
            default:
                // 28, 32, 36...
                col1Limit = columnCount;
                col2Limit = col1Limit + columnCount;
                col3Limit = col2Limit + columnCount;
                col4Limit = count;
                break;
        }
        return populateFlyoutLimit(col1Limit, col2Limit, col3Limit, col4Limit);
    }

    /**
     * Limit till 24.
     *
     * @param count
     *            the count
     * @return the flyout limit
     */
    private FlyoutLimit limitTill24(final int count) {
        int col1Limit = 0;
        int col2Limit = 0;
        int col3Limit = 0;
        int col4Limit = 0;
        switch (count / 6) {
            case 4:
            case 3:
                // 18-24
                col1Limit = 6;
                col2Limit = col1Limit + 6;
                col3Limit = col2Limit + 6;
                col4Limit = count;
                break;
            case 2:
                // 12-17
                col1Limit = 6;
                col2Limit = col1Limit + 6;
                col3Limit = count;
                break;
            case 1:
                // 6-11
                col1Limit = 6;
                col2Limit = count;
                break;
            default:
                // 0-5
                col1Limit = count;
                break;
        }
        return populateFlyoutLimit(col1Limit, col2Limit, col3Limit, col4Limit);
    }

    /**
     * Populate flyout limit.
     *
     * @param col1Limit
     *            the col 1 limit
     * @param col2Limit
     *            the col 2 limit
     * @param col3Limit
     *            the col 3 limit
     * @param col4Limit
     *            the col 4 limit
     * @return the flyout limit
     */
    private FlyoutLimit populateFlyoutLimit(final int col1Limit, final int col2Limit, final int col3Limit,
            final int col4Limit) {
        final FlyoutLimit limit = new FlyoutLimit();
        limit.setCol1Limit(col1Limit);
        limit.setCol2Limit(col2Limit);
        limit.setCol3Limit(col3Limit);
        limit.setCol4Limit(col4Limit);
        return limit;
    }

    /**
     * The Class FlyoutLimit.
     */
    class FlyoutLimit {

        /** The col 1 limit. */
        private int col1Limit;

        /** The col 2 limit. */
        private int col2Limit;

        /** The col 3 limit. */
        private int col3Limit;

        /** The col 4 limit. */
        private int col4Limit;

        /**
         * Gets the col 1 limit.
         *
         * @return the col 1 limit
         */
        public int getCol1Limit() {
            return col1Limit;
        }

        /**
         * Sets the col 1 limit.
         *
         * @param col1Limit
         *            the new col 1 limit
         */
        public void setCol1Limit(final int col1Limit) {
            this.col1Limit = col1Limit;
        }

        /**
         * Gets the col 2 limit.
         *
         * @return the col 2 limit
         */
        public int getCol2Limit() {
            return col2Limit;
        }

        /**
         * Sets the col 2 limit.
         *
         * @param col2Limit
         *            the new col 2 limit
         */
        public void setCol2Limit(final int col2Limit) {
            this.col2Limit = col2Limit;
        }

        /**
         * Gets the col 3 limit.
         *
         * @return the col 3 limit
         */
        public int getCol3Limit() {
            return col3Limit;
        }

        /**
         * Sets the col 3 limit.
         *
         * @param col3Limit
         *            the new col 3 limit
         */
        public void setCol3Limit(final int col3Limit) {
            this.col3Limit = col3Limit;
        }

        /**
         * Gets the col 4 limit.
         *
         * @return the col 4 limit
         */
        public int getCol4Limit() {
            return col4Limit;
        }

        /**
         * Sets the col 4 limit.
         *
         * @param col4Limit
         *            the new col 4 limit
         */
        public void setCol4Limit(final int col4Limit) {
            this.col4Limit = col4Limit;
        }
    }

    /**
     * Checks if pseudo category map is empty.
     *
     * @param pseudoCategoryMap
     *            the pseudo category map
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
        final ValueMap properties = nextPage.getProperties();
        return properties.get("pseudoCategory", StringUtils.EMPTY);
    }

    /**
     * Populate pseudo section.
     *
     * @param pseudoCategoryMap
     *            the pseudo category map
     * @param resourceResolver
     *            the resource resolver
     * @return the list
     */
    private List<PseudoCategoriesSectionBean> populatePseudoSection(final Map<String, String> pseudoConfigMap,
            final Map<String, List<String>> pseudoCategoryMap, final SlingHttpServletRequest request) {
        final List<PseudoCategoriesSectionBean> pseudoSection = new ArrayList<>();

        for (final Entry<String, List<String>> entrySet : pseudoCategoryMap.entrySet()) {
            if (CollectionUtils.isNotEmpty(entrySet.getValue())) {
                final PseudoCategoriesSectionBean pseudoCategoriesSectionBean = new PseudoCategoriesSectionBean();
                pseudoCategoriesSectionBean.setTitle(pseudoConfigMap.get(entrySet.getKey()));

                final List<SubSectionBean> subSectionList = getSubSectionList(entrySet.getValue(), request);
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
     * @param pathList
     *            the path list
     * @param resourceResolver
     *            the resource resolver
     * @return the sub section bean
     */
    private List<SubSectionBean> getSubSectionList(final List<String> pathList,
            final SlingHttpServletRequest request) {
        final List<SubSectionBean> subSections = new ArrayList<>();
        for (final String path : pathList) {
            final Page page = pageManager.getPage(path);
            subSections.add(populateSubSection(page, request));
        }
        return subSections;
    }

    /**
     * Populate sub section.
     *
     * @param page
     *            the page
     * @param resourceResolver
     *            the resource resolver
     * @return the sub section bean
     */
    private SubSectionBean populateSubSection(final Page page, final SlingHttpServletRequest request) {
        final SubSectionBean subSectionBean = new SubSectionBean();
        subSectionBean.setLinkText(NavigationUtil.getNavigationTitle(page));

        final ExternalTemplateBean externalTemplate = checkExternalTemplate(page);
        if (externalTemplate.isExternal()) {
            subSectionBean.setExternal(true);
            subSectionBean.setLinkPath(externalTemplate.getExternalUrl());
        } else {
            subSectionBean.setExternal(false);
            subSectionBean.setLinkPath(LinkUtils.sanitizeLink(page.getPath(), request));
            if(request.getPathInfo().contains(page.getPath()+PWConstants.HTML)) {
                subSectionBean.setHighlighted(true); 
            }
            final ValueMap valueMap = page.getProperties();
            if (Objects.nonNull(valueMap)
                    && StringUtils.isNotBlank(valueMap.get(MOBILE_OVERVIEW_LABEL, StringUtils.EMPTY))) {
                subSectionBean.setMobileOverviewLabel(valueMap.get("MOBILE_OVERVIEW_LABEL", StringUtils.EMPTY));
            }
        }

        return subSectionBean;
    }

    /**
     * Gets the page hierarchy.
     *
     * @param page
     *            the new page hierarchy
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
                final String title;
                if (StringUtils.isNotBlank(nextPage.getNavigationTitle())) {
                    title = nextPage.getNavigationTitle();
                } else {
                    title = nextPage.getTitle();
                }
                pageTitleMap.put("l" + (i + 1), title);
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
     * @param page
     *            the new section home page title
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
     * @param page
     *            the new section home page path
     * @param resourceResolver
     *            the resource resolver
     */
    public void setSectionHomePagePath(final Page page, final SlingHttpServletRequest request) {
        sectionHomePagePath = LinkUtils.sanitizeLink(page.getPath(), request);
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
