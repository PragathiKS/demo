import $ from 'jquery';

class CookiePolicy {
    constructor({ el }) {
        this.root = $(el);
    }

    init() {
        this.$confirmationButton = this.root.find('.confirmation-button');
        if ( document.cookie.indexOf('cookiePolicy') > -1) {
            this.removeCookieBar();
        }
        this.$confirmationButton.on('click', this.removeCookieBar);
        this.$confirmationButton.on('click', this.createCookie);
    }

    removeCookieBar = e => {
        $('.pc-dark-filter').addClass('hide');
        $('.otFloatingRoundedCorner').addClass('hide');
    }

    createCookie  = e => {
        document.cookie = 'cookiePolicy';
    };
}

export default CookiePolicy;
