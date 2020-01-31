import $ from 'jquery';
import { $body } from './commonSelectors';

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
  }
};
/* eslint-enable new-cap */
