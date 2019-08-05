import $ from 'jquery';
import 'bootstrap';
import { storageUtil, reloadPage } from '../../../scripts/common/common';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { LANGUAGE_PREFERENCE_SERVLET_URL } from '../../../scripts/utils/constants';
import { logger } from '../../../scripts/utils/logger';

class LanguageSelector {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};

  initCache() {
    this.cache.$modal = this.root.parent().find('.js-lang-modal');
    try {
      this.cache.selectedLanguage = this.root.find('#selectedLanguage').val();
    } catch (e) {
      this.cache.selectedLanguage = null;
      logger.error(e);
    }
  }

  bindEvents() {
    const $this = this;
    const { $modal } = $this.cache;
    this.root.on('click', '.js-close-btn', function () {
      $this.root.modal('hide');
      $this.closeModalHandler();
    })
      .on('click', function (e) {
        if ($(e.target).hasClass('js-lang-modal')) {
          $this.closeModalHandler();
        }
      })
      .on('click', '.js-lang-selector__btn', function () {
        $this.setCustomerLanguage($(this).data('langcode'));
      })
      .on('showlanuagepreferencepopup', function () {
        $modal.modal();
      });

  }
  closeModalHandler() {
    const langCookie = storageUtil.getCookie('lang-code');
    if (!langCookie) {
      storageUtil.setCookie('lang-code', 'en');
    }
  }

  setCustomerLanguage(langCode) {
    ajaxWrapper.getXhrObj({
      url: LANGUAGE_PREFERENCE_SERVLET_URL,
      data: {
        'lang-code': langCode
      }
    }).always(() => {
      storageUtil.setCookie('lang-code', langCode);
      reloadPage();
    });
  }

  showPopup() {
    const { $modal } = this.cache;
    const langCookie = storageUtil.getCookie('lang-code');
    if (!this.cache.selectedLanguage && !langCookie) {
      $modal.modal();
    }
  }

  init() {
    this.initCache();
    this.bindEvents();
    this.showPopup();
  }
}

export default LanguageSelector;
