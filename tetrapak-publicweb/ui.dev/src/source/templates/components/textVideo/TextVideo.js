
import $ from 'jquery';
import { render } from '../../../scripts/utils/render';
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

  toggleVideoContent(noVideo) {
    if(noVideo === ',1,' || noVideo === ',1,2,') {
      $('.pw-text-video__video').css('display', 'none');
      if(!$('.pw-text-video_content').children().length) {
        render.fn({
          template: 'noVideoContent',
          data: {  },
          target: '.pw-text-video_content',
          hidden: false
        });
      }
      $('.pw-text-video__novideo').css('display', 'block');
    } else {
      $('.pw-text-video__video').css('display', 'block');
      $('.pw-text-video__novideo').css('display', 'none');
    }
  }

  bindEvents() {
    const { $textVideoButton } = this.cache;
    $textVideoButton.on('click', this.trackAnalytics);

    this.root.find('.js-softconversion-pw-textvideo').on('click', (e) => {
      getLinkClickAnalytics(e,'video-title','Text & Video','.js-softconversion-pw-textvideo', false);
      $('body').find('.'+this.cache.componentName).trigger('showsoftconversion-pw');
    });

    this.root.find('.js-subscription-pw-textvideo').on('click', (e) => {
      getLinkClickAnalytics(e,'video-title','Text & Video','.js-subscription-pw-textvideo', false);
      $('body').find('.'+this.cache.componentName).trigger('showSubscription-pw');
    });

    setTimeout(() => {
      $('#accept-recommended-btn-handler').on('click', () => {
        $('.pw-text-video__video').css('display', 'block');
        $('.pw-text-video__novideo').css('display', 'none');
      });

      $('.save-preference-btn-handler').on('click', () => {
        const noVideo = window.OptanonActiveGroups;
        if(noVideo === ',1,' || noVideo === ',1,2,') {
          $('.pw-text-video__video').css('display', 'none');
          if(!$('.pw-text-video__novideo').children().length) {
            render.fn({
              template: 'noVideoContent',
              data: {  },
              target: '.pw-text-video__novideo',
              hidden: false
            });
          }
          $('.pw-text-video__novideo').css('display', 'block');
        } else {
          $('.pw-text-video__video').css('display', 'block');
          $('.pw-text-video__novideo').css('display', 'none');
        }
      });
    },2500);
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
