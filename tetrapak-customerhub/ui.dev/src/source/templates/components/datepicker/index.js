import DatePicker from './DatePicker';
import {
  DEFAULT_SELECTOR,
  DEFAULT_TYPE,
  DEFAULT_DATE_FORMAT,
  DEFAULT_INPUT_MASK,
  DEFAULT_INPUT_FORMAT,
  DEFAULT_START_DATE,
  DEFAULT_END_DATE,
  DEFAULT_MIN_DATE,
  DEFAULT_MAX_DATE,
  DEFAULT_MONTH_NAMES,
  DEFAULT_DAY_NAMES,
  DEFAULT_INPUT_FROM_LABEL,
  DEFAULT_INPUT_TO_LABEL,
  DEFAULT_INPUT_ERROR_LABEL,
  RANGE_TYPE,
  DEFAULT_ON_CORRECT_VALUE,
  DEFAULT_ON_INCORRECT_VALUE,
  DEFAULT_ON_EMPTY_VALUE
} from './constants';

function renderDatePicker({
  el = DEFAULT_SELECTOR,
  type = DEFAULT_TYPE,
  dateFormat = DEFAULT_DATE_FORMAT,
  inputMask = DEFAULT_INPUT_MASK,
  inputFormat = DEFAULT_INPUT_FORMAT,
  startDate = DEFAULT_START_DATE,
  endDate = DEFAULT_END_DATE,
  minDate = DEFAULT_MIN_DATE,
  maxDate = DEFAULT_MAX_DATE,
  monthNames = DEFAULT_MONTH_NAMES,
  dayNames = DEFAULT_DAY_NAMES,
  onCorrectValue = DEFAULT_ON_CORRECT_VALUE,
  onIncorrectValue = DEFAULT_ON_INCORRECT_VALUE,
  onEmptyValue = DEFAULT_ON_EMPTY_VALUE,
  widgetConfig = {},
  inputFromLabel,
  inputToLabel,
  inputErrorLabel
}) {
  new DatePicker({
    el,
    type,
    dateFormat,
    inputMask,
    inputFormat,
    startDate,
    endDate,
    minDate,
    maxDate,
    monthNames,
    dayNames,
    widgetConfig,
    onCorrectValue,
    onIncorrectValue,
    onEmptyValue,
    inputFromLabel,
    inputToLabel,
    inputErrorLabel
  }).init();
}

export {
  renderDatePicker,
  DatePicker,
  DEFAULT_SELECTOR,
  DEFAULT_DATE_FORMAT,
  DEFAULT_INPUT_MASK,
  DEFAULT_INPUT_FORMAT,
  DEFAULT_START_DATE,
  DEFAULT_END_DATE,
  DEFAULT_MIN_DATE,
  DEFAULT_MAX_DATE,
  DEFAULT_MONTH_NAMES,
  DEFAULT_DAY_NAMES,
  DEFAULT_INPUT_FROM_LABEL,
  DEFAULT_INPUT_TO_LABEL,
  DEFAULT_INPUT_ERROR_LABEL,
  RANGE_TYPE
};