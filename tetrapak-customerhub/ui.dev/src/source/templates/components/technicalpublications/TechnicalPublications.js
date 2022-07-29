import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import {ajaxWrapper} from '../../../scripts/utils/ajax';
import {ajaxMethods} from '../../../scripts/utils/constants';
import {logger} from '../../../scripts/utils/logger';
import {render} from '../../../scripts/utils/render';

function _getFolderData(stepKey, options) {
  const $this = this;
  const { countriesApi, customerApi, lineApi } = $this.cache;
  const { folderNavData, apiDataObj } = $this.cache;
  const { country, customer } = folderNavData;
  const { isBreadcrumbNav } = options;
  let apiUrl;

  switch (stepKey) {
    case 'countries':
      apiUrl = countriesApi;
      break;
    case 'country':
      apiUrl = `${customerApi}?countrycodes=${country.value}&count=1500`;
      break;
    case 'customer':
      apiUrl = `${lineApi}?countrycodes=${country.value}&customerNumber=${customer.value}`;
      break;
    case 'line':
      apiUrl = `${lineApi}?countrycodes=${country.value}&customerNumber=${customer.value}`;
      break;
    default:
      break;
  }

  $this.showSpinner(true);

  // if navigating back with Breadcrumbs, use saved data instead of a new API call
  if (isBreadcrumbNav) {
    const apiData = apiDataObj[stepKey];
    $this.renderFolderData(stepKey, apiData);
    $this.renderBreadcrumbs(stepKey);

    return false;
  }

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
        $this.renderBreadcrumbs(stepKey);
        $this.setApiData(stepKey, res.data);
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
      isCountriesStep: stepKey === 'countries',
      isLineStep: stepKey === 'line'
    }
  }, () => {
    $this.showSpinner(false);
  });
}

function _renderBreadcrumbs() {
  const $this = this;
  const { i18nKeys, folderNavData, $breadcrumbsWrapper } = $this.cache;
  const bcItems = [];

  Object.entries(folderNavData).every(([key]) => {
    const stepObj = folderNavData[key];

    $.extend(true, stepObj, {
      step: key
    });

    bcItems.push(stepObj);

    if (stepObj.isCurrentStep) {
      return false;
    }

    return true;
  });

  render.fn({
    template: 'technicalPublicationsBreadcrumbs',
    target: $breadcrumbsWrapper,
    data: {
      i18nKeys,
      bcItems
    }
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

// Saves folder nav data history, for rendering breadcrumbs
function _setFolderNavData(stepKey, value, text) {
  const $this = this;
  const { folderNavData } = $this.cache;

  // reset current step flag for all other items
  Object.entries(folderNavData).forEach(([key]) => {
    const stepObj = folderNavData[key];
    stepObj.isCurrentStep = false;
  });

  folderNavData[stepKey].value = value;
  folderNavData[stepKey].text = text;
  folderNavData[stepKey].isCurrentStep = true;
}

// Save API data to reuse when going back with breadcrumbs
function _setApiData(key, value) {
  const $this = this;
  $this.cache.apiDataObj[key] = value;
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
    this.cache.$breadcrumbsWrapper = this.root.find('.js-tech-pub__breadcrumbs');
    this.cache.$folderListingWrapper = this.root.find('.js-tech-pub__folder-listing');
    this.cache.$spinner = this.root.find('.js-tp-spinner');
    // save state of API data responses for backwards breadcrumb navigation
    this.cache.apiDataObj = {
      countries: {},
      country: {},
      customer: {},
      line: {}
    };
    // save state of current folder structure levels
    this.cache.folderNavData = {
      countries: {
        text: 'All files',
        value: 'countries',
        isCurrentStep: true
      },
      country: {
        text: null,
        value: null,
        isCurrentStep: false
      },
      customer: {
        text: null,
        value: null,
        isCurrentStep: false
      },
      line: {
        text: null,
        value: null,
        isCurrentStep: false
      }
    };
  }

  bindEvents() {
    this.root.on('click', '.js-tech-pub__folder-btn',  (e) => {
      const $this = this;
      const $btn = $(e.currentTarget);
      const currentStep = $btn.data('current-step');
      const nextFolder = $btn.data('next-folder');

      if (currentStep === 'countries') {
        const countryId = $btn.data('country-id');
        const countryName = $btn.data('country-name');
        $this.setFolderNavData(nextFolder, countryId, countryName);
      }

      if (currentStep === 'country') {
        const customerNumber = $btn.data('customer-number');
        const customer = $btn.data('customer');
        $this.setFolderNavData(nextFolder, customerNumber, customer);
      }

      if (currentStep === 'customer') {
        const lineCode = $btn.data('line-code');
        const lineDescription = $btn.data('line-description');
        $this.setFolderNavData(nextFolder, lineCode, lineDescription);
      }

      if (currentStep === 'line') {
        const lineCode = $btn.data('line-code');
        const lineDescription = $btn.data('line-description');
        $this.setFolderNavData(nextFolder, lineCode, lineDescription);
      }

      $this.getFolderData(nextFolder, {isBreadcrumbNav: false});
    });

    this.root.on('click', '.js-tech-pub__bc-btn',  (e) => {
      const $this = this;
      const $btn = $(e.currentTarget);
      const targetStep = $btn.data('step');
      const { folderNavData } = $this.cache;

      // set isCurrentPage after clicking on breadcrumb
      Object.entries(folderNavData).forEach(([key]) => {
        const stepObj = folderNavData[key];
        if (stepObj.step === targetStep) {
          stepObj.isCurrentStep = true;
        }
      });

      $this.getFolderData(targetStep, {isBreadcrumbNav: true});
    });
  }

  getFolderData() {
    return _getFolderData.apply(this, arguments);
  }

  setFolderNavData() {
    return _setFolderNavData.apply(this, arguments);
  }

  setApiData() {
    return _setApiData.apply(this, arguments);
  }

  renderFolderData() {
    return _renderFolderData.apply(this, arguments);
  }

  renderBreadcrumbs() {
    return _renderBreadcrumbs.apply(this, arguments);
  }

  showSpinner() {
    return _showSpinner.apply(this, arguments);
  }

  init() {
    this.initCache();
    this.bindEvents();
    this.getFolderData('countries', {isBreadcrumbNav: false});
  }
}

export default TechnicalPublications;
