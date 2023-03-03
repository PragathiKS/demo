import $ from 'jquery';
import 'bootstrap';
import { render } from '../../../scripts/utils/render';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import auth from '../../../scripts/utils/auth';
import { ajaxMethods } from '../../../scripts/utils/constants';
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

function _renderRebuildingKitDetails({ isNotConfirmed }) {
  const $this = this;
  const { $rebuildingData } = $this.cache;
  const { i18nKeys } = $this.cache;

  render.fn({
    template: 'rebuildingkitDetails',
    target: $this.cache.$content,
    data: { i18nKeys: i18nKeys, rebuildingData: $rebuildingData, isNotConfirmed }
  });
  $('.js-rebuilding-details__update').on('click', function() {
    $this.renderRebuildingKitReportModal();
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

function  _renderRebuildingKitReportModal() {
  const { i18nKeys, $reportModal } = this.cache;
  const $this = this;

  auth.getToken(({ data: authData }) => {
    ajaxWrapper
      .getXhrObj({
        url: $this.cache.rebuildingImplStatusListApi,
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
        render.fn({
          template: 'rebuildingkitDetailsReport',
          data: {
            statuses: res.data.map(status => ({ key: status.id, desc: status.implStatus })),
            i18nKeys: i18nKeys
          },
          target: '.js-update-modal'
        });
        $reportModal.modal('show');
      })
      .fail((e) => {
        logger.error(e);
      });
  });

  this.root.on('click', '.js-close-btn',  () => {
    $reportModal.modal('hide');
  });

  this.root.on('click', '.js-rk-make-update',  (e) => {
    e.preventDefault();
    const formData = new FormData(e.currentTarget.form);
    auth.getToken(({ data: authData }) => {
      ajaxWrapper
        .getXhrObj({
          url: $this.cache.rebuildingReportApi,
          method: ajaxMethods.POST,
          cache: true,
          dataType: 'json',
          contentType: 'application/json',
          data: JSON.stringify({
            serialnumber: $this.cache.$rebuildingData.serialNumber,
            reportedrebuildingkit: $this.cache.$rebuildingData.rkNumber,
            reportedrebuildingkitname: $this.cache.$rebuildingData.rkDesc,
            reportedby: 'My Tetra Pak',
            comment: formData.get('comments'),
            currentstatus: $this.cache.$rebuildingData.implStatus,
            reportedstatus: formData.get('status'),
            date: formData.get('date'),
            source: 'My Tetra Pak'
          }),
          beforeSend(jqXHR) {
            jqXHR.setRequestHeader(
              'Authorization',
              `Bearer ${authData.access_token}`
            );
          },
          showLoader: true
        })
        .done(() => {
          $this.cache.$contentWrapper.removeClass('d-none');
          $this.cache.$spinner.addClass('d-none');
          $reportModal.modal('hide');
          this.renderRebuildingKitDetails({ isNotConfirmed: false });
        })
        .fail((e) => {
          $this.cache.$contentWrapper.removeClass('d-none');
          $this.cache.$spinner.addClass('d-none');
          logger.error(e);
        });
    });
  });
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
  // const rkRelease = 'TT3_2020_01_01';
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
        $this.renderRebuildingKitDetails({ isNotConfirmed: true });
        this.getCtiDocuments();
        $this.renderRebuildingKitDetailsBottom();
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
    this.cache.rebuildingImplStatusListApi = this.root.data('rebuilding-impl-statuslist-api');
    this.cache.rebuildingReportApi = this.root.data('rebuilding-report-api');
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
    this.cache.$langModal = this.root.parent().find('.js-language-modal');
    this.cache.$reportModal = this.root.parent().find('.js-update-modal');
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
    const { apiURL, langlist, $preferredLangLink, $langModal } = this.cache;
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
            $langModal.modal('hide');
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
  renderRebuildingKitDetails(view) {
    return _renderRebuildingKitDetails.call(this, view);
  }
  renderCtiDocuments() {
    return _renderCtiDocuments.apply(this, arguments);
  }
  renderRebuildingKitDetailsBottom() {
    return _renderRebuildingKitDetailsBottom.apply(this, arguments);
  }
  renderRebuildingKitReportModal() {
    return _renderRebuildingKitReportModal.apply(this, arguments);
  }
  requestCtiLanguage() {
    return _requestCtiLanguage.apply(this, arguments);
  }
  submitCTIemail() {
    return _submitCTIemail.apply(this, arguments);
  }

  bindEvents() {
    const $this = this;
    const { $preferredLangLink, $langModal, $closeBtn, $applyLanguage } =
      this.cache;

    $preferredLangLink.on('click', function (e) {
      e.preventDefault();
      $langModal.modal('show');
    });
    $applyLanguage.on('click', function () {
      $(this).attr('disabled', 'disabled');
      $this.changePreferredLanguage(this);
    });
    $closeBtn.on('click', function () {
      $langModal.modal('hide');
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
