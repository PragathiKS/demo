import $ from 'jquery';
import { getLinkClickAnalytics,addLinkAttr } from '../../../scripts/common/common';
import { initializeDAMPlayer,ytPromise,initializeYoutubePlayer } from '../../../scripts/utils/videoAnalytics';
class TextVideo {
  constructor({ el }) {
    this.root = $(el);
  }

  cache = {};

  initCache() {
    this.cache.$textVideoButton = this.root.find('.js-textVideo-analytics');
    ytPromise.then(() => { initializeYoutubePlayer(); });
    initializeDAMPlayer();
  }

  bindEvents() {
    const { $textVideoButton } = this.cache;
    $textVideoButton.on('click', this.trackAnalytics);
  }


  trackAnalytics = (e) => {
    e.preventDefault();
    getLinkClickAnalytics(e,'video-title','Text & Video','.js-textVideo-analytics');
  }

  init() {
    this.initCache();
    this.bindEvents();
    addLinkAttr('.js-textVideo-analytics');
  }
}

export default TextVideo;
