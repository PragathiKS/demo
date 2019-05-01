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
    this.cache.$modalBtn.click((e) => {
      $('.softc-title-js', '#softConversionModal').text(this.root.data('softc-title'));
      $('.softc-desc-js', '#softConversionModal').text(this.root.data('softc-desc'));
      $('.softc-head-js', '#softConversionModal').text(this.root.data('softc-head'));
      $('.softc-last-js', '#softConversionModal').text(this.root.data('softc-last'));
      $('.softc-doc-js', '#softConversionModal').val(this.root.data('softc-doc'));
      if ($('#softConversionModal').data('form-filled')) {
        e.preventDefault();
        e.stopPropagation();
        window.open(this.root.data('softc-doc'), '_blank');
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
