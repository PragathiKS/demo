import $ from 'jquery';
import 'bootstrap';
import auth from '../../../scripts/utils/auth';
import deparam from 'jquerydeparam';
import { render } from '../../../scripts/utils/render';
import { ajaxMethods, API_ORDER_DETAIL_PARTS, API_ORDER_DETAIL_PACKMAT, ORDER_DETAILS_ROWS_PER_PAGE, EXT_EXCEL, EXT_PDF } from '../../../scripts/utils/constants';
import { apiHost, tableSort, resolveQuery } from '../../../scripts/common/common';
import { logger } from '../../../scripts/utils/logger';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { fileWrapper } from '../../../scripts/utils/file';
import { toast } from '../../../scripts/utils/toast';

/**
 *
 * @param {string} type
 */
function _trackAnalytics(obj, type) {
  const orderType = (obj.cache.orderType === 'packmat') ? 'packaging' : 'parts';
  const analyticsData = {};
  analyticsData.header = orderType;
  let trackId = '';
  const self = $(this);
  type = type || self.data('extnType');
  switch (type) {
    case 'excel': {
      analyticsData.createexcel = 'true';
      trackId = 'orderdetailsexcel';
      break;
    }
    case 'pdf': {
      analyticsData.createpdf = 'true';
      trackId = 'orderdetailsPDF';
      break;
    }
    case 'webRef': {
      const webRef = this.innerText;
      analyticsData.webreferencenumber = webRef;
      trackId = 'orderdetailswebref';
      break;
    }
    case 'trackOrder': {
      const deliverynumber = self.data('deliveryNumber');
      analyticsData.deliverynumber = deliverynumber;
      analyticsData.trackorder = 'trackorderclicked';
      trackId = 'orderdetailstrackorder';
      break;
    }
    case 'customercontactsupport': {
      analyticsData.customercontactsupport = 'true';
      trackId = 'orderdetailscontact';
      break;
    }
    default: {
      break;
    }
  }
  trackAnalytics(analyticsData, orderType, trackId);
}
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
      i18nKey: `cuhu.orderDetail.orderSummary.${key}`
    }));
  }

  if (Array.isArray(data.deliveryList)) {
    keys.length = 0;

    data.deliveryList.forEach(function (delivery) {
      if (Array.isArray(delivery.products)) {
        delivery.totalPages = delivery.totalProductsForQuery > ORDER_DETAILS_ROWS_PER_PAGE ?
          Math.ceil(delivery.totalProductsForQuery / ORDER_DETAILS_ROWS_PER_PAGE) : false;

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
              i18nKey: `cuhu.orderDetail.deliveryList.products.${key}`,
              iconClassName: 'icon-Info tp-order-detail__icon-info js-icon-Info'
            });
          } else {
            return ({
              key,
              i18nKey: `cuhu.orderDetail.deliveryList.products.${key}`
            });
          }
        });
      }
    });
  } else {
    data.noData = true;
  }
}

/**
 * Process Parts Data
 */
