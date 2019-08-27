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
import { isTablet, isMobile } from '../common/common';
import { responsive } from './responsive';
import { customDropdown } from './customDropdown';
import videoAnalytics from './videoAnalytics';
import customEvents from './customEvents';
import auth from './auth';
import tokenRefresh from './tokenRefresh';

export default {
  init() {
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
      $(this).removeClass('tp-no-backdrop');
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
  }
};
