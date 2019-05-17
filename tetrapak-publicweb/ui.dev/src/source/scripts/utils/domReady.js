/**
 * Add code which needs to be executed on DOM ready
 * @author Sachin Singh
 * @date 16/02/2019
 */
import dynamicMedia from './dynamicMedia';
import contactAnchorLink from '../../templates/components/contactAnchorLink/ContactAnchorLink';
import { $body } from './commonSelectors';

export default {
  init() {
    dynamicMedia.init();
    contactAnchorLink.init();
    $body.on('show.bs.modal', function () {
      $(this).addClass('tp-no-backdrop');
    }).on('hidden.bs.modal', function () {
      $(this).removeClass('tp-no-backdrop');
    });
  }
};
