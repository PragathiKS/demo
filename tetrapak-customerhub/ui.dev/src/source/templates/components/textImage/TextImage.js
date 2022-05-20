import $ from 'jquery';
import { getLinkClickAnalytics, addLinkAttr } from '../../../scripts/common/common';
class TextImage {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$textImageLink = this.root.find('.js-textImage-analytics');
    this.cache.componentName = this.root.find('.componentNameTextImage').val();
  }
  bindEvents() {
    this.cache.$textImageLink.on('click', this.trackAnalytics);
  }

  trackAnalytics = e => {
    e.preventDefault();
    getLinkClickAnalytics(e,'image-title','Text & Image','.js-textImage-analytics');
  };


  seoChanges() {
    const titleDiv = this.root.find('.pw-text-image__title');
    const h1tag = titleDiv.find('h1') ;
    if( h1tag.length) {
      $(h1tag).attr('class','tpatom-heading tpatom-heading--regular');
      const h2Tag = titleDiv.find('h2')[0];
      h2Tag && h2Tag.parentNode.removeChild(h2Tag);
    }
  }

  init() {
    this.initCache();
    this.bindEvents();
    this.seoChanges();
    addLinkAttr('.js-textImage-analytics');
  }
}

export default TextImage;
