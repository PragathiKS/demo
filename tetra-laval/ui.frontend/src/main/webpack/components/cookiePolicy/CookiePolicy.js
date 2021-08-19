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

    addCookieBar = e => {
        this.cache.$cookiesOverlay.addClass('show');
        this.cache.$cookiesBar.addClass('show');
    }

    removeCookieBar = e => {
        this.cache.$cookiesOverlay.removeClass('show');
        this.cache.$cookiesBar.removeClass('show');
    }

    actionAfterClick = e => {
        this.cache.$confirmationButton.on('click', this.removeCookieBar);
        this.cache.$confirmationButton.on('click', this.createCookie);
    }

    createCookie = e => {
        e.stopPropagation()

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
