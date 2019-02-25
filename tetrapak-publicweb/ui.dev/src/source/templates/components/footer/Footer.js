import $ from 'jquery';

class Footer {
  cache = {};

  initCache() {
    this.cache.toTopLink = $('.tp-pw-footer .tp_textBtn');
  }

  bindEvents() {

    this.initCache();
    this.cache.toTopLink.click(function () {
      $('html, body').animate({scrollTop: 0}, 700);
      return false;
    });
  }

  init() {
    this.bindEvents();
  }
}

export default Footer;
