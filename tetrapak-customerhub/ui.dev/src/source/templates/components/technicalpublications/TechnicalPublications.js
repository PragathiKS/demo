import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import {ajaxWrapper} from '../../../scripts/utils/ajax';
import {ajaxMethods} from '../../../scripts/utils/constants';
import {logger} from '../../../scripts/utils/logger';
import {render} from '../../../scripts/utils/render';

/**
 * Fetch the Country data
 */
function _getCountriesData() {
  const $this = this;
  auth.getToken(({ data: authData }) => {
    ajaxWrapper
      .getXhrObj({
        url: $this.cache.countriesApi,
        method: ajaxMethods.GET,
        cache: true,
        dataType: 'json',
        contentType: 'application/json',
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
          jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        showLoader: true
      }).done(res => {
        $this.cache.apiDataObj['countries'] = res.data;
        $this.renderCountries();
      }).fail((e) => {
        logger.error(e);
      });
  });
}

function _renderCountries() {
  const $this = this;
  const { apiDataObj, $folderListingWrapper } = $this.cache;
  const { countries } = apiDataObj;

  render.fn({
    template: 'technicalPublicationsGenericFolder',
    target: $folderListingWrapper,
    data: {
      i18nKeys: $this.cache.i18nKeys,
      currentStep: 'country',
      isCountryStep: true,
      folderData: countries
    }
  }, () => {
    $this.showSpinner(false);
  });
}

function _getFolderData(stepKey) {
  const $this = this;
  const { customerApi, lineApi } = $this.cache;
  const { folderNavData } = $this.cache;
  const { country, customer } = folderNavData;
  let apiUrl;

  switch (stepKey) {
    case 'customer':
      apiUrl = `${customerApi}?countrycodes=${country}&count=1500`;
      break;
    case 'line':
      apiUrl = `${lineApi}?countrycodes=${country}&customerNumber=${customer}`;
      break;
    default:
      break;
  }

  $this.showSpinner(true);

  auth.getToken(({ data: authData }) => {
    ajaxWrapper
      .getXhrObj({
        url: apiUrl,
        method: ajaxMethods.GET,
        cache: true,
        dataType: 'json',
        contentType: 'application/json',
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
          jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        showLoader: true
      }).done(res => {
        $this.renderFolderData(stepKey, res.data);
      }).fail((e) => {
        logger.error(e);
      });
  });
}

function _renderFolderData(stepKey, folderData) {
  const $this = this;
  const { $folderListingWrapper } = $this.cache;

  render.fn({
    template: 'technicalPublicationsGenericFolder',
    target: $folderListingWrapper,
    data: {
      i18nKeys: $this.cache.i18nKeys,
      currentStep: stepKey,
      folderData,
      isCustomerStep: stepKey === 'customer',
      isCountryStep: stepKey === 'country',
      isLineStep: stepKey === 'line'
    }
  }, () => {
    $this.showSpinner(false);
  });
}

function _showSpinner(state) {
  const $this = this;
  const { $contentWrapper, $spinner } = $this.cache;

  if (state) {
    $contentWrapper.addClass('d-none');
    $spinner.removeClass('d-none');
  } else {
    $contentWrapper.removeClass('d-none');
    $spinner.addClass('d-none');
  }
}

function _setFolderNavData(key, value) {
  const $this = this;
  $this.cache.folderNavData[key] = value;
}

class TechnicalPublications {
  constructor({ el }) {
    this.root = $(el);
  }

  cache = {};

  initCache() {
    this.cache.configJson = this.root.find('.js-tech-pub__config').text();
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
    this.cache.countriesApi = this.root.data('countries-api');
    this.cache.equipmentApi = this.root.data('equipment-api');
    this.cache.lineApi = this.root.data('line-api');
    this.cache.customerApi = this.root.data('customer-api');
    this.cache.techPubApi = this.root.data('tech-pub-api');
    this.cache.$contentWrapper = this.root.find('.js-tech-pub__wrapper');
    this.cache.$folderListingWrapper = this.root.find('.js-tech-pub__folder-listing');
    this.cache.$spinner = this.root.find('.js-tp-spinner');
    // save state of API data responses
    this.cache.apiDataObj = {
      countries: {},
      customers: {},
      lines: {}
    };
    // save state of current folder structure levels
    this.cache.folderNavData = {
      country: null,
      customer: null,
      line: null
    };
  }

  bindEvents() {
    this.root.on('click', '.js-tech-pub__folder-btn',  (e) => {
      const $this = this;
      const $btn = $(e.currentTarget);
      const nextFolder = $btn.data('next-folder');
      const countryId = $btn.data('country-id');
      const customerNumber = $btn.data('customer-number');
      const lineCode = $btn.data('line-code');

      if (countryId) {
        $this.setFolderNavData('country', countryId);
      }

      if (customerNumber) {
        $this.setFolderNavData('customer', customerNumber);
      }

      if (lineCode) {
        $this.setFolderNavData('line', lineCode);
      }

      $this.getFolderData(nextFolder);
    });
  }

  getCountriesData() {
    return _getCountriesData.apply(this, arguments);
  }

  renderCountries() {
    return _renderCountries.apply(this, arguments);
  }

  getFolderData() {
    return _getFolderData.apply(this, arguments);
  }

  setFolderNavData() {
    return _setFolderNavData.apply(this, arguments);
  }

  renderFolderData() {
    return _renderFolderData.apply(this, arguments);
  }

  showSpinner() {
    return _showSpinner.apply(this, arguments);
  }

  init() {
    this.initCache();
    this.bindEvents();
    this.getCountriesData();
  }
}

export default TechnicalPublications;
