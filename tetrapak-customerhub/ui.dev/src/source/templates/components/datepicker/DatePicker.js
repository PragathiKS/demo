import $ from 'jquery';
import moment from 'moment';
import InputMask from 'inputmask';
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
  DEFAULT_ON_EMPTY_VALUE,
  WrongInitDateError
} from './constants';
require('jquery-ui/ui/widgets/datepicker');
import { logger } from '../../../scripts/utils/logger';


class DatePicker {
  constructor({
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
    this.root = $(el);
    this.cache = {
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

      widgetSelectorId: 'js-dp-widget',
      inputsWrapperSelectorId: 'js-dp-inputs-wrapper',
      inputFromWrapperSelectorId: 'js-dp-input-from-wrapper-',
      inputFromLabelSelectorId: 'js-dp-input-from-label',
      inputToWrapperSelectorId: 'js-dp-input-to-wrapper',
      inputToLabelSelectorId: 'js-dp-input-to-label',
      inputFromSelectorId: 'js-dp-input-from',
      inputToSelectorId: 'js-dp-input-to',
      fromDateClearIconSelectorId: 'js-dp-from-date-clear-icon',
      toDateClearIconSelectorId: 'js-dp-to-date-clear-icon',
      inputFromErrorSelectorId: 'js-dp-input-from-error',
      inputToErrorSelectorId: 'js-dp-input-to-error',
      monthIconSelectorId: 'js-dp-month-icon',
      yearIconSelectorId: 'js-dp-year-icon',

      inputsWrapperClass: 'dp-inputs-wrapper',
      inputWrapperClass: 'dp-input-wrapper',
      dayHighlightClass: 'dp-highlight',
      firstDayHighlightClass: 'dp-highlight-first',
      lastDayHighlightClass: 'dp-highlight-last',
      emptyFirstHighlightClass: 'dp-highlight-empty-first',
      emptyLastHighlightClass: 'dp-highlight-empty-last',
      hoverFirstClass: 'dp-highlight-hover-first',
      hoverLastClass: 'dp-highlight-hover-last',
      hoverClass: 'dp-highlight-hover',
      hoverEmptyFirstClass: 'dp-highlight-empty-first',
      hoverEmptyLastClass: 'dp-highlight-empty-last',
      inputErrorClass: 'dp-input-error',
      inputLabelClass: 'dp-input-label',
      inputClass: 'tpatom-input-box__input',

      year: new Date().getFullYear(),
      month: new Date().getMonth() + 1,
      inputFromLabel: inputFromLabel || $(el).attr('data-from-label') || DEFAULT_INPUT_FROM_LABEL,
      inputToLabel: inputToLabel || $(el).attr('data-to-label') || DEFAULT_INPUT_TO_LABEL,
      inputErrorLabel: inputErrorLabel || $(el).attr('data-error-label') || DEFAULT_INPUT_ERROR_LABEL,
      isRangeType: type === RANGE_TYPE,

      config: {
        firstDay: 1,
        changeMonth: true,
        changeYear: true,
        minDate: minDate,
        maxDate: maxDate,
        yearRange: `${minDate.getFullYear()}:${maxDate.getFullYear()}`,
        dateFormat: dateFormat,
        monthNamesShort: monthNames,
        dayNamesMin: dayNames,
        beforeShowDay: this.beforeShowDay,
        onSelect: this.onSelect,
        onUpdateDatepicker: this.onUpdateDatepicker,
        onChangeMonthYear: this.onChangeMonthYear,
        ...widgetConfig
      }
    };

    this.onCorrectValue = onCorrectValue;
    this.onIncorrectValue = onIncorrectValue;
    this.onEmptyValue = onEmptyValue;
    this.inputMaskUtil = new InputMask(inputMask, {
      placeholder: inputFormat,
      clearIncomplete: true,
      insertMode: false,
      insertModeVisual: false
    });
  }

  onChangeMonthYear = (year, month) => {
    this.cache.year = year;
    this.cache.month = month;
  }

  beforeShowDay = (date) => {
    const inputFromSelector = `.${this.cache.inputFromSelectorId}`;
    const inputToSelector = `.${this.cache.inputToSelectorId}`;
    const $inputFrom = $(inputFromSelector);
    const $inputTo = $(inputToSelector);
    const inputFromValue = $inputFrom.val();
    const inputToValue = $inputTo.val();
    const inputFromValueValid = this.validateDate(inputFromValue) ? inputFromValue : '';
    const inputToValueValid = this.validateDate(inputToValue) ? inputToValue : '';

    const fromDate = inputFromValueValid && $.datepicker.parseDate(this.cache.dateFormat, inputFromValueValid);
    const toDate = inputToValueValid && $.datepicker.parseDate(this.cache.dateFormat, inputToValueValid);

    if (!fromDate && toDate) {
      return [true, ''];
    }

    const dateTime = date && date.getTime();
    const fromDateTime = fromDate && fromDate.getTime();
    const toDateTime = toDate && toDate.getTime();
    const currentDay = date && date.getDate();

    const isFirstDay = dateTime === fromDateTime;
    const isLastDay = fromDateTime && dateTime === toDateTime;
    const isWithinRange = !!date && !!fromDate && !!toDate && (
      date.getTime() === fromDate.getTime()) || (toDate && date >= fromDate && date <= toDate);
    const isFirstDayOfMonth = currentDay === 1;
    const isLastDayOfMonth = currentDay === new Date(this.cache.year, this.cache.month, 0).getDate();
    const isFirstDayEqToLastDay = inputFromValue === inputToValue;

    const firstDayHighlightClass = isLastDayOfMonth ?
      '' : (!toDate || isFirstDayEqToLastDay) ?
        '' : this.cache.firstDayHighlightClass;
    const lastDayHighlightClass = isFirstDayOfMonth ?
      '' :isFirstDayEqToLastDay ?
        '' : this.cache.lastDayHighlightClass;
    const lightHighlightClass = isFirstDayOfMonth ?
      this.cache.emptyFirstHighlightClass : isLastDayOfMonth ?
        this.cache.emptyLastHighlightClass : this.cache.dayHighlightClass;

    return [
      true,
      isFirstDay ? firstDayHighlightClass : isLastDay ?
        lastDayHighlightClass : isWithinRange ?
          lightHighlightClass : ''
    ];
  }

  onSelect = (dateStr, _inst, isFromInputChange=false, isToInputChange=false) => {
    const inputFromSelector = `.${this.cache.inputFromSelectorId}`;
    const inputToSelector = `.${this.cache.inputToSelectorId}`;
    const $inputFrom = $(inputFromSelector);
    const $inputTo = $(inputToSelector);

    const dateFromValue = isFromInputChange ? '' : $inputFrom.val();
    const dateToValue = isToInputChange ? '' : $inputTo.val();

    const dateFrom = this.validateDate(dateFromValue) && $.datepicker.parseDate(this.cache.dateFormat, dateFromValue);
    const dateTo = this.validateDate(dateToValue) && $.datepicker.parseDate(this.cache.dateFormat, dateToValue);
    const selectedDate = $.datepicker.parseDate(this.cache.dateFormat, dateStr);

    if (this.cache.isRangeType) {
      if (!dateFrom && !dateTo) {
        $inputFrom.val(dateStr);
        this.removeInputFromError();
        this.enableToDate();
        this.focusToDate();
      } else if (dateFrom && !dateTo) {
        if (selectedDate < dateFrom) {
          $inputTo.val($inputFrom.val());
          $inputFrom.val(dateStr);
          this.removeInputFromError();
          this.removeInputToError();
        } else {
          $inputTo.val(dateStr);
          this.removeInputToError();
        }
      } else if (dateTo && !dateFrom) {
        if (selectedDate > dateTo) {
          $inputFrom.val($inputTo.val());
          $inputTo.val(dateStr);
          this.removeInputFromError();
          this.removeInputToError();
        } else {
          $inputFrom.val(dateStr);
          this.removeInputFromError();
        }
      } else if (dateFrom && dateTo) {
        if (isFromInputChange) {
          $inputFrom.val(dateStr);
          this.removeInputFromError();
          this.focusToDate();
        } else {
          $inputFrom.val(dateStr);
          $inputTo.val('');
          this.removeInputFromError();
          this.removeInputToError();
          this.focusToDate();
        }
      }
      this.inputMaskUtil.mask($inputFrom);
      this.inputMaskUtil.mask($inputTo);
    } else {
      $inputFrom.val(dateStr);
      this.removeInputFromError();
      this.inputMaskUtil.mask($inputFrom);
    }

    this.callDates(this.onCorrectValue);
  }

  removeInputFromError = () => {
    $(`.${this.cache.inputFromErrorSelectorId}`).remove();
  }

  removeInputToError = () => {
    $(`.${this.cache.inputToErrorSelectorId}`).remove();
  }

  onUpdateDatepicker = () => {
    this.replaceIcons();

    if (this.cache.isRangeType) {
      const wrapperSelector = `.${this.cache.widgetSelectorId}`;
      const inputFromSelector = `.${this.cache.inputFromSelectorId}`;
      const inputToSelector = `.${this.cache.inputToSelectorId}`;
      const daySelectorId = '.ui-state-default';
      const restSelectorId = `.ui-state-disabled`;
      const leaveSelectorId = `td:first-of-type, td:last-of-type, tr, tbody, div`;

      const onDayMouseEnter = this.onDayMouseEnter;
      const onDayMouseLeave = this.onDayMouseLeave;
      const $inputFrom = $(inputFromSelector);
      const $inputTo = $(inputToSelector);

      const dateFromValue = $inputFrom.val();
      const dateToValue = $inputTo.val();
      const startDate = this.validateDate(dateFromValue) ? $.datepicker.parseDate(this.cache.dateFormat, dateFromValue) : null;
      const endDate = this.validateDate(dateToValue) ? $.datepicker.parseDate(this.cache.dateFormat, dateToValue) : null;

      const hoveredYear = this.cache.year;
      const hoveredMonth = this.cache.month;
      const hoverClass = this.cache.hoverClass;
      const hoverFirstClass = this.cache.hoverFirstClass;
      const hoverLastClass = this.cache.hoverLastClass;
      const hoverEmptyFirstClass = this.cache.hoverEmptyFirstClass;
      const hoverEmptyLastClass = this.cache.hoverEmptyLastClass;

      $(wrapperSelector).off('mouseleave');
      $(wrapperSelector).off('mouseenter');

      $(wrapperSelector).on('mouseleave', leaveSelectorId, function() {
        onDayMouseLeave.call(this, {
          wrapperSelector,
          startDate,
          endDate,
          hoveredYear,
          hoveredMonth
        });
      });

      $(wrapperSelector).on('mouseenter', restSelectorId, function() {
        onDayMouseLeave.call(this, {
          wrapperSelector,
          startDate,
          endDate,
          hoveredYear,
          hoveredMonth
        });
      });

      $(wrapperSelector).on('mouseenter', daySelectorId, function() {
        onDayMouseEnter.call(this, {
          wrapperSelector,
          startDate: !startDate ? endDate : startDate,
          endDate: !startDate ? null : endDate,
          hoveredYear,
          hoveredMonth,
          hoverClass,
          hoverFirstClass,
          hoverLastClass,
          hoverEmptyFirstClass,
          hoverEmptyLastClass
        });
      });
    }
  }

  onDayMouseLeave({
    wrapperSelector,
    startDate,
    endDate
  }) {
    if (startDate && !(startDate && endDate)) {
      const daysSelector = `${wrapperSelector} tbody td`;
      const $days = $(daysSelector).not('.ui-state-disabled');
      $days.each(function() {
        $(this).attr('class', '');
      });
    }
  }

  onDayMouseEnter({
    wrapperSelector,
    startDate,
    endDate,
    hoveredYear,
    hoveredMonth,
    hoverClass,
    hoverFirstClass,
    hoverLastClass,
    hoverEmptyFirstClass,
    hoverEmptyLastClass
  }) {
    if (startDate && !(startDate && endDate)) {
      const hoveredDay = Number(this.innerText);
      const startDay = startDate.getDate();
      const startYear = startDate.getFullYear();
      const startMonth = startDate.getMonth() + 1;
      const isEarlierDay = startDay > hoveredDay;
      const startViewTime = new Date(startYear, startMonth).getTime();
      const hoveredViewTime = new Date(hoveredYear, hoveredMonth).getTime();
      const isEarlierView = startViewTime > hoveredViewTime;
      const isLaterView = startViewTime < hoveredViewTime;
      const hoverDirection = isEarlierView ? 'left' : isLaterView ? 'right' : isEarlierDay ? 'left' : 'right';
      const daysSelector = `${wrapperSelector} tbody td`;
      const $days = $(daysSelector).not('.ui-state-disabled');
      const startDayCursor = hoverDirection === 'left' ?
        (isEarlierView ? 32 : startDay) : (isLaterView ? 0 : startDay);
      const isLastDayOfMonth = hoveredDay === new Date(hoveredYear, hoveredMonth, 0).getDate();


      $days.each(function() {
        const currDay = Number(this.innerText);
        if (!hoveredDay) {
          return $(this).attr('class', '');
        }

        if (hoverDirection === 'left' && currDay <= startDayCursor && currDay >= hoveredDay) {
          if (currDay === hoveredDay) {
            if (!isLastDayOfMonth) {
              $(this).attr('class', hoverFirstClass);
            }
          } else if (isEarlierView && currDay === $days.length) {
            $(this).attr('class', hoverEmptyLastClass);
          } else if (!isEarlierView && currDay === startDay) {
            $(this).attr('class', hoverLastClass);
          } else {
            $(this).attr('class', hoverClass);
          }
        } else if (hoverDirection === 'right' && currDay >= startDayCursor && currDay <= hoveredDay) {
          if (currDay === hoveredDay) {
            $(this).attr('class', hoverLastClass);
          } else if (isLaterView && currDay === 1) {
            $(this).attr('class', hoverEmptyFirstClass);
          } else if (!isLaterView && currDay === startDay) {
            $(this).attr('class', hoverFirstClass);
          } else {
            $(this).attr('class', hoverClass);
          }
        } else {
          $(this).attr('class', '');
        }
      });
    }
  }

  appendElements = () => {
    const inputsWrapperSelector = `.${this.cache.inputsWrapperSelectorId}`;
    const inputFromWrapperSelector = `.${this.cache.inputFromWrapperSelectorId}`;
    const inputToWrapperSelector = `.${this.cache.inputToWrapperSelectorId}`;
    const inputFromLabelSelector = `.${this.cache.inputFromLabelSelectorId}`;
    const inputToLabelSelector = `.${this.cache.inputToLabelSelectorId}`;
    const inputFromSelector = `.${this.cache.inputFromSelectorId}`;
    const inputToSelector = `.${this.cache.inputToSelectorId}`;

    $('<div />', {
      class: `${this.cache.inputsWrapperClass} ${this.cache.inputsWrapperSelectorId}`
    }).appendTo(this.cache.el);
    $('<div />', {
      class: `${this.cache.inputWrapperClass} ${this.cache.inputFromWrapperSelectorId}`
    }).appendTo(inputsWrapperSelector);
    $('<label />', {
      class: `${this.cache.inputLabelClass} ${this.cache.inputFromLabelSelectorId}`
    }).appendTo(inputFromWrapperSelector);

    $(inputFromLabelSelector).text(this.cache.inputFromLabel);

    $('<input />', {
      class: `${this.cache.inputClass} ${this.cache.inputFromSelectorId}`,
      placeholder: this.cache.inputFormat,
      autofocus: true
    }).appendTo(inputFromLabelSelector);
    $('<div />', {
      class: this.cache.widgetSelectorId
    }).appendTo(this.cache.el);

    this.inputMaskUtil.mask(inputFromSelector);

    $(inputFromSelector).on('change', this.handleInputChange(
      this.cache.inputFromSelectorId,
      this.cache.inputFromErrorSelectorId,
      this.cache.inputFromLabelSelectorId
    ));

    if (this.cache.isRangeType) {
      $('<div />', {
        class: `${this.cache.inputWrapperClass} ${this.cache.inputToWrapperSelectorId}`
      }).appendTo(inputsWrapperSelector);
      $('<label />', {
        class: `${this.cache.inputLabelClass} ${this.cache.inputToLabelSelectorId}`
      }).appendTo(inputToWrapperSelector);

      $(inputToLabelSelector).text(this.cache.inputToLabel);

      $('<input />', {
        class: `${this.cache.inputClass} ${this.cache.inputToSelectorId}`,
        placeholder: this.cache.inputFormat
      }).appendTo(inputToLabelSelector);

      this.inputMaskUtil.mask(inputToSelector);
      $(inputToSelector).on('change', this.handleInputChange(
        this.cache.inputToSelectorId,
        this.cache.inputToErrorSelectorId,
        this.cache.inputToLabelSelectorId
      ));
    }
  }

  handleInputChange = (
    inputSelectorId,
    inputErrorSelectorId,
    inputLabelSelectorId
  ) => (e) => {
    const widgetSelector = `.${this.cache.widgetSelectorId}`;
    const { value } = e.target;
    const isValid = this.validateDate(value);

    if (value === '') {
      this.callDates(this.onEmptyValue);
      return;
    }

    if (isValid) {
      const parsedDate = $.datepicker.parseDate(this.cache.dateFormat, value);
      const isFromDateSelected = inputSelectorId === this.cache.inputFromSelectorId;
      $(`.${inputErrorSelectorId}`).remove();
      $(widgetSelector).datepicker('setDate', parsedDate);
      const newDate = moment($(widgetSelector).datepicker('getDate')).format(this.cache.inputFormat);
      this.onSelect(newDate, null, isFromDateSelected, !isFromDateSelected);
      $(widgetSelector).datepicker('refresh');
    } else {
      const isError = $(`.${inputErrorSelectorId}`).length;

      if (inputSelectorId === this.cache.inputFromSelectorId) {
        this.clearFromDate(false);
      } else {
        this.clearToDate(false);
      }
      if (!isError) {
        $(`.${inputLabelSelectorId}`).after(
          `<div class="${this.cache.inputErrorClass} ${inputErrorSelectorId}">${this.cache.inputErrorLabel}</div>`
        );
      }

      this.callDates(this.onIncorrectValue);
    }
  }

  callDates = (cb) => {
    const inputFromSelector = `.${this.cache.inputFromSelectorId}`;
    const inputToSelector = `.${this.cache.inputToSelectorId}`;
    const $inputFrom = $(inputFromSelector);
    const $inputTo = $(inputToSelector);
    const dateFromValue = $inputFrom.val();
    const dateToValue = $inputTo.val();
    const isFromDateValid = this.validateDate(dateFromValue);
    const isToDateValid = this.validateDate(dateToValue);
    const dateFrom = isFromDateValid ? $.datepicker.parseDate(this.cache.dateFormat, dateFromValue) : dateFromValue;
    const dateTo = isToDateValid ? $.datepicker.parseDate(this.cache.dateFormat, dateToValue) : dateToValue;

    if (typeof cb === 'function') {
      cb(dateFrom, dateTo);
    }
  }

  validateDate = (dateStr) => {
    const isValidDate = moment(dateStr, this.cache.inputFormat, true).isValid();
    return isValidDate;
  }

  appendIcons = () => {
    $(`.${this.cache.inputFromLabelSelectorId}`).append(`<i class="icon-Close_new ${this.cache.fromDateClearIconSelectorId}"></i>`);
    $(`.${this.cache.fromDateClearIconSelectorId}`).on('click', this.clearFromDate);

    if (this.cache.isRangeType) {
      $(`.${this.cache.inputToLabelSelectorId}`).append(`<i class="icon-Close_new ${this.cache.toDateClearIconSelectorId}"></i>`);
      $(`.${this.cache.toDateClearIconSelectorId}`).on('click', this.clearToDate);
    }
  }

  replaceIcons = () => {
    if (!$(`.${this.cache.monthIconSelectorId}`).length) {
      $('.ui-datepicker-next').html('⁠<i class="icon icon-Right_new"></i>');
      $('.ui-datepicker-prev').html('<i class="icon icon-Right_new icon-left"></i>');
      $('.ui-datepicker-month').after(`<i class="icon icon-Right_new icon-down ${this.cache.monthIconSelectorId}"></i>`);
      $('.ui-datepicker-year').after(`<i class="icon icon-Right_new icon-down ${this.cache.yearIconSelectorId}"></i>`);
    }
  }

  disableToDate = () => {
    $(`.${this.cache.inputToWrapperSelectorId}`).addClass('disabled');
    $(`.${this.cache.toDateClearIconSelectorId}`).addClass('disabled');
    $(`.${this.cache.inputToSelectorId}`).prop('disabled', true);
    $(`.${this.cache.inputToSelectorId}`).val('');
    $(`.${this.cache.inputToErrorSelectorId}`).remove();
  }

  enableToDate = () => {
    $(`.${this.cache.inputToWrapperSelectorId}`).removeClass('disabled');
    $(`.${this.cache.toDateClearIconSelectorId}`).removeClass('disabled');
    $(`.${this.cache.inputToSelectorId}`).prop('disabled', false);
  }

  focusFromDate = () => {
    $(`.${this.cache.inputFromSelectorId}`).trigger('focus');
  }

  focusToDate = () => {
    $(`.${this.cache.inputToSelectorId}`).trigger('focus');
  }

  clearDateSelection = () => {
    const widgetSelector = `.${this.cache.widgetSelectorId}`;
    $(widgetSelector).datepicker('setDate', '');
    $('.ui-state-active').attr('aria-current', 'false').removeClass('ui-state-active');
  }

  clearFromDate = (clearValue=true) => {
    const widgetSelector = `.${this.cache.widgetSelectorId}`;
    const inputFromSelector = `.${this.cache.inputFromSelectorId}`;
    const inputToSelector = `.${this.cache.inputToSelectorId}`;
    const $inputFrom = $(inputFromSelector);
    const $inputTo = $(inputToSelector);
    const inputToValue = $inputTo.val();
    const parsedToDate = this.validateDate(inputToValue) && $.datepicker.parseDate(this.cache.dateFormat, inputToValue);

    if (clearValue) {
      $inputFrom.val('').trigger('change');
    }
    parsedToDate && $(widgetSelector).datepicker('setDate', parsedToDate);
  }

  clearToDate = (clearValue=true) => {
    const widgetSelector = `.${this.cache.widgetSelectorId}`;
    const inputFromSelector = `.${this.cache.inputFromSelectorId}`;
    const inputToSelector = `.${this.cache.inputToSelectorId}`;
    const $inputFrom = $(inputFromSelector);
    const $inputTo = $(inputToSelector);
    const inputFromValue = $inputFrom.val();
    const parsedFromDate = this.validateDate(inputFromValue) && $.datepicker.parseDate(this.cache.dateFormat, inputFromValue);

    if (clearValue) {
      $inputTo.val('').trigger('change');
    }
    parsedFromDate && $(widgetSelector).datepicker('setDate', parsedFromDate);
  }

  initValue = (date) => {
    if (date) {
      const widgetSelector = `.${this.cache.widgetSelectorId}`;
      const isValidDateObj = date instanceof Date && moment(date).isValid();
      const isValidDateStr = date && this.validateDate(date);
      const value = isValidDateObj ? moment(date).format(this.cache.inputFormat) : isValidDateStr ? date : '';
      const parsedDate = value && $.datepicker.parseDate(this.cache.dateFormat, value);

      if (!isValidDateObj && !isValidDateStr) {
        throw WrongInitDateError;
      } else {
        $(widgetSelector).datepicker('setDate', parsedDate);
        const newDate = moment($(widgetSelector).datepicker('getDate')).format(this.cache.inputFormat);
        this.onSelect(newDate);
        $(widgetSelector).datepicker('refresh');
      }
    }
  }

  initValues = () => {
    if (!this.cache.startDate && !this.cache.endDate) {
      this.callDates(this.onEmptyValue);
    }
    try {
      this.initValue(this.cache.startDate);
      this.initValue(this.cache.endDate);
    } catch (err) {
      logger.error(err.message);
    }
  }

  bindEvents() {
    const widgetSelector = `.${this.cache.widgetSelectorId}`;
    this.appendElements();
    this.appendIcons();
    this.disableToDate();
    $(widgetSelector).datepicker(this.cache.config);
    this.clearDateSelection();
    this.initValues();
    this.focusFromDate();
  }

  init() {
    this.bindEvents();
  }
}

export default DatePicker;
