import $ from 'jquery';
import { isExternal, isDownloable } from '../../../scripts/utils/updateLink';
import { getLinkClickAnalytics } from '../../../scripts/common/common';

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

  addLinkAttr() {
    $('.js-textImage-analytics').each(function() {
      const thisHref = $(this).attr('href');
      if (thisHref) {
        if (isExternal(thisHref)) {
          $(this).attr('target', '_blank');
        }
        if (isDownloable(thisHref)) {
          $(this).attr('target', '_blank');
          $(this).data('download-type', 'download');
        }
      }
    });
  }

  trackAnalytics = e => {
    e.preventDefault();
    getLinkClickAnalytics(e,'image-title','Text & Image','.js-textImage-analytics');
  };

  init() {
    this.initCache();
    this.bindEvents();
    this.addLinkAttr();
  }
}

export default TextImage;
