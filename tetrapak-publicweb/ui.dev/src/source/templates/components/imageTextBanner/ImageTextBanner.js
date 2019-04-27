import $ from 'jquery';

class ImageTextBanner {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize cache here */
    this.cache.$modalBtn = $('.opensoftc', this.root);
  }
  bindEvents() {
    /* Bind jQuery events here */
    this.cache.$modalBtn.click(() => {
      $('.softc-title-js', '#softConversionModal').text(this.root.data('softc-title'));
      $('.softc-desc-js', '#softConversionModal').text(this.root.data('softc-desc'));
      $('.softc-head-js', '#softConversionModal').text(this.root.data('softc-head'));
      $('.softc-doc-js', '#softConversionModal').val(this.root.data('softc-doc'));
      /* TODO: set radio btns, ask if make sense to do it here,
       * I think this should be on the modal with fixed fields because the linked funcionality
       *
       * $('.softc-radio-js', '#softConversionModal').text(this.root.data('softc-radio'));
       *
       */
    });
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default ImageTextBanner;
