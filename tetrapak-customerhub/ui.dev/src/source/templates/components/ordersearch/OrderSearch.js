import $ from 'jquery';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import { ajaxMethods } from '../../../scripts/utils/constants';

/**
 * Processes data before rendering
 * @param {object} data JSON data object
 */
function _processOrderSearchData(data) {
  data = $.extend(true, data, this.cache.config);
  data['dateRange'] = data.summary.filterStartDate + ' - ' + data.summary.filterEndDate;
  return data;
}

class OrderSearch {
  cache = {};
  constructor({ el }) {
    this.root = $(el);
  }
  initCache() {
    /* Initialize cache here */
    this.cache.config = this.root.find('.js-order-search__config').text();
  }
  bindEvents() {
    /* Bind jQuery events here */
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();

    try {
      this.cache.config = JSON.parse(this.cache.config);

      render.fn({
        template: 'orderSearch',
        url: this.cache.config.apiURL,
        target: '.js-order-search__form',
        ajaxConfig: {
          method: ajaxMethods.POST
        },
        beforeRender: (...args) => _processOrderSearchData.apply(this, args)
      });
    } catch (err) {
      logger.error(err.message);
    }
  }
}

export default OrderSearch;
