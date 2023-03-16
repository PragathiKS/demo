import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import {ajaxWrapper} from '../../../scripts/utils/ajax';
import {ajaxMethods} from '../../../scripts/utils/constants';
import {logger} from '../../../scripts/utils/logger';
import {render} from '../../../scripts/utils/render';
import {sanitize} from '../../../scripts/common/common';
import {
  _trackAccordionClick,
  _trackWithdrawStart,
  _trackWithdrawCancel,
  _trackWithdrawComplete
} from './PlantMasterLicenses-active.analytics';

/**
 * Render Active Licenses data
 */
function _renderActiveLicenses(licensesObj) {
  const { i18nKeys } = this.cache;
  const { engLicenses, siteLicenses } = licensesObj;

  render.fn({
    template: 'plantMasterLicensesActiveEng',
    target: this.cache.$activeEngLicensesWrapper,
    data: {
      i18nKeys,
      engLicenses
    }
  });

  render.fn({
    template: 'plantMasterLicensesActiveSite',
    target: this.cache.$activeSiteLicensesWrapper,
    data: {
      i18nKeys,
      siteLicenses
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

function _processLicenseData(licenseArr) {
  const engLicenseKey = 'T-PM ENG';
  const siteLicenseKey = 'T-PM SITE';

  const engLicensesArr = licenseArr.filter(item => item.licenseType === engLicenseKey);
  const siteLicensesArr = licenseArr.filter(item => item.licenseType === siteLicenseKey);

  return {
    engLicenses: engLicensesArr,
    siteLicenses: siteLicensesArr
  };
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
        const groupedLicenseData = _processLicenseData(res.data);
        this.renderActiveLicenses(groupedLicenseData);
      }).fail((e) => {
        logger.error(e);
      });
  });
}

/**
 * Submit License withdraw
 */
function _submitLicenseWithdraw(licenseDetails) {
  const { submitApi } = this.cache;
  this.showModalSpinner();

  auth.getToken(({ data: authData }) => {
    ajaxWrapper.getXhrObj({
      url: `${submitApi  }?licenseType=activeWithdrawal`,
      method: ajaxMethods.POST,
      cache: true,
      contentType: 'application/json; charset=utf-8',
      dataType:'json',
      data: JSON.stringify(licenseDetails),
      beforeSend(jqXHR) {
        jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
      },
      showLoader: true
    }).done(() => {
      this.renderLicenseWithdrawSuccess();
      this.trackWithdrawComplete(licenseDetails);
    }).fail(() => {
      this.showModalContent();
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
    this.cache.$aipLicenseObj = aipLicenseObj;
    const configJson = aipLicenseObj.find('.js-aip-licenses__config').text();
    try {
      this.cache.i18nKeys = JSON.parse(configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
    this.cache.activeLicensesApi = aipLicenseObj.data('activelicense-api');
    this.cache.submitApi = aipLicenseObj.data('submit-api');
    this.cache.$activeEngLicensesWrapper = aipLicenseObj.find('.js-tp-aip-licenses-active__eng');
    this.cache.$activeSiteLicensesWrapper = aipLicenseObj.find('.js-tp-aip-licenses-active__site');
    this.cache.$activeLicensesModal = aipLicenseObj.find('.js-tp-aip-licenses-active__modal');
    this.cache.$engLicensesDesc = this.root.find('.js-tp-aip-licenses-eng__description');
    this.cache.engLicenseUerGroup = this.engLicenseUerGroup;
  }

  showModalContent = () => {
    const { $aipLicenseObj } = this.cache;
    const $modalContentWrapper = $aipLicenseObj.find('.js-tp-aip-licenses-active__modal-content');
    const $modalSpinner = $modalContentWrapper.find('.js-tp-aip-licenses-active__modal-spinner');

    $modalContentWrapper.find('.modal-header, .modal-body, .modal-footer').removeClass('d-none');
    $modalSpinner.addClass('d-none');
  };

  showModalSpinner = () => {
    const { $aipLicenseObj } = this.cache;
    const $modalContentWrapper = $aipLicenseObj.find('.js-tp-aip-licenses-active__modal-content');
    const $modalSpinner = $modalContentWrapper.find('.js-tp-aip-licenses-active__modal-spinner');

    $modalContentWrapper.find('.modal-header, .modal-body, .modal-footer').addClass('d-none');
    $modalSpinner.removeClass('d-none');
  };

  bindEvents() {
    this.root.on('click', '.js-tp-aip-licenses-active__withdraw', (e) => {
      const $btn = $(e.currentTarget);

      const licenseDetails = {
        name: $btn.data('name'),
        platform: $btn.data('platform'),
        licenseKey: $btn.data('licenseKey'),
        countryName: $btn.data('countryName'),
        site: $btn.data('site'),
        startDate: $btn.data('startDate'),
        endDate: $btn.data('endDate')
      };

      this.renderLicenseWithdrawModal(licenseDetails);
      this.trackWithdrawStart();
    });

    this.root.on('click', '.js-close-btn, .js-tp-aip-licenses-active__cancel, .js-tp-aip-licenses-active__back',  () => {
      const { $activeLicensesModal } = this.cache;
      $activeLicensesModal.modal('hide');
    });

    this.root.on('click', '.js-tp-aip-licenses-active__cancel',  () => {
      this.trackWithdrawCancel();
    });

    this.root.on('click', '.js-tp-aip-licenses-active__confirm',  (e) => {
      const $btn = $(e.currentTarget);
      const { $aipLicenseObj } = this.cache;
      const $modalContentWrapper = $aipLicenseObj.find('.js-tp-aip-licenses-active__modal-content');
      const comments = $modalContentWrapper.find('.js-tp-aip-licenses-active__input').val();

      const licenseDetails = {
        name: $btn.data('name'),
        platform: $btn.data('platform'),
        licenseKey: $btn.data('licenseKey'),
        country: $btn.data('countryName'),
        site: $btn.data('site'),
        startDate: $btn.data('startDate'),
        endDate: $btn.data('endDate'),
        comments: sanitize(comments)
      };

      this.submitLicenseWithdraw(licenseDetails);
    });

    // track Accordion click analytics
    this.root.on('click', '.tp-aip__accordion .btn-link', e => {
      const $btn = $(e.currentTarget);
      const text = $btn.find('span').text();
      const $this = this;

      if ($btn.attr('aria-expanded') === 'true') {
        $this.trackAccordionClick(text, false);
      } else {
        $this.trackAccordionClick(text, true);
      }
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

  submitLicenseWithdraw(licenseDetails) {
    return _submitLicenseWithdraw.call(this, licenseDetails);
  }

  trackAccordionClick() {
    return _trackAccordionClick.apply(this, arguments);
  }

  trackWithdrawStart() {
    return _trackWithdrawStart.apply(this, arguments);
  }

  trackWithdrawCancel() {
    return _trackWithdrawCancel.apply(this, arguments);
  }

  trackWithdrawComplete() {
    return _trackWithdrawComplete.apply(this, arguments);
  }

  init() {
    this.initCache();
    this.getActiveLicensesData();
    this.bindEvents();
  }
}

export default PlantMasterLicensesActive;
