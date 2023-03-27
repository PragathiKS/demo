/* eslint-disable no-empty-function */
import moment from 'moment';

export const DEFAULT_SELECTOR = '.js-dp';
export const DEFAULT_TYPE = 'default';
export const DEFAULT_DATE_FORMAT = 'yy-mm-dd';
export const DEFAULT_INPUT_MASK = '9999-99-99';
export const DEFAULT_INPUT_FORMAT= 'YYYY-MM-DD';
export const DEFAULT_START_DATE = '';
export const DEFAULT_END_DATE = '';
export const DEFAULT_MIN_DATE = moment(new Date()).subtract(100, 'years').toDate();
export const DEFAULT_MAX_DATE = moment(new Date()).add(100, 'years').toDate();
export const DEFAULT_MONTH_NAMES = [
  'Jan',
  'Feb',
  'Mar',
  'Apr',
  'May',
  'Jun',
  'Jul',
  'Aug',
  'Sep',
  'Okt',
  'Nov',
  'Dec'
];
export const DEFAULT_DAY_NAMES = [
  'Su',
  'Mo',
  'Tu',
  'We',
  'Th',
  'Fr',
  'Sa'
];
export const DEFAULT_INPUT_FROM_LABEL = 'From';
export const DEFAULT_INPUT_TO_LABEL = 'To';
export const DEFAULT_INPUT_ERROR_LABEL = 'This is not a valid date';
export const RANGE_TYPE = 'range';

export const DEFAULT_ON_CORRECT_VALUE = () => {};
export const DEFAULT_ON_INCORRECT_VALUE = () => {};
export const DEFAULT_ON_EMPTY_VALUE = () => {};

export const WrongInitDateError = new Error('Invalid "startDate" or "endDate" provided in DatePicker configuration');