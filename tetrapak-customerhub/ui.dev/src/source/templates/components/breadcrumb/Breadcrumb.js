import $ from 'jquery';

import { trackAnalytics } from '../../../scripts/utils/analytics';

class Breadcrumb {
  constructor({ el }) {
    this.root = $(el);
  }

  cache = {};

  initCache() {
    this.cache.$breadcrumbLink = this.root.find('.js-tp_pw-breadcrumb__link');
  }

  bindEvents() {
    const { $breadcrumbLink } = this.cache;
    $breadcrumbLink.on('click', this.trackAnalytics);
  }

  trackAnalytics = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const url = $target.attr('href');
    const $this = $target.closest('.js-tp_pw-breadcrumb__link');
    const linkName = $this.text().trim();

    const trackingObj = {
      linkName,
      linkType : 'internal',
      linkSection: 'breadcrumb link',
      linkParentTitle: ''
    };
    const eventObj = {
      eventType: 'linkClick',
      event: 'breadcrumb'
    };

    trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);
    if(url){
      if (e.metaKey || e.ctrlKey || e.keyCode === 91 || e.keyCode === 224){
        window.open($this.attr('href'), '_blank');
      }
      else {
        window.open($this.attr('href'), '_self');
      }
    }
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default Breadcrumb;
