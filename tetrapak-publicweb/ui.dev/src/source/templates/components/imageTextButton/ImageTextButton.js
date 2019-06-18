import $ from 'jquery';

class ImageTextButton {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize cache here */
    this.cache.$imageTextButtonLink = $('.imageTextButton', this.root);
    this.cache.digitalData = digitalData; //eslint-disable-line
  }
  bindEvents() {
    /* Bind jQuery events here */
    this.cache.$imageTextButtonLink.click((e) => {
      let comp = e.target.closest('.pw-image-text-button');
      let $thisClick = $(e.target);
      if (this.cache.digitalData) {
        this.cache.digitalData.linkClick = {};
        this.cache.digitalData.linkClick.linkType = comp.getAttribute('data-imageButton-linkType');
        this.cache.digitalData.linkClick.linkSection = 'imageTextButton';
        this.cache.digitalData.linkClick.linkParentTitle = $thisClick.closest('.pw-image-text-button').find('.pw-image-text-button__title').text().trim();
        this.cache.digitalData.linkClick.linkName = $thisClick.text().trim();
        this.cache.digitalData.linkClick.contentName = $thisClick.closest('.pw-image-text-button').find('.pw-image-text-button__subtitle').text().trim();
        if (typeof _satellite !== 'undefined') { //eslint-disable-line
          _satellite.track('linkClicked'); //eslint-disable-line
        }
      }
    });
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default ImageTextButton;