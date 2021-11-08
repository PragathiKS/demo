import $ from 'jquery';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';

class AddEquipment {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$contentWrapper = this.root.find('.tp-add-equipment__content-wrapper');
    this.cache.$spinner = this.root.find('.tp-spinner');
    this.cache.configJson = this.root.find('.js-tp-add-equipment__config').text();
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
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
  renderLayout() {
    const $this = this;
    render.fn({
      template: 'addEquipmentForm',
      target: '.js-tp-add-equipment__form',
      data: $this.cache.i18nKeys
    }, () => {
      $this.cache.$contentWrapper.removeClass('d-none');
      $this.cache.$spinner.addClass('d-none');
    });
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.renderLayout();
    this.bindEvents();
  }
}

export default AddEquipment;
