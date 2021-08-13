import $ from 'jquery';
import { getLinkClickAnalytics } from 'tpPublic/scripts/common/common';

class Footer {
  constructor({ el }) {
    this.root = $(el);
  }

  cache = {};
  initCache() {
    this.cache.linkClassSelector = 'a.tp-pw-footer-data-analytics';
    this.cache.$footerLogo = this.root.find(`.tp-pw-footer__logo ${this.cache.linkClassSelector}`);
    this.cache.$footerLinks = this.root.find(`.tp-pw-footer__links-list ${this.cache.linkClassSelector}`);
    this.cache.$footerSocialMedias = this.root.find(`.tp-pw-footer__social-media-items ${this.cache.linkClassSelector}`);
  }

  bindEvents() {
    this.cache.$footerLogo.on('click', this.logoTrackAnalytics);
    this.cache.$footerLinks.on('click', this.linksTrackAnalytics);
    this.cache.$footerSocialMedias.on('click', this.socialMediasTrackAnalytics);
  }

  logoTrackAnalytics = e => {
    e.preventDefault();
    getLinkClickAnalytics(e, '', 'Footer', this.cache.linkClassSelector, true, { linkParentTitle: '' });
  }

  linksTrackAnalytics = e => {
    e.preventDefault();
    getLinkClickAnalytics(e, '', 'Footer', this.cache.linkClassSelector, true, { linkParentTitle: '' });
  }

  socialMediasTrackAnalytics = e => {
    e.preventDefault();
    getLinkClickAnalytics(e, '', 'Footer - Social Share', this.cache.linkClassSelector, true, { linkParentTitle: '' });
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default Footer;