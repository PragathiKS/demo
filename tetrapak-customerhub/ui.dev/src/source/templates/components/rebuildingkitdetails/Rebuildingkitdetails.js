import $ from 'jquery';
import 'bootstrap';
import { render } from '../../../scripts/utils/render';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import auth from '../../../scripts/utils/auth';
import { ajaxMethods } from '../../../scripts/utils/constants';
import { storageUtil } from '../../../scripts/common/common';
import { logger } from '../../../scripts/utils/logger';

/**
 * Fetch and render the Rebuilding kit Details
 */

function _renderRebuildingKitDetailsBottom() {
  const $this = this;
  const { $rebuildingData } = $this.cache;
  const { i18nKeys } = $this.cache;

  render.fn({
    template: 'rebuildingkitDetailsBottom',
    target: this.cache.$contenbottom,
    data: { i18nKeys: i18nKeys, rebuildingData: $rebuildingData }
  });
}

function _renderRebuildingKitDetails() {
  const $this = this;
  const { $rebuildingData } = $this.cache;
  const { i18nKeys } = $this.cache;

  render.fn({
    template: 'rebuildingkitDetails',
    target: $this.cache.$content,
    data: { i18nKeys: i18nKeys, rebuildingData: $rebuildingData }
  });
}

function _renderCtiDocuments(langAvailable, otherLang, reqOtherLang) {
  const $this = this;
  const { i18nKeys } = $this.cache;

  if (!langAvailable) {
    render.fn({
      template: 'rebuildingCtiDocuments',
      target: $this.cache.$contentdocs,
      data: {
        noData: true,
        i18nKeys: i18nKeys,
        reqLanguages: reqOtherLang
      }
    });

    const rkCTIModal = $('.js-rk-cti-modal');
    $this.cache.$requestTranlation = rkCTIModal.find('.js-request-translation');
    $this.cache.$reqCtiDrpdwn = rkCTIModal.find('#requestCtiLanguage');
    $this.cache.$errMsg = rkCTIModal.find('.error-msg');
    $this.cache.$ctiDocContainer = rkCTIModal.find('.tp-rebuilding-details__content');
    $this.cache.$reqCtiSuccess = rkCTIModal.find('.tp-rebuilding-details__success');

    $('.js-langcode').on('click', function (e) {
      e.preventDefault();
      rkCTIModal.modal('show');
      $this.cache.$ctiDocContainer.removeClass('d-none');
      $this.cache.$reqCtiSuccess.addClass('d-none');
    });
    $('.js-close-btn').on('click', function (e) {
      e.preventDefault();
      rkCTIModal.modal('hide');
      $this.cache.$ctiDocContainer.removeClass('d-none');
      $this.cache.$reqCtiSuccess.addClass('d-none');
    });
    $this.cache.$ctiDocContainer.removeClass('d-none');
    $this.cache.$reqCtiSuccess.addClass('d-none');
    $this.cache.$requestTranlation.on('click', function () {
      const reqLang = $this.cache.$reqCtiDrpdwn.val();
      if (reqLang === '') {
        $this.cache.$errMsg.addClass('error-msg--active');
      } else {
        if ($this.cache.$errMsg.hasClass('error-msg--active')) {
          $this.cache.$errMsg.removeClass('error-msg--active');
        }
        $this.requestCtiLanguage(reqLang);
      }
    });
  } else {
    render.fn({
      template: 'rebuildingCtiDocuments',
      target: $this.cache.$contentdocs,
      data: {
        i18nKeys: i18nKeys,
        ctiData: langAvailable,
        ctiOther: otherLang,
        reqLanguages: reqOtherLang
      }
    });

    const rkCTIModal = $('.js-rk-cti-modal');
    $this.cache.$requestTranlation = rkCTIModal.find('.js-request-translation');
    $this.cache.$reqCtiDrpdwn = rkCTIModal.find('#requestCtiLanguage');
    $this.cache.$errMsg = rkCTIModal.find('.error-msg');
    $this.cache.$ctiDocContainer = rkCTIModal.find('.tp-rebuilding-details__content');
    $this.cache.$reqCtiSuccess = rkCTIModal.find('.tp-rebuilding-details__success');

    $('.js-langcode').on('click', function (e) {
      e.preventDefault();
      rkCTIModal.modal('show');
      $this.cache.$ctiDocContainer.removeClass('d-none');
      $this.cache.$reqCtiSuccess.addClass('d-none');
    });
    $('.js-close-btn').on('click', function (e) {
      e.preventDefault();
      rkCTIModal.modal('hide');
      $this.cache.$ctiDocContainer.removeClass('d-none');
      $this.cache.$reqCtiSuccess.addClass('d-none');
    });

    $this.cache.$ctiDocContainer.removeClass('d-none');
    $this.cache.$reqCtiSuccess.addClass('d-none');
    $this.cache.$requestTranlation.on('click', function () {
      const reqLang = $this.cache.$reqCtiDrpdwn.val();
      if (reqLang === '') {
        $this.cache.$errMsg.addClass('error-msg--active');
      } else {
        if ($this.cache.$errMsg.hasClass('error-msg--active')) {
          $this.cache.$errMsg.removeClass('error-msg--active');
        }
        $this.requestCtiLanguage(reqLang);
      }
    });
  }
}

