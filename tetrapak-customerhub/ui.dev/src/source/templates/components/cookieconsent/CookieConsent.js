import $ from 'jquery';
import { storageUtil } from '../../../scripts/common/common';
import { TRANSITION_END } from '../../../scripts/utils/constants';

class CookieConsent {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$bannerBtn = this.root.find('.js-cookie-consent__btn');
  }
  bindEvents() {
    const { $bannerBtn } = this.cache;
    $bannerBtn.on('click', this.removeBanner);
  }
  removeBanner = () => {
    this.root.one(TRANSITION_END, () => {
      this.root.addClass('d-none');
    });
    this.root.removeClass('active');
    storageUtil.set('gdprCookie', true);
  };
  init() {
    this.initCache();
    this.bindEvents();
    const hideBanner = storageUtil.get('gdprCookie');
    if (!hideBanner) {
      this.root.removeClass('d-none');
      const bannerTimeout = setTimeout(() => {
        this.root.addClass('active');
        clearTimeout(bannerTimeout);
      }, 60);
    }
  }
}

export default CookieConsent;
