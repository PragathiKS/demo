import $ from 'jquery';
import { $global } from '../../../scripts/utils/commonSelectors';

class Footer {
  cache = {};
  initCache() {
    this.cache.toTopLink = $('.tp-pw-footer .tp_textBtn');
  }
  bindEvents() {
    this.initCache();
    this.cache.toTopLink.on('click', this.goToTop);
  }
  goToTop() {
    $global.animate({ scrollTop: 0 }, 700);
    return false;
  }
  init() {
    this.bindEvents();
  }
}

export default Footer;
