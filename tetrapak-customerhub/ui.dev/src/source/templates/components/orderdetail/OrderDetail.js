import $ from 'jquery';
import auth from '../../../scripts/utils/auth';
import deparam from 'jquerydeparam';
import { render } from '../../../scripts/utils/render';
import { ajaxMethods, API_ORDER_DETAIL_PARTS, API_ORDER_DETAIL_PACKMAT } from '../../../scripts/utils/constants';
import { apiHost, tableSort } from '../../../scripts/common/common';
import { logger } from '../../../scripts/utils/logger';


function _processPackmatData(data) {
  let keys = [];
  if (Array.isArray(data.orderSummary)) {
    data.orderSummary = data.orderSummary.map(summary => {
      keys = (keys.length === 0) ? Object.keys(summary) : keys;
      return tableSort.call(this, summary, keys);
    });
    data.orderSummaryHeadings = keys.map(key => ({
      key,
      i18nKey: `cuhu.orderDetail.${key}`
    }));
  }
}

/**
 * Renders Order Data
 */
function _renderOrderSummary() {
  const $this = this;
  auth.getToken(({ data: authData }) => {
    render.fn({
      template: 'orderDetailSummary',
      url: {
        path: `${apiHost}/${$this.cache.apiUrl}`
      },
      target: '.js-order-detail__summary',
      ajaxConfig: {
        method: ajaxMethods.GET,
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
          jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        cache: true,
        showLoader: true,
        cancellable: true
      },
      beforeRender(data) {
        if (!data) {
          this.data = data = {
            isError: true
          };
        } else {
          data = $.extend(true, data, $this.cache.i18nKeys);

          if ($this.cache.orderType === 'packmat') {
            _processPackmatData(data);
          }
        }
        //return _processFinancialStatementData.apply($this, [data]);
      }
    }, (data) => {
      if (!data.isError) {
        console.log(data); //eslint-disable-line
      }
    });
  });
}

class OrderDetail {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize selector cache here */
    this.cache.configJson = this.root.find('.js-order-detail__config').text();
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }

    const { orderType } = deparam(window.location.search.replace('?', '').replace('&', ','));

    if (orderType) {
      this.cache.orderType = orderType.toLowerCase();
    } else {
      this.cache.orderType = orderType;
    }

    if (this.cache.orderType === 'packmat') {
      this.cache.apiUrl = API_ORDER_DETAIL_PACKMAT;
    } else {
      this.cache.apiUrl = API_ORDER_DETAIL_PARTS;
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
  renderOrderSummary = () => _renderOrderSummary.call(this);
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderOrderSummary();
  }
}

export default OrderDetail;
