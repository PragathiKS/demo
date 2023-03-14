import $ from 'jquery';
import { render } from '../../../scripts/utils/render';

class Rkliabilityconditions {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$contentWrapper = this.root.find('.js-tp-rk-liabilityconditions-buttons');
    this.cache.$spinner = this.root.find('.js-tp-spinner');
    this.cache.configJson = this.root.find('.js-tp-rk-liability__config').text();
    try { this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) { this.cache.i18nKeys = {}; logger.error(e);
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
  renderButtons() {
    render.fn({
      template: 'rkliabilityconditionsButtons',
      target: '.js-tp-rk-liabilityconditions-buttons',
      data: { i18nKeys: this.cache.i18nKeys }}, () => { 
        this.cache.$contentWrapper.removeClass('d-none');
        this.cache.$spinner.addClass('d-none')
    });
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderButtons();
  }
}

export default Rkliabilityconditions;