function _requestCtiLanguage(lang) {
  const $this = this;
  const { apiRequestCTI, $rebuildingData } = $this.cache;
  const data = {
    apiURL: apiRequestCTI,
    rkCTIdetails: {
      rkTbNumber: $rebuildingData.technicalBulletin,
      mcon: $rebuildingData.rkGeneralNumber,
      functionalLocation: $rebuildingData.location,
      requestedCTILanguage: lang
    }
  };
  $this.submitCTIemail(data);
}

function _submitCTIemail(dataObj) {
  const $this = this;
  const {
    $ctiDocContainer,
    $reqCtiSuccess,
    $requestTranlation,
    $reqCtiDrpdwn,
    $errMsg,
    $spinner
  } = $this.cache;
  $requestTranlation.prop('disabled', true);
  auth.getToken(({ data: authData }) => {
    ajaxWrapper
      .getXhrObj({
        url: dataObj.apiURL,
        method: ajaxMethods.POST,
        cache: true,
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        data: JSON.stringify(dataObj.rkCTIdetails),
        showLoader: true,
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader(
            'Authorization',
            `Bearer ${authData.access_token}`
          );
        }
      })
      .done((response) => {
        if (response.status === 202) {
          $requestTranlation.prop('disabled', false);
          $ctiDocContainer.addClass('d-none');
          $reqCtiSuccess.removeClass('d-none');
          $spinner.addClass('d-none');
          storageUtil.setCookie('ctiLangCode', dataObj.rkCTIdetails.requestedCTILanguage || 'en');
        }
      })
      .fail((e) => {
        logger.error(e);
        $requestTranlation.prop('disabled', false);
        $ctiDocContainer.removeClass('d-none');
        $reqCtiSuccess.addClass('d-none');
        $reqCtiDrpdwn.val('');
        $spinner.addClass('d-none');
        if ($errMsg.hasClass('error-msg--active')) {
          $errMsg.removeClass('error-msg--active');
        }
      });
  });
}

