// Constants
export const IS_MOBILE_REGEX = /android|webos|iphone|blackberry|iemobile|opera mini/i;
export const IS_TABLET_REGEX = /ipad|android 3.0|xoom|sch-i800|playbook|tablet|kindle/i;
export const INVALID_OBJECT = 'Function \'fn\' expects a valid configuration object';
export const PARSE_ERROR = 'Block data could not be parsed';
export const TEMPLATE_MISSING = 'Target template is missing';
export const INVALID_URL = 'URL is invalid';
export const NO_OF_EVENTS_PER_PAGE = 12;
export const ajaxMethods = {
  GET: 'GET',
  POST: 'POST',
  PUT: 'PUT',
  DELETE: 'DELETE'
};
export const mediaTypes = {
  MOBILE: 'mobile',
  TABLET: 'tablet',
  DESKTOP: 'desktop'
};
export const orientationTypes = {
  LANDSCAPE: 'landscape',
  PORTRAIT: 'portrait',
  UNSUPPORTED: 'unsupported'
};
// Regular expressions
export const REG_EMAIL = /^(("[\w-\s]+")|([\w-]+(?:\.[\w-]+)*)|("[\w-\s]+")([\w-]+(?:\.[\w-]+)*))(@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$)|(@\[?((25[0-5]\.|2[0-4][0-9]\.|1[0-9]{2}\.|[0-9]{1,2}\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\]?$)/i;
// API URLs
export const API_PRODUCT_LISTING = '/bin/tetrapak/pw-productlisting';
export const API_SHAREPOINT_OFFICES = '/apps/settings/wcm/designs/publicweb/jsonData/tp-offices.json';
export const API_SEARCH_RESULTS = '/bin/tetrapak/pw-search';
export const GET_CAROUSEL_ITEM = '/bin/tetrapak/pw-carousellisting';
export const API_SOFT_CONVERSION = '/bin/tetrapak/pw-softconversion';
export const API_CONTACT_FORM = '/bin/tetrapak/pw-contactfooter';
