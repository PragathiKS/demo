import $ from 'jquery';
import { $body } from '../../../scripts/utils/commonSelectors';
class Footer {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  bindEvents() {
    this.root.find('.js-footer__change-lang').on('click', (e) => {
      e.preventDefault();
      $body.find('.js-lang-modal').trigger('showlanuagepreferencepopup');
    });
  }
  init() {
    this.bindEvents();
  }
}

export default Footer;
