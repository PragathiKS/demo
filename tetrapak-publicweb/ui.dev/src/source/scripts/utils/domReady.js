/**
 * Add code which needs to be executed on DOM ready
 * @author Sachin Singh
 * @date 16/02/2019
 */
import dynamicMedia from './dynamicMedia';
import contactAnchorLink from '../../templates/components/contactAnchorLink/ContactAnchorLink';

export default {
  init() {
    dynamicMedia.init();
    contactAnchorLink.init();
  }
};
