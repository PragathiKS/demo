import $ from 'jquery';
import { logger } from '../../../scripts/utils/logger';
import { render } from '../../../scripts/utils/render';
// import auth from '../../../scripts/utils/auth';
import {ajaxMethods} from '../../../scripts/utils/constants';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { storageUtil } from '../../../scripts/common/common';

class Rkliabilityconditions {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$content = this.root.find('.js-tp-rk-liabilityconditions-buttons');
    this.cache.$spinner = this.root.find('.js-tp-spinner');
    this.cache.configJson = this.root.find('.js-tp-rk-liability__config').text();
    this.cache.pdfButtonsApi = this.root.data('pdfbuttons-api');
    this.cache.contentWrapper = this.root.find('.tp-rk-liabilityConditions__content-wrapper');
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) { 
      this.cache.i18nKeys = {}; logger.error(e);
    }
  }
  bindEvents() {
    /* Bind jQuery events here */
    /**
     * Example:
     * const { $submitBtn } = this.cache;
     * $submitBtn.on('click', () => { ... });
     */
  }
  getPDFDocumentLinks() {
    const $this = this;
    const ctiLangCode = storageUtil.getCookie('lang-code') || 'en';
    // auth.getToken(({ data: authData }) => {
    ajaxWrapper.getXhrObj({
      url: `${this.cache.pdfButtonsApi}?preferredLanguage=${ctiLangCode}`,
      method: ajaxMethods.GET,
      dataType: 'json',
      contentType: 'application/json',
      // beforeSend(jqXHR) {
      //   jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
      // },
      showLoader: true
    }).done(( res ) => {
      $this.cache.$spinner.addClass('d-none');
      $this.cache.$content.removeClass('d-none');
      $this.cache.$pdfvalue = res;
      $this.renderPDFLinks(ctiLangCode);
    }).fail(() => {
      $this.cache.$content.removeClass('d-none');
      $this.cache.$spinner.addClass('d-none');
    });
    // });
  }
  renderPDFLinks(ctiLangCode) {
    render.fn({
      template: 'rkliabilityPDFLinks',
      target: '.js-tp-rk-liabilityconditions-pdfLinks',
      data: { 
        i18nKeys: this.cache.i18nKeys,
        showPreferredLangPDF: ctiLangCode !== 'en' && !!this.cache.$pdfvalue[ctiLangCode],
        pdfvalue: this.cache.$pdfvalue 
      }}, () => { 
      this.cache.contentWrapper.removeClass('d-none');
      this.cache.$spinner.addClass('d-none');
      const $pdfButtons = $('body').find('.liability-condition-pdf');
      $pdfButtons.each((_, button) => {
        button.addEventListener('click', (e) => {
          e.preventDefault();   
          window.open($(e.currentTarget).attr('href'), '_blank');
        });
      });
    });
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.getPDFDocumentLinks();
  }
}

export default Rkliabilityconditions;
