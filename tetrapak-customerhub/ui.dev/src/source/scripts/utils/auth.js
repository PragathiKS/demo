import { ajaxWrapper } from './ajax';
import 'core-js/features/promise';
import { RESULTS_EMPTY, ajaxMethods, API_TOKEN } from './constants';
import { storageUtil } from '../common/common';
import { getURL } from './uri';

/**
 * Generates a valid APIGEE token and ensures token validity
 */
function generateToken() {
  return (
    new Promise(function (resolve, reject) {
      const access_token = storageUtil.get('authToken');
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
      } else {
        ajaxWrapper.getXhrObj({
          url: getURL(API_TOKEN),
          method: ajaxMethods.GET
        }).done(function (data, textStatus, jqXHR) {
          try {
            if (data && data.status === 'success') {
              const result = JSON.parse(data.result);
              const expiry = (+result.expires_in) / (24 * 60 * 60 * 1000);
              storageUtil.setCookie('authToken', result.access_token, expiry);
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
              jqXHR,
              textStatus: 'empty',
              errorThrown: e.message
            });
          }
        }).fail(function (jqXHR, textStatus, errorThrown) {
          reject({
            jqXHR,
            textStatus,
            errorThrown
          });
        });
      }
    })
  );
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
function handleRejection(callback) {
  this.tokenPromise = null;
  storageUtil.removeCookie('authToken');
  if (typeof callback === 'function') {
    callback();
  }
}

export default {
  tokenPromise: null,
  /**
   * Retrieves a valid APIGEE token
   * @param {Function} callback Success callback
   * @param {Function} errorCallback Failure callback
   */
  getToken(callback, errorCallback) {
    if (!this.tokenPromise) {
      this.tokenPromise = generateToken();
    }
    return this.tokenPromise
      .then((...args) => execCallback.apply(this, [callback, ...args]))
      .catch(() => handleRejection.apply(this, [errorCallback]));
  }
};
