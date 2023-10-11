import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import {ajaxWrapper} from '../../../scripts/utils/ajax';
import {ajaxMethods} from '../../../scripts/utils/constants';
import {logger} from '../../../scripts/utils/logger';
import {render} from '../../../scripts/utils/render';
import {getI18n} from '../../../scripts/common/common';
import { DOCUMENT_TYPES } from './constants';

function _getFolderData(stepKey, options) {
  const $this = this;
  const {
    countriesApi,
    customerApi,
    lineApi,
    equipmentApi,
    techPubApi,
    searchResults,
    i18nKeys,
    techPubApiResults
  } = $this.cache;
  const { folderNavData, apiDataObj } = $this.cache;
  const { country, customer, line, lineFolders, folderDetails } = folderNavData;
  const { isBreadcrumbNav, useOriginalSerialNumber } = options;
  let apiUrl;
  let serialNumber;

  this.cache.typeCode = null;

  switch (stepKey) {
    case 'countries': {
      apiUrl = countriesApi;
      break;
    }
    case 'country': {
      apiUrl = `${customerApi}?countrycodes=${country.value}&count=1500&sort=customer asc`;
      break;
    }
    case 'customer': {
      apiUrl = `${lineApi}?countrycodes=${country.value}&customerNumber=${customer.value}&sort=lineDescription asc`;
      break;
    }
    case 'line': {
      apiUrl = `${equipmentApi}?skip=0&countrycodes=${country.value}&customerNumber=${customer.value}&linecodes=${line.value}&results=extended&sort=lineName asc`;
      break;
    }
    case 'lineFolders': {
      const value = `${lineFolders.value}`;
      const valueParts = value.split('/');
      
      serialNumber = useOriginalSerialNumber ? 
        value : `${valueParts[0]}%2F${valueParts[1]}`;
      apiUrl = `${techPubApi}/${serialNumber}`;
      
      break;
    }
    case 'folderDetails': {
      const value = `${folderDetails.value}`;
      const valueParts = value.split(',')[1].split('/');

      serialNumber = useOriginalSerialNumber ? 
        value : `${valueParts[0]}%2F${valueParts[1]}`;
      apiUrl = `${techPubApi}/${serialNumber}`;

      break;
    }
    default: {
      break;
    }
  }

  $this.showSpinner(true);

  // if navigating back with Breadcrumbs, use saved data instead of a new API call
  if (isBreadcrumbNav) {
    const apiData = apiDataObj[stepKey];
    $this.renderFolderData(stepKey, apiData);
    $this.renderBreadcrumbs(stepKey);

    return false;
  }

  if (techPubApiResults && stepKey === 'folderDetails') {
    searchResults.hide();

    const documentType = folderDetails.value.split(',')[0];
    const searchResultsLabel = getI18n(i18nKeys.searchResults);

    const srNo = folderDetails.value.split(',')[1];
    const finalData = techPubApiResults.filter(data => data.typeCode === documentType);
    searchResults.show();
    searchResults.text(`${finalData.length} ${searchResultsLabel}`);

    $this.renderFolderData(stepKey, finalData, srNo, documentType);
    $this.renderBreadcrumbs(stepKey);
    $this.setApiData(stepKey, finalData);

    return;
  }

  return auth.getToken(({ data: authData }) => {
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
        searchResults.hide();

        let finalData = res.data;
        let srNo = '';
        let docType = '';

        if (stepKey === 'countries') {
          finalData.sort((A, B) => A.countryName.toLowerCase() <= B.countryName.toLowerCase() ? -1 : 1);
        }

        if (stepKey === 'customer') {
          let plantDocsLine;
          finalData = res.data.filter((docLine) => {
            if (docLine.lineDescription === 'PLANT DOCUMENTATION') {           
              plantDocsLine = ({
                ...docLine,
                lineDescription: this.cache.plantDocsLabel
              });
              return false;
            }
            return true;
          });
          if (plantDocsLine) {
            finalData.unshift(plantDocsLine);
          }
        }

        if(stepKey === 'line') {
          finalData = res.data
            .filter(data => data.serialNumber !== '')
            .map(docLine => {
              if (docLine.lineName === 'PLANT DOCUMENTATION') {
                return ({
                  ...docLine,
                  lineName: this.cache.plantDocLabel
                });
              }
              return docLine;
            });
        }
        
        if(stepKey === 'lineFolders') {
          const docData = [];
          const docObj = {};

          // Filter Data by Document Type
          res.data.forEach(function(data) {
            docObj[data.typeCode] = `${data.typeCode} - ${data.type}`;
          });

          Object.keys(docObj).forEach(function(docType) {
            const docObject = {
              docType,
              'docTitle': docObj[docType],
              'serialNo': lineFolders.value
            };
            docData.push(docObject);
          });

          finalData = docData.filter(function(data){
            const isValidDocType = DOCUMENT_TYPES.includes(data.docType);

            if(isValidDocType) {
              return data;
            }
          });

          $this.setTechPubApiResults(res.data);
        }
        if (stepKey === 'folderDetails') {
          const documentType = folderDetails.value.split(',')[0];
          const searchResultsLabel = getI18n(i18nKeys.searchResults);

          srNo = folderDetails.value.split(',')[1];
          finalData = res.data.filter(data => data.typeCode === documentType);
          docType = documentType;
          searchResults.show();
          searchResults.text(`${finalData.length} ${searchResultsLabel}`);
        }
        $this.renderFolderData(stepKey, finalData, srNo, docType);
        $this.renderBreadcrumbs(stepKey);
        $this.setApiData(stepKey, finalData);
      }).fail((e) => {
        logger.error(e);
      });
  });
}

