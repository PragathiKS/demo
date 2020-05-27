package com.tetrapak.publicweb.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.beans.ExternalTemplateBean;
import com.tetrapak.publicweb.core.beans.PseudoCategoriesSectionBean;
import com.tetrapak.publicweb.core.beans.SectionMenuBean;
import com.tetrapak.publicweb.core.beans.SubSectionBean;
import com.tetrapak.publicweb.core.beans.SubSectionMenuBean;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.utils.LinkUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.annotation.PostConstruct;

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
        final String solutionPagePath = StringUtils.substringBefore(fetchSolutionPagePath(), ".html");
        // Fetch absolute parent page
        final Page page = fetchAbsoluteParent(solutionPagePath);
        // Set section menu home page title
        setSectionHomePageTitle(page);
        // Set section menu home page url
        setSectionHomePagePath(page);
        // Set page hierarchy
        setPageHierarchy(currentPage);
        // Populate section menu
        populateSectionMenu(page, solutionPagePath);
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
     * @param page the page
     * @param path the path
     */
    public void populateSectionMenu(final Page page, final String path) {
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
                } else {
                    sectionMenuBean.setExternal(false);
                    sectionMenuBean.setLinkPath(LinkUtils.sanitizeLink(nextPage.getPath()));
                }
                sectionMenuBean.setSubSectionMenu(populateSubSectionMenu(nextPage, path));
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
     * @param page the page
     * @param path the path
     * @return the sub section menu bean
     */
    private SubSectionMenuBean populateSubSectionMenu(final Page page, final String path) {
        final Map<String, List<String>> pseudoCategoryMap = new LinkedHashMap<>();
        final List<SubSectionBean> subSections = new ArrayList<>();

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
            final PseudoCategoriesSectionBean pseudoCategoriesSectionBean = new PseudoCategoriesSectionBean();
            pseudoCategoriesSectionBean.setTitle(entrySet.getKey());

            final List<SubSectionBean> subSectionList = getSubSectionList(entrySet.getValue());
            pseudoCategoriesSectionBean.setSubSections(subSectionList);
            pseudoCategoriesSectionBean.setSubSectionCount(subSectionList.size());
            pseudoSection.add(pseudoCategoriesSectionBean);
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
        return sectionMenu;
    }
}
