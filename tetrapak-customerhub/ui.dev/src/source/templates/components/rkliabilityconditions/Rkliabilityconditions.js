import $ from 'jquery';
import { logger } from '../../../scripts/utils/logger';
import { render } from '../../../scripts/utils/render';
import auth from '../../../scripts/utils/auth';
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
  getPDFButtons() {
    const $this = this;
    const ctiLangCode = storageUtil.getCookie('ctiLangCode') || 'en';
    logger.log(`getPdfbuttons: ${this.cache.pdfbButtonsApi}?preferredLanguage=${ctiLangCode}`);
    auth.getToken(({ data: authData }) => {
      ajaxWrapper.getXhrObj({
        url: `${this.cache.pdfButtonsApi}?preferredLanguage=${ctiLangCode}`,
        method: ajaxMethods.GET,
        dataType: 'json',
        contentType: 'application/json',
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
        },
        showLoader: true
      }).done(() => {
        $this.cache.$spinner.addClass('d-none');
        $this.cache.$content.removeClass('d-none');
        $this.renderButtons();
      }).fail(() => {
        $this.cache.$content.removeClass('d-none');
        $this.cache.$spinner.addClass('d-none');
      });
    });
  }
  renderButtons() {
    render.fn({
      template: 'rkliabilityconditionsButtons',
      target: '.js-tp-rk-liabilityconditions-buttons',
      data: { i18nKeys: this.cache.i18nKeys }}, () => { 
      this.cache.$contentWrapper.removeClass('d-none');
      this.cache.$spinner.addClass('d-none');
    });
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.getPDFButtons();
  }
}

export default Rkliabilityconditions;
