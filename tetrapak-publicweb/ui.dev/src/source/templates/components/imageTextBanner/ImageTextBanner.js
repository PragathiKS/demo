import $ from 'jquery';

class ImageTextBanner {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$modalBtn = this.root.find('.opensoftc');
    this.cache.itbLink = this.root.find('.itblink');
    this.cache.$softConversionModal = $('#softConversionModal');
  }
  bindEvents() {
    const { $modalBtn } = this.cache;
    $modalBtn.on('click', this.softConversionFlow);
    this.cache.itbLink.on('click', this.trackAnalytics);
  }
  trackAnalytics = (e) => {
    const $target = $(e.target);
    const banner = $target.closest('.pw-banner');
    if (window.digitalData) {
      $.extend(window.digitalData, {
        linkClick: {
          linkType: banner.attr('data-itb-linkType'),
          linkSection: banner.attr('data-itb-linkSection'),
          linkParentTitle: $.trim($target.closest('.pw-banner').find('.pw-banner__content__title').text()),
          linkName: $.trim($target.text()),
          contentName: $.trim($target.closest('.pw-banner').find('.pw-banner__content__subtitle').text())
        }
      });
      if (window._satellite) {
        window._satellite.track('linkClicked');
      }
    }
  }
  softConversionFlow = () => {
    const { $softConversionModal } = this.cache;
    $softConversionModal.find('.softc-title-js').text(this.root.data('softc-title'));
    $softConversionModal.find('.softc-desc-js').text(this.root.data('softc-desc'));
    $softConversionModal.find('.softc-head-js').text(this.root.data('softc-head'));
    $softConversionModal.find('.softc-last-js').text(this.root.data('softc-last'));
    $softConversionModal.find('.softc-doc-js').val(this.root.data('softc-doc'));
    if (window.digitalData) {
      $.extend(window.digitalData, {
        formInfo: {
          contentName: this.root.data('softc-title'),
          formName: 'gated content sign up',
          stepName: 'gated content sign up start',
          totalSteps: 4
        }
      });
      if (window._satellite) {
        window._satellite.track('signup_form_tracking');
      }
    }
  }
  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default ImageTextBanner;
