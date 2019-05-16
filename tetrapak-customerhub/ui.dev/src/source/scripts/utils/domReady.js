/**
 * Add code which needs to be executed on DOM ready
 * @author Sachin Singh
 * @date 16/02/2019
 */
import dynamicMedia from './dynamicMedia';
import { toast } from './toast';
import { $body } from './commonSelectors';
import { isFirefox, isIE, isEdge } from './browserDetect';
import { isTablet, isMobile } from '../common/common';

export default {
  init() {
    // Dynamic media
    dynamicMedia.init();
    // Toast error messages
    toast.init();
    // Body events
    $body.on('click', 'js-prevent-default', (e) => {
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
