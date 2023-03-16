import $ from 'jquery';
import { $body } from './commonSelectors';
import { render } from './render';

function toggleVideoContent(noVideo) {
  if (noVideo === ',1,' || noVideo === ',1,2,') {
    $('.pw-text-video__video').css('display', 'none');
    if (!$('.pw-text-video_content').children().length) {
      render.fn({
        template: 'noVideoContent',
        data: {},
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

/* eslint-disable new-cap */
export default {
  init() {
    // Define custom events
    $body.on('keydown', function (e) {
      const currentKeyCode = e.keyCode || e.which;
      const currentTarget = $(e.target);
      if (currentKeyCode === 13) {
        currentTarget.trigger($.Event('key.return', $.extend(e, { type: 'key.return' })));
      }
      if (currentKeyCode === 27) {
        currentTarget.trigger($.Event('key.esc', $.extend(e, { type: 'key.esc' })));
      }
    }).on('key.esc', function () {
      // Check for open modals and hide them
      $('.modal.show').modal('hide');
    });

    $('.validateForTags').on('keypress', function (e) {
      if(e.keyCode === 60 || e.keyCode === 62) {
        return false;
      }
    });

    // YouTube video cookie handler
    setTimeout(() => {
      if (document.cookie.includes('OptanonAlertBoxClosed')) {
        toggleVideoContent(window.OptanonActiveGroups);
      }
  
      $('#accept-recommended-btn-handler, #onetrust-accept-btn-handler').on(
        'click',
        () => {
          $('.pw-text-video__video').css('display', 'block');
          $('.pw-text-video__novideo').css('display', 'none');
        }
      );
  
      $('.save-preference-btn-handler').on('click', () => {
        toggleVideoContent(window.OptanonActiveGroups);
      });
    }, 2500);
  }
};
/* eslint-enable new-cap */
