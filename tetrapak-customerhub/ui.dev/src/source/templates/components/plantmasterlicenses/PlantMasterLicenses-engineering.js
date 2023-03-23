import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import moment from 'moment';
import {ajaxWrapper} from '../../../scripts/utils/ajax';
import {ajaxMethods} from '../../../scripts/utils/constants';
import {logger} from '../../../scripts/utils/logger';
import {render} from '../../../scripts/utils/render';
import {sanitize} from '../../../scripts/common/common';
import {_trackTabClick, _trackFormError, _trackFormComplete, _trackFormStart} from './PlantMasterLicenses-engineering.analytics';

/**
 * Render Engineering Licenses data
 */
function _renderEngLicensesDesc() {
  render.fn({
    template: 'plantmasterLicensesEngDesc',
    target: this.cache.$engLicensesDesc,
    data: {
      i18nKeys: this.cache.i18nKeys,
      engLicensesDataArr: this.cache.engLicensesDataArr
    }
  });
}

function _processEngLicensesDesc(data,pingUserGroup) {
  const processedDataArr = [];
  const engUserGroup = pingUserGroup ? pingUserGroup :[];
  data.forEach((item) => {
    if(engUserGroup.length > 0){
      if(engUserGroup.includes(item.extRef.material.number)){
        processedDataArr.push(item);
      }
    }
  });
  return processedDataArr;
}

/**
 * Build obj containing licenses checkbox data
 */
function _getLicenseCheckboxData(licensesDataArr) {
  const licenseCheckboxesArr = licensesDataArr.map(license => ({
    value: license.name,
    name: license.name
  }));

  return licenseCheckboxesArr;
}

/**
 * Fetch Engineering Licenses data
 */
function _getEngineeringLicensesData() {
  auth.getToken(({ data: authData }) => {
    ajaxWrapper
      .getXhrObj({
        url: this.cache.engineeringLicensesApi,
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
        this.cache.engLicensesDataArr = _processEngLicensesDesc(res.data,this.cache.engLicenseUerGroup);
        this.renderEngLicensesDesc();
        this.renderLicenseHolders({removable: false});
        this.showContent();
      }).fail((e) => {
        logger.error(e);
      });
  });
}

class PlantMasterLicensesEngineering {
  constructor(el,engLicenseUerGroup) {
    this.root = $(el);
    this.engLicenseUerGroup = engLicenseUerGroup;
  }

  cache = {};

  initCache() {
    const aipLicenseObj = $('.tp-aip-licenses');
    const configJson = aipLicenseObj.find('.js-aip-licenses__config').text();
    try {
      this.cache.i18nKeys = JSON.parse(configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
    this.cache.submitApi = aipLicenseObj.data('submit-api');
    this.cache.engineeringLicensesApi = aipLicenseObj.data('engineeringlicense-api');
    this.cache.$spinner = aipLicenseObj.find('.js-tp-spinner');
    this.cache.$contentWrapper = aipLicenseObj.find('.js-aip-licenses__wrapper');
    this.cache.$engContentWrapper = aipLicenseObj.find('.js-tp-aip-licenses__eng');
    this.cache.$addUserBtn = this.root.find('.js-tp-aip-licenses-eng__add-user');
    this.cache.$submitBtn = this.root.find('.js-tp-aip-licenses-eng__btn');
    this.cache.$licenseHoldersEl = this.root.find('.js-tp-aip-licenses-eng__holders-wrap');
    this.cache.$engLicensesDesc = this.root.find('.js-tp-aip-licenses-eng__description');
    this.cache.engLicensesDataArr = [];
    this.cache.licenseHoldersCount = 1;
    this.cache.engLicenseUerGroup = this.engLicenseUerGroup;
    this.cache.formError = [];
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
        target: this.cache.$engContentWrapper,
        data: { i18nKeys: this.cache.i18nKeys, template: 'engineering' }
      },
      this.showContent
    );
  }

  submitRequestForm = (e) => {
    e.preventDefault();
    const { $licenseHoldersEl } = this.cache;
    const $newHolderForms = $licenseHoldersEl.find('.js-tp-aip-licenses-eng__new-holder');
    const $commentsInput = this.root.find('#engineeringLicensesComments');
    const $submitBtnWrapper = this.cache.$submitBtn.parent();
    let allFormsValid = true;
    const formObj = {};
    const users = [];
    const comments = $commentsInput.val();

    $newHolderForms.each((idx, formEl) => {
      const isHolderValid = this.validateLicenseHolder($(formEl), idx);

      if (!isHolderValid) {
        allFormsValid = false;
      }
    });

    if (!allFormsValid) {
      const { formError } = this.cache;
      this.addErrorMsg($submitBtnWrapper,'.js-tp-aip-licenses__error-msg-required');
      this.trackFormError(formError);
      this.cache.formError = [];

      return;
    }

    this.showSpinner();

    $newHolderForms.each((idx, formEl) => {
      const $newHolderForm = $(formEl);
      const $requiredFormElements = $newHolderForm.find('input[required]');
      const $licensesCheckboxGroup = $newHolderForm.find('.js-tp-aip-licenses__checkbox-group');
      const $selectedLicenses = $licensesCheckboxGroup.find('input:checked');
      const user = {
        licenses: []
      };

      $requiredFormElements.each((idx, el) => {
        const updatedKey = el.name.split('-')[0];
        user[updatedKey] = sanitize(el.value);
      });

      $selectedLicenses.each((idx, el) => {
        user.licenses.push(el.value);
      });

      users.push(user);
    });

    formObj.users = users;
    formObj.comments = sanitize(comments);

    ajaxWrapper
      .getXhrObj({
        url: `${this.cache.submitApi  }?licenseType=engineering`,
        method: ajaxMethods.POST,
        cache: true,
        contentType: 'application/json; charset=utf-8',
        dataType:'json',
        data: JSON.stringify(formObj),
        showLoader: true
      }).done(() => {
        this.renderSuccessMessage();
        this.trackFormComplete(formObj);
      }).fail(() => {
        this.showContent();
      });
  }

