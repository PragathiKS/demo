import $ from 'jquery';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import { ajaxMethods } from '../../../scripts/utils/constants';

class OrderSearch {
  cache = {};
  constructor({ el }) {
    this.root = $(el);
  }
  initCache() {
    /* Initialize cache here */
    this.cache.config = this.root.find('.order-search__config').text();
  }
  bindEvents() {
    /* Bind jQuery events here */
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();

    let config = {};
    try {
      config = JSON.parse(this.cache.config);
    } catch (err) {
      logger.error(err.message);
    }

    render.fn({
      template: 'orderSearch',
      url: config.apiURL,
      target: '.js-order-search__form',
      ajaxConfig: {
        method: ajaxMethods.POST
      },
      beforeRender(data) {
        this.data = $.extend(data, config);
      }
    });
  }
}

export default OrderSearch;
