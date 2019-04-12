// Constants
export const IS_MOBILE_REGEX = /android|webos|iphone|blackberry|iemobile|opera mini/i;
export const IS_TABLET_REGEX = /ipad|android 3.0|xoom|sch-i800|playbook|tablet|kindle/i;
export const INVALID_OBJECT = 'Function \'fn\' expects a valid configuration object';
export const PARSE_ERROR = 'Block data could not be parsed';
export const TEMPLATE_MISSING = 'Target template is missing';
export const INVALID_URL = 'URL is invalid';
export const INIT_FAILED = 'One or more components were not initialized';
export const RESULTS_EMPTY = 'Empty results';
export const DATE_FORMAT = 'YYYY-MM-DD';
export const ORDER_HISTORY_ROWS_PER_PAGE = 10;
export const FINANCIAL_DATE_RANGE_PERIOD = 45;
export const ajaxMethods = {
  GET: 'GET',
  POST: 'POST',
  PUT: 'PUT',
  DELETE: 'DELETE'
};
// CUSTOM EVENTS
export const EVT_FINANCIAL_SUMMARY_RENDER = 'financialSummary.render';
// API URLs
export const API_TOKEN = 'bin/customerhub/token-generator';
export const API_SEARCH = 'orders/summary';
export const API_ORDER_HISTORY = 'orders/history';
export const API_ORDER_DETAIL = 'orders/details';
export const API_FINANCIAL_SUMMARY = 'financials/summary';
export const API_FINANCIALS_STATEMENTS = 'financials/results';
