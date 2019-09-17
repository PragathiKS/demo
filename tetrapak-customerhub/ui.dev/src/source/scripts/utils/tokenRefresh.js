import 'core-js/features/promise';
import { storageUtil, isCurrentPageIframe, getMaxSafeInteger, isLocalhost } from '../common/common';
import { $body } from './commonSelectors';
import { ACC_TOKEN_COOKIE, EVT_TOKEN_REFRESH, EVT_REFRESH_INITIATE, AUTH_TOKEN_COOKIE, DELETE_COOKIE_SERVLET_URL, EVT_POST_REFRESH, AUTH_TOKEN_EXPIRY, EMPTY_PAGE_URL } from './constants';
import { logger } from './logger';

const cache = {};

const MAX_SAFE_INTEGER = getMaxSafeInteger();

const deleteCookieServletURL = isLocalhost()
  ? 'http://localhost:4502/content/customerhub-ux/pageredirect.ux-preview.html'
  : DELETE_COOKIE_SERVLET_URL;

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
 * Initiates a timer to refresh token
 */
function initiateTokenTimer() {
  logger.log('[TokenRefresh]: Token timer initiated');
  if (cache.tokenTimeout) {
    clearTimeout(cache.tokenTimeout);
  }
  const currentTimestamp = Date.now();
  const savedTimestamp = storageUtil.get(AUTH_TOKEN_EXPIRY);
  const remainingTime = (+savedTimestamp) - currentTimestamp - (2 * 60 * 1000); // Substracting 2 minutes from original difference
  if (remainingTime <= 0) {
    // Either token refresh already happened or is pending
    // Check if a valid access token has already been created
    logger.log('[TokenRefresh]: Entered a dead zone');
    if (!storageUtil.get(ACC_TOKEN_COOKIE)) {
      // If cookie doesn't exists then trigger refresh
      $body.trigger(EVT_TOKEN_REFRESH);
    }
  } else {
    const timeoutTime = (remainingTime > MAX_SAFE_INTEGER) ? MAX_SAFE_INTEGER : remainingTime;
    cache.tokenTimeout = setTimeout(() => {
      $body.trigger(EVT_TOKEN_REFRESH);
      clearTimeout(cache.tokenTimeout);
    }, timeoutTime);
    logger.log(`[TokenRefresh]: Remaining time ${timeoutTime}ms`);
  }
}

/**
 * Triggered when delete cookie AJAX call is completed
 */
function postResolveHandler() {
  // Remove iframe
  $('.js-token-refresh-ifrm').remove();
  // Remove cookie if it still exists
  if (storageUtil.get(AUTH_TOKEN_COOKIE)) {
    storageUtil.removeCookie(AUTH_TOKEN_COOKIE);
  }
  $body.trigger(EVT_POST_REFRESH);
  logger.log(`[TokenRefresh]: Access token refreshed`);
}

/**
 * Triggers token refresh cycle
 */
function triggerRefresh() {
  if (!cache.refreshTokenPromise) {
    cache.refreshTokenPromise = new Promise((resolve) => {
      logger.log(`[TokenRefresh]: Token refresh triggered`);
      // Insert iframe
      const iFrame = document.createElement('iframe');
      $(iFrame).addClass('d-none js-token-refresh-ifrm');
      $(document.body).append(iFrame);
      iFrame.src = `${deleteCookieServletURL}?redirectURL=${EMPTY_PAGE_URL}`;
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

export default {
  bindEvents() {
    $body.on(EVT_TOKEN_REFRESH, function () {
      triggerRefresh().then(() => {
        cache.refreshTokenPromise = null;
      });
    }).on(EVT_REFRESH_INITIATE, function () {
      cache.authToken = storageUtil.get(AUTH_TOKEN_COOKIE);
      initiateTokenTimer();
    });
    window.addEventListener('message', (e) => {
      if (e.data && e.data.refresh) {
        postResolveHandler();
      }
    });
  },
  init() {
    if (!isCurrentPageIframe()) {
      this.bindEvents();
      cache.authToken = storageUtil.get(AUTH_TOKEN_COOKIE);
      // If auth token exists then previous token timer was initiated. This will restore the timer in case the page reloads.
      if (cache.authToken) {
        initiateTokenTimer();
      }
    }
  }
};
