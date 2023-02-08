import $ from 'jquery';
import { getLinkClickAnalytics,addLinkAttr } from '../../../scripts/common/common';
class TextVideo {
  constructor({ el }) {
    this.root = $(el);
  }

  cache = {};

  initCache() {
    this.cache.$textVideoButton = this.root.find('.js-textVideo-analytics');
    this.cache.componentName = this.root.find('.componentName-textvideo').val();
  }

  bindEvents() {
    const { $textVideoButton } = this.cache;
    $textVideoButton.on('click', this.trackAnalytics);
    setTimeout(function(){
      const noVideo = window.OptanonActiveGroups;
      if (!noVideo.includes('4')) {
        $('.pw-text-video__video').css('display', 'none');
        $('.pw-text-video__novideo').css('display', 'flex');
      }
    }, 2000);

    this.root.find('.js-softconversion-pw-textvideo').on('click', (e) => {
      getLinkClickAnalytics(e,'video-title','Text & Video','.js-softconversion-pw-textvideo', false);
      $('body').find('.'+this.cache.componentName).trigger('showsoftconversion-pw');
    });

    this.root.find('.js-subscription-pw-textvideo').on('click', (e) => {
      getLinkClickAnalytics(e,'video-title','Text & Video','.js-subscription-pw-textvideo', false);
      $('body').find('.'+this.cache.componentName).trigger('showSubscription-pw');
    });
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
