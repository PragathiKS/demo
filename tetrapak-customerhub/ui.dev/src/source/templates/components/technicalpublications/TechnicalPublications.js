import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import {ajaxWrapper} from '../../../scripts/utils/ajax';
import {ajaxMethods} from '../../../scripts/utils/constants';
import {logger} from '../../../scripts/utils/logger';
import {render} from '../../../scripts/utils/render';

/**
 * Fetch the Learning History data
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
  const { apiDataObj, $folderListingWrapper, $contentWrapper, $spinner } = $this.cache;
  const { countries } = apiDataObj;

  render.fn({
    template: 'technicalPublicationsGenericFolder',
    target: $folderListingWrapper,
    data: { i18nKeys: $this.cache.i18nKeys, folderData: countries}
  }, () => {
    $contentWrapper.removeClass('d-none');
    $spinner.addClass('d-none');
  });
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
    this.cache.apiDataObj = {
      countries: {},
      customers: {},
      lines: {}
    };
  }

  bindEvents() {
    logger.log('bind events');
  }

  getCountriesData() {
    return _getCountriesData.apply(this, arguments);
  }

  renderCountries() {
    return _renderCountries.apply(this, arguments);
  }

  init() {
    this.initCache();
    this.bindEvents();
    this.getCountriesData();
  }
}

export default TechnicalPublications;
