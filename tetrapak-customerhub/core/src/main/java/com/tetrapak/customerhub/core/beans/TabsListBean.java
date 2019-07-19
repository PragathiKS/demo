package com.tetrapak.customerhub.core.beans;

/**
 * List Content Bean class
 */
public class TabsListBean {
    private String type;

    private String tabTitleI18n;

    private String mediaTitleI18n;

    private String mediaDescriptionI18n;
    
    private String linkTextI18n;
    
    private String linkURL;
    
    private String linkType;
    
    private Boolean isExternal;
    
    private String videoSource;
    
    private String damVideoPath;
    
    private String youtubeEmbedURL;
    
    private String thumbnailPath;
    
    private String imagePath;
    
	private String imageAltI18n;

	public String getType() {
        return type;
    }
	
    public void setType(String type) {
        this.type = type;
    }

    public String getTabTitleI18n() {
        return tabTitleI18n;
    }

    public void setTabTitleI18n(String tabTitleI18n) {
        this.tabTitleI18n = tabTitleI18n;
    }

    public String getMediaTitleI18n() {
        return mediaTitleI18n;
    }

    public void setMediaTitleI18n(String mediaTitleI18n) {
        this.mediaTitleI18n = mediaTitleI18n;
    }

    public String getMediaDescriptionI18n() {
        return mediaDescriptionI18n;
    }

    public void setMediaDescriptionI18n(String mediaDescriptionI18n) {
        this.mediaDescriptionI18n = mediaDescriptionI18n;
    }
    
    public String getLinkTextI18n() {
		return linkTextI18n;
	}

	public void setLinkTextI18n(String linkTextI18n) {
		this.linkTextI18n = linkTextI18n;
	}
	
	public String getLinkURL() {
		return linkURL;
	}

	public void setLinkURL(String linkURL) {
		this.linkURL = linkURL;
	}
	
	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public Boolean getIsExternal() {
		return isExternal;
	}

	public void setIsExternal(Boolean isExternal) {
		this.isExternal = isExternal;
	}

	public String getVideoSource() {
		return videoSource;
	}

	public void setVideoSource(String videoSource) {
		this.videoSource = videoSource;
	}

	public String getDamVideoPath() {
		return damVideoPath;
	}

	public void setDamVideoPath(String damVideoPath) {
		this.damVideoPath = damVideoPath;
	}

	public String getYoutubeEmbedURL() {
		return youtubeEmbedURL;
	}

	public void setYoutubeEmbedURL(String youtubeEmbedURL) {
		this.youtubeEmbedURL = youtubeEmbedURL;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getImageAltI18n() {
		return imageAltI18n;
	}

	public void setImageAltI18n(String imageAltI18n) {
		this.imageAltI18n = imageAltI18n;
	}
}
