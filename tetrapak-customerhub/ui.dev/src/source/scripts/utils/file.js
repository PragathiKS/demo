import $ from 'jquery';
import { ajaxWrapper } from './ajax';
import 'core-js/features/promise';
import { INVALID_CONFIG, INVALID_STREAM, FILEEXT_EMPTY } from './constants';
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
  const { extension, filename } = config;
  return new Promise(function (resolve, reject) {
    if (!extension || typeof extension !== 'string') {
      logger.log(FILEEXT_EMPTY);
      reject(FILEEXT_EMPTY);
    } else if (!config || (config && typeof config !== 'object')) {
      logger.log(INVALID_CONFIG);
      reject(INVALID_CONFIG);
    } else {
      ajaxWrapper.getXhrObj(config)
        .done((...args) => {
          const [data, , xhr] = args;
          const contentDisposition = xhr.getResponseHeader('Content-Disposition') || '';
          const fileNameVar = 'filename=';
          const fileVarIndex = contentDisposition.indexOf(fileNameVar);
          const extensionIndex = contentDisposition.indexOf(`.${extension}`);
          let contentFileName = '';
          if (
            fileVarIndex > -1
            && extensionIndex > fileVarIndex
          ) {
            contentFileName = contentDisposition.substring(
              fileVarIndex + fileNameVar.length,
              extensionIndex
            );
          }
          if (!contentFileName) {
            contentFileName = filename ? filename : `${Date.now()}`;
          }
          if (!data) {
            logger.log(INVALID_STREAM);
            reject(INVALID_STREAM);
          } else if (window.navigator.msSaveOrOpenBlob) {
            // Handle IE and Edge
            window.navigator.msSaveOrOpenBlob(data, `${contentFileName}.${extension}`);
            resolve({ data, filename: contentFileName, extension });
          } else {
            // Handle other browsers
            const anchor = $('<a class="d-none"></a>');
            const href = window.URL.createObjectURL(data);
            anchor.attr({
              href,
              download: `${contentFileName}.${extension}`
            });
            $body.append(anchor); // Firefox does not react to in-memory elements
            anchor[0].click(); // Triggers file download
            window.URL.revokeObjectURL(href); // Clears in-memory file data
            anchor.remove();
            resolve({ data, filename: contentFileName, extension });
          }
        })
        .fail((...args) => {
          logger.log(args);
          reject(args);
        });
    }
  });
};
