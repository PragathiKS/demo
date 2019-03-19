import $ from 'jquery';
import { ajaxWrapper } from './ajax';
import LZStorage from 'lzstorage';
import 'core-js/features/promise';
import { RESULTS_EMPTY, ajaxMethods, API_TOKEN } from './constants';
import { logger } from './logger';

const lzs = new LZStorage({
  compression: true
});

const servletHost = $('#servletHost').val() || '';

function generateToken() {
  return (
    new Promise(function (resolve, reject) {
      const appData = lzs.get('appData');
      logger.debug(appData);
      if (appData) {
        resolve({
          data: appData,
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
              logger.debug(result);
              const expiry = Math.floor((+result.expires_in) / (24 * 60 * 60 * 1000));
              lzs.setCookie('appData', result, expiry);
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
    return generateToken().then(callback).catch(errorCallback);
  }
};
