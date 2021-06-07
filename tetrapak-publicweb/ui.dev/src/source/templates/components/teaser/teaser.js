import $ from 'jquery';
import { addLinkAttr,getLinkClickAnalytics } from '../../../scripts/common/common';


class Teaser {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$teaserLink = this.root.find('.js-teaser-analytics');
  }
  bindEvents() {
    this.cache.$teaserLink.on('click', this.trackAnalytics);
  }

  trackAnalytics = (e) => {
    e.preventDefault();
    getLinkClickAnalytics(e,'paren-link-title','Teaser','.js-teaser-analytics');
  }

  init() {
    this.initCache();
    this.bindEvents();
    addLinkAttr('.js-teaser-analytics');
  }
}

export default Teaser;
