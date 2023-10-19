import $ from 'jquery';
import { render } from '../../../scripts/utils/render';
import AllPaymentsTag from './allpayments.tag';
import AllPaymentsDateRange from './allpayments.date-range';
import {sanitize} from '../../../scripts/common/common';
import {_getFilterDateRange} from './allpayments.utils';
// import allpaymentFilterData from './data/allpaymentsfilter.json';

class AllPaymentsFilter {

  constructor (cache, root) {
    // const self = this;
    this.cache = cache;
    this.root = root;
    const selector = {
      filter: this.root.querySelector('.tp-all-payments__filter-wrapper'),
      modal: this.root.querySelector('.js-filter-modal')
    };
    this.cache = {
      ...this.cache,
      ...selector,
      customDateRange: [],
      filterModalData: {},
      activeFilter: {},
      activeTagFilter: [], //To maintain the order
      invoiceDateStaticOption: [
        'last90Days', 'other'
      ],
      defaultActiveFilter: {
        invoiceDates: ['last90Days']
      },
      defaultActiveTagFilter: [
        {
          invoiceDates: {
            val: _getFilterDateRange(90).join(' - '),
            trimVal: _getFilterDateRange(90).join(' - ')
          }
        }
      ]
    };
    // To overlap the active filter with default filter
    this.cache.activeFilter = {...this.cache.filterModal, ...this.cache.defaultActiveFilter};
    this.cache.activeTagFilter = [...this.cache.activeTagFilter, ...this.cache.defaultActiveTagFilter];
    this.allPaymentsTag = new AllPaymentsTag(this.cache, this.root);
    this.allPaymentDateRange = new AllPaymentsDateRange(this.cache, this.root);
  }

  getFilterQueryString = () => {
    const self = this;

    return Object.keys(this.cache.activeFilter)
      .map(key => {
        const value = this.cache.activeFilter[key];
        if (Array.isArray(value) && value.length > 0) {
          if (value.includes('last90Days')) {
            const [fromDate, toDate] = _getFilterDateRange(90, true);
            return `fromdatetime=${fromDate}&todatetime=${toDate}`;
          }
          else if (value.includes('other')) {
            const [fromDate, toDate] = self.allPaymentDateRange.getFilterCustomDateRange(true);
            return `fromdatetime=${fromDate}&todatetime=${toDate}`;
          }
          else {
            return `${encodeURIComponent(self.cache.queryParamMapping[key])}=${value.map(val => encodeURIComponent(val)).join(',')}`;
          }
        }
        else {
          return '';
        }
      })
      .filter(param => param !== '')
      .join('&');
  }

  getPaymentFiltersApiUrl = () => {
    const paymentApi = this.cache.paymentApi.getAttribute('data-filter-api'),
      apiUrlRequest = `${paymentApi}?${this.getFilterQueryString()}`;

    return apiUrlRequest;
  }

  resetFilter = () => {
    this.cache.activeFilter = {...this.cache.filterModal, ...this.cache.defaultActiveFilter};
    this.cache.activeTagFilter = [...this.cache.defaultActiveTagFilter];
    this.cache.customDateRange = [];

    const customEvent = new CustomEvent('FilterChanged', {});
    this.root.dispatchEvent(customEvent);
  }

