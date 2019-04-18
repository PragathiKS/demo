import $ from 'jquery';
import 'bootstrap';
import auth from '../../../scripts/utils/auth';
import deparam from 'jquerydeparam';
import { render } from '../../../scripts/utils/render';
import { ajaxMethods, API_ORDER_DETAIL_PARTS, API_ORDER_DETAIL_PACKMAT } from '../../../scripts/utils/constants';
import { apiHost, tableSort, resolveQuery } from '../../../scripts/common/common';
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

  if (Array.isArray(data.deliveryList)) {
    keys.length = 0;

    data.deliveryList.forEach(function (delivery) {
      if (Array.isArray(delivery.products)) {
        delivery.products = delivery.products.map((product) => {
          const productColList = data.packagingDeliveryTableCols || Object.keys(product);
          keys = keys.length === 0 ? productColList : keys;
          product.quantityKPK = `${product.orderQuantity}/${product.deliveredQuantity}/${product.remainingQuantity}`;
          return tableSort.call(this, product, keys);
        });
        data.tableHeadings = keys.map(key => {
          if (key === 'quantityKPK') {
            return ({
              key,
              i18nKey: `cuhu.orderdetail.packaging.delivery.${key}`,
              iconClassName: 'icon-Info tp-order-detail__icon-info js-icon-Info'
            });
          } else {
            return ({
              key,
              i18nKey: `cuhu.orderdetail.packaging.delivery.${key}`
            });
          }
        });
      }
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
        product.serialNo = index + 1;
        const productColList = data.partsDeliveryTableCols || Object.keys(product);
        keys = keys.length === 0 ? productColList : keys;

        return tableSort.call(this, product, keys);
      });
      data.tableHeadings = keys.map(key => ({
        key,
        i18nKey: `cuhu.orderDetail.${key}`
      }));
    });
  } else {
    data.noData = true;
  }
}

/**
 * Renders Order Data
 */
function _renderOrderSummary() {
  const $this = this;
  auth.getToken(({ data: authData }) => {
    render.fn({
      template: 'orderDetail',
      url: {
        path: `${apiHost}/${$this.cache.apiUrl}`,
        data: {
          'order-number': $this.cache.orderNumber
        }
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
          const { i18nKeys, downloadPdfExcelServletUrl, orderType, packagingProductsTableCols, packagingDeliveryTableCols, partsDeliveryTableCols } = $this.cache;
          data.i18nKeys = i18nKeys;

          if (packagingProductsTableCols.length > 0) {
            data.packagingProductsTableCols = packagingProductsTableCols;
          }

          if (packagingDeliveryTableCols.length > 0) {
            data.packagingDeliveryTableCols = packagingDeliveryTableCols;
          }

          if (partsDeliveryTableCols.length > 0) {
            data.partsDeliveryTableCols = partsDeliveryTableCols;
          }

          data.isPackmat = orderType === 'packmat';
          data.servletUrl = downloadPdfExcelServletUrl;
          data.orderType = orderType;

          return $this.processTableData(data);
        }
      }
    });
  });
}

function _openOverlay() {
  this.root.find('.js-order-detail__info-modal').modal();
}

/**
 * Downloads Excel or PDF content
 */
function _downloadContent() {
  const self = $(this);
  const data = self.data();
  self.attr('disabled', 'disabled');
  auth.getToken(({ data: authData }) => {
    self.removeAttr('disabled');
    data.token = authData.access_token;
    const pdfExcelUrl = resolveQuery(self.data('servletUrl'), data);
    window.open(pdfExcelUrl, '_self');
  });
}

class OrderDetails {
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
    this.cache.packagingDeliveryTableCols = this.cache.packagingDeliveryTableCols !== '' ? this.cache.packagingDeliveryTableCols.split(',') : [];
    this.cache.downloadPdfExcelServletUrl = this.root.find('#downloadPdfExcelServletUrl').val();

    const { orderType, q } = deparam();
    this.cache.orderType = typeof orderType === 'string' ? orderType.toLowerCase() : orderType;
    this.cache.apiUrl = this.cache.orderType === 'packmat' ? API_ORDER_DETAIL_PACKMAT : API_ORDER_DETAIL_PARTS;
    this.cache.orderNumber = q;
  }
  bindEvents() {
    /* Bind jQuery events here */
    this.root
      .on('click', '.js-icon-Info', this.openOverlay)
      .on('click', '.js-create-excel, .js-create-pdf', this.downloadContent)
      .on('click', '.js-order-detail__back-btn', () => {
        window.history.back();
      });
  }
  downloadContent() {
    return _downloadContent.apply(this, arguments);
  }
  openOverlay = (...args) => _openOverlay.apply(this, args);
  renderOrderSummary = () => _renderOrderSummary.call(this);
  processTableData() {
    if (this.cache.orderType === 'packmat') {
      return _processPackmatData.apply(this, arguments);
    } else {
      return _processPartsData.apply(this, arguments);
    }
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderOrderSummary();
  }
}

export default OrderDetails;
