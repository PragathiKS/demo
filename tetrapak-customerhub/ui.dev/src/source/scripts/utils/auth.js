import $ from 'jquery';
import { ajaxWrapper } from './ajax';
import 'core-js/features/promise';
import { RESULTS_EMPTY, ajaxMethods, API_TOKEN } from './constants';
import { storageUtil, strCompressed } from '../common/common';
import { getURL } from './uri';
import { $body } from './commonSelectors';

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
  storageUtil.removeCookie('authToken');
  if (typeof callback === 'function') {
    callback(...args);
  }
}

/**
 * Fetches currency mapping
 */
function fetchCurrencyMapping() {
  return new Promise((resolve, reject) => {
    const currencyMappingURL = $body.find('#currencyMappingURL').val();
    if (typeof currencyMappingURL === 'string' && currencyMappingURL.length) {
      const isoMapping = strCompressed.get('isoMapping');
      if (isoMapping) {
        resolve(isoMapping);
      } else {
        ajaxWrapper.getXhrObj({
          url: currencyMappingURL
        }).done(response => {
          strCompressed.set('isoMapping', response, true);
          resolve(response);
        }).fail((jqXHR, textStatus) => {
          reject({
            data: {
              access_token: null
            },
            textStatus,
            jqXHR
          });
        });
      }
    } else {
      reject({
        data: {
          access_token: null
        },
        textStatus: 'empty',
        jqXHR: {}
      });
    }
  });
}

export default {
  tokenPromise: null,
  currencyPromise: null,
  /**
   * Retrieves a valid APIGEE token
   * @param {Function} callback Success callback
   */
  getToken(callback) {
    if (!this.tokenPromise) {
      this.tokenPromise = generateToken();
    }
    if (!this.currencyPromise) {
      this.currencyPromise = fetchCurrencyMapping();
    }
    return Promise.all([
      this.tokenPromise,
      this.currencyPromise
    ]).then(response => execCallback.apply(this, getArgs(callback, response)))
      .catch(error => handleRejection.apply(this, getArgs(callback, error)));
  }
};
