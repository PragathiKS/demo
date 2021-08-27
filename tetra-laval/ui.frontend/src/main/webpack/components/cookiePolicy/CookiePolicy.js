import $ from 'jquery';

class CookiePolicy {
  constructor({ el }) {
    this.root = $(el);
  }

    cache = {};
    initCache() {
      this.cache.$confirmationButton = this.root.find('.confirmation-button');
      this.cache.$cookiesOverlay = $('.pc-dark-filter');
      this.cache.$cookiesBar = $('.otFloatingRoundedCorner');
    }

    bindEvents() {
      // checking if cookie not exist
      if ( document.cookie.indexOf('cookieconsent') === -1) {
        this.addCookieBar();
        this.actionAfterClick();
      }
    }

    addCookieBar = () => {
      this.cache.$cookiesOverlay.addClass('show');
      this.cache.$cookiesBar.addClass('show');
    }

    removeCookieBar = () => {
      this.cache.$cookiesOverlay.removeClass('show');
      this.cache.$cookiesBar.removeClass('show');
    }

    actionAfterClick = () => {
      this.cache.$confirmationButton.on('click', this.removeCookieBar);
      this.cache.$confirmationButton.on('click', this.createCookie);
    }

    createCookie = e => {
      e.stopPropagation();
      this.removeCookieBar();

      const date = new Date();

      date.setFullYear(date.getFullYear() + 10);

      document.cookie = `cookieconsent=true; expires=${date}; path=/`;
    }

    init () {
      this.initCache();
      this.bindEvents();
    }
}

export default CookiePolicy;
