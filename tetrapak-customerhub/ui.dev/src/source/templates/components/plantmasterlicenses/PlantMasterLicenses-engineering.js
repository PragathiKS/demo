import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import {ajaxWrapper} from '../../../scripts/utils/ajax';
import {ajaxMethods} from '../../../scripts/utils/constants';
import {logger} from '../../../scripts/utils/logger';
import {render} from '../../../scripts/utils/render';

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

/**
 * Build obj containing licenses checkbox data
 */
function _getLicenseCheckboxData(licensesDataArr) {
  const licenseCheckboxesArr = licensesDataArr.map(license => {
    return {
      value: license.id,
      name: license.name
    };
  });

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
        this.cache.engLicensesDataArr = res.data;
        this.renderEngLicensesDesc();
        this.renderLicenseHolders({removable: false});
        this.showContent();
      }).fail((e) => {
        logger.error(e);
      });
  });
}

class PlantMasterLicensesEngineering {
  constructor(el) {
    this.root = $(el);
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
      const isHolderValid = this.validateLicenseHolder($(formEl));

      if (!isHolderValid) {
        allFormsValid = false;
      }
    });

    if (!allFormsValid) {
      this.addErrorMsg($submitBtnWrapper);
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
        user[updatedKey] = el.value;
      });

      $selectedLicenses.each((idx, el) => {
        user.licenses.push(el.value);
      });

      users.push(user);
    });

    formObj.users = users;
    formObj.comments = comments;

    ajaxWrapper
      .getXhrObj({
        url: this.cache.submitApi,
        method: ajaxMethods.POST,
        headers: { 'licenseType': 'engineering' },
        cache: true,
        contentType: 'application/json; charset=utf-8',
        dataType:'json',
        data: JSON.stringify(formObj),
        showLoader: true
      }).done(() => {
        this.renderSuccessMessage();
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
  }

  validateLicenseHolder($newHolderForm) {
    let isFormValid = true;
    this.removeAllErrorMessages($newHolderForm);

    const $requiredFormElements = $newHolderForm.find('input[required]:visible');
    const $licensesCheckboxGroup = $newHolderForm.find('.js-tp-aip-licenses__checkbox-group');
    const selectedLicenses = $licensesCheckboxGroup.find('input:checked');

    if (!selectedLicenses.length) {
      isFormValid = false;
      this.addErrorMsg($licensesCheckboxGroup);
    }

    $requiredFormElements.each((idx, ele) => {
      if (!$(ele).val()) {
        isFormValid = false;
        this.addErrorMsg(ele);
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

  addErrorMsg(ele) {
    $(ele)
      .closest('.js-tp-aip-licenses__form-element')
      .addClass('tp-aip-licenses__form-element--error')
      .find('.js-tp-aip-licenses__error-msg-required')
      .addClass('error-msg--active');
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

  init() {
    this.initCache();
    this.getEngineeringLicensesData();
    this.bindEvents();
  }
}

export default PlantMasterLicensesEngineering;
