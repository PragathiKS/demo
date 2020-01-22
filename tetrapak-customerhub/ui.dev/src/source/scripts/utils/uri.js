import $ from 'jquery';
import { logger } from './logger';

export const apiHost = $('#apiHost').val();
export const uriCache = {};
/**
 * Returns URL for given URL name
 * @param {string} urlName API URL name
 */
export function getURL(urlName) {
  try {
    const $apiMapping = $('#apiMappings');
    if (!uriCache.apiMappings) {
      if ($apiMapping.length) {
        uriCache.apiMappings = $.extend({}, JSON.parse($apiMapping.val()));
      } else {
        uriCache.apiMappings = {};
      }
    }
  } catch (e) {
    uriCache.apiMappings = {};
    logger.error(e);
  }
  return uriCache.apiMappings[urlName] || '';
}
