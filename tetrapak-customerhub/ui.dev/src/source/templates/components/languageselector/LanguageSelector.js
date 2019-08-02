import $ from 'jquery';
import 'bootstrap';
import { storageUtil } from '../../../scripts/common/common';
import { PageReloader } from '../../../scripts/utils/pageReloader';
import { ajaxWrapper } from '../../../scripts/utils/ajax';

class LanguageSelector {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};

  bindEvents() {
    const $this = this;
    this.root.on('click', '.js-close-btn', function () {
      $this.root.modal('hide');
      $this.closeModalHandler();
    })
      .on('click', function (e) {
        if ($(e.target).hasClass('js-lang-modal')) {
          $this.closeModalHandler();
        }
      })
      .on('click', '.js-lang-selector__btn', function (e) {
        $this.setCustomerLanguage($(e.target).text());
      })
      .on('showLanuagePreferencePopup', function () {
        $('.js-lang-modal').modal();
      });

  }
  closeModalHandler() {
    const langCookie = storageUtil.getCookie('lang-code');
    if (!langCookie) {
      storageUtil.setCookie('lang-code', 'en');
    }
  }

  setCustomerLanguage(langCode) {
    const selectedLangCode = langCode.substring(9);
    storageUtil.setCookie('lang-code', selectedLangCode);
    ajaxWrapper.getXhrObj({
      url: '/bin/customerhub/saveLanguagePreference',
      data: {
        'lang-code': selectedLangCode
      }
    }).done(
      () => {
        PageReloader.reload();
      }
    ).fail(
      () => {
        this.root.modal('hide');
      }
    );
  }

  showPopup() {
    const langCookie = storageUtil.getCookie('lang-code');
    if (!langCookie) {
      $('.js-lang-modal').modal();
    }
  }

  init() {
    this.bindEvents();
    this.showPopup();
  }
}

export default LanguageSelector;
