import $ from 'jquery';
import { getLinkClickAnalytics,addLinkAttr } from '../../../scripts/common/common';

class TextImage {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$textImageLink = this.root.find('.js-textImage-analytics');
    this.cache.componentName = this.root.find('#componentName').val();
  }
  bindEvents() {
    this.cache.$textImageLink.on('click', this.trackAnalytics);

    this.root.find('.js-softconversion-pw').on('click', (e) => {
      getLinkClickAnalytics(e,'image-title','Text & Image','.js-softconversion-pw', false);
      $('body').find('.'+this.cache.componentName).trigger('showsoftconversion-pw');
    });
  }

  trackAnalytics = e => {
    e.preventDefault();
    getLinkClickAnalytics(e,'image-title','Text & Image','.js-textImage-analytics');
  };

  init() {
    this.initCache();
    this.bindEvents();
    addLinkAttr('.js-textImage-analytics');
  }
}

export default TextImage;
