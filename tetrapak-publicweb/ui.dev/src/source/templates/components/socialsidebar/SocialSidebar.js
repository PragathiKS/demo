import $ from 'jquery';
class SocialSidebar {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$socialLink = this.root.find('.tp-pw-sidebar__link');
    this.cache.$imageModal = this.root.find('.js-qrCode-modal');
    this.cache.$closeModal = this.root.find('.js-close-btn');
  }
  bindEvents() {
    const $this = this;
    this.cache.$socialLink.on('click', function (e) {
      e.preventDefault();
      $this.showPopup();
    });
    // PopUp Close Button
    this.cache.$closeModal.on('click', function (e) {
      e.preventDefault();
      $this.cache.$imageModal.modal('hide');
    });
  }
  showPopup = () => {
    this.cache.$imageModal.modal();
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default SocialSidebar;
