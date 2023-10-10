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
    const { documentreferenceid} = this.getUrlQueryParams();
    auth.getToken(() => {
      file.get({
        extension: 'pdf',
        url: `${this.cache.exportToPdfURL}?documentreferenceid=${documentreferenceid}&fromdatetime=2023-07-01T00:00:00&todatetime=2023-07-30T00:00:00`,
        method: ajaxMethods.GET
      });
    });
  }
  bindEvents() {
    // Download PDF
    this.root.on('click', '.js-payment-details__export-pdf-action',  () => {
      this.downloadPDf();
    });
    this.cache.mobileHeadersActions.on('click', () => {
      if($('.tp-payment-details__header-actions').hasClass('show')){
        $('.tp-payment-details__header-actions').removeClass('show');
      } else {
        $('.tp-payment-details__header-actions').addClass('show');
      }
    });
  }
  getFilterDateRange = (month) => {
    const currentDate = new Date();
    const monthsAgo = new Date(currentDate.getFullYear(), currentDate.getMonth() - Number(month), currentDate.getDate());

    // Format the date as a string (YYYY-MM-DDTHH:MM:SS)
    const formattedDate = `&fromdatetime=${monthsAgo.toISOString().slice(0, 11)}00:00:00&todatetime=${currentDate.toISOString().slice(0, 19)}`;

    return formattedDate;
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
    const { documentreferenceid} = this.getUrlQueryParams();
    const dateRange = this.getFilterDateRange(this.cache.dateRange);
    // TODO: Need to remove count query param. these are added only for testing purpose.
    const url = `${this.cache.detailsApi}?documentreferenceid=${documentreferenceid}&count=1${dateRange}`;
    const paymentApiPromise = fetch(url, fetchHeaderOption).then(resp => resp.json());
    return paymentApiPromise;
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
    const isPaymentData = Object.keys(data).length > 0;
    if (isPaymentData) {
      render.fn({
        template: 'paymentDetails',
        data: {paymentData: data, isError: false, i18nKeys, isFetchError  },
        target: this.cache.content,
        hidden: false
      });
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
      this.getApiPromise(authData)
        .then(response => {
          this.showLoader(false);
          const paymentData = response?.data[0] || {};
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