  bindEvents() {
    this.cache.$addUserBtn.on('click', () => {
      this.cache.licenseHoldersCount++;
      this.renderLicenseHolders({removable: true});
    });

    this.root.on('click', '.js-tp-aip-licenses-eng__remove-user', (e) => {
      const $btn = $(e.target);
      const $holderForm = $btn.parents('.js-tp-aip-licenses-eng__new-holder');
      $holderForm.remove();
    });

    this.cache.$submitBtn.on('click', this.submitRequestForm);

    // track Tab click analytics
    $('#nav-engineering-licenses-tab').on('click', this.trackTabClick);

    // track Form start analytics
    this.root.on('input', '.tpatom-textarea-box__input, .tpatom-input-box__input, .tpatom-checkbox__input', e => {
      const $formWrapper = $(e.currentTarget).parents('.js-tp-aip-licenses-eng__form').find('form');

      if (!$formWrapper.data('form-touched')) {
        $formWrapper.attr('data-form-touched', true);
        this.trackFormStart();
      }
    });
  }

  validateLicenseHolder($newHolderForm, idx) {
    let isFormValid = true;
    const formIdx = idx;
    this.removeAllErrorMessages($newHolderForm);

    const $requiredFormElements = $newHolderForm.find('input[required]:visible');
    const $licensesCheckboxGroup = $newHolderForm.find('.js-tp-aip-licenses__checkbox-group');
    const selectedLicenses = $licensesCheckboxGroup.find('input:checked');

    if (!selectedLicenses.length) {
      isFormValid = false;
      this.addErrorMsg($licensesCheckboxGroup,'.js-tp-aip-licenses__error-msg-required', formIdx);
    }

    $requiredFormElements.each((idx, ele) => {
      if (!$(ele).val()) {
        isFormValid = false;
        this.addErrorMsg(ele,'.js-tp-aip-licenses__error-msg-required', formIdx);
      }

      if ($(ele).hasClass('js-aip-license__date-input') && $.trim($(ele).val())) {
        const date = moment($(ele).val(), 'YYYY-MM-DD', true);
        if (!date.isValid()) {
          isFormValid = false;
          this.addErrorMsg(ele,'.js-date-formate__error', formIdx);
        }
      }
    });

    return isFormValid;
  }

  renderLicenseHolders({removable}) {
    const { engLicensesDataArr, licenseHoldersCount } = this.cache;
    const availableLicensesCheckboxes = this.getLicenseCheckboxData(engLicensesDataArr);

    render.fn({
      append: 'append',
      template: 'plantmasterLicensesEngHolders',
      data: {
        i18nKeys: this.cache.i18nKeys.engineeringLicense,
        licenses: availableLicensesCheckboxes,
        removable,
        idx: licenseHoldersCount
      },
      target: this.cache.$licenseHoldersEl
    });
  }

  addErrorMsg(el, errorMsgSelector, formIndex) {
    const $el = $(el);
    const $this = this;
    let formErrorField;
    const formErrorMessage = $el.closest('.js-tp-aip-licenses__form-element').find(errorMsgSelector).text();

    $el
      .closest('.js-tp-aip-licenses__form-element')
      .addClass('tp-aip-licenses__form-element--error')
      .find(errorMsgSelector)
      .addClass('error-msg--active');

    // only track errors in first form on page
    if (formIndex === 0) {
      formErrorField = $el.hasClass('js-tp-aip-licenses__checkbox-group') ? 'licensesCheckboxGroup' : $el.attr('id').split('-')[0];

      $this.cache.formError.push({
        formErrorMessage,
        formErrorField
      });
    }
  }

  removeAllErrorMessages($form) {
    $form.find('.error-msg--active').removeClass('error-msg--active');
    $form
      .find('.tp-aip-licenses__form-element--error')
      .removeClass('tp-aip-licenses__form-element--error');
  }

  getEngineeringLicensesData() {
    return _getEngineeringLicensesData.apply(this, arguments);
  }

  renderEngLicensesDesc() {
    return _renderEngLicensesDesc.apply(this, arguments);
  }

  getLicenseCheckboxData() {
    return _getLicenseCheckboxData.apply(this, arguments);
  }

  trackTabClick() {
    return _trackTabClick.apply(this, arguments);
  }

  trackFormError() {
    return _trackFormError.apply(this, arguments);
  }

  trackFormComplete() {
    return _trackFormComplete.apply(this, arguments);
  }

  trackFormStart() {
    return _trackFormStart.apply(this, arguments);
  }

  init() {
    this.initCache();
    this.getEngineeringLicensesData();
    this.bindEvents();
  }
}

export default PlantMasterLicensesEngineering;
