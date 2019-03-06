// Constants
import $ from 'jquery';
import 'core-js/features/array/includes';
export const IS_MOBILE_REGEX = /android|webos|iphone|blackberry|iemobile|opera mini/i;
export const IS_TABLET_REGEX = /ipad|android 3.0|xoom|sch-i800|playbook|tablet|kindle/i;
export const INVALID_OBJECT = 'Function \'fn\' expects a valid configuration object';
export const PARSE_ERROR = 'Block data could not be parsed';
export const TEMPLATE_MISSING = 'Target template is missing';
export const INVALID_URL = 'URL is invalid';
export const ajaxMethods = {
  GET: 'GET',
  POST: ([true, 'true'].includes($('#isDummyLayout').val()) ? 'GET' : 'POST'),
  PUT: 'PUT',
  DELETE: 'DELETE'
};
