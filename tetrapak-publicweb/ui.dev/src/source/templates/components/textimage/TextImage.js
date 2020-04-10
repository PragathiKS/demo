import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';


class TextImage {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$textImageLink = this.root.find('.js-textImage-analytics');
  }
  bindEvents() {
    this.cache.$textImageLink.on('click', this.trackAnalytics);
  }



  trackAnalytics = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-textImage-analytics');
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

export default TextImage;
