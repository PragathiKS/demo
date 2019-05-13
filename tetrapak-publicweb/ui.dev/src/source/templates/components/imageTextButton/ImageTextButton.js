import $ from 'jquery';

class ImageTextButton {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize cache here */
    this.cache.$imageTextButtonLink = $('.imageTextButton', this.root);
  }
  bindEvents() {
    /* Bind jQuery events here */
    this.cache.$imageTextButtonLink.click((e) => {
      debugger; // eslint-disable-line
      let comp = e.target.closest('.pw-image-text-button');
      if (digitalData) { // eslint-disable-line
        digitalData.linkClick = {}; //eslint-disable-line
        digitalData.linkClick.linkType = comp.getAttribute('data-imageButton-linkType'); //eslint-disable-line
        digitalData.linkClick.linkSection = 'imageTextButton'; //eslint-disable-line
        digitalData.linkClick.linkParentTitle = comp.getAttribute('data-imageButton-title'); //eslint-disable-line
        digitalData.linkClick.linkName = comp.getAttribute('data-imageButton-linkName'); //eslint-disable-line
        _satellite.track('linkClicked'); //eslint-disable-line
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