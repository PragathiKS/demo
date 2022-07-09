import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import {ajaxWrapper} from '../../../scripts/utils/ajax';
import {REG_NUM, ajaxMethods} from '../../../scripts/utils/constants';
import {logger} from '../../../scripts/utils/logger';
import {render} from '../../../scripts/utils/render';
import {
  _trackFormComplete,
  _trackFormError,
  _trackFormStart
} from './PlantMasterLicenses-site.analytics';

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

function _processLicensesData(data,pingUserGroup) {
  const processedDataArr = [];
  const siteUserGroup = pingUserGroup ? pingUserGroup :[];
  data.forEach((item) => {
    if(siteUserGroup.length > 0){
      if(siteUserGroup.includes(item.extRef.material.number)){
        processedDataArr.push(item);
      }
    }
  });
  return processedDataArr;
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
        this.cache.siteLicensesData =  _processLicensesData(res.data,this.cache.siteLicenseUerGroup);
        this.renderSiteLicensesData();
        this.cache.$sitecontentWrapper.removeClass('d-none');
      }).fail((e) => {
        logger.error(e);
      });
  });
}

class PlantMasterLicensesSite {
  constructor( el , siteLicenseUerGroup) {
    this.root = $(el);
    this.siteLicenseUerGroup = siteLicenseUerGroup;
  }

  cache = {};

  initCache() {
    this.cache.$sitecontentWrapper = this.root.find('.js-tp-aip-licenses__site-description');
    const aipLicenseObj = $('.tp-aip-licenses');
    this.cache.siteLicensesApi = aipLicenseObj.data('sitelicense-api');
    this.cache.$spinner = aipLicenseObj.find('.js-tp-spinner');
    this.cache.$contentWrapper = aipLicenseObj.find('.js-aip-licenses__wrapper');
    this.cache.submitApi = aipLicenseObj.data('submit-api');
    const configJson = aipLicenseObj.find('.js-aip-licenses__config').text();
    this.cache.siteLicenseUerGroup = this.siteLicenseUerGroup;
    this.cache.formError = [];
    try {
      this.cache.i18nKeys = JSON.parse(configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
  }

  isNumeric(number) {
    return REG_NUM.test(number);
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
        target: '.js-tp-aip-licenses__site',
        data: { i18nKeys: this.cache.i18nKeys, template: 'site' }
      },
      this.showContent
    );
  }

  addErrorMsg(ele, errorMsgSelector) {
    const $el = $(ele);
    const $this = this;
    const formErrorMessage = $el.closest('.js-tp-aip-licenses__form-element').find(errorMsgSelector).text();
    const formErrorField = $el.attr('id').split('-')[0];

    $el
      .closest('.js-tp-aip-licenses__form-element')
      .addClass('tp-aip-licenses__form-element--error')
      .find(errorMsgSelector)
      .addClass('error-msg--active');

    $this.cache.formError.push({
      formErrorMessage,
      formErrorField
    });
  }

  removeAllErrorMessages() {
    this.root.find('.error-msg--active').removeClass('error-msg--active');
    this.root
      .find('.tp-aip-licenses__form-element--error')
      .removeClass('tp-aip-licenses__form-element--error');
    this.cache.formError = [];
  }

  submitRequestForm = (e) => {
    e.preventDefault();
    let isFormValid = true;
    this.removeAllErrorMessages();

    const $requiredFormElements = this.root.find(':input[required]:visible, select[required]:visible');
    $requiredFormElements.each((idx, ele) => {
      if (!$(ele).val()) {
        isFormValid = false;
        this.addErrorMsg(ele, '.js-tp-aip-licenses__error-msg-required');
      }
    });

    const $numberField = this.root.find(':input[type="number"]:visible');
    $numberField.each((idx, ele) => {
      const numberFieldVal = $(ele).val();
      if (numberFieldVal && (!this.isNumeric(numberFieldVal) || (numberFieldVal < 1))) {
        isFormValid = false;
        this.addErrorMsg(ele, '.js-tp-aip-licenses__error-msg-invalid-number');
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
          this.trackFormComplete(object);
        }).fail(() => {
          this.showContent();
        });
      });
    } else {
      const { formError } = this.cache;
      this.trackFormError(formError);
    }
  }

  bindEvents() {
    this.root.on('click', '.js-tp-aip-licenses-site__btn', this.submitRequestForm);

    // track Form Start analytics
    this.root.on('input', '.tpatom-input-box__input', e => {
      const $formWrapper = $(e.currentTarget).parents('form');

      if (!$formWrapper.data('form-touched')) {
        $formWrapper.attr('data-form-touched', true);
        this.trackFormStart();
      }
    });

    this.root.on('change', '.tpatom-dropdown__select-box', e => {
      const $formWrapper = $(e.currentTarget).parents('form');

      if (!$formWrapper.data('form-touched')) {
        $formWrapper.attr('data-form-touched', true);
        this.trackFormStart();
      }
    });
  }

  renderSiteLicensesData() {
    return _renderSiteLicensesData.apply(this, arguments);
  }

  getSiteLicensesData() {
    return _getSiteLicensesData.apply(this, arguments);
  }

  trackFormComplete() {
    return _trackFormComplete.apply(this, arguments);
  }

  trackFormError() {
    return _trackFormError.apply(this, arguments);
  }

  trackFormStart() {
    return _trackFormStart.apply(this, arguments);
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
