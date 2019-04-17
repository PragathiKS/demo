import $ from 'jquery';
import 'bootstrap';
import auth from '../../../scripts/utils/auth';
import deparam from 'jquerydeparam';
import { render } from '../../../scripts/utils/render';
import { ajaxMethods, API_ORDER_DETAIL_PARTS, API_ORDER_DETAIL_PACKMAT } from '../../../scripts/utils/constants';
import { apiHost, tableSort } from '../../../scripts/common/common';
import { logger } from '../../../scripts/utils/logger';

/**
 * Process Order Data
 */
function _processPackmatData(data) {
  let keys = [];
  if (Array.isArray(data.orderSummary)) {
    data.orderSummary = data.orderSummary.map(summary => {
      const productColList = data.packagingProductsTableCols || Object.keys(summary);
      keys = keys.length === 0 ? productColList : keys;

      return tableSort.call(this, summary, keys);
    });
    data.orderSummaryHeadings = keys.map(key => ({
      key,
      i18nKey: `cuhu.orderdetail.${key}`
    }));
  }

  const { packagingDeliveryTableCols } = this.cache;
  let deliveryTablekeys = packagingDeliveryTableCols.split(',');
  data.tableHeadings = [];
  deliveryTablekeys.map(item => {
    if (item === 'quantityKPK') {
      data.tableHeadings.push({
        key: `${item}`,
        i18nKey: `cuhu.orderdetail.packaging.delivery.${item}`,
        iconClassName: 'icon-Info tp-order-detail__icon-info js-icon-Info'
      });
    }
    else {
      data.tableHeadings.push({
        key: `${item}`,
        i18nKey: `cuhu.orderdetail.packaging.delivery.${item}`
      });
    }
  });

  if (Array.isArray(data.deliveryList)) {
    data.deliveryList = data.deliveryList.map(deliveryList => {
      if (Array.isArray(deliveryList.products)) {
        deliveryList.products = deliveryList.products.map(product => {
          deliveryTablekeys = (deliveryTablekeys.length === 0) ? Object.keys(product) : deliveryTablekeys;
          product.quantityKPK = `${product.orderQuantity}/${product.deliveredQuantity}/${product.remainingQuantity}`;
          return tableSort.call(this, product, deliveryTablekeys);
        });
      }
      return deliveryList;
    });
  }
}

/**
 * Process Parts Data
 */
function _processPartsData(data) {
  let keys = [];
  if (Array.isArray(data.deliveryList)) {
    data.deliveryList.forEach(function (delivery) {
      delete delivery.deliveryOrder;
      delete delivery.ETD;
      delivery.products = delivery.products.map((product, index) => {
        product['#'] = index + 1;
        const productColList = data.partsDeliveryTableCols || Object.keys(product);
        keys = keys.length === 0 ? productColList : keys;

        return tableSort.call(this, product, keys);
      });
      data.tableHeadings = keys.map(key => ({
        key,
        i18nKey: `cuhu.orderDetail.${key}`
      }));
    });
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
          data.i18nKeys = $this.cache.i18nKeys;

          if ($this.cache.packagingProductsTableCols.length > 0) {
            data.packagingProductsTableCols = $this.cache.packagingProductsTableCols;
          }

          if ($this.cache.partsDeliveryTableCols.length > 0) {
            data.partsDeliveryTableCols = $this.cache.partsDeliveryTableCols;
          }

          if ($this.cache.orderType === 'packmat') {
            data.packmat = true;
          } else {
            data.parts = true;
          }

          return $this.processTableData([data]);
        }
      }
    });
  });
}

function _openOverlay() {
  this.root.find('.js-order-detail__packaging-modal').modal();
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

    this.cache.partsDeliveryTableCols = this.root.find('#partsDeliveryTableCols').val();
    this.cache.partsDeliveryTableCols = this.cache.partsDeliveryTableCols !== '' ? this.cache.partsDeliveryTableCols.split(',') : [];
    this.cache.packagingProductsTableCols = this.root.find('#packagingProductsTableCols').val();
    this.cache.packagingProductsTableCols = this.cache.packagingProductsTableCols !== '' ? this.cache.packagingProductsTableCols.split(',') : [];
    this.cache.packagingDeliveryTableCols = this.root.find('#packagingDeliveryTableCols').val();

    const { orderType } = deparam();
    this.cache.orderType = typeof orderType === 'string' ? orderType.toLowerCase() : orderType;
    this.cache.apiUrl = this.cache.orderType === 'packmat' ? API_ORDER_DETAIL_PACKMAT : API_ORDER_DETAIL_PARTS;
  }
  bindEvents() {
    /* Bind jQuery events here */
    this.root
      .on('click', '.js-icon-Info', this.openOverlay);
  }
  openOverlay = (...args) => _openOverlay.apply(this, args);
  renderOrderSummary = () => _renderOrderSummary.call(this);
  processTableData(data) {
    if (this.cache.orderType === 'packmat') {
      return _processPackmatData.apply(this, data);
    } else {
      return _processPartsData.apply(this, data);
    }
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderOrderSummary();
  }
}

export default OrderDetail;
