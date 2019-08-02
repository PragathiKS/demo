import $ from 'jquery';
import { $body } from '../../../scripts/utils/commonSelectors';

class Header {
  constructor({ el }) {
    this.root = $(el);
  }

  bindEvents() {
    this.root.find('.tp-header__burger-menu').on('click', () => {
      $body.trigger('showLeftNav');
    });
    this.root.find('.js-header__selected-lang').on('click', () => {
      this.root.find('.js-lang-modal').trigger('showLanuagePreferencePopup');
    });
  }

  init() {
    this.bindEvents();
  }
}

export default Header;

