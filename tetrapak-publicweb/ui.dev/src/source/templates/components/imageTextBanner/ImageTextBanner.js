import $ from 'jquery';

class ImageTextBanner {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize cache here */
    this.cache.$modalBtn = $('.opensoftc', this.root);
    this.cache.itbLink = $('.itblink', this.root);
  }
  bindEvents() {
    /* Bind jQuery events here */
    this.cache.$modalBtn.click((e) => {
      $('.softc-title-js', '#softConversionModal').text(this.root.data('softc-title'));
      $('.softc-desc-js', '#softConversionModal').text(this.root.data('softc-desc'));
      $('.softc-head-js', '#softConversionModal').text(this.root.data('softc-head'));
      $('.softc-last-js', '#softConversionModal').text(this.root.data('softc-last'));
      $('.softc-doc-js', '#softConversionModal').val(this.root.data('softc-doc'));
      if (digitalData) { // eslint-disable-line
        digitalData.formInfo = {}; //eslint-disable-line
        digitalData.formInfo.contentName = this.root.data('softc-title'); //eslint-disable-line
        digitalData.formInfo.formName = 'gated content sign up'; //eslint-disable-line
        digitalData.formInfo.stepName = 'gated content sign up start'; //eslint-disable-line
        digitalData.formInfo.totalSteps = 4; //eslint-disable-line
        if (typeof _satellite !== 'undefined') { //eslint-disable-line
            _satellite.track('signup_form_tracking'); //eslint-disable-line
        }
      }
      if ($('#softConversionModal').data('form-filled')) {
        e.preventDefault();
        e.stopPropagation();
        window.open(this.root.data('softc-doc'), '_blank');
      }
    });
    this.cache.itbLink.click((e) => { // eslint-disable-line
      let banner = e.target.closest('.pw-banner');
      if (digitalData) { //eslint-disable-line
        digitalData.linkClick = {}; //eslint-disable-line
        digitalData.linkClick.linkType = banner.getAttribute('data-itb-linkType'); //eslint-disable-line
        digitalData.linkClick.linkSection = banner.getAttribute('data-itb-linkSection'); //eslint-disable-line
        digitalData.linkClick.linkParentTitle = banner.getAttribute('data-itb-linkParentTitle'); //eslint-disable-line
        digitalData.linkClick.linkName = banner.getAttribute('data-itb-linkName'); //eslint-disable-line
        digitalData.linkClick.contentName = banner.getAttribute('data-itb-contentName'); //eslint-disable-line
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

export default ImageTextBanner;
