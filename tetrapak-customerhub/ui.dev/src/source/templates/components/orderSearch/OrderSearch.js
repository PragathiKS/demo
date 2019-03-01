import { render } from '../../../scripts/utils/render';

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

    let config = JSON.parse(this.cache.config);

    render.fn({
      template: 'orderSearch',
      url: config.apiURL,
      target: '.order-search__form',
      ajaxConfig: {
        method: 'GET'
      },
      beforeRender(data) {
        this.data = $.extend(data, config);
      }
    });
  }
}

export default OrderSearch;
