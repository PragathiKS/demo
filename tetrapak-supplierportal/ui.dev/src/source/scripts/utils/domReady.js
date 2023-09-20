/**
 * Add code which needs to be executed on DOM ready
 * @author Sachin Singh
 * @date 16/02/2019
 */
import dynamicMedia from './dynamicMedia';
import movehash from './moveHash';
import { $body } from './commonSelectors';
import $ from 'jquery';
import { responsive } from './responsive';
import { isCurrentPageIframe } from '../common/common';
import auth from './auth';
import tokenRefresh from './tokenRefresh';

export default {
  init() {
    // Auth and Token refresh
    tokenRefresh.init();
    auth.init();
    dynamicMedia.init();
    responsive.init();
    movehash();
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

    const isIframe = isCurrentPageIframe();
    if (isIframe && $('.js-empty-page-script').length === 0) {
      window.parent.postMessage({
        refresh: true
      });
    }
    const $autoRefreshSession = $('#autoRefreshSession');
    if (!isIframe && $autoRefreshSession.length && $autoRefreshSession.val() === 'true') {
      // Fetch bearer token to start token refresh timer
      auth.getToken();
    }
  }
};
