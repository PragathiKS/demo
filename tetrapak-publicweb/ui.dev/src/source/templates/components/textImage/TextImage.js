import $ from 'jquery';
import { getLinkClickAnalytics,addLinkAttr } from '../../../scripts/common/common';

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
