import $ from 'jquery';
import 'bootstrap';
import { storageUtil } from '../../../scripts/common/common';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { LANGUAGE_PREFERENCE_SERVLET_URL } from '../../../scripts/utils/constants';
import { logger } from '../../../scripts/utils/logger';
import { $body } from '../../../scripts/utils/commonSelectors';
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
        this.root.on('click', '.js-close-btn', function () {
          $this.root.modal('hide');
          $this.closeModalHandler();
        })
          .on('click', function () {
            if ($(this).hasClass('js-lang-modal')) {
              $this.closeModalHandler();
            }
          })
          .on('click', '.js-lang-selector__btn', function () {
            $this.setCustomerLanguage($(this).data('langcode'), $(this).data('link'));
          })
          .on('showlanuagepreferencepopup', function () {
            $this.showPopup();
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
          this.newUrlReload(langCode);
        });
      }

      newUrlReload(langCode){
        let listOfLangCodes = $('.js-lang-modal').data('lang-data'); 
        let newUrl = window.location.href;
        if(!listOfLangCodes || !langCode){
          window.location.reload();
          return;
        }
        listOfLangCodes= listOfLangCodes ? listOfLangCodes.split(',') : [];
        for (const languageCode of listOfLangCodes) {
          if(window.location.href.indexOf(`/${languageCode}/`)!== -1) {
            newUrl = window.location.href.replace(`/${languageCode}/`, `/${langCode}/`);
            break;
          }
        }
        window.location.replace(newUrl);
      }

      showPopup() {
        const { $modal } = this.cache;
        $body.addClass('tp-no-backdrop');
        $modal.modal();
      }

      setDefaultLangCode(){
        storageUtil.setCookie('lang-code', this.cache.selectedLanguage || 'en');
      }

      init() {
        this.initCache();
        this.bindEvents();
        this.setDefaultLangCode();
      }
}

export default LanguageSelector;
