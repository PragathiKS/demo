package com.tetrapak.publicweb.core.utils;

import com.adobe.cq.sightly.WCMUsePojo;
import com.tetrapak.publicweb.core.beans.NavigationLinkBean;

import org.apache.jackrabbit.vault.util.Text;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;

import java.util.List;

public class LinkUtils extends WCMUsePojo {

    private static final String PARAM_LINK = "linkPath";
    private String sanitizedLink;

    /** The Constant FORWARD_SLASH. */
    private static final String FORWARD_SLASH = "/";

    /**
     * Add .html to link if is internal
     *
     * @param link
     */
    public static String sanitizeLink(String link) {
        if (StringUtils.isBlank(link)) {
            return "#";
        } else if (link.startsWith("/content/") && !link.startsWith("/content/dam/") && !link.endsWith(".html")
                && !link.endsWith(".htm")) {
            return link + ".html";
        }
        return link;
    }

    /**
     * Gets the root path.
     *
     * @param pagePath
     *            the page path
     * @return the root path
     */
    public static String getRootPath(String pagePath) {
        return Text.getAbsoluteParent(pagePath, 4);
    }

    /**
     * Used for analytics code to determine the link type
     */
    public static String linkType(String linkPath) {
        if (StringUtils.startsWith(linkPath, "/content/dam/") || StringUtils.endsWith(linkPath, ".pdf")) {
            return "download";
        } else if (StringUtils.startsWith(linkPath, "/content/")) {
            return "internal";
        } else {
            return "external";
        }
    }

    @Override
    public void activate() throws Exception {
        sanitizedLink = get(PARAM_LINK, String.class);
    }

    public String getSanitizedLink() {
        return LinkUtils.sanitizeLink(sanitizedLink);
    }

    /**
     * Method to get multi field menu link items Used for navigation links on Header and Footer
     */
    public static void setMultifieldNavLinkItems(String[] footerNavLinks, List<NavigationLinkBean> navLinksList,
            Logger log) {
        JSONObject jObj;
        try {
            if (footerNavLinks == null || navLinksList == null) {
                log.error("footerNavLinks is NULL");
                return;
            }
            for (int i = 0; i < footerNavLinks.length; i++) {
                jObj = new JSONObject(footerNavLinks[i]);
                NavigationLinkBean bean = getNavigationLinkBean(jObj);
                navLinksList.add(bean);
            }
        } catch (JSONException e) {
            log.error("Exception while Multifield data {}", e.getMessage(), e);
        }
    }

    private static NavigationLinkBean getNavigationLinkBean(JSONObject jObj) throws JSONException {
        NavigationLinkBean bean = new NavigationLinkBean();

        String linkTextI18n = "";
        String linkTooltipI18n = "";
        String linkPath = "";
        String targetBlank = "";
        if (jObj.has("linkTextI18n")) {
            linkTextI18n = jObj.getString("linkTextI18n");
        }
        if (jObj.has("linkTooltipI18n")) {
            linkTooltipI18n = jObj.getString("linkTooltipI18n");
        }
        if (jObj.has(PARAM_LINK)) {
            linkPath = jObj.getString(PARAM_LINK);
        }
        if (jObj.has("targetBlank")) {
            targetBlank = jObj.getString("targetBlank");
        }

        bean.setLinkTextI18n(linkTextI18n);
        bean.setLinkTooltipI18n(linkTooltipI18n);
        bean.setLinkPath(sanitizeLink(linkPath));
        bean.setLinkType(linkType(linkPath));
        bean.setTargetBlank(targetBlank);
        return bean;
    }

    /**
     * Gets the asset name.
     *
     * @param path the asset path.
     * @return the asset name.
     */
    public static String getAssetName(final String path) {
        String assetName = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(path)) {
            assetName = getSubstringAfterLast(path);
        }
        return assetName;
    }

    /**
     * Gets the substring after last.
     *
     * @param path the path
     * @return the substring after last
     */
    private static String getSubstringAfterLast(final String path) {
        return StringUtils.substringAfterLast(path, FORWARD_SLASH);
    }
}
