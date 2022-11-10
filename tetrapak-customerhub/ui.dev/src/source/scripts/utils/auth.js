import $ from 'jquery';
import { ajaxWrapper } from './ajax';
import { RESULTS_EMPTY, ajaxMethods, API_TOKEN, AUTH_TOKEN_COOKIE, EVT_REFRESH_INITIATE, EVT_POST_REFRESH, REFRESH_TIMEOUT } from './constants';
import { storageUtil } from '../common/common';
import { getURL } from './uri';
import { refreshToken } from './tokenRefresh';
import { $body } from './commonSelectors';
import { logger } from './logger';

/**
 * Generates a valid APIGEE token and ensures token validity
 */
function generateToken() {
  const env_var = $('.tp-financials').attr('data-src_ispublishenvironment')
    || $('.tp-my-equipment').attr('data-src_ispublishenvironment')
    || $('.tp-equipment-details').attr('data-src_ispublishenvironment')
    || $('.tp-add-equipment').attr('data-src_ispublishenvironment')
    || $('.tp-aip-trainings').attr('data-src_ispublishenvironment')
    || $('.tp-aip-licenses').attr('data-src_ispublishenvironment')
    || $('.tp-tech-pub').attr('data-src_ispublishenvironment')
    || $('.tp-rk').attr('data-src_ispublishenvironment')
    || $('.tp-rk-detail').attr('data-src_ispublishenvironment');
  return (
    new Promise(function (resolve, reject) {
      const access_token = storageUtil.get(AUTH_TOKEN_COOKIE);
      if (access_token) {
        resolve({
          data: {
            access_token
          },
          textStatus: 'success',
          jqXHR: {
            fromStorage: true
          }
        });
      } else if(env_var === 'true'){
        ajaxWrapper.getXhrObj({
          url: getURL(API_TOKEN),
          method: ajaxMethods.GET
        }).done(function (data, textStatus, jqXHR) {
          try {
            if (data && data.status === 'success') {
              const result = JSON.parse(data.result);
              const expiry = (+result.expires_in) / (24 * 60 * 60);
              storageUtil.setCookie(AUTH_TOKEN_COOKIE, `${result.access_token}`, expiry);
              storageUtil.set(REFRESH_TIMEOUT, (Date.now() + (60 * 60 * 1000)));
              $body.trigger(EVT_REFRESH_INITIATE);
              resolve({
                data: result,
                textStatus,
                jqXHR
              });
            } else {
              throw new Error(RESULTS_EMPTY);
            }
          } catch (e) {
            reject({
              data: {
                access_token: null
              },
              textStatus: 'empty',
              jqXHR
            });
          }
        }).fail(function (jqXHR, textStatus) {
          reject({
            data: {
              access_token: null
            },
            textStatus,
            jqXHR
          });
        });
      } else {
        logger.log('No api gee call on author for financial page');
      }
    })
  );
}

/**
 * Resolves arguments
 * @param {function} callback Callback function
 * @param {object|any[]} response Promise response
 */
function getArgs(callback, response) {
  const args = [callback];
  if (Array.isArray(response)) {
    args.push($.extend(...response));
  } else {
    args.push(response);
  }
  return args;
}

/**
 * Executes callback if promise resolves
 * @param {Function} callback Callback function
 * @param  {...any} args Promise arguments
 */
function execCallback(callback, ...args) {
  this.tokenPromise = null;
  if (typeof callback === 'function') {
    callback(...args);
  }
}

/**
 * Executes callback if promise is rejected
 * @param {Function} callback Callback function
 */
function handleRejection(callback, ...args) {
  this.tokenPromise = null;
  storageUtil.removeCookie(AUTH_TOKEN_COOKIE);
  if (typeof callback === 'function') {
    callback(...args);
  }
}

export default {
  tokenPromise: null,
  /**
   * Retrieves a valid APIGEE token
   * @param {Function} callback Success callback
   */
  getToken(callback) {
    refreshToken(() => {
      if (!this.tokenPromise) {
        this.tokenPromise = generateToken();
      }
      return this.tokenPromise
        .then(response => execCallback.apply(this, getArgs(callback, response)))
        .catch(error => handleRejection.apply(this, getArgs(callback, error)));
    });
  },
  init() {
    $body.on(EVT_POST_REFRESH, () => {
      this.getToken(() => {
        logger.log('[TokenRefresh]: Bearer token refreshed');
      });
    });
  }
};
