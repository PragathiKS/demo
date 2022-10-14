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

function _renderCtiDocuments(langAvailable, otherLang) {
  const $this = this;
  render.fn({
    template: 'rebuildingCtiDocuments',
    target: $this.cache.$contentdocs,
    data : {ctiData: langAvailable, ctiOther:otherLang}
  });
  $('.js-langcode').on('click',function(e){
    e.preventDefault();
    $('.js-rk-cti').modal('show');
  });
}

function _getCtiDocuments() {
  const $this = this;
  auth.getToken(({ data: authData }) => {
    ajaxWrapper
      .getXhrObj({
        url: 'https://api-dev.tetrapak.com//technicalbulletins/TP_2018_31_04/cti',
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
        const langAvailable = $this.cache.$ctiData.ctiDocuments.filter(item => item.langCode === $this.cache.$currentLanguage || item.langCode === 'en'); 
        const otherLang =  $this.cache.$ctiData.ctiDocuments.filter(item => item.langCode !== $this.cache.$currentLanguage || item.langCode !== 'en'); 
        $this.renderCtiDocuments(langAvailable,otherLang);
      })
      .fail((e) => {
        logger.error(e);
      });
  });
}

function _getRebuildingKitDetails() {
  const $this = this;
  auth.getToken(({ data: authData }) => {
    ajaxWrapper
      .getXhrObj({
        url: 'https://api-dev.tetrapak.com/installedbase/rebuildingkits?rknumbers=1284002-0781&equipmentnumber=9060000022',
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
        $this.cache.$rebuildingData = res.data[0];
        $this.renderRebuildingKitDetails();
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
    this.cache.rebuildingdetailsApi = this.root.data('rebuilding-details-api');
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
    this.cache.apiURL = this.root.data('preferred-language-api');
    // Create Local Array Object for Language List
    const $this = this;
    this.cache.langlist = {};
    $.each(this.cache.languagesList, function (index, element) {
      const lcode = $(element).data('langcode');
      const ldesc = $(element).data('langdesc');
      $this.cache.langlist[lcode] = ldesc;
    });
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
  }
  changePreferredLanguage(btn) {
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
          }
          $(btn).removeAttr('disabled');
        })
        .fail((e) => {
          logger.error(e);
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
    this.getCtiDocuments();
  }
}

export default Rebuildingkitdetails;