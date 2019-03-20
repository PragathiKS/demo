package com.tetrapak.publicweb.core.beans;

public class PracticeLineBean {
	
	private String contentType;

	private String articlePath;

	private String articleTitle;

	private String vanityDescriptionI18n;

	private String ctaTexti18nKey;

	private Boolean openInNewWindow;

	private String articleImagePath;

	private String articleImageAltI18n;

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getArticlePath() {
		return articlePath;
	}

	public void setArticlePath(String articlePath) {
		this.articlePath = articlePath;
	}

	public String getArticleTitle() {
		return articleTitle;
	}

	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}

	public String getVanityDescriptionI18n() {
		return vanityDescriptionI18n;
	}

	public void setVanityDescriptionI18n(String vanityDescriptionI18n) {
		this.vanityDescriptionI18n = vanityDescriptionI18n;
	}

	public String getCtaTexti18nKey() {
		return ctaTexti18nKey;
	}

	public void setCtaTexti18nKey(String ctaTexti18nKey) {
		this.ctaTexti18nKey = ctaTexti18nKey;
	}

	public Boolean getOpenInNewWindow() {
		return openInNewWindow;
	}

	public void setOpenInNewWindow(Boolean openInNewWindow) {
		this.openInNewWindow = openInNewWindow;
	}

	public String getArticleImagePath() {
		return articleImagePath;
	}

	public void setArticleImagePath(String articleImagePath) {
		this.articleImagePath = articleImagePath;
	}

	public String getArticleImageAltI18n() {
		return articleImageAltI18n;
	}

	public void setArticleImageAltI18n(String articleImageAltI18n) {
		this.articleImageAltI18n = articleImageAltI18n;
	}

}
