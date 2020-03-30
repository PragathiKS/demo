import $ from 'jquery';
import { $global } from '../../../scripts/utils/commonSelectors';

class Footer {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.toTopLink = $('.tp-pw-footer .tp_textBtn');
    this.cache.$footerLink=this.root.find('.tp-pw-footer__link');
  }
  bindEvents() {
    this.initCache();
    this.cache.toTopLink.on('click', this.goToTop);
    const {$footerLink} = this.cache;
    $footerLink.on('click', this.trackAnalytics);
  }
  trackAnalytics = (e) => {
    e.preventDefault();
    const $this = this.cache.$footerLink;
    if (window.digitalData) {
      $.extend(window.digitalData, {
        linkClick: {
          linkType: 'internal',
          linkSection: $this.data('link-section'),
          linkParentTitle: $this.data('link-parent-title'),
          linkName: $this.data('link-name')
        }
      });
      if (window._satellite) {
        window._satellite.track('linkClicked');
      }
    }
    var url = $this.attr('href');
    window.open(url, $this.attr('target'));
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