  applyFilterAction = () => {
    const modal = this.cache.modal,
      activeForm = modal.querySelector('.tp-modal-dialog').getAttribute('auto_locator'),
      inputFields = modal.querySelectorAll('input'),
      {activeFilter} = this.cache;
    let {activeTagFilter} = this.cache;

    this.cache.activeFilter[activeForm] = []; //reset

    inputFields.forEach((field) => {
      switch(field.type) {
      case 'radio':
        if(field.checked) {
          activeFilter[activeForm].push(field.value);
          activeTagFilter = activeTagFilter.filter(obj => !Object.prototype.hasOwnProperty.call(obj, activeForm));
          if(field.value !== 'other') {
            this.cache.customDateRange = [];
            activeTagFilter.unshift({[activeForm]: {
              val: _getFilterDateRange(90).join(' - '),
              trimVal: _getFilterDateRange(90).join(' - ')
            }});
          }
          else {
            this.cache.customDateRange = this.allPaymentDateRange.getFilterCustomDateRange();
            activeTagFilter.unshift({[activeForm]: {
              val: this.cache.customDateRange.join(' - '),
              trimVal: this.cache.customDateRange.join(' - ')
            }});
          }
        }
        break;
      case 'checkbox':
        if (field.checked) {
          activeFilter[activeForm].push(field.value);
          if(!activeTagFilter.some(obj => obj[activeForm] && obj[activeForm].val === field.value)) {
            activeTagFilter.push({[activeForm]: {
              val: field.value,
              trimVal: field.value
            }});
          }
        }
        else {
          activeTagFilter = activeTagFilter.filter(obj => !obj[activeForm] || (obj[activeForm] && obj[activeForm].val !== field.value));
        }
        break;
      case 'text':
        if (field.value && !field.classList.contains('js-date-range')) {
          const sanitizeVal = sanitize(field.value); // To sanitize the value
          activeFilter[activeForm].push(sanitizeVal);

          activeTagFilter = activeTagFilter.filter(obj => !Object.prototype.hasOwnProperty.call(obj, activeForm));
          activeTagFilter.push({[activeForm]: {
            val: sanitizeVal,
            trimVal: sanitizeVal
          }});
        }
        break;
      default:
        break;
      }
    });
    this.cache.activeFilter = activeFilter;
    this.cache.activeTagFilter = activeTagFilter;

    const customEvent = new CustomEvent('FilterChanged', {});
    this.root.dispatchEvent(customEvent);
  }

  getFormData = (target) => {
    const self = this,
      key = target.getAttribute('data-key');

    if (target.getAttribute('data-isTextInput')) {
      return self.cache.activeFilter[key];
    }
    else if (target.getAttribute('data-isRadio')) {
      return self.cache.invoiceDateStaticOption.map((item) => ({
        isChecked: self.cache.activeFilter[key].includes(item) ? true : false,
        option: item,
        // optionDisplayText: item[self.cache.keyMapping[key]] // TODO: with i18nkeys
        optionDisplayText: item
      }));
    }
    else {
      return self.cache.filterModalData[key].map((item) => ({
        ...item,
        isChecked: self.cache.activeFilter[key].includes(item[target.getAttribute('data-key-field')]) ? true : false,
        option: item[self.cache.keyMapping[key]],
        optionDisplayText: item[self.cache.keyMapping[key]]
      }));
    }
  }

  getFilterData = (target) => {
    const { i18nKeys } = this.cache,
      key = target.getAttribute('data-key');

    return {
      header: target.getAttribute('data-label'),
      formData: this.getFormData(target),
      maxItemsNo: null,
      selectedItemsNo: this.cache.activeFilter[key] && this.cache.activeFilter[key].length,
      ...i18nKeys,
      singleButton: !target.getAttribute('data-isShowResetBtn'),
      isRadio: target.getAttribute('data-isRadio'),
      radioGroupName: target.getAttribute('data-isRadio') ? `${key}Radio` : '',
      isTextInput: target.getAttribute('data-isTextInput'),
      isCheckBox: target.getAttribute('data-isCheckBox'),
      autoLocatorModal: `${key}`,
      autoLocatorInput: `${key}InputBox`,
      autoLocatorCheckbox: `${key}FilterCheckboxOverlay`,
      autoLocatorCheckboxText: `${key}FilterItemOverlay`,
      isFilterModal: true,
      customDateRange: this.cache.customDateRange
    };
  }

