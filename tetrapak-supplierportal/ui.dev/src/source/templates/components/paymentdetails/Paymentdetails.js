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
    /**
     * Use "this.root" to find elements within current component
     * Example:
     * this.cache.$submitBtn = this.root.find('.js-submit-btn');
     */
    this.cache.configJson = this.root.find('.js-payment-details__config').text();
    this.cache.detailsApi = this.root.data('details-api');
    this.cache.exportToPdfURL = this.root.data('export-pdf-url');
    this.cache.contentWrapper = this.root.find(
      '.tp-payment-details-content'
    );
    this.cache.content = this.root.find('.tp-payment-details__content-wrapper');
    this.cache.spinner = this.root.find('.tp-spinner');
    this.cache.header = this.root.find('.tp-payment-details__header-actions');
    this.cache.headerAction = this.root.find('.tp-payment-details__header-action-wrapper');
    this.cache.data = {};
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
  }
  getActiveCountryCode = () => {
    try {
      const { countryData } = this.cache;
      const activeCountry = countryData.find(e => e.isChecked);
      if (activeCountry) {
        return activeCountry.countryCode;
      } else {
        throw Error('Couldn\'t get active country');
      }
    } catch (err) {
      logger.error(err.message);
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
    auth.getToken(() => {
      file.get({
        extension: 'pdf',
        url: this.cache.exportToPdfURL,
        method: ajaxMethods.GET
      });
    });
  }
  bindEvents() {
    // Download PDF
    this.root.on('click', '.js-payment-details__export-pdf-action',  () => {
      this.downloadPDf();
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
    const { documentreferenceid, fromdatetime = '', todatetime = '' } = this.getUrlQueryParams();
    let url = `${this.cache.detailsApi}?documentreferenceid=${documentreferenceid}`;
    if(fromdatetime){
      url = `${url}&fromdatetime=${fromdatetime}`;
    }
    if(todatetime){
      url = `${url}&todatetime=${todatetime}`;
    }
    const paymentApiPromise = fetch(url, fetchHeaderOption).then(resp => resp.json());
    return [paymentApiPromise];
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
    // i18nKeys = JSON.parse(i18nKeys);
    const isPaymentData = Object.keys(data).length > 0;
    if (isPaymentData) {
      render.fn({
        template: 'paymentDetails',
        data: {paymentData: data, isError: false, i18nKeys, isFetchError  },
        target: this.cache.content,
        hidden: false
      });
      this.cache.header.addClass('show');
    }
    else {
      render.fn({
        template: 'paymentDetails',
        data: { paymentData: data, isError: true, i18nKeys, isFetchError },
        target: this.cache.content,
        hidden: false
      });
      this.cache.headerAction.addClass('d-none');
    }
    this.cache.contentWrapper.removeClass('d-none');
  }
  renderPaymentDetails = () => {
    this.showLoader(true);
    auth.getToken(({ data: authData }) => {
      Promise.all(this.getApiPromise(authData))
        .then(response => {
          this.showLoader(false);
          const paymentData = response[0]?.data[0] || {};
          this.renderPaymentDetailsData(paymentData);
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
