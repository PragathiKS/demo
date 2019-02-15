/**
 * Add code which needs to be executed on DOM ready
 * @author Sachin Singh
 * @date 16/02/2019
 */
import LazyLoad from 'vanilla-lazyload';

export default {
  init() {
    new LazyLoad({
      elements_selector: '.tp-image[data-src]'
    });
  }
};
