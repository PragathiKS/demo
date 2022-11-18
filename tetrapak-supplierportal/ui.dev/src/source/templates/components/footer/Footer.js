import $ from 'jquery';
import { isExternal } from '../../../scripts/utils/updateLink';
import { trackAnalytics } from '../../../scripts/utils/analytics';

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
    const {$footerLink} = this.cache;
    // Footer Social Media Click
    $footerLink.on('click', this.trackAnalytics);
  }
  trackAnalytics = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.tp-pw-footer-data-analytics');
    const url = $this.attr('href');
    const linkText = $this.text().trim();
    const linkName = linkText ? linkText : $this.data('link-name');
    const myDomain = 'tetrapak.com';
    const myDomainAdobe = 'adobecqms.net';
    if (url && (url.includes('http://') || url.includes('https://')) && !url.includes(myDomain) && !url.includes(myDomainAdobe)) {
      $this.attr('target','_blank');
    }
    else {
      $this.attr('target','_self');
    }
    const linkType = $this.attr('target') === '_blank'? 'external' :'internal';

    const trackingObj = {
      linkType,
      linkName,
      linkSection: linkText ? 'Footer Navigation' : 'Footer - Social Share link',
      linkParentTitle: ''
    };
    const eventObj = {
      eventType: 'linkClick',
      event: linkText ? 'Footer' : 'Footer - Social Share'
    };
    trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);
  }

  isExternal(thisHref){
    return isExternal(thisHref);
  }

  addLinkAttr() {
    const that = this;
    $('.tp-pw-footer-data-analytics').each(function () {
      const thisHref = $(this).attr('href');
      if (thisHref) {
        if (that.isExternal(thisHref)) {
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
