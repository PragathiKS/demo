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
        if ( document.cookie.indexOf('cookiePolicy') > -1) {
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
        document.cookie = 'cookiePolicy';
    }

    init () {
        this.initCache();
        this.bindEvents();
    }
}

export default CookiePolicy;
