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
      template: 'packaging',
      url: {
        path: '/apps/settings/wcm/designs/customerhub/jsonData/packaging.json' //Mock JSON
      },
      target: '.js-packaging__delivery',
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
    }, (data) => {
      logger.log(data);
    });
  });

}
function _processTableData(data) {
  let deliveryTablekeys = [];
  data.tableHeadings = [];
  debugger; //eslint-disable-line
  data.packagingDeliveryTable.map(item => {
    deliveryTablekeys.push(item);
    data.tableHeadings.push({
      key: `${item}`,
      i18nKey: `cuhu.orderdetail.packaging.delivery.${item}`
    });
  });

  if (Array.isArray(data.deliveryList)) {
    data.deliveryList = data.deliveryList.map(deliveryList => {
      if (Array.isArray(deliveryList.products)) {
        deliveryList.products = deliveryList.products.map(product => {
          product.quantityKPK = `${product.orderQuantity}/${product.deliveredQuantity}/${product.remainingQuantity}`;
          return tableSort.call(this, product, deliveryTablekeys);
        });
      }
      return deliveryList;
    });
  }
  this.cache.data = data;
}

class Packaging {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.i18nKeys = this.root.find('.js-packaging__config').text();
    this.cache.packagingDeliveryTableConfig = this.root.find('.js-packaging__delivery-table-config').text();
    this.cache.packagingDeliveryTable = [];
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.i18nKeys);
      this.cache.packagingDeliveryTable = this.cache.packagingDeliveryTableConfig.split(',');
    } catch (e) {
      this.cache.i18nKeys = {};
      this.cache.packagingDeliveryTable = {};
      this.cache.packagingProductsTable = {};
      logger.error(e);
    }
  }
  bindEvents() {
    // bind event
  }
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

export default Packaging;
