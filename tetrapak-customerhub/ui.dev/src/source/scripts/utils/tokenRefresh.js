import 'core-js/features/promise';
import { storageUtil, isCurrentPageIframe, getMaxSafeInteger, isLocalhost } from '../common/common';
import { $body, $win } from './commonSelectors';
import { ACC_TOKEN_COOKIE, EVT_TOKEN_REFRESH, EVT_REFRESH_INITIATE, AUTH_TOKEN_COOKIE, DELETE_COOKIE_SERVLET_URL, EVT_POST_REFRESH, AUTH_TOKEN_EXPIRY, EMPTY_PAGE_URL, EVT_IFRAME_TIMEOUT, TOKEN_REFRESH_IFRAME_TIMEOUT, ajaxMethods } from './constants';
import { logger } from './logger';
import { ajaxWrapper } from './ajax';

const cache = {};

const MAX_SAFE_INTEGER = getMaxSafeInteger();

const { protocol, host } = window.location;

const iFrameSrc = `${protocol}//${host}${(
  isLocalhost()
    ? '/content/customerhub-ux/pageredirect.ux-preview.html'
    : EMPTY_PAGE_URL
)}`;

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
  const remainingTime = (+savedTimestamp) - currentTimestamp - (5 * 60 * 1000); // Substracting 5 minutes from original difference
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
  // Reset timeout
  clearTimeout(cache.iframeTimeoutRef);
  logger.log(`[TokenRefresh]: Refresh token iframe timeout is cleared`);
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
      ajaxWrapper.getXhrObj({
        method: ajaxMethods.GET,
        url: DELETE_COOKIE_SERVLET_URL
      }).always(() => {
        $(document.body).append(iFrame);
        iFrame.src = iFrameSrc;
        $win.trigger(EVT_IFRAME_TIMEOUT);
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
    $win.on(EVT_IFRAME_TIMEOUT, () => {
      clearTimeout(cache.iframeTimeoutRef);
      cache.iframeTimeoutRef = setTimeout(() => {
        logger.log('[TokenRefresh]: Refresh occurred due to timeout');
        postResolveHandler();
      }, TOKEN_REFRESH_IFRAME_TIMEOUT);
      logger.log(`[TokenRefresh]: Refresh token iframe will timeout in ${TOKEN_REFRESH_IFRAME_TIMEOUT}ms`);
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
