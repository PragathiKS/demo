package com.tetrapak.publicweb.core.utils;

import org.apache.commons.lang3.StringUtils;

import com.adobe.cq.sightly.WCMUsePojo;

public class LinkUtils extends WCMUsePojo{

    private static final String PARAM_LINK = "linkPath";
    private String sanitizedLink;


    /**
     * Add .html to link if is internal
     *
     * @param link
     */
    public static String sanitizeLink(String link) {
        if (StringUtils.isBlank(link))
            return "#";
        else if(link.startsWith("/content/") && !link.startsWith("/content/dam/") && !link.endsWith(".html") && !link.endsWith(".htm")){
            return link + ".html";
        }
        return link;
    }

	@Override
	public void activate() throws Exception {
		sanitizedLink = get(PARAM_LINK, String.class);
	}

	public String getSanitizedLink(){
		return LinkUtils.sanitizeLink(sanitizedLink);
	}

}