  modalClickAction (e) {
    if (e.target.closest('.js-apply-filter-button')) {
      this.applyFilterAction();
      $(this.cache.modal).modal('hide');
      // this.renderTags();
    }
    else if (e.target.closest('.js-date-range-clear-icon')) {
      const filterWrapper = e.target.closest('.js-date-range-input-wrapper'),
        inputField = filterWrapper.querySelector('.js-date-range'),
        errorField = filterWrapper.querySelector('.js-date-range-error');

      if (inputField) {inputField.value = '';}
      if (errorField) {errorField.classList.add('hide'); }
    }
    else if (e.target.closest('.js-close-btn')) {
      $(this.cache.modal).modal('hide');
    }
  }


  bindEvents() {
    const self = this;

    this.cache.filter && this.cache.filter.addEventListener('click', function(e) {
      if (e.target.closest('.tp-all-payments__filter-button')) {
        const filterData = self.getFilterData(e.target.closest('.tp-all-payments__filter-button'));
        render.fn({
          template: 'filterForm',
          data: filterData,
          target: '.tp-all-payments__filter-form',
          hidden: false
        });
        $(self.cache.modal).modal();
        self.allPaymentDateRange.getInputMaskUtil().mask(self.cache.modal.querySelectorAll('.js-date-range'));
      }
      else if(e.target.closest('.tp-all-payments__reset-button')) {
        self.resetFilter();
      }
    });

    this.cache.modal && this.cache.modal.addEventListener('click', function(e) {
      self.modalClickAction(e);
    });

    this.cache.modal && this.cache.modal.addEventListener('keydown', function(e) {
      if (e.target.closest('.js-tp-all-payments-filter-input')) {
        const currentKeyCode = e.keyCode || e.which;
        if (currentKeyCode === 13) {
          self.applyFilterAction();
          $(self.cache.modal).modal('hide');
        }
      }
    });

    $(this.cache.modal).on('shown.bs.modal', () => {
      const firstInput = this.cache.modal.querySelector('input[type="text"]');

      if(firstInput) {
        firstInput.focus();
      }
    });

    this.allPaymentsTag.bindEvents();
    this.allPaymentDateRange.bindEvents();
  }

  addFilterActiveClass = () => {
    const activeFilter = new Set(this.cache.activeTagFilter.map(item => Object.keys(item)[0])),
      filterWrapper = this.cache.filter,
      buttonElem = filterWrapper.querySelectorAll(`.tp-all-payments__filter-chips button`),
      filterModalData = this.cache.filterModalData;

    buttonElem.forEach((field) => {
      const fieldName = field.getAttribute('data-key');
      field.classList.remove('active');
      if (activeFilter.has(fieldName)) {
        field.classList.add('active');
      }
      if (Object.keys(filterModalData).length > 0) {
        if(filterModalData[field] && filterModalData[fieldName].length <= 0) {
          field.disabled = true;
        }
        else {
          field.removeAttribute('disabled');
        }
      }
      else {
        field.disabled = true;
      }
    });
  }

  renderTags = () => {
    const self = this;

    this.addFilterActiveClass();
    render.fn({
      template: 'tagContainer',
      data: {
        formData: (self.cache.activeTagFilter.length > 0) ? self.cache.activeTagFilter : [],
        defaultActiveTagFilter: self.cache.defaultActiveTagFilter.map(function(obj) {
          return Object.keys(obj)[0];
        }).join(',')
      },
      target: '.tagContainer',
      hidden: false
    });
  }

  getAllFilters = (authData) => {
    const self = this;
    const fetchHeaderOption = {
      method: 'GET',
      contentType: 'application/json',
      headers: {
        'Authorization': `Bearer ${authData.access_token}`,
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    };
    return fetch(this.getPaymentFiltersApiUrl(), fetchHeaderOption)
      .then(resp => resp.json())
      .then(response => {
        self.cache.filterModalData = response.data;
        self.renderTags();
      })
      .catch(() => {
        self.cache.filterModalData = {};
        // self.cache.filterModalData = allpaymentFilterData.data;
        self.renderTags();
        // eslint-disable-next-line no-console
        console.log('Filter fetch error');
      });
  }
}

export default AllPaymentsFilter;
