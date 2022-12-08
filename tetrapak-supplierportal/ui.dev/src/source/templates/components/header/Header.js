import $ from 'jquery';

class Header {
  constructor({ el }) {
    this.root = $(el);
  }

  bindEvents() {
    this.root.find('.tp-header__burger-menu').on('click', () => {
      document.body.trigger('showleftnav');
    });
  }

  init() {
    this.bindEvents();
  }
}

export default Header;

