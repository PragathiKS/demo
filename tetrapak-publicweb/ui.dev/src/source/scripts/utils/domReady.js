/**
 * Add code which needs to be executed on DOM ready
 * @author Sachin Singh
 * @date 16/02/2019
 */
import dynamicMedia from './dynamicMedia';
import customEvents from './customEvents';
import { $body } from './commonSelectors';
import $ from 'jquery';

export default {
  init() {
    customEvents.init();
    dynamicMedia.init();
    $body.on('show.bs.modal', function () {
      $(this).addClass('tp-no-backdrop');
    }).on('hidden.bs.modal', function () {
      const $this = $(this);
      if ($('.modal.show').length === 0) {
        $this.removeClass('tp-no-backdrop');
      } else {
        $this.addClass('modal-open');
      }
    });
  }
};
