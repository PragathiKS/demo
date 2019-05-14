// Constants
export const IS_MOBILE_REGEX = /android|webos|iphone|blackberry|iemobile|opera mini/i;
export const IS_TABLET_REGEX = /ipad|android 3.0|xoom|sch-i800|playbook|tablet|kindle/i;
export const INVALID_OBJECT = 'Function \'fn\' expects a valid configuration object';
export const PARSE_ERROR = 'Block data could not be parsed';
export const TEMPLATE_MISSING = 'Target template is missing';
export const INVALID_URL = 'URL is invalid';
export const ajaxMethods = {
  GET: 'GET',
  POST: 'POST',
  PUT: 'PUT',
  DELETE: 'DELETE'
};
// API URLs

export const API_PRODUCT_LISTING = '/bin/tetrapak/pw-productlisting';
export const API_SHAREPOINT_OFFICES = '/apps/settings/wcm/designs/publicweb/jsonData/tp-offices.json';
export const GET_CAROUSEL_ITEM = '/bin/tetrapak/pw-carousellisting';
