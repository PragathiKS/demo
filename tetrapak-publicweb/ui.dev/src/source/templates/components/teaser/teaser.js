import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';


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
    const $target = $(e.target);
    const $this = $target.closest('.js-teaser-analytics');
    let trackingObj = {};
    let eventType = 'content-load';
    const downloadtype = $this.data('download-type');

    if(downloadtype ==='download'){
      eventType = 'download';
      trackingObj = {
        eventType
      };
    } else {
      trackingObj = {
        eventType
      };
    }

    trackAnalytics(trackingObj, 'linkClick', 'TextImageClick', undefined, false);
    window.open($this.attr('href'), $this.attr('target'));
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default Teaser;
