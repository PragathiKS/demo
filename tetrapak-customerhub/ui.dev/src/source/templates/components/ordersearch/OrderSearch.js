import $ from 'jquery';
import { route } from 'jqueryrouter';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import { ajaxMethods } from '../../../scripts/utils/constants';
import routing from '../../../scripts/utils/routing';

/**
 * Processes data before rendering
 * @param {object} data JSON data object
 */
function _processOrderSearchData(data) {
  data = $.extend(true, data, this.cache.config);
  data['dateRange'] = `${data.summary.filterStartDate} - ${data.summary.filterEndDate}`;
  return data;
}

class OrderSearch {
  cache = {};
  constructor({ el }) {
    this.root = $(el);
  }
  initCache() {
    /* Initialize cache here */
    this.cache.configJson = this.root.find('.js-order-search__config').text();
    try {
      this.cache.config = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.config = {};
      logger.error(e);
    }
  }
  bindEvents() {
    route((...args) => {
      const [info] = args;
      if (info.hash) {
        const { config } = this.cache;
        render.fn({
          template: 'orderSearch',
          url: config.apiURL,
          target: '.js-order-search__form',
          ajaxConfig: {
            method: ajaxMethods.POST
          },
          beforeRender: (...args) => _processOrderSearchData.apply(this, args)
        });
      }
    });
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    routing.push('OrderSearch');
  }
}

export default OrderSearch;
