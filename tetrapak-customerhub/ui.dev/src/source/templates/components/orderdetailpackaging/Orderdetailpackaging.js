import $ from 'jquery';
import 'bootstrap';
import 'core-js/features/array/includes';
import { render } from '../../../scripts/utils/render';
import { logger } from '../../../scripts/utils/logger';
import auth from '../../../scripts/utils/auth';
import { ajaxMethods } from '../../../scripts/utils/constants';
import { tableSort } from '../../../scripts/common/common';


function _renderDeliveryDetails() {
  const $this = this;
  auth.getToken(({ data: authData }) => {
    render.fn({
      template: 'packagingDeliveryDetail',
      url: {
        path: '/apps/settings/wcm/designs/customerhub/jsonData/packaging.json' //Mock JSON
      },
      target: '.js-packaging__delivery-detail',
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
        }
        data = $.extend(true, data, $this.cache);
        return $this.processTableData([data]);
      }
    });
  });

}
function _processTableData(data) {
  const { packagingDeliveryTableCols } = this.cache;
  let deliveryTablekeys = packagingDeliveryTableCols.split(',');
  data.tableHeadings = [];
  deliveryTablekeys.map(item => {
    if (item === 'quantityKPK') {
      data.tableHeadings.push({
        key: `${item}`,
        i18nKey: `cuhu.orderdetail.packaging.delivery.${item}`,
        iconClassName: 'icon-Info tp-order-detail-packaging__icon-info js-icon-Info'
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
  this.cache = data;
}

function _openOverlay() {
  this.root.find('.js-order-detail-packaging__modal').modal();
}
class Orderdetailpackaging {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.packagingDeliveryTableCols = $('#packagingDeliveryTableCols').val();
    this.cache.i18nKeys = this.root.find('.js-packaging__config').text();
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.i18nKeys);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
  }
  bindEvents() {
    this.root
      .on('click', '.js-icon-Info', this.openOverlay);
  }
  openOverlay = (...args) => _openOverlay.apply(this, args);

  renderDeliveryDetails() {
    return _renderDeliveryDetails.apply(this, arguments);
  }

  processTableData(data) {
    return _processTableData.apply(this, data);
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
    this.renderDeliveryDetails();
  }
}

export default Orderdetailpackaging;
