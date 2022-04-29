import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import {ajaxWrapper} from '../../../scripts/utils/ajax';
import {ajaxMethods} from '../../../scripts/utils/constants';
import {logger} from '../../../scripts/utils/logger';
import {render} from '../../../scripts/utils/render';

/**
 * Render Active Licenses data
 */
function _renderActiveLicenses() {
  const { i18nKeys } = this.cache;

  render.fn({
    template: 'plantMasterLicensesActiveEng',
    target: this.cache.$activeEngLicensesWrapper,
    data: {
      i18nKeys
    }
  });

  render.fn({
    template: 'plantMasterLicensesActiveSite',
    target: this.cache.$activeSiteLicensesWrapper,
    data: {
      i18nKeys
    }
  });
}

/**
 * Render License Withdraw modal
 */
function _renderLicenseWithdrawModal(licenseDetails) {
  const { $activeLicensesModal, i18nKeys } = this.cache;

  render.fn({
    template: 'plantMasterLicensesWithdraw',
    target: $activeLicensesModal,
    data: {
      licenseDetails,
      i18nKeys
    }
  });

  $activeLicensesModal.modal();
}

/**
 * Render License Withdrawn Successful modal
 */
function _renderLicenseWithdrawSuccess() {
  const { i18nKeys } = this.cache;
  render.fn({
    template: 'plantMasterLicensesWithdrawSuccess',
    target: '.js-tp-aip-licenses-active__modal-content',
    data: {
      i18nKeys
    }
  });
}

/**
 * Fetch Active Licenses data
 */
function _getActiveLicensesData() {
  auth.getToken(({ data: authData }) => {
    ajaxWrapper
      .getXhrObj({
        url: this.cache.activeLicensesApi,
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
        this.renderActiveLicenses(res);
        this.showContent();
      }).fail((e) => {
        logger.error(e);
      });
  });
}

class PlantMasterLicensesActive {
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
    this.cache.activeLicensesApi = aipLicenseObj.data('activelicense-api');
    this.cache.$spinner = aipLicenseObj.find('.js-tp-spinner');
    this.cache.$contentWrapper = aipLicenseObj.find('.js-aip-licenses__wrapper');
    this.cache.$activeEngLicensesWrapper = aipLicenseObj.find('.js-tp-aip-licenses-active__eng');
    this.cache.$activeSiteLicensesWrapper = aipLicenseObj.find('.js-tp-aip-licenses-active__site');
    this.cache.$activeLicensesModal = aipLicenseObj.find('.js-tp-aip-licenses-active__modal');
    this.cache.$engLicensesDesc = this.root.find('.js-tp-aip-licenses-eng__description');
    this.cache.engLicenseUerGroup = this.engLicenseUerGroup;
  }

  showContent = () => {
    this.cache.$contentWrapper.removeClass('d-none');
    this.cache.$spinner.addClass('d-none');
  };

  showSpinner = () => {
    this.cache.$contentWrapper.addClass('d-none');
    this.cache.$spinner.removeClass('d-none');
  };

  bindEvents() {
    this.root.on('click', '.js-tp-aip-licenses-active__withdraw', (e) => {
      const $btn = $(e.currentTarget);

      const licenseDetails = {
        firstName: $btn.data('firstName'),
        surname: $btn.data('surname'),
        platform: $btn.data('platform')
      };

      this.renderLicenseWithdrawModal(licenseDetails);
    });

    this.root.on('click', '.js-close-btn, .js-tp-aip-licenses-active__cancel, .js-tp-aip-licenses-active__back',  () => {
      const { $activeLicensesModal } = this.cache;
      $activeLicensesModal.modal('hide');
    });

    this.root.on('click', '.js-tp-aip-licenses-active__confirm',  () => {
      this.renderLicenseWithdrawSuccess();
    });
  }

  getActiveLicensesData() {
    return _getActiveLicensesData.apply(this, arguments);
  }

  renderLicenseWithdrawSuccess() {
    return _renderLicenseWithdrawSuccess.apply(this, arguments);
  }

  renderActiveLicenses() {
    return _renderActiveLicenses.apply(this, arguments);
  }

  renderLicenseWithdrawModal(licenseDetails) {
    return _renderLicenseWithdrawModal.call(this, licenseDetails);
  }

  init() {
    this.initCache();
    this.getActiveLicensesData();
    this.bindEvents();
  }
}

export default PlantMasterLicensesActive;
