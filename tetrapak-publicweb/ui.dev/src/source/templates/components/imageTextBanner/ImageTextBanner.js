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
    this.cache.digitalData = window.digitalData;
  }
  bindEvents() {
    /* Bind jQuery events here */
    this.cache.$modalBtn.click(() => {
      $('.softc-title-js', '#softConversionModal').text(this.root.data('softc-title'));
      $('.softc-desc-js', '#softConversionModal').text(this.root.data('softc-desc'));
      $('.softc-head-js', '#softConversionModal').text(this.root.data('softc-head'));
      $('.softc-last-js', '#softConversionModal').text(this.root.data('softc-last'));
      $('.softc-doc-js', '#softConversionModal').val(this.root.data('softc-doc'));
      if (this.cache.digitalData) {
        this.cache.digitalData.formInfo = {};
        this.cache.digitalData.formInfo.contentName = this.root.data('softc-title');
        this.cache.digitalData.formInfo.formName = 'gated content sign up';
        this.cache.digitalData.formInfo.stepName = 'gated content sign up start';
        this.cache.digitalData.formInfo.totalSteps = 4;
        if (typeof _satellite !== 'undefined') { //eslint-disable-line
            _satellite.track('signup_form_tracking');  //eslint-disable-line
        }
      }
    });
    this.cache.itbLink.click((e) => {
      let banner = e.target.closest('.pw-banner');
      let $thisClick = $(e.target);
      if (this.cache.digitalData) {
        this.cache.digitalData.linkClick = {};
        this.cache.digitalData.linkClick.linkType = banner.getAttribute('data-itb-linkType');
        this.cache.digitalData.linkClick.linkSection = banner.getAttribute('data-itb-linkSection');
        this.cache.digitalData.linkClick.linkParentTitle = $thisClick.closest('.pw-banner').find('.pw-banner__content__title').text().trim();
        this.cache.digitalData.linkClick.linkName = $thisClick.text().trim();
        this.cache.digitalData.linkClick.contentName = $thisClick.closest('.pw-banner').find('.pw-banner__content__subtitle').text().trim();
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
