import $ from 'jquery';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import { ajaxMethods } from '../../../scripts/utils/constants';
import auth from '../../../scripts/utils/auth';
import file from '../../../scripts/utils/file';
class Paymentdetails {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize selector cache here */
    this.cache.configJson = this.root.find('.js-payment-details__config').text();
    this.cache.detailsApi = this.root.data('details-api');
    this.cache.dateRange = this.root.data('date-range');
    this.cache.exportToPdfURL = this.root.data('export-pdf-url');
    this.cache.contentWrapper = this.root.find(
      '.tp-payment-details-content'
    );
    this.cache.content = this.root.find('.tp-payment-details__content-wrapper');
    this.cache.spinner = this.root.find('.tp-spinner');
    this.cache.header = this.root.find('.tp-payment-details__header-actions');
    this.cache.headerAction = this.root.find('.tp-payment-details__header-action-wrapper');
    this.cache.mobileHeadersActions = this.root.find('.js-mobile-header-actions');
    this.cache.statusApiUrl = this.root.data('status-api');
    this.cache.statusMapping = {};
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
  }
  getUrlQueryParams() {
    const url = window.location.href;
    const params = {};
    url.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (_, key, value) {
      return (params[key] = value);
    });
    return params;
  }
  downloadPDf = () => {
    const { documentreferenceid, fromdatetime, todatetime, status} = this.getUrlQueryParams();
    const queryParam = `&fromdatetime=${fromdatetime}&todatetime=${todatetime}&status=${status}`;
    auth.getToken(() => {
      file.get({
        extension: 'pdf',
        url: `${this.cache.exportToPdfURL}?documentreferenceid=${documentreferenceid}${queryParam}`,
        method: ajaxMethods.GET
      });
    });
  }
  bindEvents() {
    const {header} = this.cache;
    // Download PDF
    this.root.on('click', '.js-payment-details__export-pdf-action',  () => {
      this.downloadPDf();
    });
    this.cache.mobileHeadersActions.on('click', () => {
      if($(header).hasClass('show')){
        $(header).removeClass('show');
      } else {
        $(header).addClass('show');
      }
    });
    $('body').on('click', (e) => {
      const $actionBtn = e.target;
      if(!$($actionBtn).hasClass('icon-Three_Dot')){
        if($(header).hasClass('show')){
          $(header).removeClass('show');
        }
      }
    });
  }
  getApiPromise = (authData) => {
    const fetchHeaderOption = {
      method: 'GET',
      contentType: 'application/json',
      headers: {
        'Authorization': `Bearer ${authData.access_token}`,
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    };
    const { documentreferenceid, fromdatetime, todatetime, status} = this.getUrlQueryParams();
    const queryParam = `&fromdatetime=${fromdatetime}&todatetime=${todatetime}&status=${status}`;
    // TODO: Need to remove count query param. these are added only for testing purpose.
    const url = `${this.cache.detailsApi}?documentreferenceid=${documentreferenceid}&count=1${queryParam}`;
    const paymentApiPromise = fetch(url, fetchHeaderOption).then(resp => resp.json());
    const statusApiPromise = fetch(this.cache.statusApiUrl).then(resp => resp.json());
    return [paymentApiPromise, statusApiPromise];
  }
  showLoader = (isShow) => {
    if (isShow) {
      this.cache.spinner.removeClass('d-none');
      this.cache.content.addClass('d-none');
    }
    else {
      this.cache.spinner.addClass('d-none');
      this.cache.content.removeClass('d-none');
    }
  }
  renderPaymentDetailsData(data, isFetchError = false){
    const { i18nKeys } = this.cache;
    const isPaymentData = data?.isPaymentData || false;
    if (isPaymentData) {
      const showToolTip = {
        totalAmountTooltip: i18nKeys['totalAmountTooltip']?.trim() || '',
        taxTooltip: i18nKeys['taxTooltip']?.trim() || '',
        withHoldingTaxesTooltip: i18nKeys['withHoldingTaxesTooltip']?.trim() || '',
        netPayableTooltip: i18nKeys['netPayableTooltip']?.trim() || '',
        paymentTermTooltip: i18nKeys['paymentTermTooltip']?.trim() || '',
        detailsDueDateTooltip: i18nKeys['detailsDueDateTooltip']?.trim() || '',
        paidDateTooltip: i18nKeys['paidDateTooltip']?.trim() || '',
        bankAccountTooltip: i18nKeys['bankAccountTooltip']?.trim() || '',
        paymentMethodTooltip: i18nKeys['paymentMethodTooltip']?.trim() || '',
        detailsPoNoTooltip: i18nKeys['detailsPoNoTooltip']?.trim() || ''
      };
      render.fn({
        template: 'paymentDetails',
        data: {paymentData: data, isError: false, i18nKeys, isFetchError, showToolTip  },
        target: this.cache.content,
        hidden: false
      }, () => {
        $(function () {
          $('[data-toggle="tooltip"]').tooltip();
        });
      });
    }
    else {
      render.fn({
        template: 'paymentDetails',
        data: { paymentData: data, isError: true, i18nKeys, isFetchError, showToolTip: {} },
        target: this.cache.content,
        hidden: false
      });
      this.cache.headerAction.addClass('d-none');
    }
    this.cache.contentWrapper.removeClass('d-none');
  }
  getStatusName = (statusCode) => {
    const data = this.cache.statusMapping;

    if (statusCode) {
      for (const key in data) {
        if (data[key].includes(statusCode)) {
          return key;
        }
      }
    }
    return '';
  };
  getTotalAmount = (paymentData) => {
    let amountInTransactionCurrency = paymentData?.amountInTransactionCurrency || '';
    let withholdingTaxAmmount = paymentData?.withholdingTaxAmmount || '';
    amountInTransactionCurrency = parseFloat(amountInTransactionCurrency);
    withholdingTaxAmmount = parseFloat(withholdingTaxAmmount);
    amountInTransactionCurrency = Number.isNaN(amountInTransactionCurrency) ? '' : amountInTransactionCurrency;
    withholdingTaxAmmount = Number.isNaN(withholdingTaxAmmount) ? '' : withholdingTaxAmmount;
    let totalInvoiceAmount = '';
    if(typeof amountInTransactionCurrency === 'number'){
      totalInvoiceAmount = amountInTransactionCurrency;
    }
    if(typeof withholdingTaxAmmount === 'number'){
      totalInvoiceAmount = totalInvoiceAmount + withholdingTaxAmmount;
    }
    return { totalInvoiceAmount, amountInTransactionCurrency, withholdingTaxAmmount };
  }
  renderPaymentDetails = () => {
    this.showLoader(true);
    auth.getToken(({ data: authData }) => {
      Promise.all(this.getApiPromise(authData))
        .then(response => {
          this.showLoader(false);
          const paymentData = response[0]?.data[0] || {};
          this.cache.statusMapping = response[1];
          const isPaymentData = Object.keys(paymentData).length > 0;
          let amountData = {};
          if(isPaymentData){
            amountData = this.getTotalAmount(paymentData);
          }
          const data = {
            ...paymentData,
            invoiceStatusName: this.getStatusName(paymentData?.invoiceStatusCode),
            isPaymentData,
            ...amountData
          };
          this.renderPaymentDetailsData(data);
        })
        .catch(() => {
          this.showLoader(false);
          this.renderPaymentDetailsData({}, true);
        });
    });
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderPaymentDetails();
  }
}

export default Paymentdetails;
