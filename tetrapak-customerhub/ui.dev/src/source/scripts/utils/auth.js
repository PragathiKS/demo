import $ from 'jquery';
import { ajaxWrapper } from './ajax';
import 'core-js/features/promise';
import { RESULTS_EMPTY, ajaxMethods, API_TOKEN } from './constants';
import { storageUtil } from '../common/common';

const servletHost = $('#servletHost').val() || '';

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
          url: `${servletHost}/${API_TOKEN}`,
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

export default {
  getToken(callback, errorCallback) {
    return generateToken().then(callback).catch(() => {
      storageUtil.removeCookie('authToken');
      if (typeof errorCallback === 'function') {
        errorCallback();
      }
    });
  }
};
