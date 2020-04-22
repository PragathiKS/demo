import $ from 'jquery';
import { $global } from '../../../scripts/utils/commonSelectors';

class Footer {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.toTopLink = $('#tp-pw-footer__link');
    this.cache.$footerLink=this.root.find('.tp-pw-footer-data-analytics');
  }
  bindEvents() {
    this.initCache();
    this.cache.toTopLink.on('click', this.goToTop);
    const {$footerLink} = this.cache;
    $footerLink.on('click', this.trackAnalytics);
  }
  trackAnalytics = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.tp-pw-footer-data-analytics');
    const targetLink = $this.attr('target');
    const url = $this.attr('href');
    
    if(targetLink === '_blank'){
      window._satellite.track('linkClick');
    }

    if(url && targetLink){
      window.open(url, targetLink);
    }
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
