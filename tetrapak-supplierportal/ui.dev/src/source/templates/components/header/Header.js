import $ from 'jquery';
import { $body } from '../../../scripts/utils/commonSelectors';

class Header {
  constructor({ el }) {
    this.root = $(el);
  }

  cache = {};
  initCache() {
    this.cache.$burgerMenu = this.root.find('.tp-header__burger-menu');
    this.cache.$closeBtn = this.root.find('.js-close-btn');
  }
  bindEvents() {
    const { $burgerMenu, $closeBtn } = this.cache;
    $burgerMenu.on('click', () => {
      this.root.addClass('menu-opened');
      $closeBtn.removeClass('d-none');
      $body.trigger('showleftnav');
    });
    $closeBtn.on('click', () => {
      this.root.removeClass('menu-opened');
      $closeBtn.addClass('d-none');
      $body.trigger('hideleftnav');
    });
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default Header;

