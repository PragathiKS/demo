import $ from 'jquery';
import { logger } from './logger';

export const apiHost = $('#apiHost').val();
let apiMappings = {};
try {
  apiMappings = JSON.parse($('#apiMappings').val());
} catch (e) {
  apiMappings = {};
  logger.error(e);
}
/**
 * Returns URL for given URL name
 * @param {string} urlName API URL name
 */
export default function getURL(urlName) {
  return apiMappings[urlName] || '';
}
