import $ from 'jquery';
import { $global } from '../../../scripts/utils/commonSelectors';
import { isExternal } from '../../../scripts/utils/updateLink';

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
  goToTop(e) {
    e.preventDefault();
    $global.animate({ scrollTop: 0 }, 700);
    return false;
  }
  addLinkAttr() {
    $('.tp-pw-footer-data-analytics').each(function () {
      const thisHref = $(this).attr('href');
      if (thisHref) {
        if (isExternal(thisHref)) {
          $(this).attr('target', '_blank');
          $(this).data('download-type', 'download');
          $(this).data('link-section', $(this).data('link-section') + '_Download');
          $(this).attr('rel', 'noopener noreferrer');
          $(this).data('link-type','external');
        } else {
          $(this).data('link-type','internal');
        }
      }
    });
  }

  init() {
    this.bindEvents();
    this.addLinkAttr();
  }
}

export default Footer;