function _getCtiDocuments() {
  const $this = this;
  const { apiCTI } = $this.cache;
  const rkRelease = $this.cache.$rebuildingData.technicalBulletin;
  //const rkRelease = 'TT3_2020_01_01';
  if (rkRelease !== '') {
    auth.getToken(({ data: authData }) => {
      ajaxWrapper
        .getXhrObj({
          url: `${apiCTI}/${rkRelease}/cti`,
          method: ajaxMethods.GET,
          cache: true,
          dataType: 'json',
          contentType: 'application/json',
          beforeSend(jqXHR) {
            jqXHR.setRequestHeader(
              'Authorization',
              `Bearer ${authData.access_token}`
            );
            jqXHR.setRequestHeader(
              'Content-Type',
              'application/x-www-form-urlencoded'
            );
          },
          showLoader: true
        })
        .done((res) => {
          $this.cache.$ctiData = res.data[0];
          if ($this.cache.$ctiData && $this.cache.$ctiData.ctiDocuments) {
            const langAvailable = $this.cache.$ctiData.ctiDocuments.filter(
              (item) => {
                if (
                  item.langCode === $this.cache.$currentLanguage ||
                  item.langCode === 'en'
                ) {
                  item['langDesc'] = $this.cache.langlist[item.langCode]
                    ? $this.cache.langlist[item.langCode]
                    : '';
                  return item;
                }
              }
            );
            const otherLang = $this.cache.$ctiData.ctiDocuments.filter(
              (item) => {
                if (
                  item.langCode === $this.cache.$currentLanguage ||
                  item.langCode === 'en'
                ) {
                  return false;
                } else {
                  item['langDesc'] = $this.cache.langlist[item.langCode]
                    ? $this.cache.langlist[item.langCode]
                    : '';
                  return item;
                }
              }
            );
            const dataA = Object.keys($this.cache.langlist);
            const reqOtherLang = dataA.filter(item => !$this.cache.$ctiData.ctiDocuments.some(docData => docData.langCode === item));
            const reqLangList = [];
            Object.keys($this.cache.langlist).forEach((key) => {
              reqOtherLang.filter(function (item) {
                if (key === item) {
                  var newObj = {};
                  newObj.langCode = key;
                  newObj.langDesc = $this.cache.langlist[key];
                  reqLangList.push(newObj);
                }
              });
            });
            // sort language alphabetically
            otherLang.sort(function(currentObj, compareWithObj) {
              return currentObj['langDesc'].localeCompare(compareWithObj['langDesc']);
            });
            $this.renderCtiDocuments(langAvailable, otherLang, reqLangList);
          }
        })
        .fail((e) => {
          logger.error(e);
          const objKeys = Object.keys($this.cache.langlist);
          const objValues = Object.values($this.cache.langlist);
          const reqOtherLang = [];
          objKeys.forEach((item, i) => {
            const obj = {
              langCode: objKeys[i],
              langDesc: objValues[i]
            };
            reqOtherLang.push(obj);
          });
          $this.renderCtiDocuments(false, false, reqOtherLang);
        });
    });
  } else {
    $this.renderCtiDocuments();
  }
}

function _getRebuildingKitDetails() {
  const $this = this;
  const urlParams = new URLSearchParams(window.location.search);
  let rkNumber, equipmentNumber;
  for (const [key, value] of urlParams) {
    if (key === 'rkNumber') {
      rkNumber = value;
    }
    if (key === 'equipment') {
      equipmentNumber = value;
    }
  }

  auth.getToken(({ data: authData }) => {
    ajaxWrapper
      .getXhrObj({
        url: `${$this.cache.rebuildingdetailsApi}?rknumbers=${rkNumber}&equipmentnumber=${equipmentNumber}`,
        method: ajaxMethods.GET,
        cache: true,
        dataType: 'json',
        contentType: 'application/json',
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader(
            'Authorization',
            `Bearer ${authData.access_token}`
          );
          jqXHR.setRequestHeader(
            'Content-Type',
            'application/x-www-form-urlencoded'
          );
        },
        showLoader: true
      })
      .done((res) => {
        $this.cache.$contentWrapper.removeClass('d-none');
        $this.cache.$spinner.addClass('d-none');
        $this.cache.$rebuildingData = res.data[0];
        $this.renderRebuildingKitDetails();
        this.getCtiDocuments();
        $this.renderRebuildingKitDetailsBottom();
        $this.updateRkValidationRows();
      })
      .fail((e) => {
        logger.error(e);
      });
  });
}

