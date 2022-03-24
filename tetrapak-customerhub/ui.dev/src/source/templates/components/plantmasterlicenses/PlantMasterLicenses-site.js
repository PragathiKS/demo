import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import {ajaxWrapper} from '../../../scripts/utils/ajax';
import {ajaxMethods} from '../../../scripts/utils/constants';
import {logger} from '../../../scripts/utils/logger';
import {render} from '../../../scripts/utils/render';

function _processSiteLicensesData(data) {
  const processedDataArr = data.descriptions[0].body;
  return processedDataArr;
}


function _renderSiteLicensesData() {
  render.fn({
    template: 'plantMasterLicensesSite',
    target: '.js-tp-aip-licenses__site-description',
    data: {
      i18nKeys: this.cache.i18nKeys,
      siteLicensesDataArr: this.cache.siteLicensesData
    }
  });
}

/**
 * Fetch and process the Site Licenses data
 */
function _getSiteLicensesData() {
  auth.getToken(({ data: authData }) => {
    ajaxWrapper
      .getXhrObj({
        url: this.cache.siteLicensesApi,
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
        this.cache.siteLicensesData = _processSiteLicensesData(res.data[0]);
        this.renderSiteLicensesData();
        this.cache.$sitecontentWrapper.removeClass('d-none');
      }).fail((e) => {
        logger.error(e);
      });
  });
}

class PlantMasterLicensesSite {
  constructor( el ) {
    this.root = $(el);
  }

  cache = {};

  initCache() {
    this.cache.$sitecontentWrapper = this.root.find('.js-tp-aip-licenses__site-description');
    const aipLicenseObj = $('.tp-aip-licenses');
    this.cache.siteLicensesApi = aipLicenseObj.data('sitelicense-api');
    this.cache.$spinner = aipLicenseObj.find('.js-tp-spinner');
    this.cache.$contentWrapper = aipLicenseObj.find('.js-tp-aip-licenses__site');
    this.cache.submitApi = aipLicenseObj.data('submit-api');
    const configJson = aipLicenseObj.find('.js-aip-licenses__config').text();
    try {
      this.cache.i18nKeys = JSON.parse(configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
  }

  showContent = () => {
    this.cache.$contentWrapper.removeClass('d-none');
    this.cache.$spinner.addClass('d-none');
  };

  showSpinner = () => {
    this.cache.$contentWrapper.addClass('d-none');
    this.cache.$spinner.removeClass('d-none');
  };

  renderSuccessMessage() {
    render.fn(
      {
        template: 'plantMasterLicensesSuccessMessage',
        target: this.cache.$contentWrapper,
        data: { i18nKeys: this.cache.i18nKeys, template: 'site' }
      },
      this.showContent
    );
  }

  addErrorMsg(ele) {
    $(ele)
      .closest('.js-tp-aip-licenses__form-element')
      .addClass('tp-aip-licenses__form-element--error')
      .find('.js-tp-aip-licenses__error-msg-required')
      .addClass('error-msg--active');
  }

  removeAllErrorMessages() {
    this.root.find('.error-msg--active').removeClass('error-msg--active');
    this.root
      .find('.tp-aip-licenses__form-element--error')
      .removeClass('tp-aip-licenses__form-element--error');
  }

  submitRequestForm = (e) => {
    e.preventDefault();
    let isFormValid = true;
    this.removeAllErrorMessages();

    const $requiredFormElements = this.root.find(':text[required]:visible, select[required]:visible');
    $requiredFormElements.each((idx, ele) => {
      if (!$(ele).val()) {
        isFormValid = false;
        this.addErrorMsg(ele);
      }
    });

    if (isFormValid) {
      const formData = new FormData(e.currentTarget.form);
      this.showSpinner();

      const object = {};
      formData.forEach((value, key) => object[key] = value);
      const siteJson = JSON.stringify(object);

      auth.getToken(({ data: authData }) => {
        ajaxWrapper.getXhrObj({
          url: this.cache.submitApi,
          method: ajaxMethods.POST,
          cache: true,
          contentType: 'application/json; charset=utf-8',
          dataType:'json',
          data: siteJson,
          beforeSend(jqXHR) {
            jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
          },
          showLoader: true
        }).done(() => {
          this.renderSuccessMessage();
        }).fail(() => {
          this.cache.$contentWrapper.removeClass('d-none');
          this.cache.$spinner.addClass('d-none');
        });
      });
    }
  }

  bindEvents() {
    this.root.on('click', '.js-tp-aip-licenses-site__btn', this.submitRequestForm);
  }

  renderSiteLicensesData() {
    return _renderSiteLicensesData.apply(this, arguments);
  }

  getSiteLicensesData() {
    return _getSiteLicensesData.apply(this, arguments);
  }

  showTooltip(){
    $('[data-toggle="tooltip"]').tooltip();
  }

  init() {
    this.initCache();
    this.showTooltip();
    this.getSiteLicensesData();
    this.bindEvents();
  }
}

export default PlantMasterLicensesSite;
