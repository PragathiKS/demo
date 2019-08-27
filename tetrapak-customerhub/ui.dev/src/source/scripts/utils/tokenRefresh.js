import 'core-js/features/promise';
import { storageUtil, isCurrentPageIframe, getMaxSafeInteger } from '../common/common';
import { $body } from './commonSelectors';
import { ACC_TOKEN_COOKIE, EVT_TOKEN_REFRESH, EVT_REFRESH_INITIATE, AUTH_TOKEN_COOKIE, DELETE_COOKIE_SERVLET_URL, EVT_POST_REFRESH, AUTH_TOKEN_EXPIRY, EMPTY_PAGE_URL, ajaxMethods } from './constants';
import { logger } from './logger';
import { ajaxWrapper } from './ajax';

const cache = {};

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
  logger.log('[Webpack]: Token timer initiated');
  if (cache.tokenTimeout) {
    clearTimeout(cache.tokenTimeout);
  }
  const currentTimestamp = Date.now();
  const savedTimestamp = storageUtil.get(AUTH_TOKEN_EXPIRY);
  const remainingTime = (+savedTimestamp) - currentTimestamp - (2 * 60 * 1000); // Substracting 2 minutes from original difference
  if (remainingTime <= 0) {
    // Either token refresh already happened or is pending
    // Check if a valid access token has already been created
    logger.log('[Webpack]: Entered a dead zone');
    if (!storageUtil.get(ACC_TOKEN_COOKIE)) {
      // If cookie doesn't exists then trigger refresh
      $body.trigger(EVT_TOKEN_REFRESH);
    }
  } else {
    const MAX_SAFE_INTEGER = getMaxSafeInteger();
    const timeoutTime = (remainingTime > MAX_SAFE_INTEGER) ? MAX_SAFE_INTEGER : remainingTime;
    cache.tokenTimeout = setTimeout(() => {
      $body.trigger(EVT_TOKEN_REFRESH);
      clearTimeout(cache.tokenTimeout);
    }, timeoutTime);
    logger.log(`[Webpack]: Remaining time ${timeoutTime}ms`);
  }
}

/**
 * Triggered when iframe is loaded
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
      let iFrame = null;
      const existingIframe = $('.js-token-ifrm');
      if (!existingIframe.length) {
        iFrame = document.createElement('iframe');
        document.body.appendChild(iFrame);
        iFrame.classList.add('d-none');
        iFrame.classList.add('js-token-ifrm');
      } else {
        iFrame = existingIframe[0];
      }
      ajaxWrapper.getXhrObj({
        url: DELETE_COOKIE_SERVLET_URL,
        data: {
          redirectURL: EMPTY_PAGE_URL
        },
        method: ajaxMethods.GET
      }).always(postResolveHandler);
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
