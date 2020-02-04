import $ from 'jquery';

class ImageTextButton {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$imageTextButtonLink = $('.imageTextButton', this.root);
  }
  bindEvents() {
    this.cache.$imageTextButtonLink.on('click', this.trackAnalytics);
  }
  trackAnalytics = (e) => {
    const $target = $(e.target);
    const comp = $target.closest('.pw-image-text-button');
    if (window.digitalData) {
      window.digitalData.linkClick = {};
      window.digitalData.linkClick.linkType = comp.attr('data-imageButton-linkType');
      window.digitalData.linkClick.linkSection = 'imageTextButton';
      window.digitalData.linkClick.linkParentTitle = $.trim($target.closest('.pw-image-text-button').find('.pw-image-text-button__title').text());
      window.digitalData.linkClick.linkName = $.trim($target.text());
      window.digitalData.linkClick.contentName = $.trim($target.closest('.pw-image-text-button').find('.pw-image-text-button__subtitle').text());
      if (window._satellite) {
        window._satellite.track('linkClicked');
      }
    }
  }
  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default ImageTextButton;
