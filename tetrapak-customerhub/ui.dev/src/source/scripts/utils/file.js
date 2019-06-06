import $ from 'jquery';
import { ajaxWrapper } from './ajax';
import 'core-js/features/promise';
import { INVALID_CONFIG, INVALID_STREAM, FILEEXT_EMPTY } from './constants';
import { logger } from './logger';
import { $body } from './commonSelectors';
import { isIOS } from './browserDetect';

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
            try {
              window.navigator.msSaveOrOpenBlob(data, `${contentFileName}.${extension}`);
              resolve({ data, filename: contentFileName, extension });
            } catch (e) {
              logger.error(e);
              reject(e.message);
            }
          } else if (isIOS()) {
            /*
            resolve({ data, filename: contentFileName, extension });
            // Workaround as IOS browsers does not consistently handle file downloads of different file type
            const formData = $.extend({}, config.data);
            const currentMethod = config.method || ajaxMethods.POST;
            $body.append(`
              <form class="js-file-download" action="${config.url}" method="${currentMethod}">
                ${Object.keys(formData).map(key => `<input type="hidden" name="${key}" value="${formData[key]}" />`)}
              </form>
            `);
            const $form = $body.find('.js-file-download');
            $form.submit();
            $form.remove();
            */

            const servLetURL = `${window.location.protocol}//${window.location.hostname}${window.location.port ? ':' + location.port : ''}${config.url}`;
            const anchor = $('<a target="_blank" class="downloadPdf" id="downloadPdf">Target</a>');
            anchor.attr({
              href: servLetURL
            });
            $body.append(anchor); // Firefox does not react to in-memory elements
            anchor[0].click(); // Triggers file download
            anchor.remove();
          } else {
            // Handle other browsers
            try {
              const anchor = $('<a class="d-none"></a>');
              const $urlRef = window.URL || window.webkitURL;
              const href = $urlRef.createObjectURL(data);
              anchor.attr({
                href,
                download: `${contentFileName}.${extension}`
              });
              $body.append(anchor); // Firefox does not react to in-memory elements
              anchor[0].click(); // Triggers file download
              $urlRef.revokeObjectURL(href); // Clears in-memory file data
              anchor.remove();
              resolve({ data, filename: contentFileName, extension });
            } catch (e) {
              logger.log(e);
              reject(e.message);
            }
          }
        })
        .fail((...args) => {
          logger.log(args);
          reject(args);
        });
    }
  });
};
