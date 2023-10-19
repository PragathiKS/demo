/* istanbul ignore file */
import InputMask from 'inputmask';
import { DEFAULT_DATE_INPUT_MASK, DEFAULT_DATE_INPUT_FORMAT } from '../../../scripts/utils/constants';
import {_isValidDate, _isDateLessThanOrEqualToToday, _isToDateGreaterThanFromDate} from './allpayments.utils';


class AllPaymentsDateRange {
  constructor (cache, root) {
    const self = this;
    this.cache = cache;
    this.root = root;
    this.inputMaskUtil = new InputMask(DEFAULT_DATE_INPUT_MASK, {
      placeholder: DEFAULT_DATE_INPUT_FORMAT,
      clearIncomplete: true,
      insertMode: false,
      insertModeVisual: false,
      postValidation: function (value, opts) {
        if (opts === DEFAULT_DATE_INPUT_FORMAT.length -1) {
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
      toInput = dateWrapper.querySelector('.js-date-range-input-to').value;

    if (fromInput && toInput && !_isToDateGreaterThanFromDate(fromInput, toInput)) {
      dateWrapper.querySelector('.js-date-range-from-error').classList.remove('hide');
      this.enableFilterApplyBtn(false);
    }
    else if (fromInput && (!_isValidDate(fromInput) || !_isDateLessThanOrEqualToToday(fromInput))) {
      dateWrapper.querySelector('.js-date-range-from-error').classList.remove('hide');
      this.enableFilterApplyBtn(false);
    }
    else if (toInput && (!_isValidDate(toInput) || !_isDateLessThanOrEqualToToday(toInput))) {
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

  getDateRange = (fromDateVal, toDateVal, ISOFormat) => {
    const fromDate = (ISOFormat) ? `${fromDateVal.toISOString().slice(0, 11)}00:00:00` : `${fromDateVal.toISOString().slice(0, 10)}`;
    const toDate = (ISOFormat) ? `${toDateVal.toISOString().slice(0, 19)}` : `${toDateVal.toISOString().slice(0, 10)}`;

    return [fromDate, toDate];
  }

  getFilterCustomDateRange = (ISOFormat) => {
    const [fromDateInput, toDateInput] =  this.cache.modal.querySelectorAll('.js-date-range');
    let fromDateVal, toDateVal;

    if (fromDateInput.value && toDateInput.value) {
      fromDateVal = new Date(fromDateInput.value);
      toDateVal = new Date(toDateInput.value);
    }
    else if (fromDateInput.value && !toDateInput.value) {
      fromDateVal = new Date(fromDateInput.value);
      toDateVal = new Date(fromDateVal.getFullYear(), fromDateVal.getMonth(), fromDateVal.getDate() + Number(90));
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
      if (e.target.closest('.tpatom-radio')) {
        self.toggleDateRange(e);
      }
    });
  }
}

export default AllPaymentsDateRange;
