import $ from 'jquery';
import { ajaxWrapper } from './ajax';
import 'core-js/features/promise';
import { FILENAME_EMPTY, INVALID_CONFIG, INVALID_STREAM } from './constants';
import { logger } from './logger';
import { $body } from './commonSelectors';

/**
 * Handles file downloads via AJAX
 */

/**
 * Downloads a file
 * @param {string} filename Name of downloaded file
 * @param {*} config AJAX config
 */
export const fileWrapper = (config) => {
  $.extend(config, {
    xhrFields: {
      responseType: 'blob'
    }
  });
  const { filename } = config;
  return new Promise(function (resolve, reject) {
    if (!filename || typeof filename !== 'string') {
      logger.log(FILENAME_EMPTY);
      reject(FILENAME_EMPTY);
    } else if (!config || (config && typeof config !== 'object')) {
      logger.log(INVALID_CONFIG);
      reject(INVALID_CONFIG);
    } else {
      ajaxWrapper.getXhrObj(config)
        .done((data) => {
          if (!data) {
            logger.log(INVALID_STREAM);
            reject(INVALID_STREAM);
          } else if (window.navigator.msSaveOrOpenBlob) {
            // Handle IE and Edge
            window.navigator.msSaveOrOpenBlob(data, filename);
            resolve({ data, filename });
          } else {
            // Handle other browsers
            const anchor = $('<a class="d-none"></a>');
            const href = window.URL.createObjectURL(data);
            anchor.attr({
              href,
              download: filename
            });
            $body.append(anchor); // Firefox does not react to in-memory elements
            anchor.trigger('click'); // Triggers file download
            window.URL.revokeObjectURL(href); // Clears in-memory file data
            anchor.remove();
            resolve({ data, filename });
          }
        })
        .fail((...args) => {
          reject(args);
        });
    }
  });
};
