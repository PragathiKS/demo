import 'core-js/features/array/includes';
import LZStorage from 'lzstorage';
import $ from 'jquery';
import { IS_MOBILE_REGEX, IS_TABLET_REGEX } from '../utils/constants';
import { $global } from '../utils/commonSelectors';
import { templates } from '../utils/templates';

const currentUserAgent = window.navigator.userAgent;

// Initialize storage utility
export const storageUtil = new LZStorage();

const { getCookie } = storageUtil;

// Initialize functions for user agent detection

/**
 * Checks if current user agent belongs to mobile
 */
export const isMobile = () => {
  return IS_MOBILE_REGEX.test(currentUserAgent);
};

/**
 * Checks if current user agent belongs to tablet
 */
export const isTablet = () => {
  return IS_TABLET_REGEX.test(currentUserAgent);
};

/**
 * Checks if current user agent belongs to desktop
 */
export const isDesktop = () => !(isMobile() || isTablet());

/**
 * Checks if current screen mode is mobile
 */
export const isMobileMode = () => ($(window).outerWidth() < 768);

/**
 * Checks if current screen mode is tablet
 */
export const isTabletMode = () => {
  const outerWidth = $(window).outerWidth();
  return outerWidth >= 768 && outerWidth < 992;
};

/**
 * Checks if current screen mode is mobile or tablet
 */
export const isMobileOrTabletMode = () => (isMobileMode() || isTabletMode());

/**
 * Checks if current screen mode is desktop
 */
export const isDesktopMode = () => !isMobileOrTabletMode();

/**
 * Checks if current screen mode is tablet or desktop
 */
export const isTabletOrDesktopMode = () => (isTabletMode() || isDesktopMode());

/**
 * Checks if author mode is enabled
 */
export const isAuthorMode = () => {
  const wcmmode = getCookie('wcmmode');
  return ['edit', 'preview', 'design'].includes(wcmmode);
};

/**
 * Checks if given param is callable
 * @param {*} param Any param
 */
export const isCallable = (param) => (typeof param === 'function');

/**
 * Scrolls the page to a particular element
 * @param {string|object} selector Selector or element
 * @param {number} duration Duration in number
 * @param {function} callback Callback function
 */
export const scrollToElement = (selector = document.body, duration = 500, callback) => {
  let executed = false;
  $global.animate(
    {
      scrollTop: $(selector).offset().top
    },
    {
      duration,
      complete() {
        if (!executed) {
          executed = true;
          if (isCallable(callback)) {
            callback.apply(this, arguments);
          }
        }
      }
    }
  );
};

/**
 * Scrolls the page to given offset location
 * @param {number} offset Offset from top
 * @param {number} duration Duration in number
 * @param {function} callback Callback function
 */
export const scrollToOffset = (offset, duration = 500, callback) => {
  let executed = false;
  $global.animate(
    {
      scrollTop: offset
    },
    {
      duration,
      complete() {
        if (!executed) {
          executed = true;
          if (isCallable(callback)) {
            callback.apply(this, arguments);
          }
        }
      }
    }
  );
};

/**
 * Scrolls the page to top
 * @param {number} duration Duration in number
 * @param {function} callback Callback function
 */
export const scrollToTop = (duration = 500, callback) => {
  scrollToOffset(0, duration, callback);
};

/**
 * Loader class to automatically insert loading animation through code
 */
export class Loader {
  constructor(loader) {
    this.context = $(loader);
    this.length = this.context.length;
    if (this.length) {
      this.htmlText = this.context[0].outerHTML;
      this[0] = this.context[0];
    }
  }
  target(el) {
    if (this.length) {
      $(el).html(this.context);
    }
    return this;
  }
  appendTo(el) {
    if (this.length) {
      $(el).append(this.context);
    }
    return this;
  }
  prependTo(el) {
    if (this.length) {
      $(el).prepend(this.context);
    }
    return this;
  }
  insertAfter(el) {
    if (this.length) {
      $(el).after(this.context);
    }
    return this;
  }
  insertBefore(el) {
    if (this.length) {
      $(el).before(this.context);
    }
    return this;
  }
  remove() {
    return this.context.remove();
  }
}

/**
 * Returns a loader object with default template
 * @param {object} target Target where loader needs to be placed
 * @param {boolean} appendMode Append mode
 */
export const loader = (target, appendMode) => {
  const loading = templates.loading();
  if (target) {
    $(target)[appendMode ? 'append' : 'html'](loading);
  } else {
    return new Loader(loading);
  }
};
