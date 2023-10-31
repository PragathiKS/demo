import InputMask from 'inputmask';
import { DEFAULT_DATE_INPUT_MASK, DEFAULT_DATE_INPUT_FORMAT } from './constant';
import AllPaymentsUtils from './allpayments.utils';
import { getI18n } from '../../../scripts/common/common';

class AllPaymentsDateRange {
  constructor (cache, root) {
    const self = this;
    this.cache = cache;
    this.root = root;
    this.allPaymentsUtils = new AllPaymentsUtils();
    this.inputMaskUtil = new InputMask(DEFAULT_DATE_INPUT_MASK, {
      placeholder: DEFAULT_DATE_INPUT_FORMAT,
      clearIncomplete: true,
      insertMode: false,
      insertModeVisual: false,
      postValidation: function (value, pos) {
        if ((pos === DEFAULT_DATE_INPUT_FORMAT.length -1 && this.unmaskedvalue() !== '') || this.isComplete()) {
          self.validateDateRange();
        }
      }
    });
  }

  getInputMaskUtil = () => this.inputMaskUtil

  enableFilterApplyBtn = (isShow) => {
    const applyBtn = this.cache.modal.querySelector('.js-apply-filter-button');
    if(isShow) {
      applyBtn.removeAttribute('disabled');
    }
    else {
      applyBtn.setAttribute('disabled', true);
    }
  }

  validateDateRange = () => {
    const dateWrapper = this.cache.modal.querySelector('.js-date-range-wrapper'),
      fromInput = dateWrapper.querySelector('.js-date-range-input-from').value,
      toInput = dateWrapper.querySelector('.js-date-range-input-to').value,
      utils = this.allPaymentsUtils;

    if (fromInput && toInput) {
      dateWrapper.querySelector('.js-date-range-from-error').classList.add('hide');
      dateWrapper.querySelector('.js-date-range-to-error').classList.add('hide');
      this.enableFilterApplyBtn(true);

      if (!utils.isValidDate(fromInput) || utils.isFutureDate(fromInput)) {
        dateWrapper.querySelector('.js-date-range-from-error').innerHTML = getI18n(this.cache.i18nKeys['invalidDate']);
        dateWrapper.querySelector('.js-date-range-from-error').classList.remove('hide');
        this.enableFilterApplyBtn(false);
      }
      if (!utils.isValidDate(toInput) || utils.isFutureDate(toInput)) {
        dateWrapper.querySelector('.js-date-range-to-error').classList.remove('hide');
        this.enableFilterApplyBtn(false);
      }
      if (utils.isNotValidDateRange(fromInput, toInput)) {
        dateWrapper.querySelector('.js-date-range-from-error').innerHTML = getI18n(this.cache.i18nKeys['invalidDateRange']);
        dateWrapper.querySelector('.js-date-range-from-error').classList.remove('hide');
        dateWrapper.querySelector('.js-date-range-to-error').classList.add('hide');
        this.enableFilterApplyBtn(false);
      }
    }
    else if (fromInput && (!utils.isValidDate(fromInput) || utils.isFutureDate(fromInput))) {
      dateWrapper.querySelector('.js-date-range-from-error').innerHTML = getI18n(this.cache.i18nKeys['invalidDate']);
      dateWrapper.querySelector('.js-date-range-from-error').classList.remove('hide');
      this.enableFilterApplyBtn(false);
    }
    else if (toInput && (!utils.isValidDate(toInput) || utils.isFutureDate(toInput))) {
      dateWrapper.querySelector('.js-date-range-to-error').classList.remove('hide');
      this.enableFilterApplyBtn(false);
    }
    else if (!fromInput && !toInput) {
      dateWrapper.querySelector('.js-date-range-from-error').classList.add('hide');
      dateWrapper.querySelector('.js-date-range-to-error').classList.add('hide');
      this.enableFilterApplyBtn(false);
    }
    else {
      dateWrapper.querySelector('.js-date-range-from-error').classList.add('hide');
      dateWrapper.querySelector('.js-date-range-to-error').classList.add('hide');
      this.enableFilterApplyBtn(true);
    }
  }

