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
      if (this.cache.digitalData) {
        this.cache.digitalData.linkClick = {};
        this.cache.digitalData.linkClick.linkType = comp.getAttribute('data-imageButton-linkType');
        this.cache.digitalData.linkClick.linkSection = 'imageTextButton';
        this.cache.digitalData.linkClick.linkParentTitle = comp.getAttribute('data-imageButton-title');
        this.cache.digitalData.linkClick.linkName = comp.getAttribute('data-imageButton-linkName');
        this.cache.digitalData.linkClick.contentName = comp.getAttribute('data-imageButton-contentName');
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