class Rebuildingkitdetails {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.configJson = this.root
      .find('.js-rebuilding-details__config')
      .text();
    this.cache.$contentWrapper = this.root.find(
      '.tp-rk-detail__content-wrapper'
    );
    this.cache.rebuildingdetailsApi = this.root.data('rebuilding-details-api');
    this.cache.apiURL = this.root.data('preferred-language-api');
    this.cache.apiCTI = this.root.data('cti-api');
    this.cache.apiRequestCTI = this.root.data('request-cti-api');
    this.cache.$content = this.root.find('.js-rebuilding-details__content');
    this.cache.$contenbottom = this.root.find(
      '.js-rebuilding-details__contentbottom'
    );
    this.cache.$contentdocs = $('.tp-rebuilding-details__ctidocs');
    this.cache.languagesList = $('input[name="preferredlanguage"]');
    this.cache.$currentLanguage = $(
      'input[name="preferredlanguage"]:checked'
    ).val();
    this.cache.$preferredLangLink = this.root.find('.js-rk-preferred-language');
    this.cache.$modal = this.root.parent().find('.js-language-modal');
    this.cache.$closeBtn = this.root.parent().find('.js-close-btn');
    this.cache.$applyLanguage = this.root.parent().find('.js-apply-language');
    this.cache.$spinner = this.root.find('.tp-spinner');
    // Create Local Array Object for Language List
    const $this = this;
    this.cache.langlist = {};
    $.each(this.cache.languagesList, function (index, element) {
      const lcode = $(element).data('langcode');
      const ldesc = $(element).data('langdesc');
      $this.cache.langlist[`${lcode}`] = ldesc;
    });
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
  }
  changePreferredLanguage(btn) {
    const $this = this;
    const { apiURL, langlist, $preferredLangLink, $modal } = this.cache;
    const updatedLang = $('input[name="preferredlanguage"]:checked').val();
    const newURL = `${apiURL}?langcode=${updatedLang}`;

    auth.getToken(({ data: authData }) => {
      ajaxWrapper
        .getXhrObj({
          url: newURL,
          method: ajaxMethods.POST,
          cache: true,
          dataType: 'json',
          contentType: 'application/json',
          beforeSend(jqXHR) {
            jqXHR.setRequestHeader(
              'Authorization',
              `Bearer ${authData.access_token}`
            );
            jqXHR.setRequestHeader(
              'Content-Type',
              'application/x-www-form-urlencoded'
            );
          }
        })
        .done((res) => {
          if (res.status === 'success') {
            this.cache.$currentLanguage = updatedLang;
            $preferredLangLink.text(langlist[updatedLang]);
            $modal.modal('hide');
            $this.getCtiDocuments();
          }
          $(btn).removeAttr('disabled');
        })
        .fail((e) => {
          logger.error(e);
          $(btn).removeAttr('disabled');
        });
    });
  }
  getRebuildingKitDetails() {
    return _getRebuildingKitDetails.apply(this, arguments);
  }
  getCtiDocuments() {
    return _getCtiDocuments.apply(this, arguments);
  }
  renderRebuildingKitDetails() {
    return _renderRebuildingKitDetails.apply(this, arguments);
  }
  renderCtiDocuments() {
    return _renderCtiDocuments.apply(this, arguments);
  }
  renderRebuildingKitDetailsBottom() {
    return _renderRebuildingKitDetailsBottom.apply(this, arguments);
  }
  updateRkValidationRows() {
    //Clear values for duplicate rows
    $('.tp-rk-details-rk_validation-title').each(function(index) {
      if(index !== 0) {
        $(this).text('');
      } 
    });
    $('.tp-rk-details-rk_validation').each(function(index) {
      if(index !== 0) {
        $(this).text('');
      } 
    });
  }
  requestCtiLanguage() {
    return _requestCtiLanguage.apply(this, arguments);
  }
  submitCTIemail() {
    return _submitCTIemail.apply(this, arguments);
  }

  bindEvents() {
    const $this = this;
    const { $preferredLangLink, $modal, $closeBtn, $applyLanguage } =
      this.cache;

    $preferredLangLink.on('click', function (e) {
      e.preventDefault();
      $modal.modal('show');
    });
    $applyLanguage.on('click', function () {
      $(this).attr('disabled', 'disabled');
      $this.changePreferredLanguage(this);
    });
    $closeBtn.on('click', function () {
      $modal.modal('hide');
    });
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.getRebuildingKitDetails();
  }
}

export default Rebuildingkitdetails;