function _processPartsData(data, deliveryNo, pageIndex) {
  let keys = [];
  if (Array.isArray(data.deliveryList)) {
    if (deliveryNo) {
      data.deliveryList = data.deliveryList.filter((delivery) => delivery.deliveryNumber === deliveryNo);
    }
    data.deliveryList.forEach(function (delivery) {
      delete delivery.deliveryOrder;
      delete delivery.ETD;

      delivery.totalPages = delivery.totalProductsForQuery > ORDER_DETAILS_ROWS_PER_PAGE ?
        Math.ceil(delivery.totalProductsForQuery / ORDER_DETAILS_ROWS_PER_PAGE) : false;

      delivery.products = delivery.products.map((product, index) => {
        if (pageIndex) {
          product.serialNo = (pageIndex * ORDER_DETAILS_ROWS_PER_PAGE) + index + 1;
        } else {
          product.serialNo = index + 1;
        }

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
          'order-number': $this.cache.orderNumber,
          top: ORDER_DETAILS_ROWS_PER_PAGE
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
        const { i18nKeys } = $this.cache;
        if (!data) {
          this.data = data = {
            isError: true,
            i18nKeys
          };
        } else {
          const { downloadPdfExcelServletUrl, orderType, packagingProductsTableCols, packagingDeliveryTableCols, partsDeliveryTableCols } = $this.cache;
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
    }, (data) => {
      if (!data.isError) {
        this.root.find('.js-pagination-multiple').each(function () {
          const $this = $(this);
          $this.trigger('orderdetail.paginate', [
            $this.data()
          ]);
        });
      }
    });
  });
}

/**
 * Renders Paginate Data
 */
function _renderPaginateData() {
  const $this = this;
  const [paginationData, data] = arguments;
  const { pageNumber, pageIndex } = paginationData;
  const { deliveryNo, target } = data;
  auth.getToken(({ data: authData }) => {
    render.fn({
      template: 'deliveryDetail',
      url: {
        path: `${apiHost}/${$this.cache.apiUrl}`,
        data: {
          'order-number': $this.cache.orderNumber,
          'delivery-number': deliveryNo,
          skip: pageIndex * ORDER_DETAILS_ROWS_PER_PAGE,
          top: ORDER_DETAILS_ROWS_PER_PAGE
        }
      },
      target,
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
        const { i18nKeys } = $this.cache;
        if (!data) {
          this.data = data = {
            isError: true,
            i18nKeys
          };
        } else {
          const { downloadPdfExcelServletUrl, orderType, partsDeliveryTableCols } = $this.cache;
          data.i18nKeys = i18nKeys;
          data.pageNumber = pageNumber;

          if (partsDeliveryTableCols.length > 0) {
            data.partsDeliveryTableCols = partsDeliveryTableCols;
          }

          data.isPackmat = orderType === 'packmat';
          data.servletUrl = downloadPdfExcelServletUrl;
          data.orderType = orderType;
          $this.processTableData(data, deliveryNo, pageIndex);
          this.data = $.extend({}, data.deliveryList[0], {
            parent: data
          });
          delete data.deliveryList;
        }
      }
    }, (data) => {
      if (!data.isError) {
        const $currentTarget = $(target).find('.js-pagination-multiple');
        $currentTarget.trigger('orderdetail.paginate', [
          $currentTarget.data()
        ]);
      }
    });
  });
}

function _openOverlay() {
  this.root.find('.js-order-detail__info-modal').modal();
}

function _getExtension(extnType) {
  if (extnType === 'excel') {
    return EXT_EXCEL;
  }
  return EXT_PDF;
}

/**
 * Downloads Excel or PDF content
 */
function _downloadContent($this) {
  const self = $(this);
  const data = self.data();
  self.attr('disabled', 'disabled');
  auth.getToken(({ data: authData }) => {
    data.token = authData.access_token;
    const pdfExcelUrl = resolveQuery(self.data('servletUrl'), data);
    fileWrapper({
      extension: `${_getExtension(data.extnType)}`,
      url: pdfExcelUrl,
      data: {
        orderNumber: data.orderNumber,
        token: data.token
      }
    }).then(() => {
      self.removeAttr('disabled');
    }).catch(() => {
      const { i18nKeys } = $this.cache;
      toast.render(
        i18nKeys.fileDownloadErrorText,
        i18nKeys.fileDownloadErrorClose
      );
      self.removeAttr('disabled');
    });
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
    const $this = this;
    this.root
      .on('click', '.js-icon-Info', this.openOverlay)
      .on('click', '.js-create-excel, .js-create-pdf', this, this.downloadContent)
      .on('click', '.js-order-detail__webRef', this, function (e) {
        const $this = e.data;
        $this.trackAnalytics.call(this, $this, 'webRef');
      })
      .on('click', '.js-order-delivery-summary-track-order', this, function (e) {
        const $this = e.data;
        $this.trackAnalytics.call(this, $this, 'trackOrder');
      })
      .on('click', '.js-support-center-email', this, function (e) {
        const $this = e.data;
        $this.trackAnalytics.call(this, $this, 'customercontactsupport');
      })
      .on('click', '.js-order-detail__back-btn', () => {
        const { p } = deparam();
        window.open(p, '_self');
      })
      .on('orderdetail.pagenav', '.js-pagination-multiple', function (...args) {
        const [, paginationData] = args;
        $this.renderPaginateData(paginationData, $(this).data());
      });
  }
  downloadContent(e) {
    const $this = e.data;
    $this.trackAnalytics.call(this, $this);
    return _downloadContent.apply(this, [$this, ...arguments]);
  }
  openOverlay = (...args) => _openOverlay.apply(this, args);
  renderOrderSummary = () => _renderOrderSummary.call(this);
  renderPaginateData = (...args) => _renderPaginateData.apply(this, args);
  processTableData() {
    if (this.cache.orderType === 'packmat') {
      return _processPackmatData.apply(this, arguments);
    } else {
      return _processPartsData.apply(this, arguments);
    }
  }

  trackAnalytics(obj, type) {
    _trackAnalytics.call(this, obj, type);
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderOrderSummary();
  }
}

export default OrderDetails;
