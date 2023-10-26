import $ from 'jquery';
class Footer {
  constructor({ el }) {
    this.root = $(el);
  }

  bindEvents() {
    this.root.find('.js-footer__trigger-lang').on('click', (e) => {
      e.preventDefault();
      this.root.find('.js-lang-modal').trigger('showlanuagepreferencepopup');
    });
  }

  init() {
    this.bindEvents();
  }
}

export default Footer;
