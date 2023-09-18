import $ from 'jquery';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import { ajaxMethods } from '../../../scripts/utils/constants';
import auth from '../../../scripts/utils/auth';
import file from '../../../scripts/utils/file';

export const getUrlQueryParams = (url) => {
  const params = {};
  url.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (_, key, value) {
    return (params[key] = value);
  });
  return params;
};

/**
 * Fetch and render the Equipment Details
 */
function _renderPaymentDetails() {
  const $this = this;
  const { documentreferenceid } = getUrlQueryParams(window.location.href);
  auth.getToken(({ data: authData }) => {
    render.fn(
      {
        template: 'paymentDetails',
        url: {
          path: `${$this.cache.detailsApi}?documentreferenceid=${documentreferenceid}`
        },
        target: $this.cache.$content,
        ajaxConfig: {
          method: ajaxMethods.GET,
          beforeSend(jqXHR) {
            jqXHR.setRequestHeader(
              'Authorization',
              `Bearer ${authData.access_token}`
            );
          },
          cache: true,
          showLoader: true,
          cancellable: true
        },
        beforeRender(data) {
          let { i18nKeys } = $this.cache;
          i18nKeys = JSON.parse(i18nKeys);
          if (!data) {
            this.data = data = {
              isError: true,
              message: `couldn't fetch equipment due to no id passed. Please provide id param in url`,
              i18nKeys
            };
            data.isError = true;
          } else {
            data.paymentData = data.data[0];
            data.i18nKeys = i18nKeys;
            $this.cache.data = { documentreferenceid, ...data.paymentData };
          }
        }
      },
      () => {
        $this.cache.$contentWrapper.removeClass('d-none');
        $this.cache.$spinner.addClass('d-none');
        if(this.cache.paymentData){
          this.cache.$header.addClass('show');
        }
      }
    );
  });
}


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
    this.cache.$contentWrapper = this.root.find(
      '.tp-payment-details-content'
    );
    this.cache.$content = this.root.find('.tp-payment-details__content-wrapper');
    this.cache.$spinner = this.root.find('.tp-spinner');
    this.cache.$modal = this.root.find('.js-update-modal');
    this.cache.$header = this.root.find('.tp-payment-details__header-actions');
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
  downloadPDf = () => {
    const { documentreferenceid } = getUrlQueryParams(window.location.href);
    auth.getToken(() => {
      const url = this.cache.exportToPdfURL;
      file.get({
        extension: 'pdf',
        url: `${url}?documentreferenceid=${documentreferenceid}`,
        method: ajaxMethods.GET
      });
    });
  }
  bindEvents() {
    /* Bind jQuery events here */
    /**
     * Example:
     * const { $submitBtn } = this.cache;
     * $submitBtn.on('click', () => { ... });
     */
    // Download CSV
    this.root.on('click', '.js-payment-details__export-pdf-action',  () => {
      this.downloadPDf();
    });
  }
  renderPaymentDetails() {
    return _renderPaymentDetails.apply(this, arguments);
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderPaymentDetails();
  }
}

export default Paymentdetails;
