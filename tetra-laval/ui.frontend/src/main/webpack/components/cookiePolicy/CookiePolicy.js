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
        // checking if cookie exist
        if ( document.cookie.indexOf('cookieconsent') > -1) {
            this.removeCookieBar();
        } else {
            this.cache.$confirmationButton.on('click', this.removeCookieBar);
            this.cache.$confirmationButton.on('click', this.createCookie);
        }
    }

    removeCookieBar = e => {
        this.cache.$cookiesOverlay.addClass('hide');
        this.cache.$cookiesBar.addClass('hide');
    }

    createCookie = e => {
        let date = new Date();
        date.setFullYear(date.getFullYear() + 10);
        document.cookie = `cookieconsent=true; expires=${date}; path=/`;
    }

    init () {
        this.initCache();
        this.bindEvents();
    }
}

export default CookiePolicy;
