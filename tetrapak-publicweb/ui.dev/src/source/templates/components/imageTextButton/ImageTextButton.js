import $ from 'jquery';

class ImageTextButton {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$imageTextButtonLink = this.root.find('.js-textImage-analytics');;
  }
  bindEvents() {
    this.cache.$imageTextButtonLink.on('click', this.trackAnalytics);
  }
 


  trackAnalytics = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-textImage-analytics');
    let trackingObj = {};
    let eventType = 'content-load';
    const downloadtype = $this.data('download-type');

    if(downloadtype ==='download'){
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

export default ImageTextButton;
