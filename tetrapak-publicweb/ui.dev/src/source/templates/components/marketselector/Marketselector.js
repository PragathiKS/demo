import $ from 'jquery';
import 'bootstrap';

class Marketselector {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$modal = this.root.parent().find('.js-lang-modal');
  }
  bindEvents() {
    this.root.on('click', '.js-close-btn', this.hidePopUp)
      .on('click', function () {
        if ($(this).hasClass('js-lang-modal')) {
          this.hidePopUp;
        }
      })
      .on('showlanuagepreferencepopup-pw', this.showPopup);
  }

  showPopup = () => {
    const $this = this;
    const { $modal } = $this.cache;
    $modal.modal();
  }

  hidePopUp = () => {
    const $this = this;
    $this.root.modal('hide');
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    // this.showPopup(true);
  }
}

export default Marketselector;
