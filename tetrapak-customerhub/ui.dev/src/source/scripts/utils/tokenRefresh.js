import 'core-js/features/promise';
import { storageUtil, isCurrentPageIframe, isLocalhost } from '../common/common';
import { $body } from './commonSelectors';
import { ACC_TOKEN_COOKIE, AUTH_TOKEN_COOKIE, DELETE_COOKIE_SERVLET_URL, EVT_POST_REFRESH, EMPTY_PAGE_URL, AUTH_WINDOW_NAME } from './constants';
import { logger } from './logger';

const cache = {};

const refreshTokenURL = isLocalhost()
  ? 'http://localhost:4502/content/customerhub-ux/pageredirect.ux-preview.html'
  : `${DELETE_COOKIE_SERVLET_URL}?redirectURL=${EMPTY_PAGE_URL}`;

/**
 * Opens a centered popup
 * @param {string} url URL string
 * @param {string} winName Window name
 * @param {string} width Width
 * @param {string} height Height
 * @param {string} scr Scroll
 */
function centeredPopup(url, winName, width, height, scr) {
  const leftPos = (screen.width) ? (screen.width - width) / 2 : 0;
  const topPos = (screen.height) ? (screen.height - height) / 2 : 0;
  const settings = `height=${height},width=${width},top=${topPos},left=${leftPos},scrollbars=${scr},resizable`;
  return window.open(url, winName, settings);
}

/**
 * Executes function if it's valid
 * @param {function} callback Callback function
 * @param  {...any} args Arguments
 */
function execFunc(callback, ...args) {
  if (typeof callback === 'function') {
    callback.apply(this, args);
  }
}

/**
 * Triggered when delete cookie AJAX call is completed
 */
function postResolveHandler() {
  if (storageUtil.get(AUTH_TOKEN_COOKIE)) {
    storageUtil.removeCookie(AUTH_TOKEN_COOKIE);
  }
  $body.trigger(EVT_POST_REFRESH);
  logger.log(`[Webpack]: Access token refreshed`);
}

/**
 * Triggers token refresh cycle
 */
function triggerRefresh() {
  if (!cache.refreshTokenPromise) {
    cache.refreshTokenPromise = new Promise((resolve) => {
      logger.log(`[Webpack]: Token refresh triggered`);
      const popupWindow = centeredPopup(refreshTokenURL, AUTH_WINDOW_NAME, '1', '1', 'no');
      $(popupWindow).one('beforeunload', () => {
        postResolveHandler();
        logger.log('[Webpack]: Window successfully closed');
      });
      $body.one(EVT_POST_REFRESH, resolve);
    });
  }
  return cache.refreshTokenPromise;
}

/**
 * Executes callback if token is refreshed
 * @param {function} callback Callback function
 */
export function refreshToken(callback) {
  const accessToken = storageUtil.getCookie(ACC_TOKEN_COOKIE);
  const authToken = storageUtil.get(AUTH_TOKEN_COOKIE);
  if (!accessToken && !authToken && !isCurrentPageIframe()) {
    triggerRefresh().then((...args) => {
      execFunc.apply(this, [callback, ...args]);
      cache.refreshTokenPromise = null;
    });
  } else {
    execFunc.apply(this, [callback]);
  }
}