function _renderFolderData(currentStep, folderData, serialNumber, typeCode) {
  const $this = this;
  const { $folderListingWrapper, i18nKeys } = $this.cache;

  const renderIssueDate = ['SPC'].includes(typeCode);
  const renderDescription = ['TEM', 'CM'].includes(typeCode);
  const renderRKNumber = ['RM', 'UP', 'KIT'].includes(typeCode);
  const renderRKName = ['RM', 'UP', 'KIT'].includes(typeCode);

  let data = [...folderData];

  if (renderRKNumber || renderRKName) {
    data = this.extractRKDetails(folderData);
  }

  render.fn({
    template: 'technicalPublicationsGenericFolder',
    target: $folderListingWrapper,
    data: {
      i18nKeys,
      documentType: typeCode,
      currentStep,
      serialNumber,
      folderData: data,
      isCustomerStep: currentStep === 'customer',
      isCountryStep: currentStep === 'country',
      isCountriesStep: currentStep === 'countries',
      isLineStep: currentStep === 'line',
      isLineFoldersStep: currentStep === 'lineFolders',
      isFolderDetailsStep: currentStep === 'folderDetails',
      renderIssueDate,
      renderDescription,
      renderRKNumber,
      renderRKName
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
    this.cache.searchResults = this.root.find('.js-tech-pub__search-count');
    this.cache.techPubApiResults = null;

    /*
      Below values are used for both the Plant Documentation labeling and for checking whether given document is a Plant Documentation.
      Contrary to other lineNames, Plant Documentation doesn't require additional formatting for serial number, 
      hence we have to filter it out. 
    */
    this.cache.plantDocsLabel = this.cache.i18nKeys.plantDocumentations ? 
      getI18n(this.cache.i18nKeys.plantDocumentations) : 'Plant Documents';
    this.cache.plantDocLabel = this.cache.i18nKeys.plantDocumentation ? 
      getI18n(this.cache.i18nKeys.plantDocumentation) : 'Plant Documentation';

    // save state of API data responses for backwards breadcrumb navigation
    this.cache.apiDataObj = {
      countries: {},
      country: {},
      customer: {},
      line: {},
      lineFolders: {},
      folderDetails: {}
    };
    // save state of current folder structure levels
    this.cache.folderNavData = {
      countries: {
        text: this.cache.i18nKeys.allFiles,
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
      },
      lineFolders: {
        text: null,
        value: null,
        isCurrentStep: false
      },
      folderDetails: {
        text: null,
        value: null,
        isCurrentStep: false
      }
    };
  }

  setTechPubApiResults = (value) => {
    this.cache.techPubApiResults = value;
  }

  bindEvents() {
    this.root.on('click', '.js-tech-pub__folder-btn',  (e) => {
      const $this = this;
      const $btn = $(e.currentTarget);
      const currentStep = $btn.data('current-step');
      const nextFolder = $btn.data('next-folder');
      const label = $btn.text().trim();
      const useOriginalSerialNumber = 
        label === this.cache.plantDocsLabel || 
        label === this.cache.plantDocLabel;

      if (currentStep === 'countries') {
        const countryId = $btn.data('country-id');
        const countryName = $btn.data('country-name');
        $this.setFolderNavData(nextFolder, countryId, countryName);
        $this.setTechPubApiResults(null);
      }

      if (currentStep === 'country') {
        const customerNumber = $btn.data('customer-number');
        const customer = $btn.data('customer');
        $this.setFolderNavData(nextFolder, customerNumber, customer);
        $this.setTechPubApiResults(null);
      }

      if (currentStep === 'customer') {
        const lineCode = $btn.data('line-code');
        const lineDescription = $btn.data('line-description');
        $this.setFolderNavData(nextFolder, lineCode, lineDescription);
        $this.setTechPubApiResults(null);
      }

      if (currentStep === 'line') {
        const lineCode = $btn.data('line-code');
        const lineDescription = $btn.data('line-description');
        $this.setFolderNavData(nextFolder, lineCode, lineDescription);
        $this.setTechPubApiResults(null);
      }

      if (currentStep === 'lineFolders') {
        const lineCode = $btn.data('folder-code');
        const lineDescription = $btn.data('folder-description');
        $this.setFolderNavData(nextFolder, lineCode, lineDescription);
      }

      if (currentStep === 'folderDetails') {
        const serialCode = $btn.data('serial-code');
        const folderDescription = $btn.data('folder-description');
        $this.setFolderNavData(nextFolder, serialCode, folderDescription);
      }

      $this.getFolderData(nextFolder, {
        isBreadcrumbNav: false,
        useOriginalSerialNumber
      });
    });

    this.root.on('click', '.js-tech-pub__bc-btn',  (e) => {
      const $this = this;
      const $btn = $(e.currentTarget);
      const targetStep = $btn.data('step');
      const currentStep = Object.keys($this.cache.folderNavData).find((key) => {
        const folderNavValue = $this.cache.folderNavData[key];
        if (folderNavValue.isCurrentStep) {
          return true;
        }
        return false;
      });

      if (targetStep === currentStep) {
        return;
      }

      const { folderNavData, searchResults } = $this.cache;

      searchResults.hide();

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

  extractRKDetails(data) {
    return data.map((doc) => {
      const { name, optionsAndKits } = doc;

      if (name && optionsAndKits && optionsAndKits.length) {
        const rK = optionsAndKits.find(({ name: kitName }) => kitName === name);
        const { name: rkName, devStep, drawingSpec } = rK;
        const rkNumber = `${drawingSpec}-${devStep}`;

        return {
          ...doc,
          rkName,
          rkNumber
        };
      }

      return doc;
    });
  }

  init() {
    this.initCache();
    this.bindEvents();
    this.getFolderData('countries', {isBreadcrumbNav: false});
  }
}

export default TechnicalPublications;