  resetDateRange = () => {
    const filterWrapper = this.cache.modal.querySelector('.tp-all-payments__date-range-wrapper'),
      dateFields = filterWrapper ? filterWrapper.querySelectorAll('.js-date-range'): [],
      errorFields = filterWrapper ? filterWrapper.querySelectorAll('.js-date-range-error'): [];

    dateFields.forEach(item => {
      item.value = '';
    });
    errorFields.forEach(item => {
      item.classList.add('hide');
    });
  }

  toggleDateRange = (e) => {
    const dataField = this.cache.modal.querySelector('.js-data-range-wrapper');
    if (dataField) {
      this.resetDateRange();
      if(e.target.closest('.tpatom-radio').querySelector('input').value === 'other') {
        this.enableFilterApplyBtn(false);
        dataField.classList.add('show');
        this.cache.modal.querySelector('.js-date-range-input-from').focus();
      }
      else {
        this.enableFilterApplyBtn(true);
        dataField.classList.remove('show');
      }
    }
  }

  //Check the to date is greater than today, then set today as to date
  checkTodateIsFutureDate = (toDateVal) => {
    const today = new Date();
    today.setHours(0, 0, 0, 0); // Set the time to midnight to compare only the dates
    if (toDateVal.getTime() > today.getTime()) {
      toDateVal = new Date();
    }

    return toDateVal;
  }

  getDateRange = (fromDateVal, toDateVal, ISOFormat) => {
    const fromDate = (ISOFormat) ? `${fromDateVal.toISOString().slice(0, 11)}00:00:00` : `${fromDateVal.toISOString().slice(0, 10)}`;
    const toDate = (ISOFormat) ? `${toDateVal.toISOString().slice(0, 19)}` : `${toDateVal.toISOString().slice(0, 10)}`;

    return [fromDate, toDate];
  }

  getFilterCustomDateRange = (ISOFormat, customDateRange) => {
    const [fromDateInput, toDateInput] =  this.cache.modal.querySelectorAll('.js-date-range');
    let fromDateVal, toDateVal;

    if(this.cache.modal.querySelectorAll('.js-date-range').length === 0 && customDateRange.length > 0) {
      return this.getDateRange(new Date(customDateRange[0]), new Date(customDateRange[1]), ISOFormat);
    }

    if (fromDateInput.value && toDateInput.value) {
      fromDateVal = new Date(fromDateInput.value);
      toDateVal = new Date(toDateInput.value);
    }
    else if (fromDateInput.value && !toDateInput.value) {
      fromDateVal = new Date(fromDateInput.value);
      toDateVal = new Date(fromDateVal.getFullYear(), fromDateVal.getMonth(), fromDateVal.getDate() + Number(90));
      toDateVal = this.checkTodateIsFutureDate(toDateVal);
    }
    else if (!fromDateInput.value && toDateInput.value) {
      toDateVal = new Date(toDateInput.value);
      fromDateVal = new Date(toDateVal.getFullYear(), toDateVal.getMonth(), toDateVal.getDate() - Number(90));
    }
    return this.getDateRange(fromDateVal, toDateVal, ISOFormat);
  }

  bindEvents() {
    const self = this;

    this.cache.modal && this.cache.modal.addEventListener('change', function(e) {
      if (e.target.closest('.tp-all-payments-filter[auto_locator="invoiceDates"]') && e.target.closest('.tpatom-radio')) {
        self.toggleDateRange(e);
      }
    });

    this.cache.modal && this.cache.modal.addEventListener('input', function(e) {
      if (e.target.closest('.js-date-range')) {
        const inputElement = e.target.closest('.js-date-range').value;

        if (inputElement.length === 0) {
          self.validateDateRange();
        }
      }
    });
  }
}

export default AllPaymentsDateRange;
