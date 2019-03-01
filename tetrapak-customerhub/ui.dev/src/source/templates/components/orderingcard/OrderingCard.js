import $ from 'jquery';
import { render } from '../../../scripts/utils/render';
import { ajaxMethods } from '../../../scripts/utils/constants';

class OrderingCard {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.apiUrl = $('#ordApiUrl').val();
  }
  bindEvents() {
    /* Bind jQuery events here */
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    render.fn({
      template: 'orderingCard',
      url: this.cache.apiUrl,
      ajaxConfig: {
        method: ajaxMethods.POST
      },
      target: this.root
    });
  }
}

export default OrderingCard;
