// Constants
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
export const TRANSITION_END = 'webkitTransitionEnd mozTransitionEnd oTransitionEnd transitionend';

// Servlet URLs
export const LANGUAGE_PREFERENCE_SERVLET_URL = '/bin/supplierportal/saveLanguagePreference';
export const DELETE_COOKIE_SERVLET_URL = '/bin/supplierportal/deleteCookies';
export const AUTH_TOKEN_SERVLET_URL = '/bin/supplierportal/token-generator';

// Regular expressions
export const REG_EMAIL = /^(("[\w-\s]+")|([\w-]+(?:\.[\w-]+)*)|("[\w-\s]+")([\w-]+(?:\.[\w-]+)*))(@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$)|(@\[?((25[0-5]\.|2[0-4][0-9]\.|1[0-9]{2}\.|[0-9]{1,2}\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\]?$)/i;
export const REG_NUM = /^\d+$/;
export const ajaxMethods = {
  GET: 'GET',
  POST: 'POST',
  PUT: 'PUT',
  DELETE: 'DELETE'
};

export const TOKEN_REFRESH_IFRAME_TIMEOUT = 30000;
export const RESULTS_EMPTY = 'Empty results';
export const ACC_TOKEN_COOKIE = 'acctoken';
export const AUTH_TOKEN_COOKIE = 'authToken';
export const REFRESH_TIMEOUT = 'tokenExpiry';

// Page URLs
export const EMPTY_PAGE_URL = '/empty.html';

// Custom events
export const EVT_TOKEN_REFRESH = 'token.refresh';
export const EVT_REFRESH_INITIATE = 'refresh.initiate';
export const EVT_POST_REFRESH = 'refresh.post';
export const EVT_IFRAME_TIMEOUT = 'iframeevents.timeout';

export const INVALID_CONFIG = 'Invalid configuration';
export const INVALID_STREAM = 'Stream is invalid';
export const FILEEXT_EMPTY = 'File extension is required';
