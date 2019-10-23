/**
 * Add code which needs to be executed on DOM ready
 * @author Sachin Singh
 * @date 16/02/2019
 */
import $ from 'jquery';
import dynamicMedia from './dynamicMedia';
import { toast } from './toast';
import { $body } from './commonSelectors';
import { isFirefox, isIE, isEdge } from './browserDetect';
import { isTablet, isMobile, isCurrentPageIframe } from '../common/common';
import { responsive } from './responsive';
import { customDropdown } from './customDropdown';
import videoAnalytics from './videoAnalytics';
import customEvents from './customEvents';
import feedback from './feedback';
import auth from './auth';
import tokenRefresh from './tokenRefresh';

export default {
  init() {
    // Feedback script
    feedback.init();
    // Custom events
    customEvents.init();
    // Auth and Token refresh
    tokenRefresh.init();
    auth.init();
    // Dynamic media
    dynamicMedia.init();
    // Toast error messages
    toast.init();
    // Responsive events
    responsive.init();
    // Custom dropdown
    customDropdown.init();
    // Video analytics
    videoAnalytics.init();
    // Body events
    if (isMobile() || isTablet()) {
      $body.addClass('is-touch');
    }
    $body.on('submit', '.js-prevent-default', (e) => {
      e.preventDefault();
    }).on('show.bs.modal', function () {
      $(this).addClass('tp-no-backdrop');
    }).on('hidden.bs.modal', function () {
      const $this = $(this);
      if ($('.modal.show').length === 0) {
        $this.removeClass('tp-no-backdrop');
      } else {
        $this.addClass('modal-open');
      }
    });
    $('.js-list-item__link').on('click', function () {
      const $this = $(this);
      window.open($this.data('href'), $this.data('target'));
    });
    // Custom scrollbar cross-browser handling
    if (
      isFirefox()
      || isIE()
      || isEdge()
      || isTablet()
      || isMobile()
    ) {
      $('[class*="custom-scrollbar"]:not(.custom-scrollbar-content)').addClass(`native${isTablet() ? ' tablet' : ''}`);
    }
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
