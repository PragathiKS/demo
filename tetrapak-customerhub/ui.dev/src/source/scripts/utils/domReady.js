/**
 * Add code which needs to be executed on DOM ready
 * @author Sachin Singh
 * @date 16/02/2019
 */
import dynamicMedia from './dynamicMedia';
import { toast } from './toast';

export default {
  init() {
    dynamicMedia.init();
    toast.init();
  }
};
